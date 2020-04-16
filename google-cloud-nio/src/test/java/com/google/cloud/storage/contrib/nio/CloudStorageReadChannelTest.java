/*
 * Copyright 2016 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.storage.contrib.nio;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonWritableChannelException;
import javax.net.ssl.SSLHandshakeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;

/** Unit tests for {@link CloudStorageReadChannel}. */
@RunWith(JUnit4.class)
public class CloudStorageReadChannelTest {

  private CloudStorageReadChannel chan;

  private final Storage gcsStorage = mock(Storage.class);
  private final BlobId file = BlobId.of("blob", "attack");
  private final Blob metadata = mock(Blob.class);
  private final ReadChannel gcsChannel = mock(ReadChannel.class);

  @Before
  public void before() throws IOException {
    when(metadata.getSize()).thenReturn(42L);
    when(metadata.getGeneration()).thenReturn(2L);
    when(gcsStorage.get(
            file,
            Storage.BlobGetOption.fields(Storage.BlobField.GENERATION, Storage.BlobField.SIZE)))
        .thenReturn(metadata);
    when(gcsStorage.reader(file, Storage.BlobSourceOption.generationMatch(2L)))
        .thenReturn(gcsChannel);
    when(gcsChannel.isOpen()).thenReturn(true);
    chan =
        CloudStorageReadChannel.create(
            gcsStorage, file, 0, 1, CloudStorageConfiguration.DEFAULT, "");
    verify(gcsStorage)
        .get(
            eq(file),
            eq(Storage.BlobGetOption.fields(Storage.BlobField.GENERATION, Storage.BlobField.SIZE)));
    verify(gcsStorage).reader(eq(file), eq(Storage.BlobSourceOption.generationMatch(2L)));
  }

  @Test
  public void testRead() throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(1);
    when(gcsChannel.read(eq(buffer))).thenReturn(1);
    assertThat(chan.position()).isEqualTo(0L);
    assertThat(chan.read(buffer)).isEqualTo(1);
    assertThat(chan.position()).isEqualTo(1L);
    verify(gcsChannel).read(any(ByteBuffer.class));
    verify(gcsChannel, times(3)).isOpen();
  }

  @Test
  public void testReadRetry() throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(1);
    when(gcsChannel.read(eq(buffer)))
        .thenThrow(
            new StorageException(
                new IOException(
                    "outer",
                    new IOException(
                        "Connection closed prematurely: bytesRead = 33554432, Content-Length = 41943040"))))
        .thenReturn(1);
    assertThat(chan.position()).isEqualTo(0L);
    assertThat(chan.read(buffer)).isEqualTo(1);
    assertThat(chan.position()).isEqualTo(1L);
    verify(gcsChannel, times(2)).read(any(ByteBuffer.class));
  }

  @Test
  public void testReadRetrySSLHandshake() throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(1);
    when(gcsChannel.read(eq(buffer)))
        .thenThrow(
            new StorageException(
                new IOException(
                    "something",
                    new IOException(
                        "thing",
                        new SSLHandshakeException("connection closed due to throttling")))))
        .thenReturn(1);
    assertThat(chan.position()).isEqualTo(0L);
    assertThat(chan.read(buffer)).isEqualTo(1);
    assertThat(chan.position()).isEqualTo(1L);
    verify(gcsChannel, times(2)).read(any(ByteBuffer.class));
  }

  @Test
  public void testReadRetryEventuallyGivesUp() throws IOException {
    try {
      ByteBuffer buffer = ByteBuffer.allocate(1);
      when(gcsChannel.read(eq(buffer)))
          .thenThrow(
              new StorageException(
                  new IOException(
                      "Connection closed prematurely: bytesRead = 33554432, Content-Length = 41943040")))
          .thenThrow(
              new StorageException(
                  new IOException(
                      "Connection closed prematurely: bytesRead = 33554432, Content-Length = 41943040")))
          .thenReturn(1);
      assertThat(chan.position()).isEqualTo(0L);
      chan.read(buffer);
      Assert.fail();
    } catch (StorageException ex) {
      assertThat(ex.getMessage()).isNotNull();
    }
  }

  @Test
  public void testRead_whenClosed_throwsCce() throws IOException {
    try {
      when(gcsChannel.isOpen()).thenReturn(false);
      chan.read(ByteBuffer.allocate(1));
      Assert.fail();
    } catch (ClosedChannelException expected) {
    }
  }

  @Test
  public void testWrite_throwsNonWritableChannelException() throws IOException {
    try {
      chan.write(ByteBuffer.allocate(1));
      Assert.fail();
    } catch (NonWritableChannelException expected) {
    }
  }

  @Test
  public void testTruncate_throwsNonWritableChannelException() throws IOException {
    try {
      chan.truncate(0);
      Assert.fail();
    } catch (NonWritableChannelException ex) {
      assertThat(ex.getClass()).isEqualTo(NonWritableChannelException.class);
    }
  }

  @Test
  public void testIsOpen() throws IOException {
    when(gcsChannel.isOpen()).thenReturn(true).thenReturn(false);
    assertThat(chan.isOpen()).isTrue();
    chan.close();
    assertThat(chan.isOpen()).isFalse();
    verify(gcsChannel, times(2)).isOpen();
    verify(gcsChannel).close();
  }

  @Test
  public void testSize() throws IOException {
    assertThat(chan.size()).isEqualTo(42L);
    verify(gcsChannel).isOpen();
    verifyNoMoreInteractions(gcsChannel);
  }

  @Test
  public void testSize_whenClosed_throwsCce() throws IOException {
    try {
      when(gcsChannel.isOpen()).thenReturn(false);
      chan.size();
      Assert.fail();
    } catch (ClosedChannelException ex) {
      assertThat(ex.getClass()).isEqualTo(ClosedChannelException.class);
    }
  }

  @Test
  public void testPosition_whenClosed_throwsCce() throws IOException {
    try {
      when(gcsChannel.isOpen()).thenReturn(false);
      chan.position();
      Assert.fail();
    } catch (ClosedChannelException ex) {
      assertThat(ex.getClass()).isEqualTo(ClosedChannelException.class);
    }
  }

  @Test
  public void testSetPosition_whenClosed_throwsCce() throws IOException {
    try {
      when(gcsChannel.isOpen()).thenReturn(false);
      chan.position(0);
      Assert.fail();
    } catch (ClosedChannelException ex) {
      assertThat(ex.getClass()).isEqualTo(ClosedChannelException.class);
    }
  }

  @Test
  public void testClose_calledMultipleTimes_doesntThrowAnError() throws IOException {
    chan.close();
    chan.close();
    chan.close();
  }

  @Test
  public void testSetPosition() throws IOException {
    assertThat(chan.position()).isEqualTo(0L);
    assertThat(chan.size()).isEqualTo(42L);
    chan.position(1L);
    assertThat(chan.position()).isEqualTo(1L);
    assertThat(chan.size()).isEqualTo(42L);
    verify(gcsChannel).seek(1);
    verify(gcsChannel, times(5)).isOpen();
  }

  /*
   * This test case was crafted in response to a bug in CloudStorageReadChannel in which the
   * channel position (a long) was getting truncated to an int when seeking on the encapsulated
   * ReadChannel in innerOpen(). This test case fails when the bad long -> int cast is present,
   * and passes when it's removed.
   */
  @Test
  public void testChannelPositionDoesNotGetTruncatedToInt() throws IOException {
    // This position value will overflow to a negative value if a long -> int cast is attempted
    long startPosition = 11918483280L;
    ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

    // Invoke CloudStorageReadChannel.create() to trigger a call to the private
    // CloudStorageReadChannel.innerOpen() method, which does a seek on our gcsChannel.
    CloudStorageReadChannel.create(
        gcsStorage, file, startPosition, 1, CloudStorageConfiguration.DEFAULT, "");

    // Confirm that our position did not overflow during the seek in
    // CloudStorageReadChannel.innerOpen()
    verify(gcsChannel).seek(captor.capture());
    assertThat(captor.getValue()).isEqualTo(startPosition);
  }
}

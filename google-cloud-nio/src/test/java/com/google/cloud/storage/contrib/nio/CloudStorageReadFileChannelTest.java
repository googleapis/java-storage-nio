/*
 * Copyright 2019 Google LLC
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

import com.google.cloud.testing.junit4.MultipleAttemptsRule;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CloudStorageReadFileChannelTest {
  @Rule public final MultipleAttemptsRule multipleAttemptsRule = new MultipleAttemptsRule(3);

  private static final class SeekableByteChannelImpl implements SeekableByteChannel {
    private boolean open = true;
    private ByteBuffer data;

    private SeekableByteChannelImpl(ByteBuffer data) {
      this.data = data;
    }

    @Override
    public boolean isOpen() {
      return open;
    }

    @Override
    public void close() throws IOException {
      open = false;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
      byte[] tmp = new byte[Math.min(dst.remaining(), data.remaining())];
      if (tmp.length == 0) {
        return -1;
      } else {
        data.get(tmp);
        dst.put(tmp);
        return tmp.length;
      }
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
      int res = src.remaining();
      if (data.position() + res > data.limit()) {
        data.limit(data.limit() + res);
      }
      data.put(src);
      return res;
    }

    @Override
    public long position() throws IOException {
      return data.position();
    }

    @Override
    public SeekableByteChannel position(long newPosition) throws IOException {
      if (newPosition >= data.limit()) {
        data.limit((int) newPosition);
      }
      data.position((int) newPosition);
      return this;
    }

    @Override
    public long size() throws IOException {
      return data.limit();
    }

    @Override
    public SeekableByteChannel truncate(long size) throws IOException {
      if (size < data.limit()) {
        if (data.position() >= size) {
          data.position((int) size - 1);
        }
        data.limit((int) size);
      }
      return this;
    }
  }

  private CloudStorageReadFileChannel fileChannel;
  private SeekableByteChannel readChannel;
  private ByteBuffer data;

  @Before
  public void before() throws IOException {
    data = ByteBuffer.allocate(5000);
    data.limit(3);
    data.put(new byte[] {1, 2, 3});
    data.position(0);
    readChannel = new SeekableByteChannelImpl(data);
    fileChannel = new CloudStorageReadFileChannel(readChannel);
  }

  @Test
  public void testRead() throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(1);
    assertThat(fileChannel.position()).isEqualTo(0L);
    assertThat(fileChannel.read(buffer)).isEqualTo(1);
    assertThat(fileChannel.position()).isEqualTo(1L);
    assertThat(buffer.get(0)).isEqualTo((byte) 1);
  }

  @Test
  public void testReadArray() throws IOException {
    ByteBuffer[] buffers =
        new ByteBuffer[] {ByteBuffer.allocate(1), ByteBuffer.allocate(1), ByteBuffer.allocate(1)};
    assertThat(fileChannel.position()).isEqualTo(0L);
    assertThat(fileChannel.read(buffers)).isEqualTo(3L);
    assertThat(fileChannel.position()).isEqualTo(3L);
    assertThat(buffers[0].get(0)).isEqualTo((byte) 1);
    assertThat(buffers[1].get(0)).isEqualTo((byte) 2);
    assertThat(buffers[2].get(0)).isEqualTo((byte) 3);
  }

  @Test
  public void testPosition() throws IOException {
    assertThat(fileChannel.position()).isEqualTo(0L);
    assertThat(fileChannel.position(1L)).isEqualTo(fileChannel);
    assertThat(fileChannel.position()).isEqualTo(1L);
    assertThat(fileChannel.position(0L)).isEqualTo(fileChannel);
    assertThat(fileChannel.position()).isEqualTo(0L);
    assertThat(fileChannel.position(100L)).isEqualTo(fileChannel);
    assertThat(fileChannel.position()).isEqualTo(100L);
  }

  @Test
  public void testSize() throws IOException {
    assertThat(fileChannel.size()).isEqualTo(3L);
  }

  @Test
  public void testTransferTo() throws IOException {
    SeekableByteChannelImpl target = new SeekableByteChannelImpl(ByteBuffer.allocate(100));
    assertThat(fileChannel.transferTo(0L, 3L, target)).isEqualTo(3L);
    assertThat(target.position()).isEqualTo(3L);
    ByteBuffer dst = ByteBuffer.allocate(3);
    target.position(0L);
    target.read(dst);
    assertThat(dst.get(0)).isEqualTo((byte) 1);
    assertThat(dst.get(1)).isEqualTo((byte) 2);
    assertThat(dst.get(2)).isEqualTo((byte) 3);
  }

  @Test
  public void testReadOnPosition() throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(1);
    assertThat(fileChannel.position()).isEqualTo(0L);
    assertThat(fileChannel.read(buffer, 1L)).isEqualTo(1);
    assertThat(fileChannel.position()).isEqualTo(0L);
    assertThat(buffer.get(0)).isEqualTo((byte) 2);
  }

  @Test
  public void testReadBeyondEnd() throws IOException {
    fileChannel.position(3L);
    assertThat(fileChannel.read(ByteBuffer.allocate(1))).isEqualTo(-1);
    fileChannel.position(2L);
    assertThat(fileChannel.read(ByteBuffer.allocate(2))).isEqualTo(1);
  }
}

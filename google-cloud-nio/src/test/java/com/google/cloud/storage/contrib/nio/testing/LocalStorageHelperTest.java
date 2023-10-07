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

package com.google.cloud.storage.contrib.nio.testing;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;

import com.google.api.client.util.Lists;
import com.google.api.gax.paging.Page;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link LocalStorageHelper}. */
@RunWith(JUnit4.class)
public class LocalStorageHelperTest {

  Storage localStorageService = null;
  private final String testBucket = "bucket";
  private final String sourceFile = "testSource";
  private final String destinationFile = "testDestination";
  private final String payload = "copy me";

  @Before
  public void before() throws IOException {
    localStorageService = LocalStorageHelper.getOptions().getService();

    byte[] payloadBytes = payload.getBytes();
    BlobId id = BlobId.of(testBucket, sourceFile);
    BlobInfo info = BlobInfo.newBuilder(id).build();

    try (WriteChannel writer = localStorageService.writer(info)) {
      writer.write(ByteBuffer.wrap(payloadBytes));
    }
  }

  @After
  public void after() {
    localStorageService.delete(testBucket, sourceFile);
    localStorageService.delete(testBucket, destinationFile);
  }

  private Storage.CopyRequest copyRequest() {
    Storage.CopyRequest request =
        Storage.CopyRequest.newBuilder()
            .setSource(BlobId.of(testBucket, sourceFile))
            .setTarget(BlobId.of(testBucket, destinationFile))
            .build();

    return request;
  }

  @Test
  public void testCopyCanBeRead() throws Exception {
    Storage.CopyRequest request = copyRequest();
    localStorageService.copy(request).getResult();
    Blob obj = localStorageService.get(BlobId.of(testBucket, destinationFile));
    String copiedContents = new String(obj.getContent(Blob.BlobSourceOption.generationMatch()));
    File file = File.createTempFile("file", ".txt");
    file.deleteOnExit();
    obj.downloadTo(file.toPath());
    Assert.assertArrayEquals(payload.getBytes(), Files.readAllBytes(file.toPath()));

    assertThat(copiedContents).isEqualTo(payload);
    assertThat(obj.getGeneration()).isEqualTo(1);
    assertThat(obj.getSize()).isEqualTo(7);
  }

  @Test
  public void testCopyIncrementsGenerations() {
    Storage.CopyRequest request = copyRequest();

    localStorageService.copy(request).getResult();
    localStorageService.copy(request).getResult();
    Blob obj = localStorageService.get(BlobId.of(testBucket, destinationFile));
    String copiedContents = new String(obj.getContent(Blob.BlobSourceOption.generationMatch()));

    assertThat(copiedContents).isEqualTo(payload);
    assertThat(obj.getGeneration()).isEqualTo(2);
    assertThat(obj.getSize()).isEqualTo(7);
  }

  @Test
  public void testWriteNewFileSetsUpdateTime() {
    Blob obj = localStorageService.get(BlobId.of(testBucket, sourceFile));

    assertThat(obj.getUpdateTime()).isNotNull();
  }

  @Test
  public void testCreateNewFileSetsUpdateTime() {
    BlobInfo info = BlobInfo.newBuilder(BlobId.of(testBucket, "newFile")).build();
    Blob obj = localStorageService.create(info);

    assertThat(obj.getUpdateTime()).isNotNull();
  }

  @Test
  public void testStorageOptionIsSerializable() throws Exception {
    StorageOptions storageOptions = LocalStorageHelper.getOptions();
    byte[] bytes;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(storageOptions);
      oos.flush();
      oos.close();
      bytes = baos.toByteArray();
    }
    try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais)) {
      assertThat(ois.readObject()).isEqualTo(storageOptions);
    }
  }

  @Test
  public void testStorageOptionIsSerializable_customOptions() throws Exception {
    StorageOptions storageOptions = LocalStorageHelper.customOptions(false);
    byte[] bytes;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(storageOptions);
      oos.flush();
      oos.close();
      bytes = baos.toByteArray();
    }
    try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais)) {
      assertThat(ois.readObject()).isEqualTo(storageOptions);
    }
  }

  @Test
  public void testCopyOperationOverwritesExistingFile() {
    String bucket = "bucket";
    String original = "original";
    String replacement = "replacement";
    byte[] originalContent = "original content".getBytes();
    byte[] replacementContent = "replacement content".getBytes();

    localStorageService.create(BlobInfo.newBuilder(bucket, original).build(), originalContent);
    localStorageService.create(
        BlobInfo.newBuilder(bucket, replacement).build(), replacementContent);

    final Storage.CopyRequest request =
        Storage.CopyRequest.newBuilder()
            .setSource(BlobId.of(bucket, replacement))
            .setTarget(BlobId.of(bucket, original))
            .build();

    localStorageService.copy(request).getResult();

    assertThat(localStorageService.readAllBytes(BlobId.of(bucket, original)))
        .isEqualTo(replacementContent);
  }

  @Test
  public void testListObjectsWithStartOffsetOnly() {
    // Arrange
    String bucket = "bucket";
    byte[] originalContent = "original content".getBytes();
    IntStream.range(1, 10)
        .forEach(
            i ->
                localStorageService.create(
                    BlobInfo.newBuilder(bucket, String.valueOf(i)).build(), originalContent));
    // Act
    Page<Blob> pages = localStorageService.list(bucket, Storage.BlobListOption.startOffset("5"));
    List<Blob> blobs = Lists.newArrayList(pages.iterateAll());
    // Assert
    assertEquals(6, blobs.size());
  }

  @Test
  public void testListObjectsWithEndOffsetOnly() {
    // Arrange
    String bucket = "bucket";
    byte[] originalContent = "original content".getBytes();
    IntStream.range(1, 10)
        .forEach(
            i ->
                localStorageService.create(
                    BlobInfo.newBuilder(bucket, String.valueOf(i)).build(), originalContent));
    // Act
    Page<Blob> pages = localStorageService.list(bucket, Storage.BlobListOption.endOffset("5"));
    List<Blob> blobs = Lists.newArrayList(pages.iterateAll());
    // Assert
    assertEquals(4, blobs.size());
  }

  @Test
  public void testListObjectsWithStartOffsetAndEndOffset() {
    // Arrange
    String bucket = "bucket";
    byte[] originalContent = "original content".getBytes();
    IntStream.range(1, 10)
        .forEach(
            i ->
                localStorageService.create(
                    BlobInfo.newBuilder(bucket, String.valueOf(i)).build(), originalContent));
    // Act
    Page<Blob> pages =
        localStorageService.list(
            bucket, Storage.BlobListOption.startOffset("1"), Storage.BlobListOption.endOffset("5"));
    List<Blob> blobs = Lists.newArrayList(pages.iterateAll());
    // Assert
    System.out.println(blobs);
    assertEquals(4, blobs.size());
  }

  @Test
  public void testListObjectsWithPrefixAndStartOffsetAndEndOffset() {
    // Arrange
    String bucket = "bucket";
    byte[] originalContent = "original content".getBytes();
    IntStream.range(1, 10)
        .forEach(
            i -> {
              localStorageService.create(
                  BlobInfo.newBuilder(bucket, "abc/" + i).build(), originalContent);
              localStorageService.create(
                  BlobInfo.newBuilder(bucket, "bcd/" + i).build(), originalContent);
            });
    // Act
    Page<Blob> pages =
        localStorageService.list(
            bucket,
            Storage.BlobListOption.startOffset("abc/1"),
            Storage.BlobListOption.endOffset("abc/5"),
            Storage.BlobListOption.prefix("abc/"));
    List<Blob> blobs = Lists.newArrayList(pages.iterateAll());
    // Assert
    assertEquals(4, blobs.size());
  }

  @Test
  public void testListObjectsWithPrefixAndStartOffsetAndEndOffsetWithFileSeparatorInBeginning() {
    // Arrange
    String bucket = "bucket";
    byte[] originalContent = "original content".getBytes();
    IntStream.range(1, 10)
        .forEach(
            i -> {
              localStorageService.create(
                  BlobInfo.newBuilder(bucket, "abc/" + i).build(), originalContent);
              localStorageService.create(
                  BlobInfo.newBuilder(bucket, "bcd/" + i).build(), originalContent);
            });
    // Act
    Page<Blob> pages =
        localStorageService.list(
            bucket,
            Storage.BlobListOption.startOffset("/abc/1"),
            Storage.BlobListOption.endOffset("/abc/5"),
            Storage.BlobListOption.prefix("/abc/"));
    List<Blob> blobs = Lists.newArrayList(pages.iterateAll());
    // Assert
    assertEquals(4, blobs.size());
  }
}

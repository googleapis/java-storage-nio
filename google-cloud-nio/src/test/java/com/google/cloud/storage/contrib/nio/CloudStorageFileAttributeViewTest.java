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
import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.cloud.storage.contrib.nio.testing.LocalStorageHelper;
import com.google.cloud.testing.junit4.MultipleAttemptsRule;
import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link CloudStorageFileAttributeView}. */
@RunWith(JUnit4.class)
public class CloudStorageFileAttributeViewTest {
  private static final Logger logger = Logger.getLogger(CloudStorageFileAttributeViewTest.class.getName());
  @Rule public final MultipleAttemptsRule multipleAttemptsRule = new MultipleAttemptsRule(3);
  @Rule public final TestName testName = new TestName();

  private static final byte[] HAPPY = "(✿◕ ‿◕ )ノ".getBytes(UTF_8);

  private Path path;

  @BeforeClass
  public static void beforeClass() throws Exception {
    logger.info("#### Env variables  ############################################################");
    TreeMap<String, String> env = new TreeMap<>();
    for (Entry<String, String> e : System.getenv().entrySet()) {
      env.put(e.getKey(), e.getValue());
    }

    printMap(env);
    logger.info("#### Env variables  ############################################################");
  }

  @Before
  public void before() {
    logger.info(String.format("testName = %s", testName.getMethodName()));
    logger.info("#### System Properties  ########################################################");
    TreeMap<String, String> env = new TreeMap<>();
    for (Entry<Object, Object> e : System.getProperties().entrySet()) {
      env.put((String) e.getKey(), (String) e.getValue());
    }
    printMap(env);
    logger.info("#### System Properties  ########################################################");

    CloudStorageFileSystemProvider.setStorageOptions(LocalStorageHelper.getOptions());
    path = Paths.get(URI.create("gs://red/water"));
  }

  private static void printMap(TreeMap<String, String> env) {
    for (Entry<String, String> e : env.entrySet()) {
      logger.info(String.format("%s -> %s", e.getKey(), e.getValue()));
    }
  }

  @Test
  public void testReadAttributes() throws IOException {
    Files.write(path, HAPPY, CloudStorageOptions.withCacheControl("potato"));
    CloudStorageFileAttributeView lazyAttributes =
        Files.getFileAttributeView(path, CloudStorageFileAttributeView.class);
    assertThat(lazyAttributes.readAttributes().cacheControl().get()).isEqualTo("potato");
  }

  @Test
  public void testReadAttributes_notFound_throwsNoSuchFileException() throws IOException {
    try {
      CloudStorageFileAttributeView lazyAttributes =
          Files.getFileAttributeView(path, CloudStorageFileAttributeView.class);
      lazyAttributes.readAttributes();
      Assert.fail();
    } catch (NoSuchFileException ex) {
      assertThat(ex.getMessage()).isNotNull();
    }
  }

  @Test
  public void testReadAttributes_pseudoDirectory() throws IOException {
    Path dir = Paths.get(URI.create("gs://red/rum/"));
    CloudStorageFileAttributeView lazyAttributes =
        Files.getFileAttributeView(dir, CloudStorageFileAttributeView.class);
    assertThat(lazyAttributes.readAttributes())
        .isInstanceOf(CloudStoragePseudoDirectoryAttributes.class);
  }

  @Test
  public void testName() throws IOException {
    Files.write(path, HAPPY, CloudStorageOptions.withCacheControl("potato"));
    CloudStorageFileAttributeView lazyAttributes =
        Files.getFileAttributeView(path, CloudStorageFileAttributeView.class);
    assertThat(lazyAttributes.name()).isEqualTo("gcs");
  }

  @Test
  public void testEquals_equalsTester() {
    new EqualsTester()
        .addEqualityGroup(
            Files.getFileAttributeView(
                Paths.get(URI.create("gs://red/rum")), CloudStorageFileAttributeView.class),
            Files.getFileAttributeView(
                Paths.get(URI.create("gs://red/rum")), CloudStorageFileAttributeView.class))
        .addEqualityGroup(
            Files.getFileAttributeView(
                Paths.get(URI.create("gs://red/lol/dog")), CloudStorageFileAttributeView.class))
        .testEquals();
  }

  @Test
  public void testNullness() throws NoSuchMethodException, SecurityException {
    new NullPointerTester()
        .ignore(CloudStorageFileAttributeView.class.getMethod("equals", Object.class))
        .setDefault(FileTime.class, FileTime.fromMillis(0))
        .testAllPublicInstanceMethods(
            Files.getFileAttributeView(path, CloudStorageFileAttributeView.class));
  }
}

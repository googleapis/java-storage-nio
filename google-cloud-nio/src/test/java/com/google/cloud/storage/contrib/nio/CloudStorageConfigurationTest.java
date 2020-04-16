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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.net.SocketTimeoutException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link CloudStorageConfiguration}. */
@RunWith(JUnit4.class)
public class CloudStorageConfigurationTest {

  @Test
  public void testBuilder() {
    CloudStorageConfiguration config =
        CloudStorageConfiguration.builder()
            .workingDirectory("/omg")
            .permitEmptyPathComponents(true)
            .stripPrefixSlash(false)
            .usePseudoDirectories(false)
            .blockSize(666)
            .retryableHttpCodes(ImmutableList.of(1, 2, 3))
            .reopenableExceptions(
                ImmutableList.<Class<? extends Exception>>of(SocketTimeoutException.class))
            .build();
    assertThat(config.workingDirectory()).isEqualTo("/omg");
    assertThat(config.permitEmptyPathComponents()).isTrue();
    assertThat(config.stripPrefixSlash()).isFalse();
    assertThat(config.usePseudoDirectories()).isFalse();
    assertThat(config.blockSize()).isEqualTo(666);
    assertThat(config.retryableHttpCodes()).isEqualTo(ImmutableList.of(1, 2, 3));
    assertThat(config.reopenableExceptions())
        .isEqualTo(ImmutableList.<Class<? extends Exception>>of(SocketTimeoutException.class));
  }

  @Test
  public void testFromMap() {
    CloudStorageConfiguration config =
        CloudStorageConfiguration.fromMap(
            new ImmutableMap.Builder<String, Object>()
                .put("workingDirectory", "/omg")
                .put("permitEmptyPathComponents", true)
                .put("stripPrefixSlash", false)
                .put("usePseudoDirectories", false)
                .put("blockSize", 666)
                .put("retryableHttpCodes", ImmutableList.of(1, 2, 3))
                .put(
                    "reopenableExceptions",
                    ImmutableList.<Class<? extends Exception>>of(SocketTimeoutException.class))
                .build());
    assertThat(config.workingDirectory()).isEqualTo("/omg");
    assertThat(config.permitEmptyPathComponents()).isTrue();
    assertThat(config.stripPrefixSlash()).isFalse();
    assertThat(config.usePseudoDirectories()).isFalse();
    assertThat(config.blockSize()).isEqualTo(666);
    assertThat(config.retryableHttpCodes()).isEqualTo(ImmutableList.of(1, 2, 3));
    assertThat(config.reopenableExceptions())
        .isEqualTo(ImmutableList.<Class<? extends Exception>>of(SocketTimeoutException.class));
  }

  @Test
  public void testFromMap_badKey_throwsIae() {
    try {
      CloudStorageConfiguration.fromMap(ImmutableMap.of("lol", "/omg"));
      Assert.fail();
    } catch (IllegalArgumentException ex) {
      assertThat(ex.getMessage()).isNotNull();
    }
  }

  @Test
  /** Spot check that our defaults are applied. */
  public void testSomeDefaults() {
    for (CloudStorageConfiguration config :
        ImmutableList.of(
            CloudStorageConfiguration.DEFAULT, CloudStorageConfiguration.builder().build())) {
      assertThat(config.retryableHttpCodes()).contains(503);
      assertThat(config.reopenableExceptions()).contains(SocketTimeoutException.class);
    }
  }
}

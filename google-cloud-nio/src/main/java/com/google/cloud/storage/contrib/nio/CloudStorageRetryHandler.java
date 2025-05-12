/*
 * Copyright 2017 Google LLC
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

import com.google.cloud.storage.StorageException;
import com.google.common.annotations.VisibleForTesting;

/**
 * Simple counter class to keep track of retry and reopen attempts when StorageExceptions are
 * encountered. Handles sleeping between retry/reopen attempts, as well as throwing an exception
 * when all retries/reopens are exhausted.
 */
public class CloudStorageRetryHandler {
  private int retries;
  private int reopens;
  private long totalWaitTime; // in milliseconds
  private final int maxRetries;
  private final int maxReopens;
  private final CloudStorageConfiguration config;

  /**
   * Create a CloudStorageRetryHandler with the maximum retries and reopens set to the same value.
   *
   * @param maxRetriesAndReopens value for both maxRetries and maxReopens
   * @deprecated use CloudStorageRetryHandler(CloudStorageConfiguration) instead.
   */
  @java.lang.Deprecated
  public CloudStorageRetryHandler(final int maxRetriesAndReopens) {
    this.maxRetries = maxRetriesAndReopens;
    this.maxReopens = maxRetriesAndReopens;
    // we're just using the retry parameters from the config, so it's OK to have a default.
    this.config = CloudStorageConfiguration.DEFAULT;
  }

  /**
   * Create a CloudStorageRetryHandler with the maximum retries and reopens set to different values.
   *
   * @param maxRetries maximum number of retries
   * @param maxReopens maximum number of reopens
   * @deprecated use CloudStorageRetryHandler(CloudStorageConfiguration) instead.
   */
  @java.lang.Deprecated
  public CloudStorageRetryHandler(final int maxRetries, final int maxReopens) {
    this.maxRetries = maxRetries;
    this.maxReopens = maxReopens;
    // we're just using the retry parameters from the config, so it's OK to have a default.
    this.config = CloudStorageConfiguration.DEFAULT;
  }

  /**
   * Create a CloudStorageRetryHandler with the maximum retries and reopens set to the same value.
   *
   * @param config - configuration for reopens and retryable codes.
   */
  public CloudStorageRetryHandler(final CloudStorageConfiguration config) {
    this.maxRetries = config.maxChannelReopens();
    this.maxReopens = config.maxChannelReopens();
    this.config = config;
  }

  /**
   * Create a CloudStorageRetryHandler with the maximum retries and reopens set to different values.
   *
   * @param maxRetries maximum number of retries
   * @param maxReopens maximum number of reopens (overrides what's in the config)
   * @param config http codes we'll retry on, and exceptions we'll reopen on.
   */
  public CloudStorageRetryHandler(
      final int maxRetries, final int maxReopens, final CloudStorageConfiguration config) {
    this.maxRetries = maxRetries;
    this.maxReopens = maxReopens;
    this.config = config;
  }

  /**
   * @return number of retries we've performed
   */
  public int retries() {
    return retries;
  }

  /**
   * @return number of reopens we've performed
   */
  public int reopens() {
    return reopens;
  }

  /**
   * Checks whether we should retry, reopen, or give up.
   *
   * <p>In the latter case it throws an exception (this includes the scenario where we exhausted the
   * retry count).
   *
   * <p>Otherwise, it sleeps for a bit and returns whether we should reopen. The sleep time is
   * dependent on the retry number.
   *
   * @param exs caught StorageException
   * @return True if you need to reopen and then try again. False if you can just try again.
   * @throws StorageException if the exception is not retryable, or if you ran out of retries.
   */
  public boolean handleStorageException(final StorageException exs) throws StorageException {
    // None of the retryable exceptions are reopenable, so it's OK to write the code this way.
    if (isRetryable(exs)) {
      handleRetryForStorageException(exs);
      return false;
    }
    if (isReopenable(exs)) {
      handleReopenForStorageException(exs);
      return true;
    }
    throw exs;
  }

  /**
   * Records a retry attempt for the given StorageException, sleeping for an amount of time
   * dependent on the attempt number. Throws a StorageException if we've exhausted all retries.
   *
   * @param exs The StorageException error that prompted this retry attempt.
   */
  private void handleRetryForStorageException(final StorageException exs) throws StorageException {
    retries++;
    if (retries > maxRetries) {
      throw new StorageException(
          exs.getCode(),
          "All "
              + maxRetries
              + " retries failed. Waited a total of "
              + totalWaitTime
              + " ms between attempts",
          exs);
    }
    sleepForAttempt(retries);
  }

  /**
   * Records a reopen attempt for the given StorageException, sleeping for an amount of time
   * dependent on the attempt number. Throws a StorageException if we've exhausted all reopens.
   *
   * @param exs The StorageException error that prompted this reopen attempt.
   */
  private void handleReopenForStorageException(final StorageException exs) throws StorageException {
    reopens++;
    if (reopens > maxReopens) {
      throw new StorageException(
          exs.getCode(),
          "All "
              + maxReopens
              + " reopens failed. Waited a total of "
              + totalWaitTime
              + " ms between attempts",
          exs);
    }
    sleepForAttempt(reopens);
  }

  void sleepForAttempt(int attempt) {
    // exponential backoff, but let's bound it around 2min.
    // aggressive backoff because we're dealing with unusual cases.
    long delay = 1000L * (1L << Math.min(attempt, 7));
    try {
      Thread.sleep(delay);
      totalWaitTime += delay;
    } catch (InterruptedException iex) {
      // reset interrupt flag
      Thread.currentThread().interrupt();
    }
  }

  /**
   * @param exs StorageException to test
   * @return true if exs is a retryable error, otherwise false
   */
  @VisibleForTesting
  boolean isRetryable(final StorageException exs) {
    if (exs.isRetryable()) {
      return true;
    }
    for (int code : config.retryableHttpCodes()) {
      if (exs.getCode() == code) {
        return true;
      }
    }
    return false;
  }

  /**
   * @param exs StorageException to test
   * @return true if exs is an error that can be resolved via a channel reopen, otherwise false
   */
  @VisibleForTesting
  boolean isReopenable(final StorageException exs) {
    Throwable throwable = exs;
    // ensures finite iteration
    int maxDepth = 20;
    while (throwable != null && maxDepth-- > 0) {
      for (Class<? extends Exception> reopenableException : config.reopenableExceptions()) {
        if (reopenableException.isInstance(throwable)) {
          return true;
        }
      }
      if (throwable.getMessage() != null
          && throwable.getMessage().contains("Connection closed prematurely")) {
        return true;
      }
      throwable = throwable.getCause();
    }
    return false;
  }
}

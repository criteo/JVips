/*
  Copyright (c) 2019 Criteo

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package com.criteo.vips;

public class VipsContext extends Vips {
    /**
     * Output leak information like GObject liveness
     *
     * @param enable Set to True for enabling information
     */
    public static native void setLeak(boolean enable);

    /**
     * Set the number of worker threads that vips should use when running a VipsThreadPool
     *
     * @param concurrency 0 means "default", the number of threads available on the host machine
     */
    public static native void setConcurrency(int concurrency);

    /**
     * Get the number of worker threads that vips should use when running a VipsThreadPool
     *
     * @return thread number
     */
    public static native int getConcurrency();

    /**
     * Set the maximum number of operations we keep in cache
     *
     * @param max maximum number of operation to cache
     */
    public static native void setMaxCache(int max);

    /**
     * Get the maximum number of operations we keep in cache
     *
     * @return maximum number of operation to cache
     */
    public static native int getMaxCache();

    /**
     * Set the maximum amount of tracked memory we allow before we start dropping cached operations
     *
     * @param max_mem maximum amound of tracked memory we use
     */
    public static native void setMaxCacheMem(int max_mem);

    /**
     * Get the maximum amount of tracked memory we allow before we start dropping cached operations
     *
     * @return maximum amound of tracked memory we use
     */
    public static native int getMaxCacheMem();

    /**
     * Shutdown vips context
     */
    public static native void shutdown();
}

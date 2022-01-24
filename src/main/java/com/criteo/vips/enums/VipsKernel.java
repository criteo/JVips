/*
  Copyright (c) 2020 Criteo
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

package com.criteo.vips.enums;

import java.util.HashMap;
import java.util.Map;

public enum VipsKernel {
    // The nearest pixel to the point.
    Nearest(0),
    // Convolve with a triangle filter. 
    Linear(1),
    // Convolve with a cubic filter. 
    Cubic(2),
    // Convolve with a Mitchell kernel.
    Mitchell(3),
    // Convolve with a two-lobe Lanczos kernel.
    Lanczos2(4),
    // Convolve with a three-lobe Lanczos kernel.
    Lanczos3(5),
    Last(6);

    private int value;
    private static Map map = new HashMap<VipsKernel, Integer>();

    private VipsKernel(int i) {
      value = i;
    }

    static {
        for (VipsKernel e : VipsKernel.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsKernel valueOf(int i) {
        return (VipsKernel) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

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

public enum VipsForeignPngFilter {
    // no filtering
    None(8),
    // difference to the left
    Sub(16),
    // difference up
    Up(32),
    // average of left and up
    Avg(64),
    // pick best neighbor predictor automatically
    Paeth(128),
    // adaptive
    All(248);

    private int value;
    private static Map<Integer, VipsForeignPngFilter> map = new HashMap<>();

    private VipsForeignPngFilter(int i) {
      value = i;
    }

    static {
        for (VipsForeignPngFilter e : VipsForeignPngFilter.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsForeignPngFilter valueOf(int i) {
        return (VipsForeignPngFilter) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

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
    NONE(8),
    // difference to the left
    SUB(16),
    // difference up
    UP(32),
    // average of left and up
    AVG(64),
    // pick best neighbor predictor automatically
    PAETH(128),
    // adaptive
    ALL(248);

    private int value;
    private static Map map = new HashMap<VipsForeignPngFilter, Integer>();

    VipsForeignPngFilter(int i) {
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

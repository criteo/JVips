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

public enum VipsSaveable {
    // 1 band (eg. CSV)
    Mono(0),
    // 1 or 3 bands (eg. PPM) 
    Rgb(1),
    // 1, 2, 3 or 4 bands (eg. PNG)
    Rgba(2),
    // 3 or 4 bands (eg. WEBP)
    RgbaOnly(3),
    // 1, 3 or 4 bands (eg. JPEG)
    RgbCmyk(4),
    // any number of bands (eg. TIFF)
    Any(5),
    Last(6);

    private int value;
    private static Map<Integer, VipsSaveable> map = new HashMap<>();

    private VipsSaveable(int i) {
      value = i;
    }

    static {
        for (VipsSaveable e : VipsSaveable.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsSaveable valueOf(int i) {
        return (VipsSaveable) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

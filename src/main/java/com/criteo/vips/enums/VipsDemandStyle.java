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

public enum VipsDemandStyle {
    Error(-1),
    // demand in small (typically 64x64 pixel) tiles
    Smalltile(0),
    // demand in fat (typically 10 pixel high) strips
    Fatstrip(1),
    // demand in thin (typically 1 pixel high) strips
    Thinstrip(2),
    // demand geometry does not matter
    Any(3);

    private int value;
    private static Map map = new HashMap<VipsDemandStyle, Integer>();

    private VipsDemandStyle(int i) {
      value = i;
    }

    static {
        for (VipsDemandStyle e : VipsDemandStyle.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsDemandStyle valueOf(int i) {
        return (VipsDemandStyle) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

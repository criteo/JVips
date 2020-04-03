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

public enum VipsCombineMode {
    // set pixels to the new value
    SET(0),
    // add pixels
    ADD(1),
    LAST(2);

    private int value;
    private static Map map = new HashMap<VipsCombineMode, Integer>();

    VipsCombineMode(int i) {
      value = i;
    }

    static {
        for (VipsCombineMode e : VipsCombineMode.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsCombineMode valueOf(int i) {
        return (VipsCombineMode) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

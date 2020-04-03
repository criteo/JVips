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

public enum VipsToken {
    // left bracket
    LEFT(1),
    // right bracket
    RIGHT(2),
    // string constant
    STRING(3),
    // equals sign
    EQUALS(4),
    // comma
    COMMA(5);

    private int value;
    private static Map map = new HashMap<VipsToken, Integer>();

    VipsToken(int i) {
      value = i;
    }

    static {
        for (VipsToken e : VipsToken.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsToken valueOf(int i) {
        return (VipsToken) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

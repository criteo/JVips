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

public enum VipsOperationMath2 {
    // pow( left, right )
    POW(0),
    // pow( right, left ) 
    WOP(1),
    LAST(2);

    private int value;
    private static Map map = new HashMap<VipsOperationMath2, Integer>();

    VipsOperationMath2(int i) {
      value = i;
    }

    static {
        for (VipsOperationMath2 e : VipsOperationMath2.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsOperationMath2 valueOf(int i) {
        return (VipsOperationMath2) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

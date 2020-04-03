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

public enum VipsOperationMath {
    // sin(), angles in degrees
    SIN(0),
    // cos(), angles in degrees
    COS(1),
    // tan(), angles in degrees
    TAN(2),
    // asin(), angles in degrees
    ASIN(3),
    // acos(), angles in degrees
    ACOS(4),
    // atan(), angles in degrees
    ATAN(5),
    // log base e 
    LOG(6),
    // log base 10 
    LOG10(7),
    // e to the something
    EXP(8),
    // 10 to the something
    EXP10(9),
    LAST(10);

    private int value;
    private static Map map = new HashMap<VipsOperationMath, Integer>();

    VipsOperationMath(int i) {
      value = i;
    }

    static {
        for (VipsOperationMath e : VipsOperationMath.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsOperationMath valueOf(int i) {
        return (VipsOperationMath) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

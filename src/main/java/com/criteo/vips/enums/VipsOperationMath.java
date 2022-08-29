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
    Sin(0),
    // cos(), angles in degrees
    Cos(1),
    // tan(), angles in degrees
    Tan(2),
    // asin(), angles in degrees
    Asin(3),
    // acos(), angles in degrees
    Acos(4),
    // atan(), angles in degrees
    Atan(5),
    // log base e 
    Log(6),
    // log base 10 
    Log10(7),
    // e to the something
    Exp(8),
    // 10 to the something
    Exp10(9),
    // sinh(), angles in radians
    Sinh(10),
    // cosh(), angles in radians
    Cosh(11),
    // tanh(), angles in radians
    Tanh(12),
    // asinh(), angles in radians
    Asinh(13),
    // acosh(), angles in radians
    Acosh(14),
    // atanh(), angles in radians
    Atanh(15),
    Last(16);

    private int value;
    private static Map<Integer, VipsOperationMath> map = new HashMap<>();

    private VipsOperationMath(int i) {
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

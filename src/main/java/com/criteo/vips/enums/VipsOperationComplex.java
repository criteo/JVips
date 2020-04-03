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

public enum VipsOperationComplex {
    // convert to polar coordinates
    POLAR(0),
    // convert to rectangular coordinates
    RECT(1),
    // complex conjugate
    CONJ(2),
    LAST(3);

    private int value;
    private static Map map = new HashMap<VipsOperationComplex, Integer>();

    VipsOperationComplex(int i) {
      value = i;
    }

    static {
        for (VipsOperationComplex e : VipsOperationComplex.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsOperationComplex valueOf(int i) {
        return (VipsOperationComplex) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

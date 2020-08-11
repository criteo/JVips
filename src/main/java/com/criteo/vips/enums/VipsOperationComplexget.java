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

public enum VipsOperationComplexget {
    // get real component
    Real(0),
    // get imaginary component
    Imag(1),
    Last(2);

    private int value;
    private static Map map = new HashMap<VipsOperationComplexget, Integer>();

    private VipsOperationComplexget(int i) {
      value = i;
    }

    static {
        for (VipsOperationComplexget e : VipsOperationComplexget.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsOperationComplexget valueOf(int i) {
        return (VipsOperationComplexget) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

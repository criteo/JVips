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

public enum VipsOperationRound {
    // round to nearest
    Rint(0),
    // the smallest integral value not less than
    Ceil(1),
    // largest integral value not greater than
    Floor(2),
    Last(3);

    private int value;
    private static Map map = new HashMap<VipsOperationRound, Integer>();

    private VipsOperationRound(int i) {
      value = i;
    }

    static {
        for (VipsOperationRound e : VipsOperationRound.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsOperationRound valueOf(int i) {
        return (VipsOperationRound) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

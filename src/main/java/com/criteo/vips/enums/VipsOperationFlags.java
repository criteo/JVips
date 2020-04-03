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

public enum VipsOperationFlags {
    // no flags
    NONE(0),
    // can work sequentially with a small buffer
    SEQUENTIAL(1),
    SEQUENTIAL_UNBUFFERED(2),
    // must not be cached
    NOCACHE(4),
    // a compatibility thing
    DEPRECATED(8);

    private int value;
    private static Map map = new HashMap<VipsOperationFlags, Integer>();

    VipsOperationFlags(int i) {
      value = i;
    }

    static {
        for (VipsOperationFlags e : VipsOperationFlags.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsOperationFlags valueOf(int i) {
        return (VipsOperationFlags) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

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

public enum VipsArgumentFlags {
    // no flags
    None(0),
    // must be set in the constructor
    Required(1),
    // can only be set in the constructor
    Construct(2),
    // can only be set once
    SetOnce(4),
    // don't do use-before-set checks
    SetAlways(8),
    // is an input argument (one we depend on)
    Input(16),
    // is an output argument (depends on us)
    Output(32),
    // just there for back-compat, hide 
    Deprecated(64),
    // the input argument will be modified
    Modify(128);

    private int value;
    private static Map map = new HashMap<VipsArgumentFlags, Integer>();

    private VipsArgumentFlags(int i) {
      value = i;
    }

    static {
        for (VipsArgumentFlags e : VipsArgumentFlags.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsArgumentFlags valueOf(int i) {
        return (VipsArgumentFlags) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

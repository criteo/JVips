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

public enum VipsPCS {
    // use CIELAB D65 as the Profile Connection Space
    csLab(0),
    // use XYZ as the Profile Connection Space
    csXyz(1),
    csLast(2);

    private int value;
    private static Map map = new HashMap<VipsPCS, Integer>();

    private VipsPCS(int i) {
      value = i;
    }

    static {
        for (VipsPCS e : VipsPCS.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsPCS valueOf(int i) {
        return (VipsPCS) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

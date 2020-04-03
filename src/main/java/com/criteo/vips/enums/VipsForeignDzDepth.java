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

public enum VipsForeignDzDepth {
    // create layers down to 1x1 pixel
    ONEPIXEL(0),
    // create layers down to 1x1 tile
    ONETILE(1),
    // only create a single layer
    ONE(2),
    LAST(3);

    private int value;
    private static Map map = new HashMap<VipsForeignDzDepth, Integer>();

    VipsForeignDzDepth(int i) {
      value = i;
    }

    static {
        for (VipsForeignDzDepth e : VipsForeignDzDepth.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsForeignDzDepth valueOf(int i) {
        return (VipsForeignDzDepth) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

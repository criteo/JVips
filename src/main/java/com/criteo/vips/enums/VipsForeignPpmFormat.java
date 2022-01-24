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

public enum VipsForeignPpmFormat {
    // portable bitmap
    Pbm(0),
    // portable greymap
    Pgm(1),
    // portable pixmap
    Ppm(2),
    // portable float map
    Pfm(3),
    Last(4);

    private int value;
    private static Map map = new HashMap<VipsForeignPpmFormat, Integer>();

    private VipsForeignPpmFormat(int i) {
      value = i;
    }

    static {
        for (VipsForeignPpmFormat e : VipsForeignPpmFormat.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsForeignPpmFormat valueOf(int i) {
        return (VipsForeignPpmFormat) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

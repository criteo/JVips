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

public enum VipsForeignDzLayout {
    // use DeepZoom directory layout
    Dz(0),
    // use Zoomify directory layout
    Zoomify(1),
    // use Google maps directory layout
    Google(2),
    // use IIIF v2 directory layout
    Iiif(3),
    // use IIIF v3 directory layout
    Iiif3(4),
    Last(5);

    private int value;
    private static Map<Integer, VipsForeignDzLayout> map = new HashMap<>();

    private VipsForeignDzLayout(int i) {
      value = i;
    }

    static {
        for (VipsForeignDzLayout e : VipsForeignDzLayout.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsForeignDzLayout valueOf(int i) {
        return (VipsForeignDzLayout) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

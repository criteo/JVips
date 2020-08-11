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

public enum VipsForeignDzContainer {
    // write tiles to the filesystem
    Fs(0),
    // write tiles to a zip file
    Zip(1),
    // write to a szi file
    Szi(2),
    Last(3);

    private int value;
    private static Map map = new HashMap<VipsForeignDzContainer, Integer>();

    private VipsForeignDzContainer(int i) {
      value = i;
    }

    static {
        for (VipsForeignDzContainer e : VipsForeignDzContainer.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsForeignDzContainer valueOf(int i) {
        return (VipsForeignDzContainer) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

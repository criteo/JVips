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

public enum VipsForeignFlags {
    // no flags set
    None(0),
    // the image may be read lazilly
    Partial(1),
    // image pixels are most-significant byte first
    Bigendian(2),
    // top-to-bottom lazy reading
    Sequential(4),
    All(7);

    private int value;
    private static Map map = new HashMap<VipsForeignFlags, Integer>();

    private VipsForeignFlags(int i) {
      value = i;
    }

    static {
        for (VipsForeignFlags e : VipsForeignFlags.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsForeignFlags valueOf(int i) {
        return (VipsForeignFlags) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

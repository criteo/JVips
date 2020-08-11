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

public enum VipsCompassDirection {
    // centre
    Centre(0),
    // north
    North(1),
    // east
    East(2),
    // south
    South(3),
    // west
    West(4),
    // north-east
    NorthEast(5),
    // south-east
    SouthEast(6),
    // south-west
    SouthWest(7),
    // north-west
    NorthWest(8),
    Last(9);

    private int value;
    private static Map map = new HashMap<VipsCompassDirection, Integer>();

    private VipsCompassDirection(int i) {
      value = i;
    }

    static {
        for (VipsCompassDirection e : VipsCompassDirection.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsCompassDirection valueOf(int i) {
        return (VipsCompassDirection) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

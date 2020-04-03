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

public enum VipsInteresting {
    // do nothing
    NONE(0),
    // just take the centre
    CENTRE(1),
    // use an entropy measure
    ENTROPY(2),
    // look for features likely to draw human attention
    ATTENTION(3),
    // position the crop towards the low coordinate
    LOW(4),
    // position the crop towards the high coordinate
    HIGH(5),
    LAST(6);

    private int value;
    private static Map map = new HashMap<VipsInteresting, Integer>();

    VipsInteresting(int i) {
      value = i;
    }

    static {
        for (VipsInteresting e : VipsInteresting.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsInteresting valueOf(int i) {
        return (VipsInteresting) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

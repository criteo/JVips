/*
  Copyright (c) 2019 Criteo

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

package com.criteo.vips;

import java.util.HashMap;
import java.util.Map;

public enum VipsBandFormat {
    NOTSET(-1),
    UCHAR(0),
    CHAR(1),
    USHORT(2),
    SHORT(3),
    UINT(4),
    INT(5),
    FLOAT(6),
    COMPLEX(7),
    DOUBLE(8),
    DPCOMPLEX(9),
    LAST(10);

    private final int value;

    private static final Map<Integer, VipsBandFormat> valueByVipsBandFormat = new HashMap<>();
    static {
        for (VipsBandFormat e : VipsBandFormat.values())
            valueByVipsBandFormat.put(e.getValue(), e);
    }

    VipsBandFormat(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }

    public static VipsBandFormat valueOf(int value) {
        if (value < 0 || value >= LAST.getValue())
            return NOTSET;
        return valueByVipsBandFormat.get(value);
    }
}

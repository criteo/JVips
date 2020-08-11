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

public enum VipsBandFormat {
    // invalid setting
    FormatNotset(-1),
    // unsigned char format
    FormatUchar(0),
    // char format
    FormatChar(1),
    // unsigned short format
    FormatUshort(2),
    // short format
    FormatShort(3),
    // unsigned int format
    FormatUint(4),
    // int format
    FormatInt(5),
    // float format
    FormatFloat(6),
    // complex (two floats) format
    FormatComplex(7),
    // double float format
    FormatDouble(8),
    // double complex (two double) format
    FormatDpcomplex(9),
    FormatLast(10);

    private int value;
    private static Map map = new HashMap<VipsBandFormat, Integer>();

    private VipsBandFormat(int i) {
      value = i;
    }

    static {
        for (VipsBandFormat e : VipsBandFormat.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsBandFormat valueOf(int i) {
        return (VipsBandFormat) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

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

public enum VipsInterpretation {
    ERROR(-1),
    MULTIBAND(0),
    B_W(1),
    HISTOGRAM(10),
    XYZ(12),
    LAB(13),
    CMYK(15),
    LABQ(16),
    RGB(17),
    CMC(18),
    LCH(19),
    LABS(21),
    sRGB(22),
    YXY(23),
    FOURIER(24),
    RGB16(25),
    GREY16(26),
    MATRIX(27),
    scRGB(28),
    HSV(29),
    LAST(30);

    private final int value;

    private static final Map<Integer, VipsInterpretation> valueByVipsInterpretation = new HashMap<>();
    static {
        for (VipsInterpretation e : VipsInterpretation.values())
            valueByVipsInterpretation.put(e.getValue(), e);

    }

    VipsInterpretation(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }

    public static VipsInterpretation valueOf(int value) {
        if (value < 0 || value >= LAST.getValue())
            return ERROR;
        return valueByVipsInterpretation.get(value);
    }
}
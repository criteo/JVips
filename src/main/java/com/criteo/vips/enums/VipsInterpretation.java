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

public enum VipsInterpretation {
    ERROR(-1),
    // generic many-band image
    MULTIBAND(0),
    // some kind of single-band image
    B_W(1),
    // a 1D image, eg. histogram or lookup table
    HISTOGRAM(10),
    // the first three bands are CIE XYZ 
    XYZ(12),
    // pixels are in CIE Lab space
    LAB(13),
    // the first four bands are in CMYK space
    CMYK(15),
    LABQ(16),
    // generic RGB space
    RGB(17),
    // a uniform colourspace based on CMC(1:1)
    CMC(18),
    // pixels are in CIE LCh space
    LCH(19),
    // CIE LAB coded as three signed 16-bit values
    LABS(21),
    // pixels are sRGB
    SRGB(22),
    // pixels are CIE Yxy
    YXY(23),
    // image is in fourier space
    FOURIER(24),
    // generic 16-bit RGB
    RGB16(25),
    // generic 16-bit mono
    GREY16(26),
    // a matrix
    MATRIX(27),
    // pixels are scRGB
    SCRGB(28),
    // pixels are HSV
    HSV(29),
    LAST(30);

    private int value;
    private static Map map = new HashMap<VipsInterpretation, Integer>();

    VipsInterpretation(int i) {
      value = i;
    }

    static {
        for (VipsInterpretation e : VipsInterpretation.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsInterpretation valueOf(int i) {
        return (VipsInterpretation) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

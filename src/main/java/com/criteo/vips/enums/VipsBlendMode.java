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

public enum VipsBlendMode {
    // where the second object is drawn, the first is removed
    CLEAR(0),
    // the second object is drawn as if nothing were below
    SOURCE(1),
    // the image shows what you would expect if you held two semi-transparent slides on top of each other
    OVER(2),
    // the first object is removed completely, the second is only drawn where the first was
    IN(3),
    // the second is drawn only where the first isn't
    OUT(4),
    // this leaves the first object mostly intact, but mixes both objects in the overlapping area
    ATOP(5),
    // leaves the first object untouched, the second is discarded completely
    DEST(6),
    // like OVER, but swaps the arguments
    DEST_OVER(7),
    // like IN, but swaps the arguments
    DEST_IN(8),
    // like OUT, but swaps the arguments
    DEST_OUT(9),
    // like ATOP, but swaps the arguments
    DEST_ATOP(10),
    // something like a difference operator
    XOR(11),
    // a bit like adding the two images
    ADD(12),
    // a bit like the darker of the two
    SATURATE(13),
    // at least as dark as the darker of the two inputs
    MULTIPLY(14),
    // at least as light as the lighter of the inputs
    SCREEN(15),
    // multiplies or screens colors, depending on the lightness
    OVERLAY(16),
    // the darker of each component
    DARKEN(17),
    // the lighter of each component
    LIGHTEN(18),
    // brighten first by a factor second
    COLOUR_DODGE(19),
    // darken first by a factor of second
    COLOUR_BURN(20),
    // multiply or screen, depending on lightness
    HARD_LIGHT(21),
    // darken or lighten, depending on lightness
    SOFT_LIGHT(22),
    // difference of the two
    DIFFERENCE(23),
    // somewhat like DIFFERENCE, but lower-contrast
    EXCLUSION(24),
    LAST(25);

    private int value;
    private static Map map = new HashMap<VipsBlendMode, Integer>();

    VipsBlendMode(int i) {
      value = i;
    }

    static {
        for (VipsBlendMode e : VipsBlendMode.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsBlendMode valueOf(int i) {
        return (VipsBlendMode) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

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
    Clear(0),
    // the second object is drawn as if nothing were below
    Source(1),
    // the image shows what you would expect if you held two semi-transparent slides on top of each other
    Over(2),
    // the first object is removed completely, the second is only drawn where the first was
    In(3),
    // the second is drawn only where the first isn't
    Out(4),
    // this leaves the first object mostly intact, but mixes both objects in the overlapping area
    Atop(5),
    // leaves the first object untouched, the second is discarded completely
    Dest(6),
    // like OVER, but swaps the arguments
    DestOver(7),
    // like IN, but swaps the arguments
    DestIn(8),
    // like OUT, but swaps the arguments
    DestOut(9),
    // like ATOP, but swaps the arguments
    DestAtop(10),
    // something like a difference operator
    Xor(11),
    // a bit like adding the two images
    Add(12),
    // a bit like the darker of the two
    Saturate(13),
    // at least as dark as the darker of the two inputs
    Multiply(14),
    // at least as light as the lighter of the inputs
    Screen(15),
    // multiplies or screens colors, depending on the lightness
    Overlay(16),
    // the darker of each component
    Darken(17),
    // the lighter of each component
    Lighten(18),
    // brighten first by a factor second
    ColourDodge(19),
    // darken first by a factor of second
    ColourBurn(20),
    // multiply or screen, depending on lightness
    HardLight(21),
    // darken or lighten, depending on lightness
    SoftLight(22),
    // difference of the two
    Difference(23),
    // somewhat like DIFFERENCE, but lower-contrast
    Exclusion(24),
    Last(25);

    private int value;
    private static Map<Integer, VipsBlendMode> map = new HashMap<>();

    private VipsBlendMode(int i) {
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

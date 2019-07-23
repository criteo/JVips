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

public enum Gravity {
    CENTRE(0),
    NORTH(1),
    EAST(2),
    SOUTH(3),
    WEST(4),
    NORTH_EAST(5),
    SOUTH_EAST(6),
    SOUTH_WEST(7),
    NORTH_WEST(8),
    LAST(9);

    private final int value;

    Gravity(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }
}

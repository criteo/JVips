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

public enum VipsForeignJpegSubsample {
    // default preset
    Auto(0),
    // always perform subsampling
    On(1),
    // never perform subsampling
    Off(2),
    Last(3);

    private int value;
    private static Map<Integer, VipsForeignJpegSubsample> map = new HashMap<>();

    private VipsForeignJpegSubsample(int i) {
      value = i;
    }

    static {
        for (VipsForeignJpegSubsample e : VipsForeignJpegSubsample.values()) {
            map.put(e.value, e);
        }
    }

    public static VipsForeignJpegSubsample valueOf(int i) {
        return (VipsForeignJpegSubsample) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

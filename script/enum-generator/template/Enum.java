$license

package com.criteo.vips.enums;

import java.util.HashMap;
import java.util.Map;

public enum $name {
$values;

    private int value;
    private static Map map = new HashMap<$name, Integer>();

    private $name(int i) {
      value = i;
    }

    static {
        for ($name e : $name.values()) {
            map.put(e.value, e);
        }
    }

    public static $name valueOf(int i) {
        return ($name) map.get(i);
    }

    public int getValue() {
      return value;
    }
}

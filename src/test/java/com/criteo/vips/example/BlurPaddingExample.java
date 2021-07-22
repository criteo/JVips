/*
  Copyright (c) 2021 Criteo

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

package com.criteo.vips.example;

import com.criteo.vips.VipsContext;
import com.criteo.vips.VipsException;
import com.criteo.vips.VipsImage;
import com.criteo.vips.VipsTestUtils;
import com.criteo.vips.enums.VipsDirection;

public class BlurPaddingExample {
    private static VipsImage padWithBlur(VipsImage img, int paddingSize, VipsDirection direction) throws VipsException {
        int width = img.getWidth();
        int height = img.getHeight();
        int borderLeft = direction == VipsDirection.Horizontal ? width - paddingSize : 0;
        int borderTop = direction == VipsDirection.Horizontal ? 0 : height - paddingSize;
        int borderWidth = direction == VipsDirection.Horizontal ? paddingSize : width;
        int borderHeight = direction == VipsDirection.Horizontal ? height : paddingSize;

        try (VipsImage blur = img.clone()) {
            blur.gaussblur(16.0, 0.2);
            try (VipsImage border1 = blur.extractArea(0, 0, borderWidth, borderHeight);
                 VipsImage border2 = blur.extractArea(borderLeft, borderTop, borderWidth, borderHeight);
                 VipsImage concat1 = VipsImage.join(border1, img, direction)) {
                return VipsImage.join(concat1, border2, direction);
            }
        }
    }

    public static void main(String[] args) {
        // Set vips memory leak report at exit
        VipsContext.setLeak(true);
        String tempDir = System.getProperty("java.io.tmpdir");
        String filename = VipsTestUtils.getRessourcePath("in_vips.jpg");
        VipsDirection direction = VipsDirection.Horizontal;
        try (VipsImage img = new VipsImage(filename)) {
            int paddingSize = direction == VipsDirection.Horizontal ? img.getWidth() / 4 : img.getHeight() / 4;
            try (VipsImage output = padWithBlur(img, paddingSize, direction)) {
                output.writeToFile(tempDir + "/pad_with_blur.jpg");
            }
        } catch (VipsException e) {
            e.printStackTrace();
        }
    }
}

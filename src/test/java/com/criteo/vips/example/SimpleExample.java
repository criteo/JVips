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

package com.criteo.vips.example;

import com.criteo.vips.VipsContext;
import com.criteo.vips.VipsException;
import com.criteo.vips.VipsImage;
import com.criteo.vips.VipsTestUtils;
import com.criteo.vips.enums.VipsImageFormat;

import java.awt.*;
import java.io.IOException;

public class SimpleExample {
    public static void main(String[] args) {
        // Set vips memory leak report at exit
        VipsContext.setLeak(true);
        try {
            byte[] contents = VipsTestUtils.getByteArray("in_vips.jpg");
            VipsImage image = new VipsImage(contents, contents.length);
            int width = image.getWidth();
            int height = image.getHeight();

            image.thumbnailImage(new Dimension(width / 2, height / 2), true);
            System.out.println(String.format("Image has been correctly resized: (%d,%d) -> (%d,%d)",
                    width, height, image.getWidth(), image.getHeight()));
            contents = image.writeToArray(VipsImageFormat.JPG, false);

            // Release object reference and resources
            image.release();

        } catch (IOException |  VipsException e) {
            e.printStackTrace();
        }
    }
}

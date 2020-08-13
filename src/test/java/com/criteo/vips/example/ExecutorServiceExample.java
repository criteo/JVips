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

import com.criteo.vips.PixelPacket;
import com.criteo.vips.VipsContext;
import com.criteo.vips.VipsImage;
import com.criteo.vips.VipsTestUtils;
import com.criteo.vips.enums.VipsCompassDirection;
import com.criteo.vips.enums.VipsImageFormat;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceExample {
    public static void main(String[] args) {
        // Set vips memory leak report at exit
        VipsContext.setLeak(true);
        try {
            final byte[] contents = VipsTestUtils.getByteArray("in_vips.jpg");
            int numCpu = 8;
            int numCall = 256;
            ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(numCpu);
            List<Callable<Void>> callables = new ArrayList();

            for (int i = 0; i < numCall; ++i)
                callables.add(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        try (VipsImage image = new VipsImage(contents.clone(), contents.length);) {
                            image.resize(new Dimension(256, 256), true);
                            image.pad(new Dimension(512, 512),
                                    new PixelPacket(255, 255, 255),
                                    VipsCompassDirection.Centre);
                            byte[] out = image.writeToArray(VipsImageFormat.JPG, false);
                        }
                        return null;
                    }
                });

            executor.invokeAll(callables);
            executor.shutdown();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

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

package com.criteo.vips.benchmark;
import com.criteo.vips.PixelPacket;
import com.criteo.vips.VipsContext;
import com.criteo.vips.VipsImage;
import com.criteo.vips.enums.VipsCompassDirection;
import com.criteo.vips.enums.VipsImageFormat;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class SimpleBenchmark {
    private static Dimension resizeTarget = new Dimension(512, 512);
    private static Rectangle cropTarget = new Rectangle(128, 128, 128, 128);
    private static Dimension padTarget = new Dimension(256, 256);
    private static PixelPacket pixelPacket = new PixelPacket(255.0, 255.0, 255.0);

    @Test
    public void TestBenchmark() throws Exception {

        Options opt = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .mode (Mode.AverageTime)
                .timeUnit(TimeUnit.MILLISECONDS)
                .warmupTime(TimeValue.seconds(1))
                .warmupIterations(3)
                .measurementTime(TimeValue.seconds(1))
                .measurementIterations(20)
                .threads(2)
                .forks(1)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Thread)
    public static class BenchmarkState
    {
        byte[] content;

        @Setup(Level.Trial)
        public void initialize() throws IOException {
            ClassLoader classLoader = SimpleBenchmark.class.getClassLoader();
            String path = classLoader.getResource("in_vips.jpg").getFile();
            content = Files.readAllBytes(new File(path).toPath());

            VipsContext.setMaxCache(0);
        }
    }

    @Benchmark
    public void ResizeCropPad(BenchmarkState state, Blackhole bh) {
        VipsImage img = new VipsImage(state.content, state.content.length);

        img.resize(resizeTarget, false);
        img.crop(cropTarget);
        img.pad(padTarget, pixelPacket, VipsCompassDirection.CENTRE);
        byte[] out = img.writeToArray(VipsImageFormat.JPG, 80, false);
        img.release();
    }
}

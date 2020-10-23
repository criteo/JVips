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

import com.criteo.vips.enums.*;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Ignore;		
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static com.criteo.vips.VipsImage.JPGQuality;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(Theories.class)
public class VipsImageTest {
    private static double Delta = 0.0001;
    private static double TrimThreshold = 30.0;
    private static PixelPacket WhitePixel = new PixelPacket(255.0, 255.0, 255.0);
    private static PixelPacket TransparentPixel = new PixelPacket(255.0, 255.0, 255.0, 0.0);

    private static Map<String, byte[]> SignatureByExtension = new HashMap<>();

    static {
        SignatureByExtension.put(".jpg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});
        SignatureByExtension.put(".png", new byte[]{(byte) 137, 80, 78, 71, 13, 10, 26, 10});
        SignatureByExtension.put(".webp", new byte[]{'R', 'I', 'F', 'F'});
        SignatureByExtension.put(".gif", new byte[]{'G', 'I', 'F'});
    }

    @DataPoints("filenames")
    public static String[] Filenames = {
            "in_vips.jpg",
            "transparent.png",
            "logo.webp",
            "cat.gif",
            "02.gif"
    };

    static class DominantColour {
        final String filename;
        final int red;
        final int green;
        final int blue;

        @Override
        public String toString() {
            return filename + " RGB " + red + " " + green + " " + blue;
        }

        DominantColour(String f, int red, int green, int blue) {
            filename = f;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
    }

    @DataPoints("fileColours")
    public static DominantColour[] FileColours = {
            new DominantColour("11-red-square-600-400.png", 237, 237, 237),
            new DominantColour("in_vips.jpg", 12, 112, 187),
            new DominantColour("monochrome.jpg", 237, 237, 237),
    };

    @BeforeClass

    public static void onceExecutedBeforeAll() {
        VipsContext.setLeak(true);
        VipsContext.setMaxCache(0);
        VipsContext.setMaxCacheMem(0);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Theory
    public void TestShouldOpenCorrectly(@FromDataPoints("filenames") String filename) throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer(filename);
        byte[] bufferArray = VipsTestUtils.getByteArray(filename);
        try (VipsImage imgFromBuffer = new VipsImage(buffer, buffer.capacity());
             VipsImage imgFromByteArray = new VipsImage(bufferArray, bufferArray.length)) {
            assertNotNull(imgFromBuffer);
            assertNotNull(imgFromByteArray);
        }
    }

    @Theory
    public void TestShouldOpenCorrectlyFromVipsImage(@FromDataPoints("filenames") String filename) throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer(filename);
        try (VipsImage img = new VipsImage(buffer, buffer.capacity());
             VipsImage copy = new VipsImage(img, WhitePixel)) {
            assertEquals(WhitePixel, copy.getPointPixelPacket(new Point(0, 0)));
            assertEquals(img.getWidth(), copy.getWidth());
            assertEquals(img.getHeight(), copy.getHeight());
        }
    }

    @Theory
    public void TestWriteFromDirectByteBufferShouldNotThrows(@FromDataPoints("filenames") String filename,
                                                             VipsImageFormat output,
                                                             boolean strip) throws IOException, VipsException {
        Assume.assumeTrue(output != VipsImageFormat.GIF);
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer(filename);
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            byte[] out = img.writeToArray(output, JPGQuality, strip);
            assertNotNull(out);
        }
    }

    @Theory
    public void TestWriteFromByteArrayShouldNotThrows(@FromDataPoints("filenames") String filename,
                                                      VipsImageFormat output,
                                                      boolean strip) throws IOException, VipsException {
        Assume.assumeTrue(output != VipsImageFormat.GIF);
        byte[] buffer = VipsTestUtils.getByteArray(filename);
        try (VipsImage img = new VipsImage(buffer, buffer.length)) {
            byte[] out = img.writeToArray(output, JPGQuality, strip);
            assertNotNull(out);
        }
    }

    // Ignoring because it crashes Surefire on macOS.
    @Ignore
    @Theory
    public void TestHistFindNdim1(@FromDataPoints("filenames") String filename) throws IOException, VipsException {
        byte[] buffer = VipsTestUtils.getByteArray(filename);
        try (VipsImage img = new VipsImage(buffer, buffer.length)) {
            img.histFindNdim(1);
            assertEquals(1, img.getWidth());
            assertEquals(1, img.getHeight());
        }
    }

    @Ignore
    @Theory
    public void TestHistFindNdim2(@FromDataPoints("filenames") String filename) throws IOException, VipsException {
        byte[] buffer = VipsTestUtils.getByteArray(filename);
        try (VipsImage img = new VipsImage(buffer, buffer.length)) {
            img.histFindNdim(2);
            assertEquals(2, img.getWidth());
            assertEquals(2, img.getHeight());
        }
    }

    @Ignore
    @Theory
    public void TestHistFindNdim10(@FromDataPoints("filenames") String filename) throws IOException, VipsException {
        byte[] buffer = VipsTestUtils.getByteArray(filename);
        try (VipsImage img = new VipsImage(buffer, buffer.length)) {
            img.histFindNdim(10);
            assertEquals(10, img.getWidth());
            assertEquals(10, img.getHeight());
        }
    }

    @Theory
    public void TestCastUchar(@FromDataPoints("filenames") String filename) throws IOException, VipsException {
        byte[] buffer = VipsTestUtils.getByteArray(filename);
        try (VipsImage img = new VipsImage(buffer, buffer.length)) {
            assertNotEquals(img.imageGetFormat(), VipsBandFormat.FormatNotset);
            img.cast(VipsBandFormat.FormatDouble);
            assertEquals(img.imageGetFormat(), VipsBandFormat.FormatDouble);
            img.castUchar(true);
            assertEquals(img.imageGetFormat(), VipsBandFormat.FormatUchar); // also checks that VipsBandFormat.UCHAR is correct
            img.castUchar();
            assertEquals(img.imageGetFormat(), VipsBandFormat.FormatUchar);
        }
    }

    @Theory
    public void TestCast(@FromDataPoints("filenames") String filename) throws IOException, VipsException {
        byte[] buffer = VipsTestUtils.getByteArray(filename);
        try (VipsImage img = new VipsImage(buffer, buffer.length)) {
            assertNotEquals(img.imageGetFormat(), VipsBandFormat.FormatNotset);
            img.cast(VipsBandFormat.FormatUchar, true);
            assertEquals(img.imageGetFormat(), VipsBandFormat.FormatUchar);
            img.cast(VipsBandFormat.FormatShort);
            assertEquals(img.imageGetFormat(), VipsBandFormat.FormatShort);
            img.cast(VipsBandFormat.FormatFloat);
            assertEquals(img.imageGetFormat(), VipsBandFormat.FormatFloat);
        }
    }

    @Test
    public void TestVipsBandFormat() {
        assertEquals(0, VipsBandFormat.FormatUchar.getValue());
        assertEquals(0, VipsBandFormat.valueOf(0).getValue());
        assertEquals(9, VipsBandFormat.FormatDpcomplex.getValue());
        assertEquals(VipsBandFormat.valueOf(9), VipsBandFormat.FormatDpcomplex);
    }

    @Test
    public void TestImageGetInterpretationB_W() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("monochrome.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            VipsInterpretation colourSpace = VipsInterpretation.BW;
            assertEquals(colourSpace, img.imageGetInterpretation());
        }
    }

    @Test
    public void TestImageGetInterpretationCMYK() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips_cmyk.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            VipsInterpretation colourSpace = VipsInterpretation.Cmyk;
            assertEquals(colourSpace, img.imageGetInterpretation());
        }
    }

    @Test
    public void TestChangeColourSpace() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            VipsInterpretation beforeColourSpace = VipsInterpretation.Srgb;
            VipsInterpretation afterColourSpace = VipsInterpretation.Cmyk;

            assertEquals(beforeColourSpace, img.imageGetInterpretation());
            img.colourspace(afterColourSpace);
            assertEquals(afterColourSpace, img.imageGetInterpretation());
        }
    }

    @Test
    public void TestChangeColourSpaceWithSourceSpace() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            VipsInterpretation beforeColourSpace = VipsInterpretation.Srgb;
            VipsInterpretation afterColourSpace = VipsInterpretation.Cmyk;

            assertEquals(beforeColourSpace, img.imageGetInterpretation());
            img.colourspace(afterColourSpace, beforeColourSpace);
            assertEquals(afterColourSpace, img.imageGetInterpretation());
        }
    }

    @Test
    public void TestReturnCorrectDimensions() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        int expectedWidth = 1920;
        int expectedHeight = 1080;
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            assertEquals(expectedWidth, img.getWidth());
            assertEquals(expectedHeight, img.getHeight());
        }
    }

    @Test
    public void TestShouldReturnCorrectFirstPixelValue() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            PixelPacket pixel = img.getPointPixelPacket(new Point(0, 0));
            PixelPacket expected = new PixelPacket(0.0, 81.0, 216.0, 255.0);
            assertEquals(expected, pixel);
        }
    }

    @Test
    public void TestShouldHandleTransparentPixel() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("transparent.png");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {

            PixelPacket pixel = img.getPointPixelPacket(new Point(0, 0));
            PixelPacket expected = new PixelPacket(0.0, 0.0, 0.0, 0.0);
            assertEquals(expected, pixel);
        }
    }

    @Test
    public void TestShouldResizeAndKeepAspectRatio() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            double aspectRatio = (double) img.getWidth() / (double) img.getHeight();
            img.resize(new Dimension(800, 800), false);
            assertEquals(800, img.getWidth());
            assertEquals(450, img.getHeight());
            assertEquals(aspectRatio, (double) img.getWidth() / (double) img.getHeight(), Delta);
        }
    }

    @Test
    public void TestShouldResizeWithExactDimension() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            img.resize(new Dimension(800, 800), true);
            assertEquals(800, img.getWidth());
            assertEquals(800, img.getHeight());
        }
    }

    @Test
    public void TestShouldFlattenTransparentBackground() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("transparent.png");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            PixelPacket blue = new PixelPacket(0, 0, 255);

            img.flatten(blue);
            PixelPacket topLeft = img.getPointPixelPacket(new Point(0, 0));
            assertEquals(topLeft.r, blue.r, Delta);
            assertEquals(topLeft.g, blue.g, Delta);
            assertEquals(topLeft.b, blue.b, Delta);
        }
    }

    @Test
    public void TestShouldCropCorrectly() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            img.crop(new Rectangle(0, 0, 50, 50));
            assertEquals(50, img.getWidth());
            assertEquals(50, img.getHeight());
        }
    }

    @Test
    public void TestShouldNotCropIfOutOfBoundCropBox() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            int w = img.getWidth();
            int h = img.getHeight();
            Rectangle box = new Rectangle(0, 0, w + 1, h + 1);

            img.crop(box);
            fail("Should throw VipsException");
        } catch (VipsException e) {
            // Expected
        }
    }

    @Test
    public void TestShouldNotCropIfZeroWidthAndHeightBox() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("white.png");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            int w = img.getWidth();
            int h = img.getHeight();
            Rectangle box = new Rectangle(w, h, 0, 0);

            img.crop(box);
            fail("Should throw VipsException");
        } catch (VipsException e) {
            // Expected
        }
    }

    @Test
    public void TestShouldPadCorrectly() throws IOException, VipsException {
        PixelPacket pixel = new PixelPacket(255.0, 255.0, 255.0, 255.0);
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            int w = img.getWidth();
            int h = img.getHeight();
            // Surround image with a 10-pixel white band
            img.pad(new Dimension(w + 20, h + 20), pixel, VipsCompassDirection.Centre);
            assertEquals(w + 20, img.getWidth());
            assertEquals(h + 20, img.getHeight());
            assertEquals(pixel, img.getPointPixelPacket(new Point(0, 0)));
            assertEquals(pixel, img.getPointPixelPacket(new Point(img.getWidth() - 1, 0)));
            assertEquals(pixel, img.getPointPixelPacket(new Point(0, img.getHeight() - 1)));
            assertEquals(pixel, img.getPointPixelPacket(new Point(img.getWidth() - 1, img.getHeight() - 1)));
        }
    }

    @Test
    public void TestShouldPadPNGWithTransparentPixel() throws IOException, VipsException {
        PixelPacket pixel = new PixelPacket(0.0, 0.0, 255.0, 50.0);
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("transparent.png");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {

            // Surround image with a 10-pixel blue band
            img.pad(new Dimension(img.getWidth() + 20, img.getHeight() + 20), pixel, VipsCompassDirection.Centre);
            assertEquals(pixel, img.getPointPixelPacket(new Point(0, 0)));
            assertEquals(pixel, img.getPointPixelPacket(new Point(img.getWidth() - 1, 0)));
            assertEquals(pixel, img.getPointPixelPacket(new Point(0, img.getHeight() - 1)));
            assertEquals(pixel, img.getPointPixelPacket(new Point(img.getWidth() - 1, img.getHeight() - 1)));
        }
    }

    @Test
    public void TestShouldPadJPGAndIgnoreAlphaChannel() throws IOException, VipsException {
        PixelPacket expected = new PixelPacket(0.0, 0.0, 255.0, 255.0);
        PixelPacket pixel = new PixelPacket(0.0, 0.0, 255.0, 50.0);
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            // Surround image with a 10-pixel blue band
            img.pad(new Dimension(img.getWidth() + 20, img.getHeight() + 20), pixel, VipsCompassDirection.Centre);
            assertEquals(expected, img.getPointPixelPacket(new Point(0, 0)));
            assertEquals(expected, img.getPointPixelPacket(new Point(img.getWidth() - 1, 0)));
            assertEquals(expected, img.getPointPixelPacket(new Point(0, img.getHeight() - 1)));
            assertEquals(expected, img.getPointPixelPacket(new Point(img.getWidth() - 1, img.getHeight() - 1)));
        }
    }

    @Test
    public void TestShouldReturnNullArrayIfGetPointFailed() throws IOException, VipsException {
        thrown.expect(VipsException.class);
        thrown.expectMessage("Unable to get image point");
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("transparent.png");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            int w = img.getWidth();
            int h = img.getHeight();
            // Try to access out of bound
            img.getPointPixelPacket(new Point(w + 1, h + 1));
        }
    }

    @Test
    public void TestShouldGetPointMonochrome() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("monochrome.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            PixelPacket pixel = img.getPointPixelPacket(new Point(0, 0));
            assertEquals(255.0, pixel.getRed(), Delta);
            assertEquals(255.0, pixel.getGreen(), Delta);
            assertEquals(255.0, pixel.getBlue(), Delta);
            assertEquals(255.0, pixel.getAlpha(), Delta);
        }
    }

    @Test
    public void TestShouldGetPointMonochromeWithTransparency() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("monochrome_with_transparency.png");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {

            int w = img.getWidth();
            int h = img.getHeight();
            PixelPacket transparentPixel = img.getPointPixelPacket(new Point(0, 0));
            // Image center is not transparent
            PixelPacket pixel = img.getPointPixelPacket(new Point(w / 2, h / 2));
            assertEquals(255.0, transparentPixel.getRed(), Delta);
            assertEquals(255.0, transparentPixel.getGreen(), Delta);
            assertEquals(255.0, transparentPixel.getBlue(), Delta);
            assertEquals(0.0, transparentPixel.getAlpha(), Delta);
            assertEquals(82.0, pixel.getRed(), Delta);
            assertEquals(82.0, pixel.getGreen(), Delta);
            assertEquals(82.0, pixel.getBlue(), Delta);
            assertEquals(255.0, pixel.getAlpha(), Delta);
        }
    }

    @Test
    public void TestTransparentPNGShouldHasTransparency() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("transparent.png");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            assertTrue(img.hasAlpha());
        }
    }

    @Test
    public void TestJpgShouldNotHasTransparency() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            assertFalse(img.hasAlpha());
        }
    }

    @Theory
    public void TestShouldWriteToArrayHasCorrectHeaderSignature(String filename, VipsImageFormat vipsImageFormat) throws IOException, VipsException {
        // libvips can't save into gif format
        Assume.assumeTrue(vipsImageFormat != VipsImageFormat.GIF);
        byte[] expected = SignatureByExtension.get(vipsImageFormat.getFileExtension());
        byte[] signature = new byte[expected.length];
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer(filename);
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            byte[] out = img.writeToArray(vipsImageFormat, JPGQuality, true);
            System.arraycopy(out, 0, signature, 0, signature.length);
            assertArrayEquals(expected, signature);
        }
    }

    @Test
    public void TestShouldFindTrim() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("logo_with_padding_50x50.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            Rectangle boundingBox = img.findTrim(TrimThreshold, WhitePixel);
            assertEquals(50, boundingBox.x);
            assertEquals(50, boundingBox.y);
            assertEquals(640, boundingBox.width);
            assertEquals(640, boundingBox.height);
        }
    }

    @Test
    public void TestShouldFindTrimWithTransparent() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("logo_with_transparent_padding_50x50.png");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            Rectangle boundingBox = img.findTrim(TrimThreshold, TransparentPixel);
            assertEquals(50, boundingBox.x);
            assertEquals(50, boundingBox.y);
            assertEquals(640, boundingBox.width);
            assertEquals(640, boundingBox.height);
        }
    }

    @Theory
    public void TestSimplePipelineShouldNotThrow(String filename, VipsImageFormat vipsImageFormat) throws IOException, VipsException {
        // libvips can't save into gif format
        Assume.assumeTrue(vipsImageFormat != VipsImageFormat.GIF);
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer(filename);
        PixelPacket pixel = new PixelPacket(5.0, 255.0, 25.0);
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            img.resize(new Dimension(400, 400), false);
            int width = img.getWidth();
            int height = img.getHeight();
            img.crop(new Rectangle(10, 10, width - 10, height - 10));
            width = img.getWidth();
            height = img.getHeight();
            img.pad(new Dimension(width + 10, height + 10), pixel, VipsCompassDirection.Centre);

            byte[] out = img.writeToArray(vipsImageFormat, JPGQuality, true);
            assertNotNull(out);
        }
    }

    @Theory
    public void TestShouldThrowAnExceptionOnCorruptedPng(VipsImageFormat vipsImageFormat) throws IOException, VipsException {
        // libvips can't save into gif format
        Assume.assumeTrue(vipsImageFormat != VipsImageFormat.GIF);
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("olin.png");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            byte[] out = img.writeToArray(vipsImageFormat, JPGQuality, true);
            fail();
        } catch (VipsException e) {
            // Should throw a vips exception
        }
    }

    @Test
    public void TestShouldInsertSubImage() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("logo_with_transparent_padding_50x50.png");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity());
             VipsImage background = new VipsImage(img, WhitePixel)) {
            background.compose(img);
            assertEquals(WhitePixel, background.getPointPixelPacket(new Point(0, 0)));
        }
    }

    @Test
    public void TestGetPointShouldReturn256bitsValue() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("white_48_bits.png");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            PixelPacket p = img.getPointPixelPacket(new Point(0, 0));
            assertEquals(255.0, p.getRed(), Delta);
            assertEquals(255.0, p.getGreen(), Delta);
            assertEquals(255.0, p.getBlue(), Delta);
        }
    }

    @Test
    public void TestShouldGuessColourspace() throws IOException, VipsException {
        ByteBuffer bufferSrgb = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        ByteBuffer bufferCmyk = VipsTestUtils.getDirectByteBuffer("in_vips_cmyk.jpg");
        try (VipsImage imgSrgb = new VipsImage(bufferSrgb, bufferSrgb.capacity());
             VipsImage imgCmyk = new VipsImage(bufferCmyk, bufferCmyk.capacity())) {
            Assert.assertEquals(VipsInterpretation.Srgb, imgSrgb.getInterpretation());
            Assert.assertEquals(VipsInterpretation.Cmyk, imgCmyk.getInterpretation());
        }
    }

    @Test
    public void TestShouldReturnExactFrameNumberFromAnimatedImage() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("cat.gif");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            assertEquals(5, img.getNbFrame());
        }
    }

    @Test
    public void TestShouldReturnExactFrameNumberFromNonAnimatedImage() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            assertEquals(1, img.getNbFrame());
        }
    }

    @Test
    public void TestImageWithNoMetadataReturOneFrameNumber() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("02.gif");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            assertEquals(1, img.getNbFrame());
        }
    }

    @Test
    public void TestDoubleReleaseShouldNotThrow() throws IOException, VipsException {
        byte[] buffer = VipsTestUtils.getByteArray("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.length)) {
            byte[] out = img.writeToArray(VipsImageFormat.JPG, JPGQuality, true);
            assertNotNull(out);
            img.release();
            img.release();
        }
    }

    @Theory
    public void TestWritePNGFromByteArrayShouldNotThrows(@FromDataPoints("filenames") String filename,
                                                         boolean palette,
                                                         boolean strip)
            throws IOException, VipsException {
        byte[] buffer = VipsTestUtils.getByteArray(filename);
        int compression = 9;
        int colors = 256;
        try (VipsImage img = new VipsImage(buffer, buffer.length)) {
            byte[] out = img.writePNGToArray(compression, palette, colors, strip);
            assertNotNull(out);
        }
    }

    @Test
    public void TestWritePNGFromByteArrayShouldShrinkOutputSize()
            throws IOException, VipsException {
        byte[] buffer = VipsTestUtils.getByteArray("logo_with_transparent_padding_50x50.png");
        int compression = 9;
        int colors = 256;
        try (VipsImage img = new VipsImage(buffer, buffer.length)) {
            byte[] out = img.writePNGToArray(compression, true, colors, true);
            Assert.assertTrue(buffer.length > out.length);
        }
    }

    @Theory
    public void TestDominantColour(@FromDataPoints("fileColours") DominantColour fileColour) throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer(fileColour.filename);
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {

            // implement https://gist.github.com/jcupitt/ee3afcbb931b41b4d7f4
            int N_BINS = 10;
            int BIN_SIZE = 256 / N_BINS;
            img.colourspace(VipsInterpretation.Srgb);
            img.histFindNdim(N_BINS);
            Max1Result maxpos = img.max1();
            double[] pixel = img.getPoint(maxpos.x, maxpos.y);
            int band = IntStream.range(0, pixel.length)
                    .filter(i -> maxpos.out == pixel[i])
                    .findFirst()
                    .orElse(-1);
            int red = maxpos.x * BIN_SIZE + BIN_SIZE / 2;
            int green = maxpos.y * BIN_SIZE + BIN_SIZE / 2;
            int blue = band * BIN_SIZE + BIN_SIZE / 2;

            assertEquals(fileColour.red, red);
            assertEquals(fileColour.green, green);
            assertEquals(fileColour.blue, blue);
        }
    }

    @Test
    public void TestMax1() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            Max1Result r = img.max1();

            assertEquals(255.0, r.out, Delta);
        }
    }

    @Test
    public void TestHistFindNdimMax1() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        final int N_BINS = 10;
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            img.histFindNdim(N_BINS);

            Max1Result r = img.max1();

            assertEquals(239667.0, r.out, Delta);
            assertEquals(0, r.x);
            assertEquals(4, r.y);
        }
    }

    @Test
    public void TestGetPoint() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            double[] point = img.getPoint(33, 8);
            assertEquals(3, point.length);
            double[] expected = {0, 84, 219};
            assertEquals(expected[0], point[0], Delta);
            assertEquals(expected[1], point[1], Delta);
            assertEquals(expected[2], point[2], Delta);
            assertArrayEquals(expected, point, 0.0);
        }
    }

    @Test
    public void TestHistFindNdimMax1GetPoint() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            img.histFindNdim(10);
            Max1Result r = img.max1();
            assertEquals(239667.0, r.out, Delta);
            assertEquals(0, r.x);
            assertEquals(4, r.y);

            double[] point = img.getPoint(r.x, r.y);
            assertEquals(10, point.length);
            double[] expected = {0, 0, 15, 226, 1966, 10993, 101474, 239667, 61531, 1693};
            assertArrayEquals(expected, point, 0.0);
        }
    }

    @Test
    public void TestGetPointLinear() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        int x = 39;
        int y = 43;
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            assertArrayEquals(new double[]{5.0, 81.0, 218.0}, img.getPoint(x, y), 0.0);
            img.linear(new double[]{3.2, 1.0, 5}, new double[]{4.3, 1.2, 6.7});
            assertArrayEquals(new double[]{20.299999237060547, 82.19999694824219, 1096.699951171875}, img.getPoint(x, y), 0.0);
        }
    }

    @Test
    public void TestGetPointLinearUchar() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        int x = 39;
        int y = 43;
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            assertArrayEquals(new double[]{5.0, 81.0, 218.0}, img.getPoint(x, y), 0.0);
            img.linear(new double[]{3.2, 1.0, 5}, new double[]{4.3, 1.2, 6.7}, true);
            assertArrayEquals(new double[]{20.0, 82.0, 255.0}, img.getPoint(x, y), 0.0);
        }
    }

    @Test
    public void TestLinearThrowsIfArraySizeAreNotEqual() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("in_vips.jpg");
        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            img.linear(new double[]{3.2, 1.0, 5}, new double[]{1.2, 6.7});
            fail();
        } catch (VipsException e) {
            // Expected
        }
    }

    @Test
    public void TestBlack() throws IOException, VipsException {
        int width = 13;
        int height = 6;
        double[] expected = new double[]{0};
        try (VipsImage img = VipsImage.black(width, height)) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    assertArrayEquals(expected, img.getPoint(x, y), 0);
                }
            }
        }
    }

    @Test
    public void TestRemoveAutorotAngle() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("exif_rotate_90_cw.jpg");

        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            assertEquals(VipsAngle.D90, img.getAutorotAngle());
            img.removeAutorotAngle();
            assertEquals(VipsAngle.D0, img.getAutorotAngle());
        }
    }

    @Test
    public void TestExif90CWAutorot() throws IOException, VipsException {
        ByteBuffer buffer = VipsTestUtils.getDirectByteBuffer("exif_rotate_90_cw.jpg");

        try (VipsImage img = new VipsImage(buffer, buffer.capacity())) {
            img.autorot();
            assertEquals(WhitePixel, img.getPointPixelPacket(0, 0));
        }
    }

    @Test
    public void TestCreateVipsImageFromFile() throws IOException, VipsException {
        String filename = VipsTestUtils.getRessourcePath("in_vips.jpg");
        int expectedBands = 3;

        try (VipsImage img = new VipsImage(filename)) {
            // Dummy assert to check if the VipsImage has been created
            assertEquals(expectedBands, img.getBands());
        }
    }
}

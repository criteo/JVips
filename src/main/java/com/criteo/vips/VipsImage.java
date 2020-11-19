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

import java.awt.*;
import java.nio.ByteBuffer;

/**
 * Operation on image is not thread safe.
 */
public class VipsImage extends Vips implements Image {
    public static int JPGQuality = 80;

    // Per instance memory pointer used by the C code to retrieve the image data (Don't remove!)
    private long vipsImageHandler = 0;

    // VipsImage is created with vips_image_new_from_buffer, we are responsible to free allocated buffer.
    // We don't want to pass pointer to jbytearray because it will be never garbage collected (Don't remove!)
    // TODO: Try to create a global ref to jbytearray and delete the ref in release()
    private long bufferHandler = 0;

    static {
        initFieldIDs();
    }

    private static native void initFieldIDs();

    private native void newFromByteBuffer(ByteBuffer buffer, int length) throws VipsException;

    private native void newFromBuffer(byte[] buffer, int length) throws VipsException;

    private native void newFromFile(String filename) throws VipsException;

    public VipsImage(ByteBuffer buffer, int length) throws VipsException {
        newFromByteBuffer(buffer, length);
    }

    public VipsImage(byte[] buffer, int length) throws VipsException {
        newFromBuffer(buffer, length);
    }

    public VipsImage(VipsImage image, PixelPacket color) throws VipsException {
        newFromImage(image, color);
    }

    public VipsImage(String filename) throws VipsException {
        newFromFile(filename);
    }

    private VipsImage() {
        // you must call a native method before using this object
    }

    private VipsImage(long handle) {
        this.vipsImageHandler = handle;
    }

    public static VipsImage black(int width, int height) throws VipsException {
        VipsImage im = new VipsImage();
        im.blackNative(width, height);
        return im;
    }

    private native void blackNative(int width, int height) throws VipsException;

    private void newFromImage(Image image, PixelPacket c) throws VipsException {
        double[] color = {c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()};
        newFromImageNative(image, color);
    }

    private native void newFromImageNative(Image image, double[] c) throws VipsException;

    public VipsBandFormat imageGetFormat() {
        return VipsBandFormat.valueOf(imageGetFormatNative());
    }

    private native int imageGetFormatNative();

    public void castUchar() throws VipsException {
        castUchar(false);
    }

    public void castUchar(boolean shift) throws VipsException {
        castUcharNative(shift);
    }

    private native void castUcharNative(boolean shift) throws VipsException;

    public void cast(VipsBandFormat format) throws VipsException {
        cast(format, false);
    }

    public void cast(VipsBandFormat format, boolean shift) throws VipsException {
        castNative(format.getValue(), shift);
    }

    private native void castNative(int format, boolean shift) throws VipsException;

    public VipsInterpretation imageGetInterpretation() {
        return VipsInterpretation.valueOf(imageGetInterpretationNative());
    }

    private native int imageGetInterpretationNative();

    public void colourspace(VipsInterpretation space) throws VipsException {
        colourspaceNative(space.getValue());
    }

    private native void colourspaceNative(int space) throws VipsException;

    public void colourspace(VipsInterpretation space, VipsInterpretation source_space) throws VipsException {
        colourspaceNative(space.getValue(), source_space.getValue());
    }

    private native void colourspaceNative(int space, int source_space) throws VipsException;

    public void histFindNdim(int bins) throws VipsException {
        histFindNdimNative(bins);
    }

    private native void histFindNdimNative(int bins) throws VipsException;

    public void resize(Dimension dimension, boolean scale) throws VipsException {
        resizeNative(dimension.width, dimension.height, scale);
    }

    public void resize(int width, int height, boolean scale) throws VipsException {
        resizeNative(width, height, scale);
    }

    private native void resizeNative(int width, int height, boolean scale) throws VipsException;

    public Max1Result max1() throws VipsException {
        Max1Result r = new Max1Result();
        max1Native(r);
        return r;
    }

    private native void max1Native(Max1Result r) throws VipsException;

    public void pad(Dimension dimension, PixelPacket background, VipsCompassDirection gravity) throws VipsException {
        padNative(dimension.width, dimension.height, background.getComponents(), gravity.getValue());
    }

    private native void padNative(int width, int height, double[] background, int gravity) throws VipsException;

    public void crop(Rectangle rectangle) throws VipsException {
        cropNative(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    private native void cropNative(int left, int top, int width, int height) throws VipsException;

    public Rectangle findTrim(double threshold, PixelPacket background) throws VipsException {
        int[] ret = findTrimNative(threshold, background.getComponents());
        return new Rectangle(ret[0], ret[1], ret[2], ret[3]);
    }

    private native int[] findTrimNative(double threshold, double[] background) throws VipsException;

    public native void compose(Image sub) throws VipsException;

    public void flatten(PixelPacket background) throws VipsException {
        flattenNative(background.getComponents());
    }

    private native void flattenNative(double[] background) throws VipsException;

    public byte[] writeToArray(VipsImageFormat imageFormat, boolean strip) throws VipsException {
        // Set quality to -1 and let default vips value
        return writeToArray(imageFormat, -1, strip);
    }

    public byte[] writeToArray(VipsImageFormat imageFormat, int quality, boolean strip) throws VipsException {
        return writeToArrayNative(imageFormat.getFileExtension(), quality, strip);
    }

    public byte[] writePNGToArray(int compression, boolean palette, int colors, boolean strip) throws VipsException {
        return writePNGToArrayNative(compression, palette, colors, strip);
    }

    private native byte[] writePNGToArrayNative(int compression, boolean palette, int colors, boolean strip) throws VipsException;

    public byte[] writeAVIFToArray(int Q, boolean lossless, int speed) throws VipsException {
        return writeAVIFToArrayNative(Q, lossless, speed);
    }

    private native byte[] writeAVIFToArrayNative(int Q, boolean lossless, int speed) throws VipsException;

    private native byte[] writeToArrayNative(String extension, int quality, boolean strip) throws VipsException;

    public native int getWidth();

    public native int getHeight();

    public native int getBands();

    public native double[] getPoint(int x, int y) throws VipsException;

    public PixelPacket getPointPixelPacket(Point point) throws VipsException {
        return getPointPixelPacket(point.x, point.y);
    }

    public PixelPacket getPointPixelPacket(int x, int y) throws VipsException {
        double[] pixel = getPointPixelPacketNative(x, y);
        boolean isMonochrome = pixel.length < 3;
        boolean hasAlpha = pixel.length == 2 || pixel.length == 4;
        if (isMonochrome)
            return new PixelPacket(pixel[0], pixel[0], pixel[0], hasAlpha ? pixel[1] : 255.0);
        else
            return new PixelPacket(pixel[0], pixel[1], pixel[2], hasAlpha ? pixel[3] : 255.0);
    }

    private native double[] getPointPixelPacketNative(int x, int y) throws VipsException;

    public native boolean hasAlpha();

    public void linear(double[] a, double[] b, boolean uchar) throws VipsException {
        linearNative(a, b, uchar);
    }

    private native void linearNative(double[] a, double[] b, boolean uchar) throws VipsException;

    public void linear(double[] a, double[] b) throws VipsException {
        linear(a, b, false);
    }

    public VipsInterpretation getInterpretation() {
        /**
         * The name of the function in libvips is vips_image_get_interpretation
         * so the Java method should be called imageGetInterpretation.  Just
         * the correctly named function here.
         */
        return imageGetInterpretation();
    }

    public native void convertTosRGB() throws VipsException;

    public native int getNbFrame();

    public native void autorot() throws VipsException;

    public native void removeAutorotAngle();

    public native VipsImage clone() throws VipsException;

    public native void release();
}

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

import java.awt.*;

public interface VipsImage {

    /** Read a single pixel from an image.
     *
     * @return The pixel values
     * @throws VipsException
     */
    double[] getPoint(int x, int y) throws VipsException;

    /** Find the single largest value
     *
     * @return maximum value and x & y positions
     * @throws VipsException
     */
    Max1Result max1() throws VipsException;

    /** Get the format of each band element.
     *
     * @return the image's format
     */
    VipsBandFormat imageGetFormat();

    /** Convert to VIPS_FORMAT_CHAR without shifting
     *
     * @throws VipsException
     */
    void castUchar() throws VipsException;

     /** Convert to VIPS_FORMAT_CHAR
      *
      * @param shift values are shifted
      * @throws VipsException
      */
    void castUchar(boolean shift) throws VipsException;

    /** Convert to format without shifting
     *
     * @param format the image's new format
     * @throws VipsException
     */
    void cast(VipsBandFormat format) throws VipsException;

    /** Convert to format
     *
     * @param format the image's new format
     * @param shift integer values are shifted
     * @throws VipsException
     */
    void cast(VipsBandFormat format, boolean shift) throws VipsException;

    /** Make a one, two or three dimensional histogram of a 1, 2 or 3 band image
     *
     * @param bins number of bins
     * @throws VipsException
     */
    void histFindNdim(int bins) throws VipsException;

    /** Get the VipsInterpretation from the image header.
     *
     * @return the VipsInterpretation set in the image header.
     */
    VipsInterpretation imageGetInterpretation();
    /**
     * Convert this VipsImage's colourspace to the given space
     *
     * @param space the new colourspace.
     * @throws VipsException
     */
    void colourspace(VipsInterpretation space) throws VipsException;
    /**
     * Convert this VipsImage's colourspace to the given space
     *
     * @param space the new colourspace.
     * @param source_space the input colourspace.
     * @throws VipsException
     */
    void colourspace(VipsInterpretation space, VipsInterpretation source_space) throws VipsException;
    /**
     * Resize this VipsImage with new target dimension
     *
     * @param dimension Target dimension
     * @param scale     If scale is enabled, force to resize ignoring aspect ratio
     * @throws VipsException
     */
    void resize(Dimension dimension, boolean scale) throws VipsException;

    /**
     * Pad VipsImage with new target dimension and background pixel
     *
     * @param dimension Target dimension
     * @param background Background pixel color to fill with
     * @param gravity Gravity direction
     * @throws VipsException
     */
    void pad(Dimension dimension, PixelPacket background, Gravity gravity) throws VipsException;

    /**
     * Crop this VipsImage with new target dimension
     *
     * @param rectangle Target rectangle where:
     *                  x, y are original top left coordinate in original image to start cropping
     *                  width, height are cropped image dimension target
     * @throws VipsException
     */
    void crop(Rectangle rectangle) throws VipsException;

    /**
     * Find VipsImage bounding box
     *
     * @param threshold background threshold
     * @param background Background pixel color
     *
     * @return Bounding box Rectangle (left edge, top edge, width, height)
     *         If the image is entirely background, width == 0 and height == 0
     * @throws VipsException
     */
    Rectangle findTrim(double threshold, PixelPacket background) throws VipsException;

    /**
     * Compose this VipsImage with a sub VipsImage overlay
     *
     * @param sub VipsImage to compose
     * @throws VipsException
     */
    void compose(VipsImage sub) throws VipsException;

    /**
     * Take the last band of this VipsImage as an alpha and use it tot blend the remaining channels with background
     *
     * @param background PixelPacket color for new pixels
     * @throws VipsException
     */
    void flatten(PixelPacket background) throws VipsException;

    /**
     * Write VipsImage to byte array with default quality
     *
     * @param imageFormat Target extension
     *                        Could not be GIF because libvips can't save in this format
     * @return Byte array of encoded VipsImageImpl
     * @throws VipsException
     */
    byte[] writeToArray(ImageFormat imageFormat, boolean strip) throws VipsException;

    /**
     * Write VipsImage to byte array
     *
     * @param imageFormat Target extension
     *                        Could not be GIF because libvips can't save in this format
     * @param quality     Output quality
     * @return Byte array of encoded VipsImageImpl
     * @throws VipsException
     */
    byte[] writeToArray(ImageFormat imageFormat, int quality, boolean strip) throws VipsException;

    /**
     * Write VipsImage to byte array in PNG output format
     *
     * @param compression     Compression level
     * @param palette         If true color quantification is enabled
     * @param colors          Number of palette colors
     * @return Byte array of encoded VipsImageImpl
     * @throws VipsException
     */
    byte[] writePNGToArray(int compression, boolean palette, int colors, boolean strip) throws VipsException;

    /**
     * @return VipsImage width
     */
    int getWidth();

    /**
     * @return VipsImage height
     */
    int getHeight();

    /**
     * @return VipsImage channel number
     */
    int getBands();

    /**
     * @param point Position in VipsImageImpl
     * @return Pixel packet with same size than VipsImageImpl bands
     * @throws VipsException
     */
    PixelPacket getPointPixelPacket(Point point) throws VipsException;

    /**
     * @return True if VipsImage has alpha channel
     */
    boolean hasAlpha();

    /**
     * @return image colorspace interpretation
     */
    VipsInterpretation getInterpretation();

    /**
     * Convert image colorspace to sRGB
     *
     * @throws VipsException
     */
    void convertTosRGB() throws VipsException;

    /**
     * @return Image frame number
     */
    int getNbFrame();

    /**
     * Release VipsImage reference
     */
    void release();
}

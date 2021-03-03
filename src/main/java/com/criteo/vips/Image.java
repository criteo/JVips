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

package com.criteo.vips;

import com.criteo.vips.enums.*;

import java.awt.*;

public interface Image extends AutoCloseable {
    /**
     * Pass image through a linear transform, ie. (@out = @in * @a + @b)
     *
     * @param a:     (array length=n): array of constants for multiplication
     * @param b:     (array length=b.length): array of constants for addition
     * @param uchar: output uchar pixels if true
     * @throws VipsException if error
     */
    void linear(double[] a, double[] b, boolean uchar) throws VipsException;

    /**
     * Pass image through a linear transform, ie. (@out = @in * @a + @b)
     *
     * @param a: (array length=n): array of constants for multiplication
     * @param b: (array length=b.length): array of constants for addition
     * @throws VipsException if error
     */
    void linear(double[] a, double[] b) throws VipsException;

    /**
     * Read a single pixel from an image.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return The pixel values
     * @throws VipsException if error
     */
    double[] getPoint(int x, int y) throws VipsException;

    /**
     * Find the single largest value
     *
     * @return maximum value and x &amp; y positions
     * @throws VipsException if error
     */
    Max1Result max1() throws VipsException;

    /**
     * Get the format of each band element.
     *
     * @return the image's format
     */
    VipsBandFormat imageGetFormat();

    /**
     * Convert to VIPS_FORMAT_CHAR without shifting
     *
     * @throws VipsException if error
     */
    void castUchar() throws VipsException;

    /**
     * Convert to VIPS_FORMAT_CHAR
     *
     * @param shift values are shifted
     * @throws VipsException if error
     */
    void castUchar(boolean shift) throws VipsException;

    /**
     * Convert to format without shifting
     *
     * @param format the image's new format
     * @throws VipsException if error
     */
    void cast(VipsBandFormat format) throws VipsException;

    /**
     * Convert to format
     *
     * @param format the image's new format
     * @param shift  integer values are shifted
     * @throws VipsException if error
     */
    void cast(VipsBandFormat format, boolean shift) throws VipsException;

    /**
     * Make a one, two or three dimensional histogram of a 1, 2 or 3 band image
     *
     * @param bins number of bins
     * @throws VipsException if error
     */
    void histFindNdim(int bins) throws VipsException;

    /**
     * Get the VipsInterpretation from the image header.
     *
     * @return the VipsInterpretation set in the image header.
     */
    VipsInterpretation imageGetInterpretation();

    /**
     * Convert this VipsImage's colourspace to the given space
     *
     * @param space the new colourspace.
     * @throws VipsException if error
     */
    void colourspace(VipsInterpretation space) throws VipsException;

    /**
     * Convert this VipsImage's colourspace to the given space
     *
     * @param space        the new colourspace.
     * @param source_space the input colourspace.
     * @throws VipsException if error
     */
    void colourspace(VipsInterpretation space, VipsInterpretation source_space) throws VipsException;

    /**
     * Make a thumbnail of this VipsImage with new target dimension
     *
     * @param dimension Target dimension
     * @param scale     If scale is enabled, force to resize ignoring aspect ratio
     * @throws VipsException if error
     */
    void thumbnailImage(Dimension dimension, boolean scale) throws VipsException;

    /**
     * Make a thumbnail of this VipsImage with new target dimension
     *
     * @param width  Target width
     * @param height Target height
     * @param scale  If scale is enabled, force to resize ignoring aspect ratio
     * @throws VipsException if error
     */
    void thumbnailImage(int width, int height, boolean scale) throws VipsException;

    /**
     * Make a thumbnail of this VipsImage with new target dimension
     *
     * @param dimension Target dimension
     * @param scale     If scale is enabled, force to resize ignoring aspect ratio
     * @throws VipsException if error
     *
     * @deprecated Use {@link #thumbnailImage(Dimension, boolean)} instead.
     */
    @Deprecated
    void resize(Dimension dimension, boolean scale) throws VipsException;

    /**
     * Pad VipsImage with new target dimension and background pixel
     *
     * @param dimension  Target dimension
     * @param background Background pixel color to fill with
     * @param gravity    Gravity direction
     * @throws VipsException if error
     */
    void pad(Dimension dimension, PixelPacket background, VipsCompassDirection gravity) throws VipsException;

    /**
     * Crop this VipsImage with new target dimension
     *
     * @param rectangle Target rectangle where:
     *                  x, y are original top left coordinate in original image to start cropping
     *                  width, height are cropped image dimension target
     * @throws VipsException if error
     */
    void crop(Rectangle rectangle) throws VipsException;

    /**
     * Find VipsImage bounding box
     *
     * @param threshold  background threshold
     * @param background Background pixel color
     * @return Bounding box Rectangle (left edge, top edge, width, height)
     * If the image is entirely background, width == 0 and height == 0
     * @throws VipsException if error
     */
    Rectangle findTrim(double threshold, PixelPacket background) throws VipsException;

    /**
     * Compose this VipsImage with a sub VipsImage overlay
     *
     * @param sub VipsImage to compose
     * @throws VipsException if error
     */
    void compose(Image sub) throws VipsException;

    /**
     * Take the last band of this VipsImage as an alpha and use it tot blend the remaining channels with background
     *
     * @param background PixelPacket color for new pixels
     * @throws VipsException if error
     */
    void flatten(PixelPacket background) throws VipsException;

    /**
     * Write VipsImage to byte array with default quality
     *
     * @param imageFormat Target extension
     *                    Could not be GIF because libvips can't save in this format
     * @param strip       Removes all metadata from image
     * @return Byte array of encoded VipsImageImpl
     * @throws VipsException if error
     */
    byte[] writeToArray(VipsImageFormat imageFormat, boolean strip) throws VipsException;

    /**
     * Write VipsImage to byte array
     *
     * @param imageFormat Target extension
     *                    Could not be GIF because libvips can't save in this format
     * @param quality     Output quality
     * @param strip       Removes all metadata from image
     * @return Byte array of encoded VipsImageImpl
     * @throws VipsException if error
     */
    byte[] writeToArray(VipsImageFormat imageFormat, int quality, boolean strip) throws VipsException;

    /**
     * Write VipsImage to byte array in PNG output format
     *
     * @param compression Compression level
     * @param palette     If true color quantification is enabled
     * @param colors      Number of palette colors
     * @param strip       Removes all metadata from image
     * @return Byte array of encoded VipsImageImpl
     * @throws VipsException if error
     */
    byte[] writePNGToArray(int compression, boolean palette, int colors, boolean strip) throws VipsException;

    /**
     * Write VipsImage to file
     *
     * @param name Output file name
     * @throws VipsException if error
     */
    void writeToFile(String name) throws VipsException;

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
     * @throws VipsException if error
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
     * @throws VipsException if error
     */
    void convertTosRGB() throws VipsException;

    /**
     * @return Image frame number
     */
    int getNbFrame();

    /**
     * Apply an auto rotation according to the image EXIF metadata
     *
     * @throws VipsException if error
     */
    void autorot() throws VipsException;

    /**
     * Remove the orientation tag on the image
     */
    void removeAutorotAngle();

    /**
     * Release VipsImage reference
     */
    void release();

    /**
     * Create a copy of this image
     *
     * @return A copy of this image
     * @throws VipsException if error
     */
    Image clone() throws VipsException;

    /**
     * {@inheritDoc}
     */
    @Override
    default void close() {
        release();
    }
}

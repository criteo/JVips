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

#include <string.h>
#include <stdio.h>
#include <jni.h>
#include <vips/vips.h>
#include <vips/foreign.h>
#include <vips/region.h>

#include "VipsException.h"
#include "VipsEnumTest.h"

#define BUF_SIZE 4096

static void
assertEqualsNativeEnumValue(JNIEnv *env, const int expected, const char *className, const char *name)
{
    char sig[BUF_SIZE] = { 0 };
    sprintf(sig, "L%s;", className);
    jclass cls = (*env)->FindClass(env, className);
    jfieldID id = (*env)->GetStaticFieldID(env, cls , name, sig);
    jobject obj = (*env)->GetStaticObjectField(env, cls, id);
    jmethodID mid = (*env)->GetMethodID(env, cls , "getValue", "()I");
    jint actual = (*env)->CallIntMethod(env, obj, mid);

    if (expected != actual)
    {
        char msg[BUF_SIZE] = { 0 };
        sprintf(msg, "%s:%s is not equal to expected value (%d)", className, name, expected);
        throwVipsException(env, msg);
    }
    return;
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsEnumTest_TestNativeEnums(JNIEnv *env, jclass c)
{
    // VipsCombineMode
    assertEqualsNativeEnumValue(env, VIPS_COMBINE_MODE_SET, "com/criteo/vips/enums/VipsCombineMode", "SET");
    assertEqualsNativeEnumValue(env, VIPS_COMBINE_MODE_ADD, "com/criteo/vips/enums/VipsCombineMode", "ADD");
    assertEqualsNativeEnumValue(env, VIPS_COMBINE_MODE_LAST, "com/criteo/vips/enums/VipsCombineMode", "LAST");
    // VipsIntent
    assertEqualsNativeEnumValue(env, VIPS_INTENT_PERCEPTUAL, "com/criteo/vips/enums/VipsIntent", "PERCEPTUAL");
    assertEqualsNativeEnumValue(env, VIPS_INTENT_RELATIVE, "com/criteo/vips/enums/VipsIntent", "RELATIVE");
    assertEqualsNativeEnumValue(env, VIPS_INTENT_SATURATION, "com/criteo/vips/enums/VipsIntent", "SATURATION");
    assertEqualsNativeEnumValue(env, VIPS_INTENT_ABSOLUTE, "com/criteo/vips/enums/VipsIntent", "ABSOLUTE");
    assertEqualsNativeEnumValue(env, VIPS_INTENT_LAST, "com/criteo/vips/enums/VipsIntent", "LAST");
    // VipsPCS
    assertEqualsNativeEnumValue(env, VIPS_PCS_LAB, "com/criteo/vips/enums/VipsPCS", "LAB");
    assertEqualsNativeEnumValue(env, VIPS_PCS_XYZ, "com/criteo/vips/enums/VipsPCS", "XYZ");
    assertEqualsNativeEnumValue(env, VIPS_PCS_LAST, "com/criteo/vips/enums/VipsPCS", "LAST");
    // VipsOperationMorphology
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MORPHOLOGY_ERODE, "com/criteo/vips/enums/VipsOperationMorphology", "ERODE");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MORPHOLOGY_DILATE, "com/criteo/vips/enums/VipsOperationMorphology", "DILATE");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MORPHOLOGY_LAST, "com/criteo/vips/enums/VipsOperationMorphology", "LAST");
    // VipsKernel
    assertEqualsNativeEnumValue(env, VIPS_KERNEL_NEAREST, "com/criteo/vips/enums/VipsKernel", "NEAREST");
    assertEqualsNativeEnumValue(env, VIPS_KERNEL_LINEAR, "com/criteo/vips/enums/VipsKernel", "LINEAR");
    assertEqualsNativeEnumValue(env, VIPS_KERNEL_CUBIC, "com/criteo/vips/enums/VipsKernel", "CUBIC");
    assertEqualsNativeEnumValue(env, VIPS_KERNEL_MITCHELL, "com/criteo/vips/enums/VipsKernel", "MITCHELL");
    assertEqualsNativeEnumValue(env, VIPS_KERNEL_LANCZOS2, "com/criteo/vips/enums/VipsKernel", "LANCZOS2");
    assertEqualsNativeEnumValue(env, VIPS_KERNEL_LANCZOS3, "com/criteo/vips/enums/VipsKernel", "LANCZOS3");
    assertEqualsNativeEnumValue(env, VIPS_KERNEL_LAST, "com/criteo/vips/enums/VipsKernel", "LAST");
    // VipsSize
    assertEqualsNativeEnumValue(env, VIPS_SIZE_BOTH, "com/criteo/vips/enums/VipsSize", "BOTH");
    assertEqualsNativeEnumValue(env, VIPS_SIZE_UP, "com/criteo/vips/enums/VipsSize", "UP");
    assertEqualsNativeEnumValue(env, VIPS_SIZE_DOWN, "com/criteo/vips/enums/VipsSize", "DOWN");
    assertEqualsNativeEnumValue(env, VIPS_SIZE_FORCE, "com/criteo/vips/enums/VipsSize", "FORCE");
    assertEqualsNativeEnumValue(env, VIPS_SIZE_LAST, "com/criteo/vips/enums/VipsSize", "LAST");
    // VipsForeignFlags
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_NONE, "com/criteo/vips/enums/VipsForeignFlags", "NONE");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_PARTIAL, "com/criteo/vips/enums/VipsForeignFlags", "PARTIAL");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_BIGENDIAN, "com/criteo/vips/enums/VipsForeignFlags", "BIGENDIAN");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_SEQUENTIAL, "com/criteo/vips/enums/VipsForeignFlags", "SEQUENTIAL");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_ALL, "com/criteo/vips/enums/VipsForeignFlags", "ALL");
    // VipsSaveable
    assertEqualsNativeEnumValue(env, VIPS_SAVEABLE_MONO, "com/criteo/vips/enums/VipsSaveable", "MONO");
    assertEqualsNativeEnumValue(env, VIPS_SAVEABLE_RGB, "com/criteo/vips/enums/VipsSaveable", "RGB");
    assertEqualsNativeEnumValue(env, VIPS_SAVEABLE_RGBA, "com/criteo/vips/enums/VipsSaveable", "RGBA");
    assertEqualsNativeEnumValue(env, VIPS_SAVEABLE_RGBA_ONLY, "com/criteo/vips/enums/VipsSaveable", "RGBA_ONLY");
    assertEqualsNativeEnumValue(env, VIPS_SAVEABLE_RGB_CMYK, "com/criteo/vips/enums/VipsSaveable", "RGB_CMYK");
    assertEqualsNativeEnumValue(env, VIPS_SAVEABLE_ANY, "com/criteo/vips/enums/VipsSaveable", "ANY");
    assertEqualsNativeEnumValue(env, VIPS_SAVEABLE_LAST, "com/criteo/vips/enums/VipsSaveable", "LAST");
    // VipsForeignWebpPreset
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_WEBP_PRESET_DEFAULT, "com/criteo/vips/enums/VipsForeignWebpPreset", "DEFAULT");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_WEBP_PRESET_PICTURE, "com/criteo/vips/enums/VipsForeignWebpPreset", "PICTURE");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_WEBP_PRESET_PHOTO, "com/criteo/vips/enums/VipsForeignWebpPreset", "PHOTO");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_WEBP_PRESET_DRAWING, "com/criteo/vips/enums/VipsForeignWebpPreset", "DRAWING");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_WEBP_PRESET_ICON, "com/criteo/vips/enums/VipsForeignWebpPreset", "ICON");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_WEBP_PRESET_TEXT, "com/criteo/vips/enums/VipsForeignWebpPreset", "TEXT");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_WEBP_PRESET_LAST, "com/criteo/vips/enums/VipsForeignWebpPreset", "LAST");
    // VipsForeignTiffCompression
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_COMPRESSION_NONE, "com/criteo/vips/enums/VipsForeignTiffCompression", "NONE");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_COMPRESSION_JPEG, "com/criteo/vips/enums/VipsForeignTiffCompression", "JPEG");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_COMPRESSION_DEFLATE, "com/criteo/vips/enums/VipsForeignTiffCompression", "DEFLATE");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_COMPRESSION_PACKBITS, "com/criteo/vips/enums/VipsForeignTiffCompression", "PACKBITS");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_COMPRESSION_CCITTFAX4, "com/criteo/vips/enums/VipsForeignTiffCompression", "CCITTFAX4");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_COMPRESSION_LZW, "com/criteo/vips/enums/VipsForeignTiffCompression", "LZW");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_COMPRESSION_WEBP, "com/criteo/vips/enums/VipsForeignTiffCompression", "WEBP");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_COMPRESSION_ZSTD, "com/criteo/vips/enums/VipsForeignTiffCompression", "ZSTD");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_COMPRESSION_LAST, "com/criteo/vips/enums/VipsForeignTiffCompression", "LAST");
    // VipsForeignTiffPredictor
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_PREDICTOR_NONE, "com/criteo/vips/enums/VipsForeignTiffPredictor", "NONE");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_PREDICTOR_HORIZONTAL, "com/criteo/vips/enums/VipsForeignTiffPredictor", "HORIZONTAL");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_PREDICTOR_FLOAT, "com/criteo/vips/enums/VipsForeignTiffPredictor", "FLOAT");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_PREDICTOR_LAST, "com/criteo/vips/enums/VipsForeignTiffPredictor", "LAST");
    // VipsForeignTiffResunit
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_RESUNIT_CM, "com/criteo/vips/enums/VipsForeignTiffResunit", "CM");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_RESUNIT_INCH, "com/criteo/vips/enums/VipsForeignTiffResunit", "INCH");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_TIFF_RESUNIT_LAST, "com/criteo/vips/enums/VipsForeignTiffResunit", "LAST");
    // VipsForeignPngFilter
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_PNG_FILTER_NONE, "com/criteo/vips/enums/VipsForeignPngFilter", "NONE");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_PNG_FILTER_SUB, "com/criteo/vips/enums/VipsForeignPngFilter", "SUB");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_PNG_FILTER_UP, "com/criteo/vips/enums/VipsForeignPngFilter", "UP");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_PNG_FILTER_AVG, "com/criteo/vips/enums/VipsForeignPngFilter", "AVG");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_PNG_FILTER_PAETH, "com/criteo/vips/enums/VipsForeignPngFilter", "PAETH");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_PNG_FILTER_ALL, "com/criteo/vips/enums/VipsForeignPngFilter", "ALL");
    // VipsForeignDzLayout
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_DZ_LAYOUT_DZ, "com/criteo/vips/enums/VipsForeignDzLayout", "DZ");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_DZ_LAYOUT_ZOOMIFY, "com/criteo/vips/enums/VipsForeignDzLayout", "ZOOMIFY");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_DZ_LAYOUT_GOOGLE, "com/criteo/vips/enums/VipsForeignDzLayout", "GOOGLE");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_DZ_LAYOUT_IIIF, "com/criteo/vips/enums/VipsForeignDzLayout", "IIIF");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_DZ_LAYOUT_LAST, "com/criteo/vips/enums/VipsForeignDzLayout", "LAST");
    // VipsForeignDzDepth
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_DZ_DEPTH_ONEPIXEL, "com/criteo/vips/enums/VipsForeignDzDepth", "ONEPIXEL");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_DZ_DEPTH_ONETILE, "com/criteo/vips/enums/VipsForeignDzDepth", "ONETILE");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_DZ_DEPTH_ONE, "com/criteo/vips/enums/VipsForeignDzDepth", "ONE");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_DZ_DEPTH_LAST, "com/criteo/vips/enums/VipsForeignDzDepth", "LAST");
    // VipsForeignDzContainer
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_DZ_CONTAINER_FS, "com/criteo/vips/enums/VipsForeignDzContainer", "FS");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_DZ_CONTAINER_ZIP, "com/criteo/vips/enums/VipsForeignDzContainer", "ZIP");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_DZ_CONTAINER_SZI, "com/criteo/vips/enums/VipsForeignDzContainer", "SZI");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_DZ_CONTAINER_LAST, "com/criteo/vips/enums/VipsForeignDzContainer", "LAST");
    // VipsForeignHeifCompression
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_HEIF_COMPRESSION_HEVC, "com/criteo/vips/enums/VipsForeignHeifCompression", "HEVC");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_HEIF_COMPRESSION_AVC, "com/criteo/vips/enums/VipsForeignHeifCompression", "AVC");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_HEIF_COMPRESSION_JPEG, "com/criteo/vips/enums/VipsForeignHeifCompression", "JPEG");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_HEIF_COMPRESSION_AV1, "com/criteo/vips/enums/VipsForeignHeifCompression", "AV1");
    assertEqualsNativeEnumValue(env, VIPS_FOREIGN_HEIF_COMPRESSION_LAST, "com/criteo/vips/enums/VipsForeignHeifCompression", "LAST");
    // VipsDemandStyle
    assertEqualsNativeEnumValue(env, VIPS_DEMAND_STYLE_ERROR, "com/criteo/vips/enums/VipsDemandStyle", "ERROR");
    assertEqualsNativeEnumValue(env, VIPS_DEMAND_STYLE_SMALLTILE, "com/criteo/vips/enums/VipsDemandStyle", "SMALLTILE");
    assertEqualsNativeEnumValue(env, VIPS_DEMAND_STYLE_FATSTRIP, "com/criteo/vips/enums/VipsDemandStyle", "FATSTRIP");
    assertEqualsNativeEnumValue(env, VIPS_DEMAND_STYLE_THINSTRIP, "com/criteo/vips/enums/VipsDemandStyle", "THINSTRIP");
    assertEqualsNativeEnumValue(env, VIPS_DEMAND_STYLE_ANY, "com/criteo/vips/enums/VipsDemandStyle", "ANY");
    // VipsImageType
    assertEqualsNativeEnumValue(env, VIPS_IMAGE_ERROR, "com/criteo/vips/enums/VipsImageType", "ERROR");
    assertEqualsNativeEnumValue(env, VIPS_IMAGE_NONE, "com/criteo/vips/enums/VipsImageType", "NONE");
    assertEqualsNativeEnumValue(env, VIPS_IMAGE_SETBUF, "com/criteo/vips/enums/VipsImageType", "SETBUF");
    assertEqualsNativeEnumValue(env, VIPS_IMAGE_SETBUF_FOREIGN, "com/criteo/vips/enums/VipsImageType", "SETBUF_FOREIGN");
    assertEqualsNativeEnumValue(env, VIPS_IMAGE_OPENIN, "com/criteo/vips/enums/VipsImageType", "OPENIN");
    assertEqualsNativeEnumValue(env, VIPS_IMAGE_MMAPIN, "com/criteo/vips/enums/VipsImageType", "MMAPIN");
    assertEqualsNativeEnumValue(env, VIPS_IMAGE_MMAPINRW, "com/criteo/vips/enums/VipsImageType", "MMAPINRW");
    assertEqualsNativeEnumValue(env, VIPS_IMAGE_OPENOUT, "com/criteo/vips/enums/VipsImageType", "OPENOUT");
    assertEqualsNativeEnumValue(env, VIPS_IMAGE_PARTIAL, "com/criteo/vips/enums/VipsImageType", "PARTIAL");
    // VipsInterpretation
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_ERROR, "com/criteo/vips/enums/VipsInterpretation", "ERROR");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_MULTIBAND, "com/criteo/vips/enums/VipsInterpretation", "MULTIBAND");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_B_W, "com/criteo/vips/enums/VipsInterpretation", "B_W");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_HISTOGRAM, "com/criteo/vips/enums/VipsInterpretation", "HISTOGRAM");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_XYZ, "com/criteo/vips/enums/VipsInterpretation", "XYZ");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_LAB, "com/criteo/vips/enums/VipsInterpretation", "LAB");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_CMYK, "com/criteo/vips/enums/VipsInterpretation", "CMYK");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_LABQ, "com/criteo/vips/enums/VipsInterpretation", "LABQ");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_RGB, "com/criteo/vips/enums/VipsInterpretation", "RGB");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_CMC, "com/criteo/vips/enums/VipsInterpretation", "CMC");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_LCH, "com/criteo/vips/enums/VipsInterpretation", "LCH");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_LABS, "com/criteo/vips/enums/VipsInterpretation", "LABS");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_sRGB, "com/criteo/vips/enums/VipsInterpretation", "SRGB");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_YXY, "com/criteo/vips/enums/VipsInterpretation", "YXY");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_FOURIER, "com/criteo/vips/enums/VipsInterpretation", "FOURIER");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_RGB16, "com/criteo/vips/enums/VipsInterpretation", "RGB16");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_GREY16, "com/criteo/vips/enums/VipsInterpretation", "GREY16");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_MATRIX, "com/criteo/vips/enums/VipsInterpretation", "MATRIX");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_scRGB, "com/criteo/vips/enums/VipsInterpretation", "SCRGB");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_HSV, "com/criteo/vips/enums/VipsInterpretation", "HSV");
    assertEqualsNativeEnumValue(env, VIPS_INTERPRETATION_LAST, "com/criteo/vips/enums/VipsInterpretation", "LAST");
    // VipsBandFormat
    assertEqualsNativeEnumValue(env, VIPS_FORMAT_NOTSET, "com/criteo/vips/enums/VipsBandFormat", "FORMAT_NOTSET");
    assertEqualsNativeEnumValue(env, VIPS_FORMAT_UCHAR, "com/criteo/vips/enums/VipsBandFormat", "FORMAT_UCHAR");
    assertEqualsNativeEnumValue(env, VIPS_FORMAT_CHAR, "com/criteo/vips/enums/VipsBandFormat", "FORMAT_CHAR");
    assertEqualsNativeEnumValue(env, VIPS_FORMAT_USHORT, "com/criteo/vips/enums/VipsBandFormat", "FORMAT_USHORT");
    assertEqualsNativeEnumValue(env, VIPS_FORMAT_SHORT, "com/criteo/vips/enums/VipsBandFormat", "FORMAT_SHORT");
    assertEqualsNativeEnumValue(env, VIPS_FORMAT_UINT, "com/criteo/vips/enums/VipsBandFormat", "FORMAT_UINT");
    assertEqualsNativeEnumValue(env, VIPS_FORMAT_INT, "com/criteo/vips/enums/VipsBandFormat", "FORMAT_INT");
    assertEqualsNativeEnumValue(env, VIPS_FORMAT_FLOAT, "com/criteo/vips/enums/VipsBandFormat", "FORMAT_FLOAT");
    assertEqualsNativeEnumValue(env, VIPS_FORMAT_COMPLEX, "com/criteo/vips/enums/VipsBandFormat", "FORMAT_COMPLEX");
    assertEqualsNativeEnumValue(env, VIPS_FORMAT_DOUBLE, "com/criteo/vips/enums/VipsBandFormat", "FORMAT_DOUBLE");
    assertEqualsNativeEnumValue(env, VIPS_FORMAT_DPCOMPLEX, "com/criteo/vips/enums/VipsBandFormat", "FORMAT_DPCOMPLEX");
    assertEqualsNativeEnumValue(env, VIPS_FORMAT_LAST, "com/criteo/vips/enums/VipsBandFormat", "FORMAT_LAST");
    // VipsCoding
    assertEqualsNativeEnumValue(env, VIPS_CODING_ERROR, "com/criteo/vips/enums/VipsCoding", "ERROR");
    assertEqualsNativeEnumValue(env, VIPS_CODING_NONE, "com/criteo/vips/enums/VipsCoding", "NONE");
    assertEqualsNativeEnumValue(env, VIPS_CODING_LABQ, "com/criteo/vips/enums/VipsCoding", "LABQ");
    assertEqualsNativeEnumValue(env, VIPS_CODING_RAD, "com/criteo/vips/enums/VipsCoding", "RAD");
    assertEqualsNativeEnumValue(env, VIPS_CODING_LAST, "com/criteo/vips/enums/VipsCoding", "LAST");
    // VipsAccess
    assertEqualsNativeEnumValue(env, VIPS_ACCESS_RANDOM, "com/criteo/vips/enums/VipsAccess", "RANDOM");
    assertEqualsNativeEnumValue(env, VIPS_ACCESS_SEQUENTIAL, "com/criteo/vips/enums/VipsAccess", "SEQUENTIAL");
    assertEqualsNativeEnumValue(env, VIPS_ACCESS_SEQUENTIAL_UNBUFFERED, "com/criteo/vips/enums/VipsAccess", "SEQUENTIAL_UNBUFFERED");
    assertEqualsNativeEnumValue(env, VIPS_ACCESS_LAST, "com/criteo/vips/enums/VipsAccess", "LAST");
    // VipsExtend
    assertEqualsNativeEnumValue(env, VIPS_EXTEND_BLACK, "com/criteo/vips/enums/VipsExtend", "BLACK");
    assertEqualsNativeEnumValue(env, VIPS_EXTEND_COPY, "com/criteo/vips/enums/VipsExtend", "COPY");
    assertEqualsNativeEnumValue(env, VIPS_EXTEND_REPEAT, "com/criteo/vips/enums/VipsExtend", "REPEAT");
    assertEqualsNativeEnumValue(env, VIPS_EXTEND_MIRROR, "com/criteo/vips/enums/VipsExtend", "MIRROR");
    assertEqualsNativeEnumValue(env, VIPS_EXTEND_WHITE, "com/criteo/vips/enums/VipsExtend", "WHITE");
    assertEqualsNativeEnumValue(env, VIPS_EXTEND_BACKGROUND, "com/criteo/vips/enums/VipsExtend", "BACKGROUND");
    assertEqualsNativeEnumValue(env, VIPS_EXTEND_LAST, "com/criteo/vips/enums/VipsExtend", "LAST");
    // VipsCompassDirection
    assertEqualsNativeEnumValue(env, VIPS_COMPASS_DIRECTION_CENTRE, "com/criteo/vips/enums/VipsCompassDirection", "CENTRE");
    assertEqualsNativeEnumValue(env, VIPS_COMPASS_DIRECTION_NORTH, "com/criteo/vips/enums/VipsCompassDirection", "NORTH");
    assertEqualsNativeEnumValue(env, VIPS_COMPASS_DIRECTION_EAST, "com/criteo/vips/enums/VipsCompassDirection", "EAST");
    assertEqualsNativeEnumValue(env, VIPS_COMPASS_DIRECTION_SOUTH, "com/criteo/vips/enums/VipsCompassDirection", "SOUTH");
    assertEqualsNativeEnumValue(env, VIPS_COMPASS_DIRECTION_WEST, "com/criteo/vips/enums/VipsCompassDirection", "WEST");
    assertEqualsNativeEnumValue(env, VIPS_COMPASS_DIRECTION_NORTH_EAST, "com/criteo/vips/enums/VipsCompassDirection", "NORTH_EAST");
    assertEqualsNativeEnumValue(env, VIPS_COMPASS_DIRECTION_SOUTH_EAST, "com/criteo/vips/enums/VipsCompassDirection", "SOUTH_EAST");
    assertEqualsNativeEnumValue(env, VIPS_COMPASS_DIRECTION_SOUTH_WEST, "com/criteo/vips/enums/VipsCompassDirection", "SOUTH_WEST");
    assertEqualsNativeEnumValue(env, VIPS_COMPASS_DIRECTION_NORTH_WEST, "com/criteo/vips/enums/VipsCompassDirection", "NORTH_WEST");
    assertEqualsNativeEnumValue(env, VIPS_COMPASS_DIRECTION_LAST, "com/criteo/vips/enums/VipsCompassDirection", "LAST");
    // VipsDirection
    assertEqualsNativeEnumValue(env, VIPS_DIRECTION_HORIZONTAL, "com/criteo/vips/enums/VipsDirection", "HORIZONTAL");
    assertEqualsNativeEnumValue(env, VIPS_DIRECTION_VERTICAL, "com/criteo/vips/enums/VipsDirection", "VERTICAL");
    assertEqualsNativeEnumValue(env, VIPS_DIRECTION_LAST, "com/criteo/vips/enums/VipsDirection", "LAST");
    // VipsAlign
    assertEqualsNativeEnumValue(env, VIPS_ALIGN_LOW, "com/criteo/vips/enums/VipsAlign", "LOW");
    assertEqualsNativeEnumValue(env, VIPS_ALIGN_CENTRE, "com/criteo/vips/enums/VipsAlign", "CENTRE");
    assertEqualsNativeEnumValue(env, VIPS_ALIGN_HIGH, "com/criteo/vips/enums/VipsAlign", "HIGH");
    assertEqualsNativeEnumValue(env, VIPS_ALIGN_LAST, "com/criteo/vips/enums/VipsAlign", "LAST");
    // VipsAngle
    assertEqualsNativeEnumValue(env, VIPS_ANGLE_D0, "com/criteo/vips/enums/VipsAngle", "D0");
    assertEqualsNativeEnumValue(env, VIPS_ANGLE_D90, "com/criteo/vips/enums/VipsAngle", "D90");
    assertEqualsNativeEnumValue(env, VIPS_ANGLE_D180, "com/criteo/vips/enums/VipsAngle", "D180");
    assertEqualsNativeEnumValue(env, VIPS_ANGLE_D270, "com/criteo/vips/enums/VipsAngle", "D270");
    assertEqualsNativeEnumValue(env, VIPS_ANGLE_LAST, "com/criteo/vips/enums/VipsAngle", "LAST");
    // VipsAngle45
    assertEqualsNativeEnumValue(env, VIPS_ANGLE45_D0, "com/criteo/vips/enums/VipsAngle45", "D0");
    assertEqualsNativeEnumValue(env, VIPS_ANGLE45_D45, "com/criteo/vips/enums/VipsAngle45", "D45");
    assertEqualsNativeEnumValue(env, VIPS_ANGLE45_D90, "com/criteo/vips/enums/VipsAngle45", "D90");
    assertEqualsNativeEnumValue(env, VIPS_ANGLE45_D135, "com/criteo/vips/enums/VipsAngle45", "D135");
    assertEqualsNativeEnumValue(env, VIPS_ANGLE45_D180, "com/criteo/vips/enums/VipsAngle45", "D180");
    assertEqualsNativeEnumValue(env, VIPS_ANGLE45_D225, "com/criteo/vips/enums/VipsAngle45", "D225");
    assertEqualsNativeEnumValue(env, VIPS_ANGLE45_D270, "com/criteo/vips/enums/VipsAngle45", "D270");
    assertEqualsNativeEnumValue(env, VIPS_ANGLE45_D315, "com/criteo/vips/enums/VipsAngle45", "D315");
    assertEqualsNativeEnumValue(env, VIPS_ANGLE45_LAST, "com/criteo/vips/enums/VipsAngle45", "LAST");
    // VipsInteresting
    assertEqualsNativeEnumValue(env, VIPS_INTERESTING_NONE, "com/criteo/vips/enums/VipsInteresting", "NONE");
    assertEqualsNativeEnumValue(env, VIPS_INTERESTING_CENTRE, "com/criteo/vips/enums/VipsInteresting", "CENTRE");
    assertEqualsNativeEnumValue(env, VIPS_INTERESTING_ENTROPY, "com/criteo/vips/enums/VipsInteresting", "ENTROPY");
    assertEqualsNativeEnumValue(env, VIPS_INTERESTING_ATTENTION, "com/criteo/vips/enums/VipsInteresting", "ATTENTION");
    assertEqualsNativeEnumValue(env, VIPS_INTERESTING_LOW, "com/criteo/vips/enums/VipsInteresting", "LOW");
    assertEqualsNativeEnumValue(env, VIPS_INTERESTING_HIGH, "com/criteo/vips/enums/VipsInteresting", "HIGH");
    assertEqualsNativeEnumValue(env, VIPS_INTERESTING_LAST, "com/criteo/vips/enums/VipsInteresting", "LAST");
    // VipsBlendMode
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_CLEAR, "com/criteo/vips/enums/VipsBlendMode", "CLEAR");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_SOURCE, "com/criteo/vips/enums/VipsBlendMode", "SOURCE");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_OVER, "com/criteo/vips/enums/VipsBlendMode", "OVER");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_IN, "com/criteo/vips/enums/VipsBlendMode", "IN");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_OUT, "com/criteo/vips/enums/VipsBlendMode", "OUT");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_ATOP, "com/criteo/vips/enums/VipsBlendMode", "ATOP");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_DEST, "com/criteo/vips/enums/VipsBlendMode", "DEST");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_DEST_OVER, "com/criteo/vips/enums/VipsBlendMode", "DEST_OVER");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_DEST_IN, "com/criteo/vips/enums/VipsBlendMode", "DEST_IN");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_DEST_OUT, "com/criteo/vips/enums/VipsBlendMode", "DEST_OUT");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_DEST_ATOP, "com/criteo/vips/enums/VipsBlendMode", "DEST_ATOP");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_XOR, "com/criteo/vips/enums/VipsBlendMode", "XOR");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_ADD, "com/criteo/vips/enums/VipsBlendMode", "ADD");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_SATURATE, "com/criteo/vips/enums/VipsBlendMode", "SATURATE");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_MULTIPLY, "com/criteo/vips/enums/VipsBlendMode", "MULTIPLY");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_SCREEN, "com/criteo/vips/enums/VipsBlendMode", "SCREEN");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_OVERLAY, "com/criteo/vips/enums/VipsBlendMode", "OVERLAY");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_DARKEN, "com/criteo/vips/enums/VipsBlendMode", "DARKEN");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_LIGHTEN, "com/criteo/vips/enums/VipsBlendMode", "LIGHTEN");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_COLOUR_DODGE, "com/criteo/vips/enums/VipsBlendMode", "COLOUR_DODGE");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_COLOUR_BURN, "com/criteo/vips/enums/VipsBlendMode", "COLOUR_BURN");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_HARD_LIGHT, "com/criteo/vips/enums/VipsBlendMode", "HARD_LIGHT");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_SOFT_LIGHT, "com/criteo/vips/enums/VipsBlendMode", "SOFT_LIGHT");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_DIFFERENCE, "com/criteo/vips/enums/VipsBlendMode", "DIFFERENCE");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_EXCLUSION, "com/criteo/vips/enums/VipsBlendMode", "EXCLUSION");
    assertEqualsNativeEnumValue(env, VIPS_BLEND_MODE_LAST, "com/criteo/vips/enums/VipsBlendMode", "LAST");
    // VipsOperationFlags
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_NONE, "com/criteo/vips/enums/VipsOperationFlags", "NONE");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_SEQUENTIAL, "com/criteo/vips/enums/VipsOperationFlags", "SEQUENTIAL");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_SEQUENTIAL_UNBUFFERED, "com/criteo/vips/enums/VipsOperationFlags", "SEQUENTIAL_UNBUFFERED");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_NOCACHE, "com/criteo/vips/enums/VipsOperationFlags", "NOCACHE");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_DEPRECATED, "com/criteo/vips/enums/VipsOperationFlags", "DEPRECATED");
    // VipsArgumentFlags
    assertEqualsNativeEnumValue(env, VIPS_ARGUMENT_NONE, "com/criteo/vips/enums/VipsArgumentFlags", "NONE");
    assertEqualsNativeEnumValue(env, VIPS_ARGUMENT_REQUIRED, "com/criteo/vips/enums/VipsArgumentFlags", "REQUIRED");
    assertEqualsNativeEnumValue(env, VIPS_ARGUMENT_CONSTRUCT, "com/criteo/vips/enums/VipsArgumentFlags", "CONSTRUCT");
    assertEqualsNativeEnumValue(env, VIPS_ARGUMENT_SET_ONCE, "com/criteo/vips/enums/VipsArgumentFlags", "SET_ONCE");
    assertEqualsNativeEnumValue(env, VIPS_ARGUMENT_SET_ALWAYS, "com/criteo/vips/enums/VipsArgumentFlags", "SET_ALWAYS");
    assertEqualsNativeEnumValue(env, VIPS_ARGUMENT_INPUT, "com/criteo/vips/enums/VipsArgumentFlags", "INPUT");
    assertEqualsNativeEnumValue(env, VIPS_ARGUMENT_OUTPUT, "com/criteo/vips/enums/VipsArgumentFlags", "OUTPUT");
    assertEqualsNativeEnumValue(env, VIPS_ARGUMENT_DEPRECATED, "com/criteo/vips/enums/VipsArgumentFlags", "DEPRECATED");
    assertEqualsNativeEnumValue(env, VIPS_ARGUMENT_MODIFY, "com/criteo/vips/enums/VipsArgumentFlags", "MODIFY");
    // VipsRegionShrink
    assertEqualsNativeEnumValue(env, VIPS_REGION_SHRINK_MEAN, "com/criteo/vips/enums/VipsRegionShrink", "MEAN");
    assertEqualsNativeEnumValue(env, VIPS_REGION_SHRINK_MEDIAN, "com/criteo/vips/enums/VipsRegionShrink", "MEDIAN");
    assertEqualsNativeEnumValue(env, VIPS_REGION_SHRINK_MODE, "com/criteo/vips/enums/VipsRegionShrink", "MODE");
    assertEqualsNativeEnumValue(env, VIPS_REGION_SHRINK_LAST, "com/criteo/vips/enums/VipsRegionShrink", "LAST");
    // VipsOperationMath
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH_SIN, "com/criteo/vips/enums/VipsOperationMath", "SIN");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH_COS, "com/criteo/vips/enums/VipsOperationMath", "COS");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH_TAN, "com/criteo/vips/enums/VipsOperationMath", "TAN");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH_ASIN, "com/criteo/vips/enums/VipsOperationMath", "ASIN");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH_ACOS, "com/criteo/vips/enums/VipsOperationMath", "ACOS");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH_ATAN, "com/criteo/vips/enums/VipsOperationMath", "ATAN");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH_LOG, "com/criteo/vips/enums/VipsOperationMath", "LOG");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH_LOG10, "com/criteo/vips/enums/VipsOperationMath", "LOG10");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH_EXP, "com/criteo/vips/enums/VipsOperationMath", "EXP");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH_EXP10, "com/criteo/vips/enums/VipsOperationMath", "EXP10");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH_LAST, "com/criteo/vips/enums/VipsOperationMath", "LAST");
    // VipsOperationMath2
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH2_POW, "com/criteo/vips/enums/VipsOperationMath2", "POW");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH2_WOP, "com/criteo/vips/enums/VipsOperationMath2", "WOP");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_MATH2_LAST, "com/criteo/vips/enums/VipsOperationMath2", "LAST");
    // VipsOperationRound
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_ROUND_RINT, "com/criteo/vips/enums/VipsOperationRound", "RINT");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_ROUND_CEIL, "com/criteo/vips/enums/VipsOperationRound", "CEIL");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_ROUND_FLOOR, "com/criteo/vips/enums/VipsOperationRound", "FLOOR");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_ROUND_LAST, "com/criteo/vips/enums/VipsOperationRound", "LAST");
    // VipsOperationRelational
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_RELATIONAL_EQUAL, "com/criteo/vips/enums/VipsOperationRelational", "EQUAL");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_RELATIONAL_NOTEQ, "com/criteo/vips/enums/VipsOperationRelational", "NOTEQ");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_RELATIONAL_LESS, "com/criteo/vips/enums/VipsOperationRelational", "LESS");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_RELATIONAL_LESSEQ, "com/criteo/vips/enums/VipsOperationRelational", "LESSEQ");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_RELATIONAL_MORE, "com/criteo/vips/enums/VipsOperationRelational", "MORE");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_RELATIONAL_MOREEQ, "com/criteo/vips/enums/VipsOperationRelational", "MOREEQ");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_RELATIONAL_LAST, "com/criteo/vips/enums/VipsOperationRelational", "LAST");
    // VipsOperationBoolean
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_BOOLEAN_AND, "com/criteo/vips/enums/VipsOperationBoolean", "AND");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_BOOLEAN_OR, "com/criteo/vips/enums/VipsOperationBoolean", "OR");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_BOOLEAN_EOR, "com/criteo/vips/enums/VipsOperationBoolean", "EOR");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_BOOLEAN_LSHIFT, "com/criteo/vips/enums/VipsOperationBoolean", "LSHIFT");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_BOOLEAN_RSHIFT, "com/criteo/vips/enums/VipsOperationBoolean", "RSHIFT");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_BOOLEAN_LAST, "com/criteo/vips/enums/VipsOperationBoolean", "LAST");
    // VipsOperationComplex
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_COMPLEX_POLAR, "com/criteo/vips/enums/VipsOperationComplex", "POLAR");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_COMPLEX_RECT, "com/criteo/vips/enums/VipsOperationComplex", "RECT");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_COMPLEX_CONJ, "com/criteo/vips/enums/VipsOperationComplex", "CONJ");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_COMPLEX_LAST, "com/criteo/vips/enums/VipsOperationComplex", "LAST");
    // VipsOperationComplex2
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_COMPLEX2_CROSS_PHASE, "com/criteo/vips/enums/VipsOperationComplex2", "CROSS_PHASE");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_COMPLEX2_LAST, "com/criteo/vips/enums/VipsOperationComplex2", "LAST");
    // VipsOperationComplexget
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_COMPLEXGET_REAL, "com/criteo/vips/enums/VipsOperationComplexget", "REAL");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_COMPLEXGET_IMAG, "com/criteo/vips/enums/VipsOperationComplexget", "IMAG");
    assertEqualsNativeEnumValue(env, VIPS_OPERATION_COMPLEXGET_LAST, "com/criteo/vips/enums/VipsOperationComplexget", "LAST");
    // VipsCombine
    assertEqualsNativeEnumValue(env, VIPS_COMBINE_MAX, "com/criteo/vips/enums/VipsCombine", "MAX");
    assertEqualsNativeEnumValue(env, VIPS_COMBINE_SUM, "com/criteo/vips/enums/VipsCombine", "SUM");
    assertEqualsNativeEnumValue(env, VIPS_COMBINE_MIN, "com/criteo/vips/enums/VipsCombine", "MIN");
    assertEqualsNativeEnumValue(env, VIPS_COMBINE_LAST, "com/criteo/vips/enums/VipsCombine", "LAST");
    // VipsToken
    assertEqualsNativeEnumValue(env, VIPS_TOKEN_LEFT, "com/criteo/vips/enums/VipsToken", "LEFT");
    assertEqualsNativeEnumValue(env, VIPS_TOKEN_RIGHT, "com/criteo/vips/enums/VipsToken", "RIGHT");
    assertEqualsNativeEnumValue(env, VIPS_TOKEN_STRING, "com/criteo/vips/enums/VipsToken", "STRING");
    assertEqualsNativeEnumValue(env, VIPS_TOKEN_EQUALS, "com/criteo/vips/enums/VipsToken", "EQUALS");
    assertEqualsNativeEnumValue(env, VIPS_TOKEN_COMMA, "com/criteo/vips/enums/VipsToken", "COMMA");
    // VipsPrecision
    assertEqualsNativeEnumValue(env, VIPS_PRECISION_INTEGER, "com/criteo/vips/enums/VipsPrecision", "INTEGER");
    assertEqualsNativeEnumValue(env, VIPS_PRECISION_FLOAT, "com/criteo/vips/enums/VipsPrecision", "FLOAT");
    assertEqualsNativeEnumValue(env, VIPS_PRECISION_APPROXIMATE, "com/criteo/vips/enums/VipsPrecision", "APPROXIMATE");
    assertEqualsNativeEnumValue(env, VIPS_PRECISION_LAST, "com/criteo/vips/enums/VipsPrecision", "LAST");

}
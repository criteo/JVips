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

#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <jni.h>
#include <vips/vips.h>

#include "VipsImage.h"
#include "VipsException.h"
#include "JniFieldsIDs.h"

#define MAX_CHANNEL_SIZE 4

jfieldID handle_fid = NULL;
jfieldID buffer_fid = NULL;
jmethodID ctor_mid = NULL;


static VipsImage *
new_from_buffer(JNIEnv *env, void *buffer, int length)
{
    VipsImage *im = NULL;
    if ((im = vips_image_new_from_buffer(buffer, length, NULL, NULL)) == NULL)
    {
        throwVipsException(env, "Unable to decode image buffer");
        return NULL;
    }
    return im;
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_blackNative(JNIEnv *env, jobject obj, jint width, jint height)
{
    VipsImage *out = NULL;
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);

    if (vips_black(&out, width, height, NULL))
    {
        throwVipsException(env, "vips_black failed");
        return;
    }
    (*env)->SetLongField(env, obj, handle_fid, (jlong) out);
    (*env)->SetLongField(env, obj, buffer_fid, (jlong) NULL);
    g_object_unref(im);
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_newFromImageNative(JNIEnv *env, jobject obj, jobject image, jdoubleArray background)
{
    VipsImage *src = (VipsImage *) (*env)->GetLongField(env, image, handle_fid);
    VipsImage *im = NULL;
    jint length = (*env)->GetArrayLength(env, background);
    jdouble background_array[MAX_CHANNEL_SIZE] = { 0 };

    (*env)->GetDoubleArrayRegion(env, background, 0, length, background_array);
    if ((im = vips_image_new_from_image(src, background_array, length)) == NULL)
    {
        throwVipsException(env, "Unable to decode image buffer");
        return;
    }
    (*env)->SetLongField(env, obj, handle_fid, (jlong) im);
    (*env)->SetLongField(env, obj, buffer_fid, (jlong) NULL);
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_initFieldIDs(JNIEnv *env, jobject cls)
{
    handle_fid = (*env)->GetFieldID(env, cls, "vipsImageHandler", "J");
    buffer_fid = (*env)->GetFieldID(env, cls, "bufferHandler", "J");
    ctor_mid = (*env)->GetMethodID(env, cls, "<init>", "(J)V");
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_newFromByteBuffer(JNIEnv *env, jobject obj, jobject buffer, jint length)
{
    void *buf = (*env)->GetDirectBufferAddress(env, buffer);

    (*env)->SetLongField(env, obj, handle_fid, (jlong) new_from_buffer(env, buf, length));
    (*env)->SetLongField(env, obj, buffer_fid, (jlong) NULL);
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_newFromBuffer(JNIEnv *env, jobject obj, jbyteArray buffer, jint length)
{
    void *internal_buffer = NULL;
    VipsImage* im = NULL;
    size_t len = length * sizeof(jbyte);

    if ((internal_buffer = vips_tracked_malloc(len)) == NULL)
    {
        (*env)->SetLongField(env, obj, handle_fid, (jlong) NULL);
        (*env)->SetLongField(env, obj, buffer_fid, (jlong) NULL);
        throwVipsException(env, "Unable to allocate memory");
        return;
    }
    (*env)->GetByteArrayRegion(env, buffer, 0, len, internal_buffer);
    im = new_from_buffer(env, internal_buffer, length);
    (*env)->SetLongField(env, obj, handle_fid, (jlong) im);
    (*env)->SetLongField(env, obj, buffer_fid, (jlong) internal_buffer);
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_newFromFile(JNIEnv *env, jobject obj, jstring filename)
{
    const char *str = (*env)->GetStringUTFChars(env, filename, NULL);
    VipsImage* im = vips_image_new_from_file(str, NULL);

    if (im == NULL)
    {
        (*env)->SetLongField(env, obj, handle_fid, (jlong) NULL);
        (*env)->SetLongField(env, obj, buffer_fid, (jlong) NULL);
        (*env)->ReleaseStringUTFChars(env, filename, str);
        throwVipsException(env, "Unable to create an image from file");
        return;
    }
    (*env)->ReleaseStringUTFChars(env, filename, str);
    (*env)->SetLongField(env, obj, handle_fid, (jlong) im);
    (*env)->SetLongField(env, obj, buffer_fid, (jlong) NULL);
}

JNIEXPORT int JNICALL Java_com_criteo_vips_VipsImage_imageGetInterpretationNative(JNIEnv *env, jobject obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    return vips_image_get_interpretation(im);
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_colourspaceNative__I(JNIEnv *env, jobject obj, int space)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    VipsImage *out = NULL;

    if (vips_colourspace(im, &out, space, NULL))
    {
        throwVipsException(env, "Unable to convert colour space I");
        return;
    }
    (*env)->SetLongField(env, obj, handle_fid, (jlong) out);
    g_object_unref(im);
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_colourspaceNative__II(JNIEnv *env, jobject obj, int space, int source_space)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    VipsImage *out = NULL;

    if (vips_colourspace(im, &out, space, "source_space", source_space, NULL))
    {
        throwVipsException(env, "Unable to convert colour space II");
        return;
    }
    (*env)->SetLongField(env, obj, handle_fid, (jlong) out);
    g_object_unref(im);
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_thumbnailImageNative(JNIEnv *env, jobject obj, jint width, jint height, jboolean scale)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    VipsImage *out = NULL;
    VipsSize vipsSize = scale ? VIPS_SIZE_FORCE : VIPS_SIZE_BOTH;

    if (vips_thumbnail_image(im, &out, width, "height", height, "size", vipsSize, NULL))
    {
        throwVipsException(env, "Unable to make thumbnail image");
        return;
    }
    (*env)->SetLongField(env, obj, handle_fid, (jlong) out);
    g_object_unref(im);
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_resizeNative(JNIEnv *env, jobject obj, jdouble hscale, jdouble vscale, jint kernel)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    VipsImage *out = NULL;

    if (vips_resize(im, &out, hscale, "vscale", vscale, "kernel", kernel, NULL))
    {
        throwVipsException(env, "Unable to resize image");
        return;
    }
    (*env)->SetLongField(env, obj, handle_fid, (jlong) out);
    g_object_unref(im);
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_padNative(JNIEnv *env, jobject obj, jint width, jint height, jdoubleArray background, jint gravity)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    VipsImage *out = NULL;
    jint length = (*env)->GetArrayLength(env, background);
    jdouble background_array[MAX_CHANNEL_SIZE] = { 0 };
    VipsArrayDouble *bg_pixel = NULL;
    VipsCompassDirection direction = gravity;

    if (im->Bands > length && length != 1)
    {
        throwVipsException(env, "Invalid background pixel size");
        return;
    }
    length = im->Bands;
    (*env)->GetDoubleArrayRegion(env, background, 0, length, background_array);
    bg_pixel = vips_array_double_new(background_array, length);
    if (vips_gravity(im, &out, direction, width, height, "extend", VIPS_EXTEND_BACKGROUND, "background", bg_pixel, NULL))
    {
        throwVipsException(env, "Unable to pad image");
        return;
    }
    (*env)->SetLongField(env, obj, handle_fid, (jlong) out);
    vips_area_unref((VipsArea *) bg_pixel);
    g_object_unref(im);
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_cropNative(JNIEnv *env, jobject obj, jint left, jint top, jint width, jint height)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    int w = vips_image_get_width(im);
    int h = vips_image_get_height(im);
    VipsImage *out = NULL;

    if (vips_crop(im, &out, left, top, width, height, NULL))
    {
        throwVipsException(env, "Unable to crop image");
        return;
    }
    (*env)->SetLongField(env, obj, handle_fid, (jlong) out);
    g_object_unref(im);
}

JNIEXPORT jintArray JNICALL
Java_com_criteo_vips_VipsImage_findTrimNative(JNIEnv *env, jobject obj, jdouble threshold, jdoubleArray background)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    jintArray ret;
    jint length = (*env)->GetArrayLength(env, background);
    jdouble background_array[MAX_CHANNEL_SIZE] = { 0 };
    int buffer[4] = { 0 }; /* top, left, width, height */
    VipsArrayDouble *bg_pixel = NULL;

    if (im->Bands > length && length != 1)
    {
        throwVipsException(env, "Invalid background pixel size");
        return ret;
    }
    length = im->Bands;
    /**
    * vips_find_trim() handles transparency by flatting transparent pixels with background pixel.
    * Then, it finds trim according to background pixel.
    * The solution is to remove the alpha channel assuming that it is the last channel.
    */
    if (vips_image_hasalpha(im))
        length = im->Bands - 1;
    (*env)->GetDoubleArrayRegion(env, background, 0, length, background_array);
    bg_pixel = vips_array_double_new(background_array, length);
    if (vips_find_trim(im, buffer, buffer + 1, buffer + 2, buffer + 3, "threshold", threshold, "background", bg_pixel, NULL))
    {
        throwVipsException(env, "Unable to find image trim");
        return ret;
    }
    ret = (*env)->NewIntArray(env, 4);
    (*env)->SetIntArrayRegion(env, ret, 0, 4, buffer);
    (*env)->ReleaseIntArrayElements(env, ret, buffer, JNI_COMMIT);
    vips_area_unref((VipsArea *) bg_pixel);
    return ret;
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_compose(JNIEnv *env, jobject obj, jobject sub)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    VipsImage *overlay = (VipsImage *) (*env)->GetLongField(env, sub, handle_fid);
    VipsImage *out = NULL;

    if (vips_composite2(im, overlay, &out,VIPS_BLEND_MODE_OVER, NULL))
    {
        throwVipsException(env, "Unable to compose image");
        return;
    }
    (*env)->SetLongField(env, obj, handle_fid, (jlong) out);
    g_object_unref(im);
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_flattenNative(JNIEnv *env, jobject obj, jdoubleArray background)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    jint length = (*env)->GetArrayLength(env, background);
    jdouble background_array[MAX_CHANNEL_SIZE] = { 0 };
    VipsArrayDouble *bg_pixel = NULL;
    VipsImage *out = NULL;

    // vector must have 1 or 3 elements
    length = 3;
    (*env)->GetDoubleArrayRegion(env, background, 0, length, background_array);
    bg_pixel = vips_array_double_new(background_array, length);
    if (vips_flatten(im, &out, "background", bg_pixel, NULL))
    {
        throwVipsException(env, "Unable to flat image");
        return;
    }

    (*env)->SetLongField(env, obj, handle_fid, (jlong) out);
    g_object_unref(im);
    vips_area_unref((VipsArea *) bg_pixel);
}

JNIEXPORT jbyteArray JNICALL
Java_com_criteo_vips_VipsImage_writeToArrayNative(JNIEnv *env, jobject obj, jstring extension, jint quality, jboolean strip)
{
    jbyteArray ret;
    const char *ext = (*env)->GetStringUTFChars(env, extension, NULL);
    void *buffer = NULL;
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    size_t result_length = 0;
    int status = 0;

    if (strcmp(ext, ".avif") == 0) {
        if (quality < 0)
            status = vips_heifsave_buffer(im, &buffer, &result_length, "Q", quality, "compression", VIPS_FOREIGN_HEIF_COMPRESSION_AV1, NULL);
        else
            status = vips_heifsave_buffer(im, &buffer, &result_length, "compression", VIPS_FOREIGN_HEIF_COMPRESSION_AV1, NULL);
    } else {
        if (quality < 0)
            status = vips_image_write_to_buffer(im, ext, &buffer, &result_length, "strip", strip, NULL);
        else
            status = vips_image_write_to_buffer(im, ext, &buffer, &result_length, "strip", strip, "Q", quality, NULL);
    }
    if (status)
    {
        (*env)->ReleaseStringUTFChars(env, extension, ext);
        throwVipsException(env, "Unable to write image buffer");
        return ret;
    }
    ret = (*env)->NewByteArray(env, result_length);
    (*env)->SetByteArrayRegion(env, ret, 0, result_length * sizeof (jbyte), buffer);
    (*env)->ReleaseStringUTFChars(env, extension, ext);
    g_free(buffer);
    return ret;
}

JNIEXPORT jbyteArray JNICALL
Java_com_criteo_vips_VipsImage_writePNGToArrayNative(JNIEnv *env, jobject obj, jint compression, jboolean palette, jint colors, jboolean strip)
{
    jbyteArray ret;
    void *buffer = NULL;
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    size_t result_length = 0;

    if (vips_pngsave_buffer(im, &buffer, &result_length,
                            "compression", compression,
                            "palette", palette,
                            "colours", colors,
                            "strip", strip,
                            NULL))
    {
        throwVipsException(env, "Unable to write PNG image buffer");
        return ret;
    }
    ret = (*env)->NewByteArray(env, result_length);
    (*env)->SetByteArrayRegion(env, ret, 0, result_length * sizeof (jbyte), buffer);
    g_free(buffer);
    return ret;
}

JNIEXPORT jbyteArray JNICALL
Java_com_criteo_vips_VipsImage_writeJPEGToArrayNative(JNIEnv *env, jobject obj, jint q, jboolean strip)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);

    void *buffer;
    size_t buffer_size;

    if (vips_jpegsave_buffer(im, &buffer, &buffer_size, "Q", q, "strip", strip, NULL))
    {
        throwVipsException(env, "Unable to write JPEG image buffer");
        return NULL;
    }

    jbyteArray ret = (*env)->NewByteArray(env, buffer_size);
    (*env)->SetByteArrayRegion(env, ret, 0, buffer_size, buffer);
    g_free(buffer);
    return ret;
}

JNIEXPORT jbyteArray JNICALL
Java_com_criteo_vips_VipsImage_writeAVIFToArrayNative(JNIEnv *env, jobject obj, jint q, jboolean lossless, jint speed)
{
    jbyteArray ret;
    void *buffer = NULL;
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    size_t result_length = 0;

    if (vips_heifsave_buffer(im, &buffer, &result_length,
                            "Q", q,
                            "lossless", lossless,
                            "compression", VIPS_FOREIGN_HEIF_COMPRESSION_AV1,
                            "speed", speed,
                            NULL))
    {
        throwVipsException(env, "Unable to write AVIF image buffer");
        return ret;
    }
    ret = (*env)->NewByteArray(env, result_length);
    (*env)->SetByteArrayRegion(env, ret, 0, result_length * sizeof (jbyte), buffer);
    g_free(buffer);
    return ret;
}

JNIEXPORT jbyteArray JNICALL
Java_com_criteo_vips_VipsImage_writeWEBPToArrayNative(JNIEnv *env, jobject obj, jint q, jboolean lossless, jboolean strip)
{
    jbyteArray ret;
    void *buffer = NULL;
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    size_t result_length = 0;

    if (vips_webpsave_buffer(im, &buffer, &result_length,
                            "Q", q,
                            "lossless", lossless,
                            "strip", strip,
                            NULL))
    {
        throwVipsException(env, "Unable to write WEBP image buffer");
        return ret;
    }
    ret = (*env)->NewByteArray(env, result_length);
    (*env)->SetByteArrayRegion(env, ret, 0, result_length * sizeof (jbyte), buffer);
    g_free(buffer);
    return ret;
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_writeToFile(JNIEnv *env, jobject obj, jstring name)
{
    const char *filename = (*env)->GetStringUTFChars(env, name, NULL);
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);

    if (vips_image_write_to_file(im, filename, NULL))
    {
        throwVipsException(env, "Unable to write to file");
    }
    (*env)->ReleaseStringUTFChars(env, name, filename);
}

JNIEXPORT jint JNICALL
Java_com_criteo_vips_VipsImage_getWidth(JNIEnv *env, jobject obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    return vips_image_get_width(im);
}

JNIEXPORT jint JNICALL
Java_com_criteo_vips_VipsImage_getHeight(JNIEnv *env, jobject obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    return vips_image_get_height(im);
}

JNIEXPORT jint JNICALL
Java_com_criteo_vips_VipsImage_getBands(JNIEnv *env, jobject obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    return im->Bands;
}

JNIEXPORT jdoubleArray JNICALL
Java_com_criteo_vips_VipsImage_getPointPixelPacketNative(JNIEnv *env, jobject obj, jint x, jint y)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    double *pixel = NULL;
    int result_length = 0;
    jdoubleArray ret;

    if (vips_getpoint(im, &pixel, &result_length, x, y, NULL) < 0)
    {
        throwVipsException(env, "Unable to get image point");
        g_free(pixel);
        return ret;
    }
    // Convert to uchar
    for (int i = 0; i < result_length; ++i)
        // Left shift of image channel bits - target channel bits (8 bits)
        pixel[i] = ((int)pixel[i]) >> ((VIPS_IMAGE_SIZEOF_ELEMENT(im) * 8) - 8);
    ret = (*env)->NewDoubleArray(env, result_length);
    (*env)->SetDoubleArrayRegion(env, ret, 0, result_length, pixel);
    (*env)->ReleaseDoubleArrayElements(env, ret, pixel, JNI_COMMIT);
    g_free(pixel);
    return ret;
}

JNIEXPORT jboolean JNICALL
Java_com_criteo_vips_VipsImage_hasAlpha(JNIEnv *env, jobject obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    return vips_image_hasalpha(im);
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_convertTosRGB(JNIEnv *env, jobject obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    VipsImage *out = NULL;
    VipsInterpretation source_space = vips_image_guess_interpretation(im);

    if (vips_colourspace(im, &out, VIPS_INTERPRETATION_sRGB, "source_space", source_space, NULL))
    {
        throwVipsException(env, "Unable to convert image");
        return;
    }
    (*env)->SetLongField(env, obj, handle_fid, (jlong) out);
    g_object_unref(im);
}

JNIEXPORT jint JNICALL
Java_com_criteo_vips_VipsImage_getNbFrame(JNIEnv *env, jobject obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    int n_pages = 0;

    if (vips_image_get_int(im, VIPS_META_N_PAGES, &n_pages))
    {
        // VIPS_META_N_PAGES may be missing.
        // Assume there is only one image.
        return 1;
    }
    return n_pages;
}

JNIEXPORT void JNICALL
Java_com_criteo_vips_VipsImage_release(JNIEnv *env, jobject obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    void *buffer = (void *) (*env)->GetLongField(env, obj, buffer_fid);

    if (im)
    {
        g_object_unref(im);
        im = NULL;
        (*env)->SetLongField(env, obj, handle_fid, (jlong) im);
    }
    if (buffer)
    {
        vips_tracked_free(buffer);
        buffer = NULL;
        (*env)->SetLongField(env, obj, buffer_fid, (jlong) buffer);
    }
}

JNIEXPORT jint JNICALL Java_com_criteo_vips_VipsImage_imageGetFormatNative(JNIEnv *env, jobject obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    return vips_image_get_format(im);
}

JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImage_castUcharNative(JNIEnv *env, jobject obj, jboolean shift)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    VipsImage *out = NULL;

    if (vips_cast_uchar(im, &out, "shift", shift, NULL))
    {
        throwVipsException(env, "Unable to cast image to uchar");
        return;
    }
    (*env)->SetLongField(env, obj, handle_fid, (jlong) out);
    g_object_unref(im);
}

JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImage_castNative(JNIEnv *env, jobject obj, jint format, jboolean shift)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    VipsImage *out = NULL;

    if (vips_cast(im, &out, format, "shift", shift, NULL))
    {
        throwVipsException(env, "Unable to cast image");
        return;
    }
    (*env)->SetLongField(env, obj, handle_fid, (jlong) out);
    g_object_unref(im);
}

JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImage_histFindNdimNative(JNIEnv *env, jobject obj, jint bins)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    VipsImage *out = NULL;

    if (vips_hist_find_ndim(im, &out, "bins", bins, NULL))
    {
        throwVipsException(env, "vips_hist_find_ndim(bins) failed");
        return;
    }
    (*env)->SetLongField(env, obj, handle_fid, (jlong) out);
    g_object_unref(im);
}

JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImage_max1Native(JNIEnv * env, jobject image_obj, jobject result_obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, image_obj, handle_fid);
    double out;
    int x;
    int y;

    if (vips_max(im, &out, "x", &x, "y", &y, NULL))
    {
        throwVipsException(env, "max(x, y) failed");
        return;
    }
    (*env)->SetDoubleField(env, result_obj, field_Max1Result_out, out);
    (*env)->SetIntField(env, result_obj, field_Max1Result_x, x);
    (*env)->SetIntField(env, result_obj, field_Max1Result_y, y);
}

JNIEXPORT jdoubleArray JNICALL
Java_com_criteo_vips_VipsImage_getPoint(JNIEnv *env, jobject image_obj, jint x, jint y)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, image_obj, handle_fid);
    double *vector;
    int n;
    jdoubleArray ret;

    if (vips_getpoint(im, &vector, &n, x, y, NULL))
    {
        throwVipsException(env, "vips_getpoint failed");
        return NULL;
    }
    ret = (*env)->NewDoubleArray(env, n);
    (*env)->SetDoubleArrayRegion(env, ret, 0, n, vector);
    (*env)->ReleaseDoubleArrayElements(env, ret, vector, JNI_COMMIT);
    g_free(vector);
    return ret;
}

JNIEXPORT void
JNICALL Java_com_criteo_vips_VipsImage_linearNative(JNIEnv *env, jobject image_obj, jdoubleArray a, jdoubleArray b, jboolean uchar)
{
    jint length = (*env)->GetArrayLength(env, a);

    if ((*env)->GetArrayLength(env, b) != length)
        throwVipsException(env, "vips_linear requires arrays of the same length");
    else
    {
        VipsImage *im = (VipsImage *) (*env)->GetLongField(env, image_obj, handle_fid);
        VipsImage *out = NULL;
        jdouble *a_values = (*env)->GetDoubleArrayElements(env, a, 0);
        jdouble *b_values = (*env)->GetDoubleArrayElements(env, b, 0);

        int errcode = vips_linear(im, &out, a_values, b_values, length, "uchar", uchar, NULL);
        (*env)->ReleaseDoubleArrayElements(env, a, a_values, 0);
        (*env)->ReleaseDoubleArrayElements(env, b, b_values, 0);

        if (errcode)
            throwVipsException(env, "vips_linear failed");
        else
        {
            (*env)->SetLongField(env, image_obj, handle_fid, (jlong) out);
            g_object_unref(im);
        }
    }
}

JNIEXPORT void
JNICALL Java_com_criteo_vips_VipsImage_gaussblur(JNIEnv *env, jobject image_obj, jdouble sigma, jdouble min_ampl)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, image_obj, handle_fid);
    VipsImage *out = NULL;

    if (vips_gaussblur(im, &out, sigma, "min_ampl", min_ampl, NULL))
    {
        throwVipsException(env, "Unable to apply gaussian blur");
        return;
    }
    (*env)->SetLongField(env, image_obj, handle_fid, (jlong) out);
    g_object_unref(im);
}

JNIEXPORT jobject
JNICALL Java_com_criteo_vips_VipsImage_extractArea(JNIEnv *env, jobject image_obj, jint left, jint top, jint width, jint height)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, image_obj, handle_fid);
    VipsImage *out = NULL;
    jobject cls = (*env)->FindClass(env, "com/criteo/vips/VipsImage");

    if (vips_extract_area(im, &out, left, top, width, height, NULL))
    {
        throwVipsException(env, "Unable to extract image area");
    }
    return (*env)->NewObject(env, cls, ctor_mid, (jlong) out);
}

JNIEXPORT void
JNICALL Java_com_criteo_vips_VipsImage_autorot(JNIEnv *env, jobject image_obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, image_obj, handle_fid);
    VipsImage *out = NULL;

    if (vips_autorot(im, &out, NULL))
    {
        throwVipsException(env, "Unable to auto rotate image");
        return;
    }
    (*env)->SetLongField(env, image_obj, handle_fid, (jlong) out);
    g_object_unref(im);
}

JNIEXPORT jobject
JNICALL Java_com_criteo_vips_VipsImage_clone(JNIEnv *env, jobject image_obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, image_obj, handle_fid);
    VipsImage *out = NULL;
    jobject cls = (*env)->FindClass(env, "com/criteo/vips/VipsImage");

    if (vips_copy(im, &out, NULL))
    {
        throwVipsException(env, "Unable to copy image");
    }
    return (*env)->NewObject(env, cls, ctor_mid, (jlong) out);
}

JNIEXPORT void
JNICALL Java_com_criteo_vips_VipsImage_removeAutorotAngle(JNIEnv *env, jobject image_obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, image_obj, handle_fid);
    vips_autorot_remove_angle(im);
}


JNIEXPORT jobject
JNICALL Java_com_criteo_vips_VipsImage_joinNative(JNIEnv *env, jclass cls, jobject in1, jobject in2, jint direction)
{
    VipsImage *im1 = (VipsImage *) (*env)->GetLongField(env, in1, handle_fid);
    VipsImage *im2 = (VipsImage *) (*env)->GetLongField(env, in2, handle_fid);
    VipsImage *out = NULL;

    if (vips_join(im1, im2, &out, direction, NULL))
    {
        throwVipsException(env, "Unable to join image");
    }
    return (*env)->NewObject(env, cls, ctor_mid, (jlong) out);
}

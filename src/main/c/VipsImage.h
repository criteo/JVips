/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_criteo_vips_VipsImageImpl */

#ifndef _Included_com_criteo_vips_VipsImageImpl
#define _Included_com_criteo_vips_VipsImageImpl
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    initFieldIDs
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_initFieldIDs
  (JNIEnv *, jclass);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    newFromImageNative
 * Signature: (Lcom/criteo/vips/VipsImage;[D)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_newFromImageNative
  (JNIEnv *, jobject, jobject, jdoubleArray);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    newFromByteBuffer
 * Signature: (Ljava/nio/ByteBuffer;I)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_newFromByteBuffer
  (JNIEnv *, jobject, jobject, jint);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    newFromBuffer
 * Signature: ([BI)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_newFromBuffer
  (JNIEnv *, jobject, jbyteArray, jint);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    imageGetFormatNative
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_criteo_vips_VipsImageImpl_imageGetFormatNative
  (JNIEnv *, jobject);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    castUcharNative
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_castUcharNative
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    castNative
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_castNative
  (JNIEnv *, jobject, jint, jboolean);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    imageGetInterpretationNative
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_criteo_vips_VipsImageImpl_imageGetInterpretationNative
  (JNIEnv *, jobject);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    colourspaceNative
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_colourspaceNative__I
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    colourspaceNative
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_colourspaceNative__II
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    histFindNdimNative
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_histFindNdimNative
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    resizeNative
 * Signature: (IIZ)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_resizeNative
  (JNIEnv *, jobject, jint, jint, jboolean);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    max1Native
 * Signature: (Lcom/criteo/vips/Max1Result;)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_max1Native
  (JNIEnv *, jobject, jobject);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    padNative
 * Signature: (II[DI)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_padNative
  (JNIEnv *, jobject, jint, jint, jdoubleArray, jint);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    cropNative
 * Signature: (IIII)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_cropNative
  (JNIEnv *, jobject, jint, jint, jint, jint);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    findTrimNative
 * Signature: (D[D)[I
 */
JNIEXPORT jintArray JNICALL Java_com_criteo_vips_VipsImageImpl_findTrimNative
  (JNIEnv *, jobject, jdouble, jdoubleArray);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    compose
 * Signature: (Lcom/criteo/vips/VipsImage;)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_compose
  (JNIEnv *, jobject, jobject);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    flattenNative
 * Signature: ([D)V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_flattenNative
  (JNIEnv *, jobject, jdoubleArray);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    writePNGToArrayNative
 * Signature: (IZIZ)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_criteo_vips_VipsImageImpl_writePNGToArrayNative
  (JNIEnv *, jobject, jint, jboolean, jint, jboolean);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    writeToArrayNative
 * Signature: (Ljava/lang/String;IZ)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_criteo_vips_VipsImageImpl_writeToArrayNative
  (JNIEnv *, jobject, jstring, jint, jboolean);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    getWidth
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_criteo_vips_VipsImageImpl_getWidth
  (JNIEnv *, jobject);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    getHeight
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_criteo_vips_VipsImageImpl_getHeight
  (JNIEnv *, jobject);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    getBands
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_criteo_vips_VipsImageImpl_getBands
  (JNIEnv *, jobject);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    getPointNative
 * Signature: (II)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_criteo_vips_VipsImageImpl_getPointNative
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    hasAlpha
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_criteo_vips_VipsImageImpl_hasAlpha
  (JNIEnv *, jobject);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    convertTosRGB
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_convertTosRGB
  (JNIEnv *, jobject);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    getNbFrame
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_criteo_vips_VipsImageImpl_getNbFrame
  (JNIEnv *, jobject);

/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    release
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_criteo_vips_VipsImageImpl_release
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif

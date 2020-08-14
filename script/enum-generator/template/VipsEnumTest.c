$license

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <jni.h>
#include <vips/vips.h>

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
$tests
}

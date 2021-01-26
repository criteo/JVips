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

#include <jni.h>
#include <vips/vips.h>
#include <stdlib.h>

#include "VipsException.h"

void throwRuntimeExceptionFallback(JNIEnv *env, const char* msg)
{
    jclass cls = (*env)->FindClass(env, "java/lang/RuntimeException");
    (*env)->ThrowNew(env, cls, msg);
}

void throwVipsException(JNIEnv *env, const char *msg)
{
    jclass cls = (*env)->FindClass(env, "com/criteo/vips/VipsException");
    if (cls == 0) {
        throwRuntimeExceptionFallback(env, "Cannot find VipsException class");
        return;
    }

    jmethodID ctor = (*env)->GetMethodID(env, cls, "<init>", "(Ljava/lang/String;Ljava/lang/String;)V");
    if (ctor == NULL) {
        throwRuntimeExceptionFallback(env, "Failed to find the VipsException constructor");
        return;
    }

    const char* vipsErrorBuffer = vips_error_buffer();
    jobject vipsErrorBufferMsg = (*env)->NewStringUTF(env, vipsErrorBuffer);
    jthrowable throwable = (*env)->NewObject(env, cls, ctor, (*env)->NewStringUTF(env, msg), vipsErrorBufferMsg);

    if (throwable == NULL) {
        throwRuntimeExceptionFallback(env, "Failed to instanciate VipsException object");
        return;
    }

    (*env)->Throw(env, throwable);
    vips_error_clear();
}

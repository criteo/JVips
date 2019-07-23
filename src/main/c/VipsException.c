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

void throwVipsException(JNIEnv *env, const char *msg)
{
    jclass vips_exception_class = (*env)->FindClass(env, "com/criteo/vips/VipsException");

    if (vips_exception_class == 0) {
        jclass rt_exception_class = (*env)->FindClass( env, "java/lang/RuntimeException");
        (*env)->ThrowNew(env, rt_exception_class, "Cannot find VipsException class");
        return;
    }
    (*env)->ThrowNew(env, vips_exception_class, msg);
}

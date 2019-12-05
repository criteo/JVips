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
#include "JniFieldsIDs.h"

jfieldID field_Max1Result_out;
jfieldID field_Max1Result_x;
jfieldID field_Max1Result_y;

JNIEXPORT void JNICALL Java_com_criteo_vips_Max1Result_initMax1ResultFieldIDs(JNIEnv * env, jclass clazz)
{
    // https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/types.html
    field_Max1Result_out = (*env)->GetFieldID(env, clazz, "out", "D");
    field_Max1Result_x = (*env)->GetFieldID(env, clazz, "x", "I");
    field_Max1Result_y = (*env)->GetFieldID(env, clazz, "y", "I");
}

/*
  Copyright (c) 2022 Criteo

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

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;

public class VipsTestUtils {
    public static String getRessourcePath(String filename) {
        ClassLoader classLoader = VipsTestUtils.class.getClassLoader();
        return classLoader.getResource(filename).getFile();
    }

    public static byte[] getByteArray(String filename) throws IOException {
        return Files.readAllBytes(new File(getRessourcePath(filename)).toPath());
    }

    public static ByteBuffer getDirectByteBuffer(String filename) throws IOException {
        byte[] bytes = getByteArray(filename);
        ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
        buffer.put(bytes, 0, bytes.length);
        return buffer;
    }

    public static byte[] toPrimitives(Byte[] oBytes)
    {

        byte[] bytes = new byte[oBytes.length];
        for(int i = 0; i < oBytes.length; i++){
            bytes[i] = oBytes[i];
        }
        return bytes;

    }
}

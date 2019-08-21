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

import java.io.*;
import java.util.logging.Logger;

public class Vips {
    private final static Logger logger = Logger.getLogger("com.criteo.thirdparty.Vips");
    private static String SystemName = System.getProperty("os.name").toLowerCase();

    static {
        try {
            logger.info("Trying to load JVips");
            loadLibVips();
            init();
        } catch (IOException e) {
            throw new RuntimeException("Can't load JVips library and dependencies");
        } catch (VipsException e) {
            throw new RuntimeException("Can't init JVips");
        }
    }

    private static void loadLibVips() throws IOException {
        /**
         * Actually, loading embedded libraries doesn't work on Windows 64.
         * An UnsatisfiedLinkError exception is thrown with embedded dll:
         * "Can't find dependent libraries"
         * Thus, JVips.dll should use system libraries.
         *
         * TODO: add Windows 64 embedded libraries
         */
        if (!isWindows()) {
            loadLibraryFromJar("png16");
            loadLibraryFromJar("gif");
            loadLibraryFromJar("jpeg");
            loadLibraryFromJar("turbojpeg");
            loadLibraryFromJar("webp");
            loadLibraryFromJar("webpmux");
            loadLibraryFromJar("webpdemux");
            loadLibraryFromJar("imagequant");
            loadLibraryFromJar("lcms2");
            loadLibraryFromJar("vips");
        }
        else
            loadLibraryFromJar("libimagequant");
        loadLibraryFromJar("JVips");
    }

    private static boolean isWindows() {
        return SystemName.indexOf("win") >= 0;
    }

    private static void loadLibraryFromJar(String name) throws IOException {
        String libName = System.mapLibraryName(name);
        File temp;
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(libName)) {
            byte[] buffer = new byte[1024];
            int read;
            temp = File.createTempFile(libName, "");
            try (FileOutputStream fos = new FileOutputStream(temp)) {
                while ((read = in.read(buffer)) != -1) {
                    fos.write(buffer, 0, read);
                }
            }
        }
        System.load(temp.getAbsolutePath());
    }

    /**
     * Initialize vips context
     *
     * @throws VipsException
     */
    private static native void init() throws VipsException;
}

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

    private final static String[] linuxLibraries = {
            "exif",
            "spng",
            "gif",
            "jpeg",
            "turbojpeg",
            "webp",
            "webpmux",
            "webpdemux",
            "imagequant",
            "lcms2",
            "vips"
    };

    /**
     * Actually, loading embedded libraries doesn't work on Windows 64.
     * An UnsatisfiedLinkError exception is thrown with embedded dll:
     * "Can't find dependent libraries"
     * Thus, JVips.dll should use system libraries.
     * We only provide libimagequant because it's out of windows binaries release.
     *
     * TODO: add Windows 64 embedded libraries
     */
    private final static String[] windowsLibraries = {
            "libimagequant"
    };

    static {
        try {
            if (tryLoadLibrariesFromJar())
                logger.info("JVips dependencies have been loaded from jar");
            else
                logger.info("Using JVips dependencies installed on system");
            logger.info("Trying to load JVips");
            loadLibraryFromJar("JVips");
            init();
        } catch (IOException e) {
            throw new RuntimeException("Can't load JVips library and/or dependencies");
        } catch (VipsException e) {
            throw new RuntimeException("Can't init JVips");
        }
    }

    private static boolean tryLoadLibrariesFromJar() throws IOException {
        String[] libraries = !isWindows() ? linuxLibraries : windowsLibraries;
        try {
            for (String library : libraries) {
                loadLibraryFromJar(library);
            }
        } catch (NullPointerException e) {
            return false;
        }
        return true;
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

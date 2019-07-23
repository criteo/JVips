prefix=@CMAKE_INSTALL_PREFIX@
includedir=${prefix}/include
libdir=${prefix}/lib
bindir=${prefix}/bin

Name: imagequant
Description: Small, portable C library for high-quality conversion of RGBA images to 8-bit indexed-color (palette) images.
URL: https://pngquant.org/lib/
Version: VERSION
Libs: -L${libdir} -L${bindir} -limagequant
Cflags: -I${includedir}

#!/bin/bash

set -ex

. variables.sh

export PKG_CONFIG_PATH="$PREFIX/lib/pkgconfig:$PKG_CONFIG_PATH"

mkdir -p $PREFIX
mkdir -p $CHECKOUT

if [ -z "$DEBUG" ] || [ $DEBUG -eq 0 ]; then
    CFLAGS="-O3 -g"
    CXXFLAGS="-O3 -g"
else
    CFLAGS="-g3 -Wall -Wmaybe-uninitialized"
    CXXFLAGS="-g3 -Wall -Wmaybe-uninitialized"
fi

pushd "$CHECKOUT"

# Build libimagequant
wget -nv -nc "https://github.com/ImageOptim/libimagequant/archive/${LIQ_VERSION}.tar.gz"
tar xf "${LIQ_VERSION}.tar.gz"
cp $PATCH_DIR/CMakeLists.txt $CHECKOUT/libimagequant-${LIQ_VERSION}/CMakeLists.txt
cp $PATCH_DIR/imagequant.pc.in.cmake $CHECKOUT/libimagequant-${LIQ_VERSION}/imagequant.pc.in.cmake
mkdir -p ${BUILDDIR}/${TARGET}/libimagequant
pushd "${BUILDDIR}/${TARGET}/libimagequant"
cmake -DCMAKE_CFLAGS=${CFLAGS} \
    -DCMAKE_TOOLCHAIN_FILE=${TOOLCHAIN} \
    -DCMAKE_INSTALL_PREFIX=${PREFIX} \
    $CHECKOUT/libimagequant-${LIQ_VERSION}
make -j ${JOBS}
make install
popd

# Build JPEG
wget -nv -nc "https://github.com/libjpeg-turbo/libjpeg-turbo/archive/${JPG_VERSION}.tar.gz"
tar xf "${JPG_VERSION}.tar.gz"
pushd "${CHECKOUT}/libjpeg-turbo-${JPG_VERSION}"
autoreconf -fiv
popd
mkdir -p ${BUILDDIR}/${TARGET}/libjpeg-turbo-${JPG_VERSION}
pushd "${BUILDDIR}/${TARGET}/libjpeg-turbo-${JPG_VERSION}"
$CHECKOUT/libjpeg-turbo-${JPG_VERSION}/configure \
    CFLAGS="${CFLAGS}" CXXFLAGS="${CXXFLAGS}" \
    ${HOST} \
    --enable-shared --disable-static \
    --prefix="$PREFIX" \
    --disable-dependency-tracking --with-jpeg8

make -j ${JOBS}
make -j ${JOBS} install
popd

# Build PNG
wget -nv -nc "http://prdownloads.sourceforge.net/libpng/libpng-${PNG_VERSION}.tar.xz"
tar xf "libpng-${PNG_VERSION}.tar.xz"
mkdir -p ${BUILDDIR}/${TARGET}/libpng-${PNG_VERSION}
pushd "${BUILDDIR}/${TARGET}/libpng-${PNG_VERSION}"
$CHECKOUT/libpng-${PNG_VERSION}/configure \
    CFLAGS="${CFLAGS}" \
    CXXFLAGS="${CXXFLAGS}" \
    ${HOST} \
    CPPFLAGS="-I$PREFIX/include" \
    LDFLAGS="-L$PREFIX/lib -Wl,-rpath=$PREFIX/lib" \
    --enable-shared \
    --disable-static \
    --prefix="$PREFIX" \
    --disable-dependency-tracking \
    --disable-silent-rules

make -j ${JOBS}
make -j ${JOBS} install
popd


# Build GIF
wget -nv -nc "https://downloads.sourceforge.net/project/giflib/giflib-${GIF_VERSION}.tar.bz2"
tar xf "giflib-${GIF_VERSION}.tar.bz2"
mkdir -p ${BUILDDIR}/${TARGET}/giflib-${GIF_VERSION}
pushd "${BUILDDIR}/${TARGET}/giflib-${GIF_VERSION}"
$CHECKOUT/giflib-${GIF_VERSION}/configure \
    CFLAGS="${CFLAGS}" \
    CXXFLAGS="${CXXFLAGS}" \
    ${HOST} \
    --enable-shared --disable-static --prefix="$PREFIX" \
    --disable-dependency-tracking

make -j ${JOBS}
make -j ${JOBS} install
popd

# Build WEBP
wget -nv -nc "http://downloads.webmproject.org/releases/webp/libwebp-${WEBP_VERSION}.tar.gz"
tar xf "libwebp-${WEBP_VERSION}.tar.gz"
mkdir -p ${BUILDDIR}/${TARGET}/libwebp-${WEBP_VERSION}
pushd "${BUILDDIR}/${TARGET}/libwebp-${WEBP_VERSION}"
$CHECKOUT/libwebp-${WEBP_VERSION}/configure \
    CFLAGS="${CFLAGS}" \
    CXXFLAGS="${CXXFLAGS}" \
    ${HOST} \
    --enable-shared --disable-static --prefix="$PREFIX" \
    --disable-dependency-tracking \
    --with-gifincludedir=$PREFIX/include \
    --with-giflibdir=$PREFIX/lib \
    --with-jpegincludedir=$PREFIX/include \
    --with-jpeglibdir=$PREFIX/lib \
    --with-pngincludedir=$PREFIX/include \
    --with-pnglibdir=$PREFIX/lib \
    --enable-libwebpmux \
    --without-orc \
    --disable-assert \
    --enable-avx2 \
    --enable-sse4.1 \
    --enable-sse2 \
    --enable-threading

make -j ${JOBS}
make -j ${JOBS} install
popd

# Build LCMS2
wget -nv -nc "https://github.com/mm2/Little-CMS/archive/lcms${LCMS2_VERSION}.tar.gz"
tar xf "lcms${LCMS2_VERSION}.tar.gz"
mkdir -p ${BUILDDIR}/${TARGET}/Little-CMS-lcms${LCMS2_VERSION}
pushd "${BUILDDIR}/${TARGET}/Little-CMS-lcms${LCMS2_VERSION}"
$CHECKOUT/Little-CMS-lcms${LCMS2_VERSION}/configure \
    CFLAGS="${CFLAGS}" \
    CXXFLAGS="${CXXFLAGS}" \
    ${HOST} \
    --enable-shared --disable-static --prefix="$PREFIX"

make -j ${JOBS}
make -j ${JOBS} install
popd

# Build LibVips
wget -nv -nc "https://github.com/libvips/libvips/archive/v${VIPS_VERSION}.tar.gz"
mkdir -p "${CHECKOUT}/libvips-${VIPS_VERSION}-${TARGET}"
tar zxf "v${VIPS_VERSION}.tar.gz" -C "${CHECKOUT}/libvips-${VIPS_VERSION}-${TARGET}" --strip-components=1
pushd "${CHECKOUT}/libvips-${VIPS_VERSION}-${TARGET}"
./autogen.sh --version # --version makes the underlying ./configure call a noop.
./configure \
     CFLAGS="${CFLAGS}" \
     CXXFLAGS="${CXXFLAGS}" \
     ${HOST} \
     PKG_CONFIG_PATH="$PREFIX/lib/pkgconfig" \
     LDFLAGS="-L$PREFIX/lib -Wl,-rpath=$PREFIX/lib" \
     --enable-shared --disable-static \
     --with-jpeg-includes=$PREFIX/include \
     --with-jpeg-libraries=$PREFIX/lib \
     --with-png-includes=$PREFIX/include \
     --with-png-libraries=$PREFIX/lib \
     --with-giflib-includes=$PREFIX/include \
     --with-giflib-libraries=$PREFIX/lib \
     --with-libwebp \
     --with-expat=$PREFIX \
     LIBWEBP_CFLAGS="-I$PREFIX/include/" \
     LIBWEBP_LIBS="-L$PREFIX/lib/ -lwebp -lwebpmux -lwebpdemux" \
     --with-lcms \
     LCMS_CFLAGS="-I$PREFIX/include/" \
     LCMS_LIBS="-L$PREFIX/lib/ -llcms2" \
     --with-imagequant \
     --without-magick \
     --without-orc \
     --without-gsf \
     --prefix="$PREFIX"
make -j ${JOBS}
make install
popd

# !pushd "$CHECKOUT"
popd

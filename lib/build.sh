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
wget --retry-connrefused -O - -nv -nc "https://github.com/ImageOptim/libimagequant/archive/${LIQ_VERSION}.tar.gz" | tar -xz
cp $PATCH_DIR/CMakeLists.txt $CHECKOUT/libimagequant-${LIQ_VERSION}/CMakeLists.txt
cp $PATCH_DIR/imagequant.pc.in.cmake $CHECKOUT/libimagequant-${LIQ_VERSION}/imagequant.pc.in.cmake
mkdir -p ${BUILDDIR}/${TARGET}/libimagequant-${LIQ_VERSION}
pushd "${BUILDDIR}/${TARGET}/libimagequant-${LIQ_VERSION}"
cmake -DCMAKE_C_FLAGS=${CFLAGS} \
    -DCMAKE_TOOLCHAIN_FILE=${TOOLCHAIN} \
    -DCMAKE_INSTALL_PREFIX=${PREFIX} \
    $CHECKOUT/libimagequant-${LIQ_VERSION}
make -j ${JOBS}
make install
popd

# Build JPEG
wget --retry-connrefused -O - -nv -nc "https://github.com/libjpeg-turbo/libjpeg-turbo/archive/${JPG_VERSION}.tar.gz" | tar -xz
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
wget --retry-connrefused -O - -nv -nc "https://downloads.sourceforge.net/libpng/libpng-${PNG_VERSION}.tar.gz" | tar -xz
mkdir -p ${BUILDDIR}/${TARGET}/libpng-${PNG_VERSION}
pushd "${BUILDDIR}/${TARGET}/libpng-${PNG_VERSION}"
$CHECKOUT/libpng-${PNG_VERSION}/configure \
    CFLAGS="${CFLAGS}" \
    CXXFLAGS="${CXXFLAGS}" \
    ${HOST} \
    CPPFLAGS="-I$PREFIX/include" \
    LDFLAGS="-L$PREFIX/lib -Wl,-rpath,$PREFIX/lib" \
    --enable-shared \
    --disable-static \
    --prefix="$PREFIX" \
    --disable-dependency-tracking \
    --disable-silent-rules
make -j ${JOBS}
make -j ${JOBS} install
popd


# Build GIF
wget --retry-connrefused -O - -nv -nc "https://downloads.sourceforge.net/project/giflib/giflib-${GIF_VERSION}.tar.gz" | tar -xz
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
wget --retry-connrefused -O - -nv -nc "https://storage.googleapis.com/downloads.webmproject.org/releases/webp/libwebp-${WEBP_VERSION}.tar.gz" | tar -xz
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
wget --retry-connrefused -O - -nv -nc "https://github.com/mm2/Little-CMS/archive/lcms${LCMS2_VERSION}.tar.gz" | tar -xz
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

# # Build Expat
# EXPAT_VERSION_TAG="R_${EXPAT_VERSION//./_}"
# wget -nv -nc https://github.com/libexpat/libexpat/releases/download/${EXPAT_VERSION_TAG}/expat-${EXPAT_VERSION}.tar.gz
# tar xf "expat-${EXPAT_VERSION}.tar.gz"
# mkdir -p "${BUILDDIR}/${TARGET}/expat-${EXPAT_VERSION}"
# pushd "${BUILDDIR}/${TARGET}/expat-${EXPAT_VERSION}"
# cmake -DCMAKE_CFLAGS=${CFLAGS} \
#     -DCMAKE_TOOLCHAIN_FILE=${TOOLCHAIN} \
#     -DCMAKE_INSTALL_PREFIX=${PREFIX} \
#     -DBUILD_tools=OFF \
#     -DBUILD_shared=ON \
#     -DBUILD_examples=OFF \
#     -DBUILD_tests=OFF \
#     -DBUILD_doc=OFF \
#     $CHECKOUT/expat-${EXPAT_VERSION}
# make -j ${JOBS}
# make -j ${JOBS} install
# popd

# Build LibVips
mkdir -p "${CHECKOUT}/libvips-${VIPS_VERSION}-${TARGET}"
wget --retry-connrefused -O - -nv -nc "https://github.com/libvips/libvips/archive/v${VIPS_VERSION}.tar.gz" | tar -xz -C "${CHECKOUT}/libvips-${VIPS_VERSION}-${TARGET}" --strip-components=1
pushd "${CHECKOUT}/libvips-${VIPS_VERSION}-${TARGET}"
./autogen.sh ${HOST} -version
./configure \
     CFLAGS="${CFLAGS}" \
     CXXFLAGS="${CXXFLAGS}" \
     ${HOST} \
     LDFLAGS="-L$PREFIX/lib -Wl,-rpath,$PREFIX/lib" \
     --enable-shared --disable-static \
     --with-jpeg-includes=$PREFIX/include \
     --with-jpeg-libraries=$PREFIX/lib \
     PNG_CFLAGS="-I$PREFIX/include/ -I$PREFIX/include/libpng16/" \
     PNG_LIBS="-L$PREFIX/lib/ -lpng16" \
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
     --without-rsvg \
     --prefix="$PREFIX"
make -j ${JOBS}
make install
popd

# !pushd "$CHECKOUT"
popd

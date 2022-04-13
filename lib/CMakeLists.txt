include(ExternalProject)

# Read external project versions
file(STRINGS ${CMAKE_CURRENT_SOURCE_DIR}/VERSIONS VERSIONS_LIST)
foreach(ITEM ${VERSIONS_LIST})
    string(REPLACE "=" ";" ARGS ${ITEM})
    list(GET ARGS 0 LIB)
    list(GET ARGS 1 VERSION)
    set(${LIB} ${VERSION})
endforeach()

set(VIPS_VERSION ${VIPS_VERSION} PARENT_SCOPE)
string(REPLACE "." "_" EXIF_VERSION_TAG ${EXIF_VERSION})

# Set the host target
if (NOT DEFINED BUILD_TARGET)
  set(CONFIGURE_HOST "--host=x86_64-pc-linux")
elseif(${BUILD_TARGET} STREQUAL "w64")
  set(CONFIGURE_HOST "--host=x86_64-w64-mingw32")
endif()

if (NOT DEFINED CMAKE_BUILD_TYPE)
set(CONFIGURE_CFLAGS "${CMAKE_C_FLAGS}")
set(CONFIGURE_CXXFLAGS "${CMAKE_CXX_FLAGS}")
elseif (${CMAKE_BUILD_TYPE} STREQUAL "Release")
  set(CONFIGURE_CFLAGS ${CMAKE_C_FLAGS_RELEASE})
  set(CONFIGURE_CXXFLAGS ${CMAKE_CXX_FLAGS_RELEASE})
elseif (${CMAKE_BUILD_TYPE} STREQUAL "Debug")
  set(CONFIGURE_CFLAGS ${CMAKE_C_FLAGS_DEBUG})
  set(CONFIGURE_CXXFLAGS ${CMAKE_CXX_FLAGS_DEBUG})
endif()

# Workaround to set more than one compiler flags
string(REPLACE " " "\ " CONFIGURE_CFLAGS "${CONFIGURE_CFLAGS}")
string(REPLACE " " "\ " CONFIGURE_CXXFLAGS "${CONFIGURE_CXXFLAGS}")

list(APPEND CONFIGURE_VARS
  PKG_CONFIG_PATH=${EXT_INSTALL_DIR}/lib/pkgconfig:$PKG_CONFIG_PATH
  CFLAGS=${CONFIGURE_CFLAGS}
  CXXFLAGS=${CONFIGURE_CXXFLAGS}
  LDFLAGS=-L${EXT_INSTALL_DIR}/lib,-Wl,-rpath=${EXT_INSTALL_DIR}/lib
  CPPFLAGS=-I${EXT_INSTALL_DIR}/include
  --prefix=${EXT_INSTALL_DIR}
  ${CONFIGURE_HOST}
  )

find_library(LIBIMAGEQUANT imagequant PATHS "${EXT_INSTALL_DIR}/lib" NO_DEFAULT_PATH)
if (NOT LIBIMAGEQUANT)
    # https://github.com/ImageOptim/libimagequant/issues/36
    if (CMAKE_COMPILER_IS_GNUCC AND CMAKE_C_COMPILER_VERSION VERSION_GREATER 7.0 AND ${BUILD_TARGET} STREQUAL "linux")
        set(LIQ_OPENMP "--with-openmp")
    else()
        set(LIQ_OPENMP "--without-openmp")
    endif()
    ExternalProject_Add(libimagequant
      URL "https://github.com/ImageOptim/libimagequant/archive/${LIQ_VERSION}.tar.gz"
      PREFIX "${CMAKE_CURRENT_BINARY_DIR}/libimagequant"
      CONFIGURE_COMMAND ./configure CC=${CMAKE_C_COMPILER} CFLAGS=${CONFIGURE_CFLAGS}
      --prefix=${EXT_INSTALL_DIR}
      --with-sse
      ${LIQ_OPENMP}
      BUILD_IN_SOURCE 1
      )

    if (${BUILD_TARGET} STREQUAL "w64")
      ExternalProject_Add_Step(libimagequant rename-dll
      COMMAND mv libimagequant.so libimagequant.dll
      WORKING_DIRECTORY ${EXT_INSTALL_DIR}/lib/
      DEPENDEES install
      )
      ExternalProject_Add_StepTargets(libimagequant rename-dll) 
    endif()
else()
    add_custom_target(libimagequant "")
endif()

find_library(LIBEXIF exif PATHS "${EXT_INSTALL_DIR}/lib" NO_DEFAULT_PATH)
if(NOT LIBEXIF)
    ExternalProject_Add(libexif
      URL "https://github.com/libexif/libexif/releases/download/libexif-${EXIF_VERSION_TAG}-release/libexif-${EXIF_VERSION}.tar.gz"
      PREFIX "${CMAKE_CURRENT_BINARY_DIR}/libexif"
      CONFIGURE_COMMAND ${CMAKE_CURRENT_BINARY_DIR}/libexif/src/libexif/configure
      ${CONFIGURE_VARS}
      --enable-shared
      --disable-static
      --disable-docs
      )
else()
    add_custom_target(libexif "")
endif()

find_library(LIBJPEG jpeg turbojpeg PATHS "${EXT_INSTALL_DIR}/lib" NO_DEFAULT_PATH)
if(NOT LIBJPEG)
    ExternalProject_Add(libjpeg
      URL "https://sourceforge.net/projects/libjpeg-turbo/files/${JPG_VERSION}/libjpeg-turbo-${JPG_VERSION}.tar.gz/download"
      PREFIX "${CMAKE_CURRENT_BINARY_DIR}/libjpeg-turbo"
      CMAKE_ARGS
      -DCMAKE_INSTALL_PREFIX=${EXT_INSTALL_DIR}
      -DCMAKE_TOOLCHAIN_FILE=${CMAKE_TOOLCHAIN_FILE}
      -DWITH_TURBOJPEG=ON
      -DENABLE_SHARED=ON
      -DENABLE_STATIC=OFF
      -DWITH_JPEG8=ON
      -DWITH_SIMD=ON
      BUILD_IN_SOURCE 1
      )
else()
    add_custom_target(libjpeg "")
endif()

find_library(TIFF tiff EXACT PATHS "${EXT_INSTALL_DIR}/lib" NO_DEFAULT_PATH)
if(NOT TIFF)
   ExternalProject_Add(tiff
     URL "http://download.osgeo.org/libtiff/tiff-${TIFF_VERSION}.tar.gz"
     PREFIX "${CMAKE_CURRENT_BINARY_DIR}/tiff"
     CONFIGURE_COMMAND ${CMAKE_CURRENT_BINARY_DIR}/tiff/src/tiff/configure
     ${CONFIGURE_VARS}
     --enable-shared
     --disable-static
     --without-x
     --disable-cxx
     --disable-lzma
     DEPENDS libjpeg
     )
else()
   add_custom_target(tiff "")
endif()

if (WITH_LIBSPNG)
    list(APPEND LIBSPNG_FLAGS
    --with-libspng
    SPNG_LIBS="-L${EXT_INSTALL_DIR}/lib -lspng"
    SPNG_CFLAGS=-I${EXT_INSTALL_DIR}/include)
    find_library(LIBSPNG spng PATHS "${EXT_INSTALL_DIR}/lib" NO_DEFAULT_PATH)
    if(NOT LIBSPNG)
        ExternalProject_Add(libspng
          URL "https://github.com/randy408/libspng/archive/v${SPNG_VERSION}.tar.gz"
          PREFIX "${CMAKE_CURRENT_BINARY_DIR}/libspng"
          PATCH_COMMAND patch -p1 < ${CMAKE_CURRENT_SOURCE_DIR}/patches/libspng-0.6-fixes.patch
          CMAKE_ARGS
          -DCMAKE_INSTALL_PREFIX=${EXT_INSTALL_DIR}
          -DCMAKE_BUILD_TYPE=Release
          -DSPNG_STATIC=OFF
          -DENABLE_OPT=ON
          BUILD_IN_SOURCE 1
          )
    else()
        add_custom_target(libspng "")
    endif()
else()
    list(APPEND LIBSPNG_FLAGS
    --without-libspng)
    add_custom_target(libspng "")
endif()

find_library(LIBPNG png PATHS "${EXT_INSTALL_DIR}/lib" NO_DEFAULT_PATH)
if(NOT LIBPNG)
    ExternalProject_Add(libpng
      URL "https://downloads.sourceforge.net/project/libpng/libpng16/${PNG_VERSION}/libpng-${PNG_VERSION}.tar.gz"
      PREFIX "${CMAKE_CURRENT_BINARY_DIR}/libpng"
      CONFIGURE_COMMAND ${CMAKE_CURRENT_BINARY_DIR}/libpng/src/libpng/configure
      ${CONFIGURE_VARS}
      --enable-shared
      --disable-static
      --disable-dependency-tracking
      --disable-silent-rules
      )
else()
    add_custom_target(libpng "")
endif()

find_library(GIFLIB gif PATHS "${EXT_INSTALL_DIR}/lib" NO_DEFAULT_PATH)
if (NOT GIFLIB)
    # giflib hasn't a standard build system, don't append CONFIGURE_VARS
    ExternalProject_Add(giflib
      URL "https://downloads.sourceforge.net/project/giflib/giflib-${GIF_VERSION}.tar.gz"
      PREFIX "${CMAKE_CURRENT_BINARY_DIR}/giflib"
      PATCH_COMMAND patch -p1 < ${CMAKE_CURRENT_SOURCE_DIR}/patches/giflib-cmake.patch
      CMAKE_ARGS
      -DCMAKE_INSTALL_PREFIX=${EXT_INSTALL_DIR}
      -DCMAKE_BUILD_TYPE=Release
      -DCMAKE_INSTALL_LIBDIR=lib
      -DBUILD_SHARED_LIBS=1
      -DENABLE_CCACHE=0
      BUILD_IN_SOURCE 1
      )
else()
    add_custom_target(giflib "")
endif()

find_library(LIBWEBP webp webpmux webpdemux PATHS "${EXT_INSTALL_DIR}/lib" NO_DEFAULT_PATH)
if(NOT LIBWEBP)
    ExternalProject_Add(libwebp
      URL "http://downloads.webmproject.org/releases/webp/libwebp-${WEBP_VERSION}.tar.gz"
      PREFIX "${CMAKE_CURRENT_BINARY_DIR}/libwebp"
      CONFIGURE_COMMAND ${CMAKE_CURRENT_BINARY_DIR}/libwebp/src/libwebp/configure
      ${CONFIGURE_VARS}
      --with-gifincludedir=${EXT_INSTALL_DIR}/include
      --with-giflibdir=${EXT_INSTALL_DIR}/lib
      --with-jpegincludedir=${EXT_INSTALL_DIR}/include
      --with-jpeglibdir=${EXT_INSTALL_DIR}/lib
      --with-pngincludedir=${EXT_INSTALL_DIR}/include
      --with-pnglibdir=${EXT_INSTALL_DIR}/lib
      --with-tiffincludedir=${EXT_INSTALL_DIR}/include
      --with-tifflibdir=${EXT_INSTALL_DIR}/lib
      --enable-shared
      --disable-static
      --disable-dependency-tracking
      --enable-libwebpmux
      --without-orc
      --disable-assert
      --enable-avx2
      --enable-sse4.1
      --enable-sse2
      --enable-threading
      DEPENDS libjpeg libpng giflib tiff
      )
else()
    add_custom_target(libwebp "")
endif()

if (WITH_LIBHEIF)
    list(APPEND LIBHEIF_FLAGS --with-heif)
    find_library(LIBAOM aom PATHS "${EXT_INSTALL_DIR}/lib" NO_DEFAULT_PATH)
    if(NOT LIBAOM)
        ExternalProject_Add(libaom
          URL "https://aomedia.googlesource.com/aom/+archive/refs/tags/v${AOM_VERSION}.tar.gz"
          PREFIX "${CMAKE_CURRENT_BINARY_DIR}/libaom"
          CMAKE_ARGS
          -DCMAKE_INSTALL_PREFIX=${EXT_INSTALL_DIR}
          -DCMAKE_BUILD_TYPE=Release
          -DCMAKE_INSTALL_LIBDIR=lib
          -DBUILD_SHARED_LIBS=1
          -DENABLE_CCACHE=0
          )
    else()
        add_custom_target(libaom "")
    endif()

    find_library(LIBHEIF heif PATHS "${EXT_INSTALL_DIR}/lib" NO_DEFAULT_PATH)
    if(NOT LIBHEIF)
        ExternalProject_Add(libheif
          URL "https://github.com/strukturag/libheif/releases/download/v${HEIF_VERSION}/libheif-${HEIF_VERSION}.tar.gz"
          PREFIX "${CMAKE_CURRENT_BINARY_DIR}/libheif"
          CMAKE_ARGS
          -DCMAKE_INSTALL_PREFIX=${EXT_INSTALL_DIR}
          -DCMAKE_BUILD_TYPE=Release
          -DHAVE_AOM=yes
          -DHAVE_GO=no
          DEPENDS libaom
          BUILD_IN_SOURCE 1
          )
    else()
        add_custom_target(libheif "")
    endif()
else()
    list(APPEND LIBHEIF_FLAGS --without-heif)
    add_custom_target(libheif "")
endif()

find_library(LCMS2 lcms2 EXACT PATHS "${EXT_INSTALL_DIR}/lib" NO_DEFAULT_PATH)
if(NOT LCMS2)
    ExternalProject_Add(lcms2
      URL "https://sourceforge.net/projects/lcms/files/lcms/${LCMS2_VERSION}/lcms2-${LCMS2_VERSION}.tar.gz/download"
      PREFIX "${CMAKE_CURRENT_BINARY_DIR}/lcms2"
      CONFIGURE_COMMAND ${CMAKE_CURRENT_BINARY_DIR}/lcms2/src/lcms2/configure
      ${CONFIGURE_VARS}
      --enable-shared
      --disable-static
      --disable-dependency-tracking
      )
else()
    add_custom_target(lcms2 "")
endif()

find_library(VIPS vips PATHS "${EXT_INSTALL_DIR}/lib" NO_DEFAULT_PATH)
if(NOT VIPS)
    # Cannot set CFLAGS with space separated flags
    ExternalProject_Add(libvips
      URL "https://github.com/libvips/libvips/archive/v${VIPS_VERSION}.tar.gz"
      PREFIX "${CMAKE_CURRENT_BINARY_DIR}/libvips"
      CONFIGURE_COMMAND ./autogen.sh
      PKG_CONFIG_PATH=${EXT_INSTALL_DIR}/lib/pkgconfig:$PKG_CONFIG_PATH
      LDFLAGS=-L${EXT_INSTALL_DIR}/lib,-Wl,-rpath=${EXT_INSTALL_DIR}/lib
      CPPFLAGS=-I${EXT_INSTALL_DIR}/include
      --prefix=${EXT_INSTALL_DIR}
      ${CONFIGURE_HOST}
      --enable-shared
      --disable-static
      --disable-dependency-tracking
      --with-libwebp
      --with-lcms
      --with-imagequant
      --with-jpeg
      --with-jpeg-includes=${EXT_INSTALL_DIR}/include
      --with-jpeg-libraries=${EXT_INSTALL_DIR}/lib
      --with-tiff
      --with-tiff-includes=${EXT_INSTALL_DIR}/include
      --with-tiff-libraries=${EXT_INSTALL_DIR}/lib
      --without-magick
      --without-orc
      --without-gsf
      --without-rsvg
      ${LIBSPNG_FLAGS}
      ${LIBHEIF_FLAGS}
      DEPENDS libjpeg libpng libspng giflib libwebp libimagequant lcms2 libheif tiff
      BUILD_IN_SOURCE 1
      )
else()
    add_custom_target(libvips "")
endif()

set(VIPS_INCLUDE_DIRS CACHE INTERNAL "${EXT_INSTALL_DIR}/include")
set(VIPS_LIBRARY_DIRS CACHE INTERNAL "${EXT_INSTALL_DIR}/lib")
set(VIPS_LIBRARIES CACHE INTERNAL "-lvips")

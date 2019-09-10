project(JVIPS C)
cmake_minimum_required(VERSION 3.6.0)

find_package(PkgConfig REQUIRED)

find_package(Java 1.8 REQUIRED)
include(UseJava)

set(JVIPS_VERSION 1.0.0)

set(CMAKE_C_FLAGS "-std=c99")
set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS} -g3")
set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS} -O3 -g")
set(CMAKE_CXX_FLAGS "-std=c++14")
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS} -g3")
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS} -O3 -g")

# Mingw toolchain fails to find awt and jvm libraries.
# Just check if JNI_INCLUDE_DIRS is defined.
find_package(JNI)
if (DEFINED JNI_INCLUDE_DIRS)
    message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
endif()
include_directories(${JNI_INCLUDE_DIRS})

# BUILD_TARGET should be define in Toolchain file
set(ENV{PKG_CONFIG_PATH} "${CMAKE_SOURCE_DIR}/build/${BUILD_TARGET}/inst/lib/pkgconfig/")

pkg_check_modules(GLIB2 REQUIRED glib-2.0)
if (GLIB2_FOUND)
    message (STATUS "GLIB2_INCLUDE_DIRS=${GLIB2_INCLUDE_DIRS}")
    message (STATUS "GLIB2_LIBRARIES=${GLIB2_LIBRARIES}")
    message (STATUS "GLIB2_LIBRARY_DIRS=${GLIB2_LIBRARY_DIRS}")
endif()
link_directories(${GLIB2_LIBRARY_DIRS})
include_directories(${GLIB2_INCLUDE_DIRS})

pkg_check_modules(VIPS REQUIRED vips)
if (VIPS_FOUND)
    message (STATUS "VIPS_INCLUDE_DIRS=${VIPS_INCLUDE_DIRS}")
    message (STATUS "VIPS_LIBRARIES=${VIPS_LIBRARIES}")
    message (STATUS "VIPS_LIBRARY_DIRS=${VIPS_LIBRARY_DIRS}")
endif()
link_directories(${VIPS_LIBRARY_DIRS})
include_directories(${VIPS_INCLUDE_DIRS})

add_subdirectory(src/main/java/com/criteo/vips/)
add_subdirectory(src/main/c)

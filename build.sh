#!/bin/bash

set -e

BASEDIR=$(pwd)

BUILD_LINUX=1
BUILD_WIN64=1
BUILD_MACOS=0
DIST=0
DEBUG=0
JOBS=8
BUILD_TYPE=Release
MAVEN_ARGS=""

while true; do
  case "$1" in
    --with-w64 ) BUILD_WIN64=1; shift;;
    --with-linux ) BUILD_LINUX=1; shift;;
    --with-macos ) BUILD_MACOS=1; shift;;
    --without-w64 ) BUILD_WIN64=0; shift;;
    --without-linux ) BUILD_LINUX=0; shift;;
    --without-macos ) BUILD_MACOS=0; shift;;
    --dist ) DIST=1; shift;;
    --minimal ) MAVEN_ARGS="-Pminimal"; shift;;
    --debug ) DEBUG=1; shift ;;
    --jobs ) JOBS="$2"; shift 2 ;;
    -- ) shift; break ;;
    * ) break ;;
  esac
done


if [ ${JOBS} -le 0 ]; then
    JOBS=1
fi;

if [ ${DEBUG} -eq 1 ]; then
    BUILD_TYPE=Debug
fi

export JOBS
export DEBUG
export BUILDDIR=${BASEDIR}/build

CMAKE_BIN=`which cmake3 || which cmake`

##########################
###### Build Linux #######
##########################

if [ ${BUILD_LINUX} -gt 0 ]; then
    export CC=gcc
    export CXX=g++
    export CPP=cpp
    export RANLIB=ranlib

    export HOST="--host=x86_64-pc-linux"
    export TARGET=linux
    export PREFIX=${BUILDDIR}/${TARGET}/inst/
    export TOOLCHAIN=${BASEDIR}/Toolchain-linux.cmake
    pushd "${BASEDIR}/lib"
    ./build.sh
    if [ $? -ne 0 ]; then
        echo "Linux dependencies build failed"
        exit 1
    fi
    popd
    mkdir -p $BUILDDIR/${TARGET}/JVips
    rm -rf $BUILDDIR/${TARGET}/JVips/*
    pushd "${BUILDDIR}/${TARGET}/JVips"
    ${CMAKE_BIN} ${BASEDIR} \
    -DCMAKE_TOOLCHAIN_FILE=${TOOLCHAIN} \
    -DCMAKE_BUILD_TYPE=${BUILD_TYPE}
    make -j ${JOBS}
    if [ $? -ne 0 ]; then
        echo "Linux JVips build failed"
        exit 1
    fi
    popd
fi

##########################
###### Build Win64 #######
##########################

if [ ${BUILD_WIN64} -gt 0 ]; then
    export MINGW=x86_64-w64-mingw32
    export CC=${MINGW}-gcc
    export CXX=${MINGW}-g++
    export CPP=${MINGW}-cpp
    export RANLIB=${MINGW}-ranlib
    export PATH=/usr/${MINGW}/bin:${PATH}

    export HOST="--host=x86_64-w64-mingw32"
    export TARGET=w64
    export PREFIX=${BUILDDIR}/${TARGET}/inst/
    export TOOLCHAIN=${BASEDIR}/Toolchain-x86_64-w64-mingw32.cmake
    export PKG_CONFIG="x86_64-w64-mingw32-pkg-config"
    pushd "${BASEDIR}/lib"
    ./build.sh
    if [ $? -ne 0 ]; then
        echo "Windows 64 dependencies build failed"
        exit 1
    fi
    popd
    mkdir -p ${BUILDDIR}/${TARGET}/JVips
    rm -rf ${BUILDDIR}/${TARGET}/JVips/*
    pushd "${BUILDDIR}/${TARGET}/JVips"
    ${CMAKE_BIN} ${BASEDIR} \
    -DCMAKE_TOOLCHAIN_FILE=${TOOLCHAIN} \
    -DCMAKE_BUILD_TYPE=${BUILD_TYPE}
    make -j ${JOBS}
    if [ $? -ne 0 ]; then
        echo "Windows 64 JVips build failed"
        exit 1
    fi
    popd
fi

##########################
###### Build macOS #######
##########################

if [ ${BUILD_MACOS} -gt 0 ]; then
    export HOST=""
    export TARGET=macOS
    export PREFIX=${BUILDDIR}/${TARGET}/inst/
    export TOOLCHAIN=${BASEDIR}/Toolchain-macOS.cmake

    # Under macOS libvips will not build as is, but you can rely on Homebrew.
    # Here's a formula for vips 8.8.3:
    # $ brew install https://raw.githubusercontent.com/Homebrew/homebrew-core/39ba7794c1678b0d61b179239f93e9ac553c045b/Formula/vips.rb

    # pushd "${BASEDIR}/lib"
    # ./build.sh
    # if [ $? -ne 0 ]; then
    #     echo "macOS dependencies build failed"
    #     exit 1
    # fi
    # popd

    mkdir -p $BUILDDIR/${TARGET}/JVips
    rm -rf $BUILDDIR/${TARGET}/JVips/*
    pushd "${BUILDDIR}/${TARGET}/JVips"
    ${CMAKE_BIN} ${BASEDIR} \
    -DCMAKE_TOOLCHAIN_FILE=${TOOLCHAIN} \
    -DCMAKE_BUILD_TYPE=${BUILD_TYPE}
    make -j ${JOBS}
    if [ $? -ne 0 ]; then
        echo "macOS JVips build failed"
        exit 1
    fi
    popd
fi


mvn ${MAVEN_ARGS} clean install

if [ ${DIST} -gt 0 ]; then
    if [ ${BUILD_LINUX} -gt 0 ]; then
       tar -czvf "JVips-linux.tar.gz" JVips.jar -C ${BUILDDIR}/linux/inst/ bin lib include share
    fi
fi

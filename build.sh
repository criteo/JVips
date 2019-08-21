#!/bin/bash

set -e

BASEDIR=$(pwd)

BUILD_LINUX=1
BUILD_WIN64=1
DEBUG=0
JOBS=8
BUILD_TYPE=Release

while true; do
  case "$1" in
    --without-w64 ) BUILD_WIN64=0; shift;;
    --without-linux ) BUILD_LINUX=0; shift;;
    --debug ) DEBUG=1; shift ;;
    --jobs ) JOBS="$2"; shift 2 ;;
    -- ) shift; break ;;
    * ) break ;;
  esac
done


if [ $JOBS -le 0 ]; then
    JOBS=1
fi;

if [ $DEBUG -eq 1 ]; then
    BUILD_TYPE=Debug
fi

export JOBS
export DEBUG
export BUILDDIR=${BASEDIR}/build

# For Fedora / CentOS
CMAKE_BIN=cmake3

if ! [ -x "$(command -v cmake3)" ]; then
    CMAKE_BIN=cmake
fi

(
##########################
###### Build Win64 #######
##########################

if [ $BUILD_WIN64 -gt 0 ]; then
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
    pushd "$BASEDIR/lib"
    ./build.sh
    if [ $? -ne 0 ]; then
        echo "Windows 64 dependencies build failed"
        exit 1
    fi
    popd
    mkdir -p $BUILDDIR/${TARGET}/JVips
    rm -rf $BUILDDIR/${TARGET}/JVips/*
    pushd "$BUILDDIR/${TARGET}/JVips"
    $CMAKE_BIN $BASEDIR \
              -DCMAKE_TOOLCHAIN_FILE=${TOOLCHAIN} \
              -DCMAKE_BUILD_TYPE=$BUILD_TYPE
    make -j $JOBS
    if [ $? -ne 0 ]; then
        echo "Windows 64 JVips build failed"
        exit 1
    fi
    popd
fi

##########################
###### Build Linux #######
##########################

if [ $BUILD_LINUX -gt 0 ]; then
    export CC=gcc
    export CXX=g++
    export CPP=cpp
    export RANLIB=ranlib

    export HOST="--host=x86_64-pc-linux"
    export TARGET=linux
    export PREFIX=${BUILDDIR}/${TARGET}/inst/
    export TOOLCHAIN=${BASEDIR}/Toolchain-linux.cmake
    pushd "$BASEDIR/lib"
    ./build.sh
    if [ $? -ne 0 ]; then
        echo "Linux dependencies build failed"
        exit 1
    fi
    popd
    mkdir -p $BUILDDIR/${TARGET}/JVips
    rm -rf $BUILDDIR/${TARGET}/JVips/*
    pushd "$BUILDDIR/${TARGET}/JVips"
    $CMAKE_BIN $BASEDIR \
              -DCMAKE_TOOLCHAIN_FILE=${TOOLCHAIN} \
              -DCMAKE_BUILD_TYPE=$BUILD_TYPE
    make -j $JOBS
    if [ $? -ne 0 ]; then
        echo "Linux JVips build failed"
        exit 1
    fi
    popd
fi

mvn clean install

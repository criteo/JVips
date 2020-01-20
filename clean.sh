#!/bin/bash

BASEDIR=$(pwd)

pushd "lib/"
./clean.sh
popd

rm -rf build
rm -f JVips.jar
rm -f JVips-*.tar.gz

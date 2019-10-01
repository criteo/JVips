#!/bin/bash

BASEDIR=$(pwd)

pushd "lib/"
./clean.sh
popd

rm -rf build
rm JVips.jar
rm JVips-*.tar.gz

#!/bin/bash

set -e

# This script sets up the dependencies to build JVips for Windows under Ubuntu through WSL.

# Install CMake from the web because Ubuntu packages an old one.
which cmake || {
    version=3.15
    build=2
    pushd /tmp
    rm -rf cmake-$version.$build-Linux-x86_64.sh 
    wget https://cmake.org/files/v$version/cmake-$version.$build-Linux-x86_64.sh 
    sudo mkdir -p /opt/cmake
    sudo sh cmake-$version.$build-Linux-x86_64.sh --prefix=/opt/cmake --exclude-subdir --skip-license        
    sudo ln -s /opt/cmake/bin/cmake /usr/bin/cmake
    popd
}

# Install mingw-w64 glib2 from a third-party PPA for glib2 because Ubuntu packages an old one.
sudo add-apt-repository --yes ppa:tobydox/mingw-w64
sudo apt-get update
sudo apt-get --yes install glib2-mingw-w64
# FIXME: Unfortunately, this PPA doesn't contain gobject-2.0 and gmodule-2.0,
# so we have to cross-compile it ourselves.

# Install build tools.
sudo apt-get --yes install mingw-w64 mingw-w64-tools nasm build-essential autoconf automake libtool

# Install JDK.
sudo apt-get --yes install openjdk-8-jdk

# Install glib/gtk official dependencies.
sudo apt-get --yes install gtk-doc-tools gobject-introspection libz-mingw-w64-dev

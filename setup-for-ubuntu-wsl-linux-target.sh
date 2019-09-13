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

# Install build tools.
sudo apt-get --yes install nasm build-essential autoconf automake libtool

# Install JDK.
sudo apt-get --yes install openjdk-8-jdk

# Install glib/gtk official dependencies.
sudo apt-get --yes install libglib2.0-dev gtk-doc-tools gobject-introspection zlib1g-dev

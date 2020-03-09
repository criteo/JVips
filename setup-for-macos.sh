#!/bin/bash

set -e

# This script sets up the dependencies of JVips for macOS.

brew tap homebrew/cask-versions
brew cask install homebrew/cask-versions/adoptopenjdk8
brew install cmake

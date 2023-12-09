JVips, a libvips Java wrapper
=====

![Logo](https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/240/emojidex/112/high-voltage-sign_26a1.png)

JVips is a Java wrapper for [libvips](https://github.com/libvips/libvips) using JNI.

# Status

This project is deployed and used in production at Criteo to serve billions of images a day.

Not all libvips capabilities are implemented. JVips currently exposes:
- Resize
- Pad
- Crop
- Find trim (get bounding box)
- Get pixel point
- Get image width / height / bands / Nb frame
- Has alpha channel
- Is sRGB colorspace
- Compose image with another one

Feel free to contribute.

# Example

Look at the hello world program in [SimpleExample.java](src/test/java/com/criteo/vips/example/SimpleExample.java). It opens an image file, resize it, and prints its size.

# Installation

## From prebuilt packages

You can download the latest JVips build from [GitHub Releases](https://github.com/criteo/JVips/releases) or from [Maven Central](https://search.maven.org/artifact/com.criteo/jvips):

```xml
<dependency>
  <groupId>com.criteo</groupId>
  <artifactId>jvips</artifactId>
</dependency>
```

Then, ensure your `libvips` and its dependencies are available on your system.

### 🐧 Linux

Install `libvips` with your favorite package manager.

For Ubuntu and Debian:
```
sudo apt-get install libvips42
```

For Fedora and CentOS:
```
sudo yum install vips
```

However, note that `JVips.jar` embeds `libvips.so` and its dependencies. The `jar` file is self-sufficient for Linux. Look the `--minimal` flag documented below if you don't want this behavior and prefer to rely on system-wide libraries.

### 🏁 Windows

Install libvips from GitHub Releases:

1. Download the [prebuilt Windows libvips archive](https://github.com/libvips/libvips/releases) matching the JVips version and unzip it
2. Add a `VIPS_HOME` environment variable pointing to the extracted directory
3. Append `$VIPS_HOME/bin` and `$VIPS_HOME` to your `$PATH` environment variable

### 🍎 macOS

Install libvips with Homebrew:

1. `brew install vips`

## From source

The build system relies on numerous dependencies including CMake 3 and Maven, as well as other native code compilers. Instead of listing them here, please refer to [Dockerfile](.github/docker/linux/Dockerfile) for an up-to-date list.

The `build.sh` script will download and build a subset of `JVips` dependencies from source in order to maximize optimizations. However, it is recommended to install all dependencies on the system first, as documented in the sections below.

Additionally, `build.sh` accepts the following options:
- `--with-w64`, `--without-w64`: enable/disable Windows 64 build (default: disable)
- `--with-linux`, `--without-linux`: enable/disable Linux build (default: enable)
- `--with-macos`, `--without-macos`: enable/disable macOS build (default: disable)
- `--skip-test`: disable unit tests (default: enable)
- `--run-benchmark`: launch benchmark suite (default: disable)
- `--dist`: build a `.tar.gz` archive containing all the build artifacts (default: disable)
- `--minimal`: build 'minimal' Maven profile so JVips dependencies aren't embedded in the .jar file (default: all)
- `--debug`: enable debugging in JVips and its dependencies (default: release mode)
- `--jobs N`: use N jobs to build (default: 8)

Run the build with:
```
$ ./build.sh [options]
```

Clean the project with:
```
$ ./clean.sh
```

### 🐧 Linux

Install `libvips` development packages with your favorite package manager.

For Ubuntu and Debian:
```
$ sudo apt-get install libvips-dev
$ ./build.sh --without-w64 --with-linux --without-macos
```

For Fedora and CentOS:
```
$ sudo yum install vips-devel
$ ./build.sh --without-w64 --with-linux --without-macos
```

To build with Docker:
```
$ docker build --build-arg UID=$(id -u) --build-arg GID=$(id -g) -f .github/docker/linux/Dockerfile -t jvips-builder-linux .
$ docker run --rm -v $(pwd):/app -w /app jvips-builder-linux
```

### 🏁 Windows

Windows builds are cross-compiled from Linux using MingW64. Docker and WSL are both supported.

To build with Docker:
```
$ docker build --build-arg UID=$(id -u) --build-arg GID=$(id -g) -f .github/docker/windows/Dockerfile -t builder .
$ docker run --rm -v $(pwd):/app -w /app builder bash -ex build.sh --with-w64 --without-linux --without-macos
```

To build with WSL:
```
$ ./setup-for-ubuntu-wsl-w64-target.sh
$ ./build.sh --with-w64 --without-linux --without-macos
```

### 🍎 macOS

As macOS is mostly a development environment, `JVips` doesn't provide tools to build dependencies from sources and will always use system-wide libraries. Homebrew installs all the required dependencies and headers to build `JVips` as is:
```
$ brew install vips
$ ./build.sh --without-w64 --without-linux --with-macos
```

# Maintenance

## Dependency upgrades

Libraries are downloaded from hard-coded URL defined in the [lib/CMakeLists.txt](lib/CMakeLists.txt) CMake project.

These libraries can be upgraded by changing the version number in the [lib/VERSIONS](lib/VERSIONS) file.

This repository uses [Renovate](https://github.com/renovatebot/renovate) as an automated dependency upgrade checking mechanism:

* The [Renovate Github app](https://github.com/apps/renovate) will periodically check the dependencies for updates
* The [lib/VERSIONS](lib/VERSIONS) file also contains Bash comments parsed by Renovate. These comments are used by Renovate to find out which data source to use to check for the latest versions of dependencies (see [Regex Managers](https://docs.renovatebot.com/configuration-options/#regexmanagers) feature)

The Renovate configuration is located in [.github/renovate.json](.github/renovate.json).

## Benchmark

To run a benchmark:
1. `./build.sh --run-benchmark`

On an Ubuntu 18.04 VM running with a quad-core Xeon E3-1271 v3 @ 3.6GHz, we apply the following operations:
- open an 1920x1080 jpg image
- resize to 512x512 dimension
- crop a 128x128 rectangle on the top left corner
- pad to a 256x256 image
- write the output image into a buffer
- release resources

|Implementation|Run time (ms)|Times slower|
|------------- |:------------:| ---------:|
| Vips C 8.7   | 12           | 1.0       |
| JVips 1.0    | 22           | 1.83      |

According to [these results](https://github.com/jcupitt/libvips/wiki/Speed-and-memory-use), `JVips` is as slower as `py-vips`.

## Tests

[JVips tests](src/test/java/com/criteo/vips/VipsImageTest.java) are a good starting point to see how methods can be used.

## `vips` function bindings

The following steps explain how to bind a function from `libvips`.

Let's add `hasAlpha()` method:

1. Declare the method in [`VipsImage`](src/main/java/com/criteo/vips/VipsImage.java) class and
   [Image](src/main/java/com/criteo/vips/Image.java) interface
1. Declare the native method in [`VipsImage`](src/main/java/com/criteo/vips/VipsImage.java) class
```java
public native boolean hasAlpha();
```
1. Run build.sh to generate JNI header file:
```c
/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    hasAlpha
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_criteo_vips_VipsImageImpl_hasAlpha
  (JNIEnv *, jobject);
```
1. Define and implement function in [VipsImage.c](src/main/c/VipsImage.c)
```c
JNIEXPORT jboolean JNICALL
Java_com_criteo_vips_VipsImageImpl_hasAlpha(JNIEnv *env, jobject obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    return vips_image_hasalpha(im);
}
```

# Docker debugging

To debug the Docker image, you should build, run, and enter it as root:
```
$ docker build --build-arg UID=$(id -u) --build-arg GID=$(id -g) -f .github/docker/linux/Dockerfile -t jvips-builder-linux .
$ docker run --rm -v $(pwd):/app -w /app -u root -it jvips-builder-linux bash
```

Once the Docker container is running and you have a prompt:

```
$ ./build.sh
```

## TODO

- [ ] Add the missing operations
- [ ] Adapt the binding design for calling function by operation name (see also: https://libvips.github.io/libvips/API/current/binding.md.html)
- [X] Publish artifacts to Maven Central

# Contact

`JVips` is developed and maintained by Criteo. All inquiries should go to `github@criteo.com`.

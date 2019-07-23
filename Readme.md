#### JVips: libvips Java wrapper

This is a Java wrapper which binds some operations from [libvips](https://github.com/libvips/libvips) using JNI.

This project is deployed and used in production for applying image transformations in the Criteo internal CDN.

Available operations:
- Resize
- Pad
- Crop
- Find trim (get bounding box)
- Get pixel point
- Get image width / height / bands / Nb frame
- Has alpha channel
- Is sRGB colorspace
- Compose image with another one

#### Disclaimer
Not all libvips capabilities are implemented. Feel free to add them.

#### Building from source
JVips build some dependencies from source in order to maximize optimizations.
Linux libraries are embedded in jar file.

Libraries are downloaded from given url in lib/build.sh.
Edit lib/variable.sh for updating libraries version.

Make sure you have cmake3 installed and every dependencies required by libvips on your system:

```
$ sudo dnf install cmake3
```

Moreover, you need some extra dependecencies for cross building Windows 64 bits libJVips.dll:
```
$ sudo dnf install mingw-w64-tools mingw64-gcc mingw64-glib2 mingw64-win-iconv mingw64-expat
```

Run the build with:
```
$ ./build.sh
```
Build options:
- --debug, enable debugging in JVips and its dependencies (default: release mode)
- --without-w64, disable Windows 64 build (default: disable)
- --without-linux, disable Linux build (default: disable)
- --jobs N, define make jobs number (default: 8)

Clean project with:
```bash
$ ./clean.sh
```

#### Installing
##### Linux
Install libvips required dependencies.

##### Windows
Install the official Vips binaries:

1. Download the 64 bit version of libvips ([vips-dev-w64-all-8.7.0.zip](https://github.com/libvips/libvips/releases/download/v8.7.0/vips-dev-w64-all-8.7.0.zip)) and unzip it
2. Set environment variable VIPS_HOME to C:\Program Files\vips-dev-w64-all-8.7.0
3. Append $VIPS_HOME/bin and $VIPS_HOME to $PATH environment variable

##### Testing
Run the HelloWorld main in src/example/java/com/criteo/vips/HelloWorld.java.
It should open the file 'src/example/ressources/in_vips.jpg', resize image and prints:
```
INFO: Trying to load JVips
Image has been correctly resized: (1920,1080) -> (960,540)
```

#### How to bind a vips function
The following steps explain how to bind a function from libvips.
Let's add hasAlpha() method:

1. Declare method in VipsImage interface in src/main/java/com/criteo/vips/VipsImage.java
2. Declare native method in VipsImageImpl in src/main/java/com/criteo/vips/VipsImageImpl.java
```java
public native boolean hasAlpha();
```
3. Run build.sh to generate JNI header file:
```c
/*
 * Class:     com_criteo_vips_VipsImageImpl
 * Method:    hasAlpha
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_criteo_vips_VipsImageImpl_hasAlpha
  (JNIEnv *, jobject);
```
4. Define and implement function in src/main/c/VipsImage.c
```c
JNIEXPORT jboolean JNICALL
Java_com_criteo_vips_VipsImageImpl_hasAlpha(JNIEnv *env, jobject obj)
{
    VipsImage *im = (VipsImage *) (*env)->GetLongField(env, obj, handle_fid);
    return vips_image_hasalpha(im);
}
```

#### Benchmark results

On an Ubuntu 18.04 VM running with a quad-core Xeon E3-1271 v3 @ 3.6GHz, we apply the following operations:
- open an 1920x1080 jpg image
- resize to 100x100 dimension
- crop a 50x30 rectangle on the top left corner
- pad to a 50x80 image
- write the output image into a buffer
- release resources

|Implementation|Run time (ms)|Times slower|
|------------- |:------------:| ---------:|
| Vips C 8.7   | 12           | 1.0       |
| JVips 1.0    | 22           | 1.83      |

According to [this results](https://github.com/jcupitt/libvips/wiki/Speed-and-memory-use), JVips is as slower as py-vips.

#### TODO
- Add the missing operations
- Adapt the binding design for calling function by operation name (see also: https://libvips.github.io/libvips/API/current/binding.md.html)
- Publish artifact on the maven central repository

#### Contact
github@criteo.com
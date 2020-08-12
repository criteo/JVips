set(BUILD_TARGET w64)

# the name of the target operating system
SET(CMAKE_SYSTEM_NAME Windows)

# which compilers to use for C and C++
SET(CMAKE_C_COMPILER x86_64-w64-mingw32-gcc)
SET(CMAKE_CXX_COMPILER x86_64-w64-mingw32-g++)
SET(CMAKE_RC_COMPILER x86_64-w64-mingw32-windres)
SET(CMAKE_AR x86_64-w64-mingw32-ar)
SET(CMAKE_LINKER x86_64-w64-mingw32-ld)
SET(CMAKE_RANLIB x86_64-w64-mingw32-ranlib)

# target environment location
SET(CMAKE_FIND_ROOT_PATH /usr/x86_64-w64-mingw32 /usr/bin/ /usr/ ${CMAKE_SOURCE_DIR}/build/${BUILD_TARGET}/inst)

# adjust the default behaviour of the FIND_XXX() commands:
# search headers and libraries in the target environment, search 
# programs in the host environment
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)

# linux (not win32) because it's cross-compiled from a Linux system.
set(JAVA_INCLUDE_PLATFORM_PATH linux)

set(BUILD_TARGET linux)

# the name of the target operating system
SET(CMAKE_SYSTEM_NAME Linux)

# which compilers to use for C and C++
SET(CMAKE_C_COMPILER gcc)
SET(CMAKE_CXX_COMPILER g++)
SET(CMAKE_RC_COMPILER windres)
SET(CMAKE_AR ar)
SET(CMAKE_RANLIB ranlib)

# target environment location
SET(CMAKE_FIND_ROOT_PATH /usr/ ${CMAKE_SOURCE_DIR}/build/${BUILD_TARGET}/inst)

# adjust the default behaviour of the FIND_XXX() commands:
# search headers and libraries in the target environment, search 
# programs in the host environment
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)

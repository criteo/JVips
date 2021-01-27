add_library(JVipsTest SHARED VipsEnumTest.c ${PROJECT_SOURCE_DIR}/src/main/c/VipsException.c)

add_dependencies(JVipsTest JVips VipsEnumTest_header)

target_link_libraries(JVipsTest ${VIPS_LIBRARIES} "-lvips")
target_include_directories(JVipsTest PUBLIC ${PROJECT_SOURCE_DIR}/src/main/c)

if ("${BUILD_TARGET}" STREQUAL "w64")
    # System.mapLibraryName doesn't expect "lib" prefix on Windows system
    set_target_properties(JVipsTest PROPERTIES PREFIX "")
endif()

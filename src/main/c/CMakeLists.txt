file(GLOB SOURCES Vips.c VipsContext.c VipsException.c VipsImage.c Max1Result.c)

add_library(JVips SHARED ${SOURCES})
add_dependencies(JVips Vips_header VipsContext_header VipsImage_header Max1Result_header)

target_link_libraries(JVips ${GLIB2_LIBRARIES} ${VIPS_LIBRARIES})
if ("${BUILD_TARGET}" STREQUAL "w64")
    # System.mapLibraryName doesn't expect "lib" prefix on Windows system
    set_target_properties(JVips PROPERTIES PREFIX "")
endif()

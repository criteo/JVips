add_library(JVipsTest SHARED VipsEnumTest.c ${PROJECT_SOURCE_DIR}/src/main/c/VipsException.c)

add_dependencies(JVipsTest JVips VipsEnumTest_header)

target_link_libraries(JVipsTest ${VIPS_LIBRARIES})
target_include_directories(JVipsTest PUBLIC ${PROJECT_SOURCE_DIR}/src/main/c)
set(JAVA_TEST_SOURCE_DIRECTORY "${PROJECT_SOURCE_DIR}/src/test/java/com/criteo/vips/")
set(JNI_TEST_HEADER_DIRECTORY "${PROJECT_SOURCE_DIR}/src/test/c/")
set(JAVA_TEST_SOURCE_FILES "${JAVA_TEST_SOURCE_DIRECTORY}/VipsEnumTest.java")
set(JUNIT_HOME "${PROJECT_SOURCE_DIR}/build/artifacts/junit-4.13.1.jar")

add_jar(JVipsTestWrapper ${JAVA_TEST_SOURCE_FILES} "${PROJECT_SOURCE_DIR}/src/main/java/com/criteo/vips//VipsException.java" INCLUDE_JARS ${JVipsWrapper} ${JUNIT_HOME})
add_dependencies(JVipsTestWrapper JVipsWrapper)

file(REMOVE ${JNI_TEST_HEADER_DIRECTORY}/VipsEnumTest.h)

create_javah(TARGET VipsEnumTest_header
             CLASSES com.criteo.vips.VipsEnumTest
             CLASSPATH JVipsTestWrapper
             OUTPUT_NAME ${JNI_TEST_HEADER_DIRECTORY}/VipsEnumTest.h)
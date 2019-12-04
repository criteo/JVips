# Pre-compile java classes for generating JNI header files

set(JAVA_SOURCE_DIRECTORY "${PROJECT_SOURCE_DIR}/src/main/java/com/criteo/vips/")
set(JNI_HEADER_DIRECTORY "${PROJECT_SOURCE_DIR}/src/main/c/")
set(JAVA_SOURCE_FILES
    "${JAVA_SOURCE_DIRECTORY}/ImageFormat.java"
    "${JAVA_SOURCE_DIRECTORY}/Gravity.java"
    "${JAVA_SOURCE_DIRECTORY}/PixelPacket.java"
    "${JAVA_SOURCE_DIRECTORY}/Vips.java"
    "${JAVA_SOURCE_DIRECTORY}/VipsBandFormat.java"
    "${JAVA_SOURCE_DIRECTORY}/VipsContext.java"
    "${JAVA_SOURCE_DIRECTORY}/VipsException.java"
    "${JAVA_SOURCE_DIRECTORY}/VipsInterpretation.java"
    "${JAVA_SOURCE_DIRECTORY}/VipsImage.java"
    "${JAVA_SOURCE_DIRECTORY}/VipsImageImpl.java")

add_jar(JVipsWrapper ${JAVA_SOURCE_FILES})

file(REMOVE ${JNI_HEADER_DIRECTORY}/Vips.h
            ${JNI_HEADER_DIRECTORY}/VipsContext.h
            ${JNI_HEADER_DIRECTORY}/VipsImage.h)

create_javah(TARGET Vips_header
             CLASSES com.criteo.vips.Vips
             CLASSPATH JVipsWrapper
             OUTPUT_NAME ${JNI_HEADER_DIRECTORY}/Vips.h)
create_javah(TARGET VipsContext_header
             CLASSES com.criteo.vips.VipsContext
             CLASSPATH JVipsWrapper
             OUTPUT_NAME ${JNI_HEADER_DIRECTORY}/VipsContext.h)
create_javah(TARGET VipsImage_header
             CLASSES com.criteo.vips.VipsImageImpl
             CLASSPATH JVipsWrapper
             OUTPUT_NAME ${JNI_HEADER_DIRECTORY}/VipsImage.h)

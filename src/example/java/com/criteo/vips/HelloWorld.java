package com.criteo.vips;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HelloWorld {
    private static byte[] getByteArrayFromRessource(String filename) throws IOException {
        ClassLoader classLoader = VipsTestUtils.class.getClassLoader();
        String path = classLoader.getResource(filename).getFile();
        return Files.readAllBytes(new File(path).toPath());
    }

    public static void main(String[] args) {
        try {
            byte[] contents = getByteArrayFromRessource("in_vips.jpg");
            VipsImageImpl image = new VipsImageImpl(contents, contents.length);
            int width = image.getWidth();
            int height = image.getHeight();

            image.resize(new Dimension(width / 2, height / 2), true);
            System.out.println(String.format("Image has been correctly resized: (%d,%d) -> (%d,%d)",
                    width, height, image.getWidth(), image.getHeight()));
            // Release object reference and resources
            image.release();

        } catch (IOException | VipsException e) {
            System.err.println("Error: " + e);
        }
    }
}
package us.marseilles.steganos.core.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageWriter
{
    void write(BufferedImage image, String path) throws IOException;
}
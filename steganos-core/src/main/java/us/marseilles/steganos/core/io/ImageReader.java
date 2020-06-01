package us.marseilles.steganos.core.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageReader
{
    BufferedImage read(String filepath) throws IOException;
}
package us.marseilles.steganos.core.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * An implementation of {@link ImageReader} for reading from the local file system
 */
public class LocalImageReader implements ImageReader
{
    @Override
    public BufferedImage read(String filepath) throws IOException
    {
        File imageFile = new File(filepath);
        BufferedImage image = ImageIO.read(imageFile);
        return image;
    }
}
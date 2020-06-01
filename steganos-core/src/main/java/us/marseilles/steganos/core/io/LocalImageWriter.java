package us.marseilles.steganos.core.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * An implementation of {@link ImageWriter} for writing to the local file system
 */
public class LocalImageWriter implements ImageWriter
{
    @Override
    public void write(BufferedImage image, String path) throws IOException
    {
        if (!path.contains("."))
        {
            throw new IllegalArgumentException("File path must include a valid extension");
        }
        String fileFormat = path.substring(path.lastIndexOf('.') + 1);
        File imageFile = new File(path);
        ImageIO.write(image, fileFormat, imageFile);
    }
}
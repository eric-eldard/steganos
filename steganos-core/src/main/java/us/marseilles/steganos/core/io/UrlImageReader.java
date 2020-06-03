package us.marseilles.steganos.core.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * An implementation of {@link ImageReader} for reading from urls
 */
public class UrlImageReader implements ImageReader
{
    @Override
    public BufferedImage read(String url) throws IOException
    {
        BufferedImage image = ImageIO.read(new URL(url));
        return image;
    }
}
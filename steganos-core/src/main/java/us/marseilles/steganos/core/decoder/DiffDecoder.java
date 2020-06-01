package us.marseilles.steganos.core.decoder;

import java.awt.image.BufferedImage;

/**
 * Decode a message encoded by a {@link us.marseilles.steganos.core.encoder.DiffEncoder}
 */
public interface DiffDecoder
{
    String decode(BufferedImage sourceImage, BufferedImage encodedImage);
}
package us.marseilles.steganos.core.decoder;

import java.awt.image.BufferedImage;

/**
 * Handles decoding for all diff-type encoders
 */
public interface DiffDecoder
{
    String decode(BufferedImage sourceImage, BufferedImage encodedImage);
}
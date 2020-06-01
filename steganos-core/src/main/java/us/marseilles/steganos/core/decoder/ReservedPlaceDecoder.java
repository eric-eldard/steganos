package us.marseilles.steganos.core.decoder;

import java.awt.image.BufferedImage;

/**
 * Decode a message encoded by a {@link us.marseilles.steganos.core.encoder.ReservedPlaceEncoder}
 */
public interface ReservedPlaceDecoder
{
    /**
     * Extract a string encoded into an image using the lowest possible conspicuousness
     */
    String decode(BufferedImage encodedImage);

    /**
     * Extract a string encoded into an image using the specified conspicuousness
     *
     * @param conspicuousness if a non-standard conspicuousness is used, you must supply it during decode
     */
    String decode(BufferedImage encodedImage, int conspicuousness);
}
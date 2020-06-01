package us.marseilles.steganos.core.encoder;

import java.awt.image.BufferedImage;

/**
 * Encodes a message into an image by reserving the top values of 255 value color channels for messaging. The size of
 * the values reserved is determined by the conspicuousness factor. The conspicuousness factor's effect on the image
 * scales up more slowly, when compared with the reserved-place method, but distorts the image more (due to potentially
 * hitting the possible color channel floor). Max conspicuousness for this encoder type is 255. At 255, the entire image
 * is stripped away.
 *
 * This method requires the source image when decoding, instead of the conspicuousness factor. This makes guessing the
 * message by examining the image bits much more difficult.
 */
public interface DiffEncoder extends Encoder
{
    /**
     * In order to use the diff method, the source image must be restricted in its possible values, by what the
     * conspicuousness value will be. Example: a pixel's maximum R-channel value is 255, but after applying this method
     * with a conspicuousness factor of 10, all R-channel values will be capped at 245.
     */
    BufferedImage prepSourceImage(BufferedImage sourceImage, int conspicuousness);
}
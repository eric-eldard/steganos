package us.marseilles.steganos.core.encoder;

import java.awt.image.BufferedImage;

/**
 * Encodes a message into an image by reserving the top values of 255 value color channels for messaging. The size of
 * the values reserved is determined by the conspicuousness factor. The conspicuousness factor's effect on the image
 * scales up more slowly, when compared with the reserved-place method, but distorts the image more (due to potentially
 * hitting the possible color channel floor). Max conspicuousness for this encoder type is 255. At 255, the entire image
 * is stripped away and the image is just a visual representation of the encoded message.
 *
 * The default conspicuousness of 1 allows messages to be decoded by anyone, in the same way that reserved-place can be
 * decoded. If you know where the bits are stored, you can just read them out. However, using a higher conspicuousness
 * with the diff method (ironically) actually leads to more obfuscation. Each bit will be multiplied by a random value
 * up to the conspicuousness factor before being added to the image, so no one bit can read to decode the message.
 * Decoding an intercepted image is only possible if you can first correctly interpret the conspicuousness factor, by
 * examining the color channel value "ceiling"—i.e., the max brightness value of the prepped image. This effect could be
 * overdone, weakening the obfuscation. For example, a conspicuousness of 128 allows for message bits to have one of 128
 * values. However, if an interceptor can figure out what true-white was supposed to be in the image, then they have
 * guessed your ceiling (127). A value less than 10 is probably ideal; your mileage may very, depending on source image.
 *
 * Decode with {@link us.marseilles.steganos.core.decoder.DiffDecoder}
 */
public interface DiffWithPrepEncoder extends Encoder
{
    /**
     * In order to use the diff method, the source image must be restricted in its possible values, by what the
     * conspicuousness value will be. Example: a pixel's maximum R-channel value is 255, but after applying this method
     * with a conspicuousness factor of 10, all R-channel values will be capped at 245.
     */
    BufferedImage prepSourceImage(BufferedImage sourceImage, int conspicuousness);
}
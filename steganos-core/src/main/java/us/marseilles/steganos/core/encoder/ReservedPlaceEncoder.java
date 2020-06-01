package us.marseilles.steganos.core.encoder;

/**
 * Encodes a message into an image by reserving the bit at the base-2 place specified by the conspicuousness factor. The
 * conspicuousness factor's effect on the image scales up quickly. Max conspicuousness for this encoder type is 8.
 *
 * This method can be decoding with only the conspicuousness factor, instead of requiring the source image. This makes
 * guessing the message by examining the image bits easier than methods requiring the source image to decode.
 */
public interface ReservedPlaceEncoder extends Encoder
{
}
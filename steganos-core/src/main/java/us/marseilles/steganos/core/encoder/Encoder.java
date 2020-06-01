package us.marseilles.steganos.core.encoder;

import java.awt.image.BufferedImage;
import java.util.BitSet;
import java.util.function.Function;

import us.marseilles.steganos.core.util.Utils;

public interface Encoder
{
    /**
     * Encode the provided text into a new copy of the source image with the lowest possible conspicuousness for maximum
     * discretion.
     */
    default BufferedImage encode(BufferedImage sourceImage, String text)
    {
        return encode(sourceImage, text, 1);
    }

    /**
     * Encode the provided text into a new copy of the source image
     *
     * @param conspicuousness a value which varies the visual intensity of the effect; use the lowest value (1) for the
     *                        highest level of discretion; use highest value provided by your impl for best demo effect
     */
    default BufferedImage encode(BufferedImage sourceImage, String text, int conspicuousness)
    {
        Utils.validateConspicuousness(conspicuousness, getMaxConspicuousness());

        long textBytes = text.getBytes().length;
        long maxBytes = Utils.getMaxEncodableBytes(sourceImage);
        if (textBytes > maxBytes)
        {
            throw new IllegalArgumentException("The provided string is " + textBytes +
                " bytes, but a maximum of " + maxBytes + " bytes can be encoded into this image.");
        }

        int cols = sourceImage.getWidth();
        int rows = sourceImage.getHeight();

        BufferedImage encodedImage = new BufferedImage(
            cols,
            rows,
            BufferedImage.TYPE_INT_RGB
        );

        byte[] bytes = text.getBytes();
        BitSet bitSet = BitSet.valueOf(bytes);
        int bitIndex = 0;

        for (int y = 0; y < rows; y++)
        {
            for (int x = 0; x < cols; x++)
            {
                int sourceRGB = sourceImage.getRGB(x, y);

                int encodedR = encodeNextValue(bitSet, bitIndex++, sourceRGB, Utils.R_CHANNEL_FUNCTION, conspicuousness);
                int encodedG = encodeNextValue(bitSet, bitIndex++, sourceRGB, Utils.G_CHANNEL_FUNCTION, conspicuousness);
                int encodedB = encodeNextValue(bitSet, bitIndex++, sourceRGB, Utils.B_CHANNEL_FUNCTION, conspicuousness);

                int encodedRGB = Utils.makePixelValue(encodedR, encodedG, encodedB);

                encodedImage.setRGB(x, y, encodedRGB);
            }
        }

        return encodedImage;
    }

    int encodeNextValue(BitSet bitSet, int bitIndex, int sourceRGB, Function<Integer, Integer> channelFunc,
        int conspicuousness);

    int getMaxConspicuousness();
}
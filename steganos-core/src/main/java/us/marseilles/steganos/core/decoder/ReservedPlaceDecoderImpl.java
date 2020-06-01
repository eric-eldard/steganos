package us.marseilles.steganos.core.decoder;

import java.awt.image.BufferedImage;
import java.util.BitSet;
import java.util.function.Function;

import us.marseilles.steganos.core.util.Utils;

public class ReservedPlaceDecoderImpl implements ReservedPlaceDecoder
{
    @Override
    public String decode(BufferedImage encodedImage)
    {
        return decode(encodedImage, 1);
    }

    @Override
    public String decode(BufferedImage encodedImage, int conspicuousness)
    {
        int cols = encodedImage.getWidth();
        int rows = encodedImage.getHeight();

        BitSet bitSet = new BitSet(cols * rows);
        int bitIndex = 0;

        for (int y = 0; y < rows; y++)
        {
            for (int x = 0; x < cols; x++)
            {
                int encodedRgb = encodedImage.getRGB(x, y);

                boolean rVal = decodeValue(encodedRgb, Utils.R_CHANNEL_FUNCTION, conspicuousness);
                boolean gVal = decodeValue(encodedRgb, Utils.G_CHANNEL_FUNCTION, conspicuousness);
                boolean bVal = decodeValue(encodedRgb, Utils.B_CHANNEL_FUNCTION, conspicuousness);

                bitSet.set(bitIndex++, rVal);
                bitSet.set(bitIndex++, gVal);
                bitSet.set(bitIndex++, bVal);
            }
        }

        byte[] bytes = bitSet.toByteArray();

        return new String(bytes);
    }

    /**
     * Get the bit value at the place specified by conspicuousness. If the color channel's value is 00000010 and the
     * specified conspicuousness is 2, this method will return true.
     */
    private boolean decodeValue(int encodedRgb, Function<Integer, Integer> channelFunc, int conspicuousness)
    {
        String encodedChannelBits = Utils.makeByteString(channelFunc.apply(encodedRgb));
        int index = 8 - conspicuousness;
        int decodedValue = Character.getNumericValue(encodedChannelBits.charAt(index));
        return decodedValue > 0;
    }
}
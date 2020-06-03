package us.marseilles.steganos.core.decoder;

import java.awt.image.BufferedImage;
import java.util.BitSet;
import java.util.function.Function;

import us.marseilles.steganos.core.util.Utils;

public class DiffDecoderImpl implements DiffDecoder
{
    @Override
    public String decode(BufferedImage sourceImage, BufferedImage encodedImage)
    {
        int cols = encodedImage.getWidth();
        int rows = encodedImage.getHeight();
        if (cols != sourceImage.getWidth() || rows != sourceImage.getHeight())
        {
            throw new IllegalArgumentException(
                "Source image must be the same dimensions as the encoded image (" + cols + 'x' + rows + ").");
        }

        BitSet bitSet = new BitSet(cols * rows);
        int bitIndex = 0;

        for (int y = 0; y < rows; y++)
        {
            for (int x = 0; x < cols; x++)
            {
                int sourceRGB = sourceImage.getRGB(x, y);
                int encodedRGB = encodedImage.getRGB(x, y);

                boolean rVal = decodeValue(sourceRGB, encodedRGB, Utils.R_CHANNEL_FUNCTION);
                boolean gVal = decodeValue(sourceRGB, encodedRGB, Utils.G_CHANNEL_FUNCTION);
                boolean bVal = decodeValue(sourceRGB, encodedRGB, Utils.B_CHANNEL_FUNCTION);

                bitSet.set(bitIndex++, rVal);
                bitSet.set(bitIndex++, gVal);
                bitSet.set(bitIndex++, bVal);
            }
        }

        byte[] bytes = bitSet.toByteArray();

        return new String(bytes);
    }

    /**
     * Any difference between source and encoded images will be counted as a bit.
     */
    private boolean decodeValue(int sourceRGB, int encodedRGB, Function<Integer, Integer> channelFunc)
    {
        return (int) channelFunc.apply(encodedRGB) != channelFunc.apply(sourceRGB);
    }
}
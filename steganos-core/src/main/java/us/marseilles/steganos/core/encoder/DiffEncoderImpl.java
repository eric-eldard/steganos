package us.marseilles.steganos.core.encoder;

import java.awt.image.BufferedImage;
import java.util.BitSet;
import java.util.function.Function;

import us.marseilles.steganos.core.util.Utils;

public class DiffEncoderImpl implements DiffEncoder
{
    private static final int MAX_CONSPICUOUSNESS = 255;

    /**
     * The source image should already have the value of conspicuousness shaved off from this color channel (see
     * {@link #prepSourceImage(BufferedImage, int)}. Add the bit to this, multiplied by the conspicuousness factor
     * (for demo effect, when factor > 1).
     */
    @Override
    public int encodeNextValue(BitSet bitSet, int bitIndex, int sourceRGB, Function<Integer, Integer> channelFunc,
        int conspicuousness)
    {
        boolean moreBits = bitIndex < bitSet.length();
        return channelFunc.apply(sourceRGB) + (moreBits && bitSet.get(bitIndex) ? 1 : 0) * conspicuousness;
    }

    @Override
    public int getMaxConspicuousness()
    {
        return MAX_CONSPICUOUSNESS;
    }

    @Override
    public BufferedImage prepSourceImage(BufferedImage sourceImage, int conspicuousness)
    {
        Utils.validateConspicuousness(conspicuousness, MAX_CONSPICUOUSNESS);

        int cols = sourceImage.getWidth();
        int rows = sourceImage.getHeight();

        BufferedImage preppedImage = new BufferedImage(
            cols,
            rows,
            BufferedImage.TYPE_INT_RGB
        );

        for (int y = 0; y < rows; y++)
        {
            for (int x = 0; x < cols; x++)
            {
                int sourceRGB = sourceImage.getRGB(x, y);

                // using 0 as the floor for this value, to prevent looping back around to the max value
                int newR = Math.max(Utils.R_CHANNEL_FUNCTION.apply(sourceRGB) - conspicuousness, 0);
                int newG = Math.max(Utils.G_CHANNEL_FUNCTION.apply(sourceRGB) - conspicuousness, 0);
                int newB = Math.max(Utils.B_CHANNEL_FUNCTION.apply(sourceRGB) - conspicuousness, 0);

                int newRGB = Utils.makePixelValue(newR, newG, newB);

                preppedImage.setRGB(x, y, newRGB);
            }
        }

        return preppedImage;
    }
}
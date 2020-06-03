package us.marseilles.steganos.core.encoder;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

/**
 * A diff encoder that does not require the source image to be prepped. Instead of needing to pre-carve out part of the
 * color channels to store data, bits are encoded with an up/down method, in which the data value (a bit, multiplied by
 * the conspicuousness factor) is added or subtracted depending on which direction can accommodate the change in value
 * without under/overflow. Max conspicuousness is 127, protecting against overflow. This method requires fewer steps
 * than a diff-with-prep encode, doesn't alter the source image, and should also be harder to guess the encoded message
 * by just examining the bits.
 *
 * Decode with {@link us.marseilles.steganos.core.decoder.DiffDecoder}
 */
public class UpDownDiffEncoder implements Encoder
{
    public static final int MAX_CONSPICUOUSNESS = 127;

    @Override
    public int encodeNextValue(BitSet bitSet, int bitIndex, int sourceRGB, Function<Integer, Integer> channelFunc,
        int conspicuousness)
    {
        boolean moreBits = bitIndex < bitSet.length();
        int sourceValue = channelFunc.apply(sourceRGB);
        int dataValue = moreBits && bitSet.get(bitIndex) ? getDataValue(sourceValue, conspicuousness) : 0;
        return sourceValue + dataValue;
    }

    @Override
    public int getMaxConspicuousness()
    {
        return MAX_CONSPICUOUSNESS;
    }

    /**
     * If adding or subtracting the conspicuousness factor to/from the source value would cause an underflow/overflow,
     * do the operation that doesn't cause that. Otherwise, flip a coin to determine if the factor will be added or
     * subtracted. This randomness makes the message harder to guess w/o the source image.
     */
    private int getDataValue(int sourceValue, int conspicuousness)
    {
        int direction;
        if (sourceValue - conspicuousness < 0)
        {
            direction = 1;
        }
        else if (sourceValue + conspicuousness > 255)
        {
            direction = -1;
        }
        else
        {
            direction = new Random().nextBoolean() ? 1 : -1;
        }
        return conspicuousness * direction;
    }
}
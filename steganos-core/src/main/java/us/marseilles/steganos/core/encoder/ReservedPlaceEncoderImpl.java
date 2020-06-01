package us.marseilles.steganos.core.encoder;

import java.util.BitSet;
import java.util.function.Function;

import us.marseilles.steganos.core.util.Utils;

public class ReservedPlaceEncoderImpl implements ReservedPlaceEncoder
{
    private static final int MAX_CONSPICUOUSNESS = 8;

    /**
     * Set the next bit value in the reserved place on this color channel. The conspicuousness factor determines which
     * bit place is the reserved place. If the color channel's value is 00000000 and the specified conspicuousness is 2,
     * then this method will return 00000010, as an integer.
     */
    @Override
    public int encodeNextValue(BitSet bitSet, int bitIndex, int sourceRGB, Function<Integer, Integer> channelFunc,
        int conspicuousness)
    {
        boolean moreBits = bitIndex < bitSet.length();
        String encodedBit = moreBits && bitSet.get(bitIndex) ? "1" : "0";
        String oldChannelBits = Utils.makeByteString(channelFunc.apply(sourceRGB));

        int replacementIndex = 8 - conspicuousness;
        String newChannelBits = oldChannelBits.substring(0, replacementIndex) +
            encodedBit +
            oldChannelBits.substring(replacementIndex + 1);

        return Integer.parseInt(newChannelBits, 2);
    }

    @Override
    public int getMaxConspicuousness()
    {
        return MAX_CONSPICUOUSNESS;
    }
}
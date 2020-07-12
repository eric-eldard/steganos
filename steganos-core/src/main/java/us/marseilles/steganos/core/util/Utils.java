package us.marseilles.steganos.core.util;

import java.awt.image.BufferedImage;
import java.util.function.Function;

public final class Utils
{
    private static final String ZERO_PADDING = "0000000";

    public static final int NUM_COLOR_CHANNELS = 3;

    public static final Function<Integer, Integer> R_CHANNEL_FUNCTION = rgb -> (rgb >> 16) & 0xff;
    public static final Function<Integer, Integer> G_CHANNEL_FUNCTION = rgb -> (rgb >> 8) & 0xff;
    public static final Function<Integer, Integer> B_CHANNEL_FUNCTION = rgb -> rgb & 0xff;

    /**
     * Pad bit strings with leading 0s so they always have 8 characters
     */
    public static String makeByteString(int oneByteNumber)
    {
        String bitString = Integer.toBinaryString(oneByteNumber);
        if (bitString.length() < 8)
        {
            return ZERO_PADDING.substring(bitString.length() - 1) + bitString;
        }
        else
        {
            return bitString;
        }
    }

    public static int makePixelValue(int red, int green, int blue)
    {
        return (red << 16) | (green << 8) | blue;
    }

    /**
     * Get the max number of characters which could be encoded into the provided image
     */
    public static int getMaxEncodableBytes(BufferedImage image)
    {
        return image.getHeight() * image.getWidth() * NUM_COLOR_CHANNELS / 8;
    }

    /**
     * Generate a string, by repeating the provided string, that will use as many of the specified bytes as possible.
     * Helpful for ensuring an entire image is covered by text when watermarking.
     *
     * Example: bytes=10, stringToRepeat="test ", return val="test test "
     */
    public static String makeLongestEncodableString(int bytes, String stringToRepeat)
    {
        int repeatTimes = bytes / stringToRepeat.getBytes().length;
        return stringToRepeat.repeat(repeatTimes);
    }

    public static void validateConspicuousness(int conspicuousness, int max)
    {
        if (conspicuousness < 1 || conspicuousness > max)
        {
            throw new IllegalArgumentException("conspicuousness value must be between 1 and " + max);
        }
    }

    private Utils()
    {
        // util ctor
    }
}
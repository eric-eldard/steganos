package us.marseilles.steganos.cmd;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;
import us.marseilles.steganos.core.decoder.DiffDecoder;
import us.marseilles.steganos.core.decoder.DiffDecoderImpl;
import us.marseilles.steganos.core.decoder.ReservedPlaceDecoderImpl;
import us.marseilles.steganos.core.encoder.DiffEncoder;
import us.marseilles.steganos.core.encoder.DiffEncoderImpl;
import us.marseilles.steganos.core.encoder.Encoder;
import us.marseilles.steganos.core.encoder.ReservedPlaceEncoder;
import us.marseilles.steganos.core.io.ImageReader;
import us.marseilles.steganos.core.io.ImageWriter;
import us.marseilles.steganos.core.io.LocalImageReader;
import us.marseilles.steganos.core.io.LocalImageWriter;
import us.marseilles.steganos.core.decoder.ReservedPlaceDecoder;
import us.marseilles.steganos.core.encoder.ReservedPlaceEncoderImpl;

/**
 * Example usage of steganos text-to-image encoder
 */
public class CommandLineEncoder
{
    private static ImageReader imageReader = new LocalImageReader();
    private static ImageWriter imageWriter = new LocalImageWriter();
    private static ReservedPlaceEncoder reservedPlaceEncoder = new ReservedPlaceEncoderImpl();
    private static ReservedPlaceDecoder reservedPlaceDecoder = new ReservedPlaceDecoderImpl();
    private static DiffEncoder diffEncoder = new DiffEncoderImpl();
    private static DiffDecoder diffDecoder = new DiffDecoderImpl();

    public static void main(String[] args)
    {
        validateArgs(args);

        Mode mode = Mode.valueOf(args[0].toUpperCase());
        try
        {
            if (mode == Mode.RESERVED_PLACE_ENCODE)
            {
                int conspicuousness = args.length == 4 ? Integer.parseInt(args[3]) : 1;
                saveEncoded(reservedPlaceEncoder, args[1], args[2], conspicuousness);
            }
            else if (mode == Mode.RESERVED_PLACE_DECODE)
            {
                int conspicuousness = args.length == 3 ? Integer.parseInt(args[2]) : 1;
                printReservedPlaceDecoded(args[1], conspicuousness);
            }
            else if (mode == Mode.DIFF_ENCODE)
            {
                int conspicuousness = args.length == 4 ? Integer.parseInt(args[3]) : 1;
                saveEncoded(diffEncoder, args[1], args[2], conspicuousness);
            }
            else if (mode == Mode.DIFF_DECODE)
            {
                printDiffDecoded(args[1], args[2]);
            }
            else if (mode == Mode.DIFF_IMG_PREP)
            {
                int conspicuousness = args.length == 3 ? Integer.parseInt(args[2]) : 1;
                savePreppedImage(args[1], conspicuousness);
            }
        }
        catch (Exception ex)
        {
            System.err.println("Error: " + ex.getMessage());
            System.exit(1);
        }

        System.exit(0);
    }

    private static void saveEncoded(Encoder encoder, String sourceFilePath, String message, int conspicuousness)
        throws IOException
    {
        System.out.println("Encoding message: " + message);

        BufferedImage sourceImage = imageReader.read(sourceFilePath);
        String outputPath = appendToFileName(sourceFilePath, "-encoded");

        BufferedImage encodedImage = encoder.encode(sourceImage, message, conspicuousness);
        imageWriter.write(encodedImage, outputPath);

        System.out.println("Encoding complete.");
        System.out.println("Encoded file location: " + outputPath);
    }

    private static void printReservedPlaceDecoded(String encodedFilePath, int conspicuousness) throws IOException
    {
        BufferedImage encodedImage = imageReader.read(encodedFilePath);
        String decodedMessage = reservedPlaceDecoder.decode(encodedImage, conspicuousness);
        System.out.println("Decoded message:");
        System.out.println(decodedMessage);
    }

    private static void printDiffDecoded(String sourceFilePath, String encodedFilePath) throws IOException
    {
        BufferedImage sourceImage = imageReader.read(sourceFilePath);
        BufferedImage encodedImage = imageReader.read(encodedFilePath);
        String decodedMessage = diffDecoder.decode(sourceImage, encodedImage);
        System.out.println("Decoded message:");
        System.out.println(decodedMessage);
    }

    private static void savePreppedImage(String sourceFilePath, int conspicuousness) throws IOException
    {
        System.out.println("Prepping source file for diff-type encoding");

        BufferedImage sourceImage = imageReader.read(sourceFilePath);
        BufferedImage preppedImage = diffEncoder.prepSourceImage(sourceImage, conspicuousness);

        String outputPath = appendToFileName(sourceFilePath, "-prepped");
        imageWriter.write(preppedImage, outputPath);

        System.out.println("Prepping complete.");
        System.out.println("Prepped file location: " + outputPath);
    }

    private static String appendToFileName(String sourceFilePath, String appendText)
    {
        String baseName = FilenameUtils.getBaseName(sourceFilePath);
        String extension = FilenameUtils.getExtension(sourceFilePath);
        String name = FilenameUtils.getName(sourceFilePath);

        return sourceFilePath.replaceAll(name + "$", baseName + appendText + '.' + extension);
    }

    private static void validateArgs(String[] args)
    {
        // Check any args passed
        if (args.length == 0)
        {
            System.err.println("Please pass a mode: " + Arrays.toString(Mode.values()));
            System.exit(1);
        }

        // Check first arg is a valid mode
        Mode mode = null; //init null because compiler doesn't get program terminates at exit(1), warns un-init later on
        try
        {
            mode = Mode.valueOf(args[0].toUpperCase());
        }
        catch (IllegalArgumentException ex)
        {
            System.err.println("Unrecognized mode: " + args[0]);
            System.err.println("Try: " + Arrays.toString(Mode.values()));
            System.exit(1);
        }

        // Check correct num of args for mode
        if (args.length < mode.minArgs || args.length > mode.maxArgs)
        {
            System.err.println("Wrong number of args. Try: " + mode.sampleUsage);
            System.exit(1);
        }

        // Check img file can be read in from specified location
        try
        {
            imageReader.read(args[1]);
        }
        catch (Exception ex)
        {
            System.err.println("Could not read image file " + args[1]);
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        // DIFF_DECODE is the only mode to take 2 files
        if (mode == Mode.DIFF_DECODE)
        {
            try
            {
                imageReader.read(args[2]);
            }
            catch (Exception ex)
            {
                System.err.println("Could not read encoded image file " + args[2]);
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }

        // We good.
    }

    private enum Mode
    {
        RESERVED_PLACE_ENCODE(3, 4, "reserved_place_encode source.file \"a message\" [2]"),
        RESERVED_PLACE_DECODE(2, 3, "reserved_place_decode encoded.file [2]"),
        DIFF_ENCODE(3, 4, "diff_encode source.file \"a message\" [2]"),
        DIFF_DECODE(3, 3, "diff_decode source.file encoded.file"),
        DIFF_IMG_PREP(2, 3, "diff_img_prep source.file [2]");

        private int minArgs;
        private int maxArgs;
        private String sampleUsage;

        Mode(int minArgs, int maxArgs, String sampleUsage)
        {
            this.minArgs = minArgs;
            this.maxArgs = maxArgs;
            this.sampleUsage = sampleUsage;
        }
    }
}
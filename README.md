# Steganos

## About
Steganos provides tools for simple text-in-image [steganography](https://en.wikipedia.org/wiki/Steganography) by subtly encoding the bits of a message into the color channels of an image. This is useful for watermarking or passing messages in plain sight.

Three encoding methods are provided
- **reserved-place**: one of the places in each color channel byte is reserved for message encoding (the least significant digit, by default); decoding only requires knowing which bit was reserved
- **diff w/ prep**: a portion of each color channel is pre-carved out for message encoding; when the value carved out is greater than 1, a random number is added to represent a bit; decoding is achieved by diffing the source image and the encoded image
- **up/down diff**: adds or subtracts from each color channel, based on which direction can accommodate the change in value; random direction chosen when both up and down can accommodate the change; source image need not be prepped; any difference between source and encoded is counted as a bit 

My hypothesis is that these methods become harder to detect, in this order. Reserved-place only requires that you read each digit place in the color channel bytes until you find a coherent message. Diff w/ prep requires the decoder to have the source image, but examination of the color channels might allow detection of where the prepped image brightness values were capped at. Up/down diff may give away parts of the message, based on color values in the source image, but it's randomness should make it the hardest to detect overall.

All of these methods are less detectable if you fill the rest of the image with padding characters, so the visual pattern left by the message encoding doesn't abruptly stop mid-image.

PNGs are strongly recommended for source files and JPEGs are discouraged. Even at its highest setting, the Java JPEG ImageWriter cannot write losslessly, which corrupts the encoded message. Steganos is also not currently handling transparency.

Steganos does not provide encryption—simply encrypt text before passing to Steganos.

Thanks to https://www.dyclassroom.com/image-processing-project/how-to-get-and-set-pixel-value-in-java for a lesson in java imaging libraries.

### Disclaimer
Steganos is a hobby project and no warranty of any kind is provided.

## Build
`mvn clean package`

## Examples
![steganos output examples](examples.png?raw=true)

# steganos

## About
steganos provides tools for simple text-in-image [steganography](https://en.wikipedia.org/wiki/Steganography) by subtly encoding the bits of a message into the color channels of an image. This is useful for watermarking or passing messages in plain sight.

Two encoding methods are provided
- **reserved-place**: one of the places in each color channel byte is reserved for message encoding (the least significant digit, by default); decoding only requires knowing which bit was reserved
- **diff**: a portion of each color channel is pre-carved out for message encoding; decoding is achieved by diffing the source image and the encoded image

steganos does not provide encryption—simply encrypt text before passing to steganos.

PNGs are strongly recommended for source files and JPEGs are discouraged. Even at its highest setting, the Java JPEG ImageWriter cannot write losslessly, which corrupts the encoded message. steganos is also not currently handling transparency.

Thanks to https://www.dyclassroom.com/image-processing-project/how-to-get-and-set-pixel-value-in-java for a lesson in java imaging libraries.

### Disclaimer
steganos is a hobby project and no warranty of any kind is provided.

## Build
`mvn clean package`

## Examples
![steganos output examples](examples.png?raw=true)
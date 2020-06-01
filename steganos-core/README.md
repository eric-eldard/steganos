# steganos

## About
steganos provides tools for simple text-in-image [steganography](https://en.wikipedia.org/wiki/Steganography).

steganos does not provide encryption—simply encrypt text before passing to steganos.

PNGs are strongly recommended for source files and JPEGs are discouraged. Even at its highest setting, the Java
JPEG ImageWriter cannot write losslessly, which corrupts the encoded message.

Thanks to https://www.dyclassroom.com/image-processing-project/how-to-get-and-set-pixel-value-in-java for a lesson in java image libraries.

## Build
`mvn clean package`

## Examples
![steganos output examples](examples.png?raw=true)
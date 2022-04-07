SMOOTH NOISE

Language: Java

STATUS: Complete and Runnable

Last Updated: 2022-4-7

SmoothNoise.java is a program that, given a set of command line arguments, produces a corresponding visual of noise that has a smooth characteristic, similar to Perlin Noise. Though, this method does not
incorporate the Perlin Noise algorithm, but uses a simpler calculation that I developed myself (though I doubt I'm the first person to come up with this algorithm).
In particular, it generates a random brightness weight of every pixel using its adjacent pixels as a reference point.

The program can be run by first compiling SmoothNoise.java with the following terminal command: javac SmoothNoise.java
Afterwards, the program can be run with the following command: java SmoothNoise WIDTH HEIGHT CONTRAST SCALE

Command line arguments:
WIDTH (positive integer) - sets the width of the window that will be displayed
HEIGHT (positive integer) - sets the height of the window that will be displayed
CONTRAST (float between 0.0 and 1.0) - determines the contrast between the pixel brightness, must be a value between 0.0 and 1.0 (0.5 produces the best result)
SCALE (positive integer) - determines the size of the pixels of the display (ex. SCALE = 10 means every display pixel will be 10x10 monitor pixels)
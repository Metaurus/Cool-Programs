import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SmoothNoise {

    static int WIDTH, HEIGHT, SCALE;
    static float SHARPNESS;
    static Random rand = new Random();

    //Command line arguments are as follows in the correct order:
    //WIDTH (int), HEIGHT (int), CONTRAST (float 0.0 - 1.0), SCALE (int) 
    public static void main(String[] args) {

        //Instance the main class and initialize all command line arguments
        SmoothNoise noise = new SmoothNoise();

        //Exception handling with the arguments
        try {
            WIDTH = Integer.parseInt(args[0]);
            HEIGHT = Integer.parseInt(args[1]);
            SHARPNESS = Float.parseFloat(args[2]);
            SCALE = Integer.parseInt(args[3]);
        } catch (Exception e) {
            String errorMessage = "The arguments must be as follows in the corresponding order: "
            + "\nWIDTH : positive integer"
            + "\nHEIGHT : positive integer"
            + "\nCONTRAST : float between 0.0 and 1.0"
            + "\nSCALE : positive integer";
            noise.displayErrorMessage(null, errorMessage);
        }
        if(WIDTH <= 0 || HEIGHT <= 0 || SHARPNESS < 0 || SHARPNESS > 1 || SCALE <= 0) {
            String errorMessage = "The arguments must be as follows in the corresponding order: "
            + "\nWIDTH : positive integer"
            + "\nHEIGHT : positive integer"
            + "\nCONTRAST : float between 0.0 and 1.0"
            + "\nSCALE : positive integer";
            noise.displayErrorMessage(null, errorMessage);
        }
        
        float[][] pixels = noise.generateSmoothNoise();

        //Create our frame and custom panel that we draw to
        JFrame frame = new JFrame();
        DrawingPanel panel = new DrawingPanel(pixels);
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setTitle("Smooth Noise");
        frame.setBackground(Color.white);
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setBackground(Color.white);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    //Function that returns a 2 dimension float array of pixel weights
    private float[][] generateSmoothNoise() {

        //Initialize our weights array based on width, height, and scale
        float[][] weights = new float[WIDTH / SCALE][HEIGHT / SCALE];
        
        //For every pixel on the screen
        for(int x = 0; x < weights.length; x++) {
            for(int y = 0; y < weights[x].length; y++) {

                //Create a float 'average' that will take the mean of surrounding pixels
                //This is used as a reference point for generating a new weight for the current pixel to ensure a smooth transition
                float average;

                //If this is the first pixel [0, 0], generate a new weight value
                //I found it to work better using the formula below to intiate the noise
                if(x == 0 && y == 0) weights[x][y] = (float) (rand.nextInt((int) (5 + (SHARPNESS * 10) - (5 - (SHARPNESS * 10)))) + (5 - SHARPNESS * 10)) / 10;

                //If the pixel is in the first column of the screen, only use the value above it (the only value available) as an average
                else if(x == 0 && y > 0) {
                    average = weights[x][y-1];
                    weights[x][y] = generateWeight(average);
                }

                //If the pixel is in the first row of the screen, use the values to its left and bottom-left corner as an average 
                else if(x > 0 && y == 0) {
                    average = (weights[x-1][y] + weights[x-1][y+1]) / 2;
                    weights[x][y] = generateWeight(average);
                }

                //Otherwise, use all three adjacent pixels as the average for its weight generation
                else {
                    average = (weights[x-1][y-1] + weights[x][y-1] + weights[x-1][y]) / 3;
                    weights[x][y] = generateWeight(average);
                }
            }
        }

        //Return the array of weights
        return weights;
    }

    //Generates a float between (lowerbounds = 0.0, upperbounds = 1.0) given a weight as a reference point for the generation
    private float generateWeight(float average) {

        //The average had a tendency to go to 0
        //Therefore, to ensure that the result isn't a black screen most of the time,
        //bound it to being the sharpness level
        if(average < SHARPNESS) average = SHARPNESS;

        //Scaled average converts our average float to be a number between 0 and 100
        //This is done because the RNG in Java is dumb and working with float generation is annoying
        //Example: 0.72 would scale to be 72
        int scaledAverage = (int) (average * 100);

        //Set the lowerbound for RNG
        //Use the sharpness as a percent for scaling down the average
        int lowerBound = (int) (scaledAverage - (scaledAverage * SHARPNESS));

        //Set the upperbound for RNG
        //Use the sharpness as a percent for scaling up the average
        int upperBound = (int) (scaledAverage + (scaledAverage * SHARPNESS));

        //If the upperbound ends up being higher than 1, clamp it back down to 1
        if(upperBound < 1) upperBound = 1;

        //Generate a new random int between using the bounds above
        int generatedInt = rand.nextInt(upperBound - lowerBound) + lowerBound;

        //Ensure the generated int is somewhere between 0 and 100
        generatedInt = clamp(generatedInt, 0, 100);

        //Convert the generated int back into a float between 0.0 and 1.0
        float convertedFloat = (float) generatedInt / 100;

        //Clamp the converted float between 0.0 and 1.0
        convertedFloat = clamp(convertedFloat, 0, 1);

        //Return the converted float
        return convertedFloat;
    }

    //Clamps an int between two bounds
    private int clamp(int num, int lowerBounds, int upperBounds) {
        if(num < lowerBounds) num = lowerBounds;
        if(num > upperBounds) num = upperBounds;
        return num;
    }

    //Clamps a float between two bounds
    private float clamp(float num, float lowerBounds, float upperBounds) {
        if(num < lowerBounds) num = lowerBounds;
        if(num > upperBounds) num = upperBounds;
        return num;
    }

    //Displays an error message in a dialog box
    void displayErrorMessage(JFrame frame, String error) {
        JOptionPane.showMessageDialog(frame, error, "Incorrect Arguments!", JOptionPane.INFORMATION_MESSAGE);
        System.exit(-1);
    }

    //Custom class for drawing the array of weighted pixels to a JPanel
    static class DrawingPanel extends JPanel {
        float[][] pixels;

        DrawingPanel(float[][] pixels) {
            this.pixels = pixels;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for(int x = 0; x < pixels.length; x++) {
                for(int y = 0; y < pixels[x].length; y++) {
                    int colorValue = (int) (pixels[x][y] * 255);
                    g.setColor(new Color(colorValue, colorValue, colorValue));
                    g.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
                }
            }
        }
    }
}

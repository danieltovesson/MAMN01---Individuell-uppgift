package com.example.danieltovesson.hellosensor.helpers;

/**
 * Created by danieltovesson on 2018-03-22.
 */

public class Functions {

    // Variables
    static final float ALPHA = 0.25f;

    /**
     * Uses a low pass filter on the input
     *
     * @param input  the input
     * @param output the output
     * @return the low pass filtered output
     */
    public static float[] lowPassFilter(float[] input, float[] output) {
        if (output == null) {
            return input;
        }
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }
}

package com.example.danieltovesson.hellosensor.helpers;

import java.math.BigDecimal;

/**
 * Created by danieltovesson on 2018-03-22.
 */

public class Functions {

    // Variables
    static final float ALPHA = 0.07f;

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
            output[i] = round(output[i] + ALPHA * (input[i] - output[i]), 2);
        }
        return output;
    }

    /**
     * Rounds a float value
     *
     * @param d            the float value
     * @param decimalPlace the number of decimals
     * @return the rounded float value
     */
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}

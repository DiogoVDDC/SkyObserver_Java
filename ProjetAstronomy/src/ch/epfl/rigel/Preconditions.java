package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */

public final class Preconditions {

    /**
     * Empty constructor to prevent instantiation of the Precondition class.
     */
    private Preconditions () {}

    /**
     * Allows to throw an exception if a given argument is wrong.
     * @param isTrue: the result of the given argument, if false, throw an exception.
     */
    public static void checkArgument(boolean isTrue) {
        if(!isTrue) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Allows to throw an exception if a given value isn't contained in a given interval.
     * @param interval: Interval to compare if the value is contained in.
     * @param value: the value that is to checked if inside the interval.
     */
    public static double checkInInterval(Interval interval, double value) {
        if(!interval.contains(value)) {
            throw new IllegalArgumentException();
        }

        return value;
    }
}

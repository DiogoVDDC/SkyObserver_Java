package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 * @author Theo Houle (312432)
 *
 */

public final class Preconditions {

    /**
     * Private constructor to prevent instantiation of the Precondition class.
     */
    private Preconditions () {}
    
    /**
     * Allows to throw an exception if a given argument is wrong.
     * @param isTrue (Boolean): the result of the given argument, if false, throw an exception.
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
     * @throws: if the value isn't contained in the interval, throws IllegalArgumentException.
     * @return: returns the value that is checked
     */
    public static double checkInInterval(Interval interval, double value) {
        if(!interval.contains(value)) {
            throw new IllegalArgumentException();
        }
        
        return value;
   }
}

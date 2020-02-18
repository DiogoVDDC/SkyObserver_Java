package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */

public final class Preconditions {
    
    /**
     * Empty constructor to prevent from instantiating a Precondition class.
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
     * @param interval(Interval): Interval to compare if the value is contained in.
     * @param value(Double): the value we want to know if 
     */
    public static void checkInInterval(Interval interval, double value) {
        if(interval.contains(value)) {
            throw new IllegalArgumentException();
        }
   }
}

package ch.epfl.rigel.math;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;

/**
 * Class to create an right-opened interval.
 * @author Theo Houle (312432)
 *
 */
public final class RightOpenInterval extends Interval {

    /**
     * Constructor of the RightOpenedInterval used only by the two auxiliary constrcuctor.
     * @param low: lower boundary of the interval.
     * @param high: upped boundaty of the interval.
     */
    private RightOpenInterval(double low, double high) {
        super(low, high);
    }
    
    /**
     * Auxiliary constructor to create a right opened interval between to given boundaries.
     * @param low: lower boundary.
     * @param high: upper boundary.
     * @return: the newly created interval.
     */
    public static RightOpenInterval of(double low, double high) {
        Preconditions.checkArgument(low < high);
        return new RightOpenInterval(low, high);
    }
    
    /**
     * Auxiliary constructor to create a righ opened interval centralized at 0 and symmetric with a given radius.
     * @param size: the radius of the interval.
     * @return: the newly created interval.
     */
    public static RightOpenInterval symmetric(double size) {
        Preconditions.checkArgument(size > 0 );
        return new RightOpenInterval(-size/2, size/2);
    }
    
    /**
     * Method to normalize the given number between the boundaries of the interval.
     * @param v: the number to normalize.
     * @return: the normalized number.
     */
    public double reduce(double v) {
        return low() + floorMod(v-low(), size());
    }
    
    private double floorMod(double x, double y) {
        return x - y*Math.floor(x/y);
    }

    @Override
    public boolean contains(double v) {
        return ((v >= low()) && (v < high()));
    }
    
    @Override
    // outputs to console the interval in the correct format
    public String toString() {
        return String.format(Locale.ROOT,"[%s,%s[", low(), high());
    }
}

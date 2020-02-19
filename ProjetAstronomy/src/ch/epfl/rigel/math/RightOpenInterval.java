package ch.epfl.rigel.math;

import java.util.Locale;

/**
 * Class to create an right-opened interval.
 * @author Theo Houle (312432)
 *
 */
public final class RightOpenInterval extends Interval {

    /**
     * Constructor of the RightOpenedInterval used only by the two auxiliary constrcuctor.
     * @param low(Double): lower boundary of the interval.
     * @param high(Double): upped boundaty of the interval.
     */
    private RightOpenInterval(double low, double high) {
        super(low, high);
    }
    
    /**
     * Auxiliary constructor to create a right opened interval between to given boundaries.
     * @param low(Double): lower boundary.
     * @param high(Double): upper boundary.
     * @return(Double): the newly created interval.
     */
    public static RightOpenInterval of(double low, double high) {
        Preconditions.checkArgument(low < high);
        return new RightOpenInterval(low, high);
    }
    
    /**
     * Auxiliary constructor to create a righ opened interval centralized at 0 and symmetric with a given radius.
     * @param size(Double): the radius of the interval.
     * @return(RightOpenInterval): the newly created interval.
     */
    public static RightOpenInterval symmetric(double size) {
        Preconditions.checkArgument(size > 0 );
        return new RightOpenInterval(-size, size);
    }
    
    /**
     * Method to normalize the given number bewteen the boundaries of the interval.
     * @param v(Double): the number to normalize.
     * @return(Double): the normalized number.
     */
    public double reduce(double v) {
        return low() + Math.floorMod((int)(v - low()), (int)size());
    }

    @Override
    public boolean contains(double v) {
        return ((v >= low()) || (v < high()));
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT,"[%s,%s[", low(), high());
    }
}

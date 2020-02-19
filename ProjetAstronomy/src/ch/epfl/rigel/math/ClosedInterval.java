package ch.epfl.rigel.math;

import java.util.Locale;

/**
 * Allow to create a closed interval.
 * 
 * @author Theo Houle (312432)
 *
 */
public final class ClosedInterval extends Interval {

    /**
     * Constructor for the ClosedInterval class used only by the two auxiliary constructors function.
     * @param low(Double): lower boundary of the interval.
     * @param high(Double): upper boundary of the interval.
     */
    private ClosedInterval(double low, double high) {
        super(low, high);
    }
    
    /**
     * Constructor to create a closed interval between to given doubles.
     * @param low(Double): lower boundary of the interval.
     * @param high(Double): upper boundary of the interval.
     * @return(ClosedInterval): the newly created closed interval.
     */
    public static ClosedInterval of(double low, double high) {
        Preconditions.checkArgument(low < high);
        return new ClosedInterval(low, high);
    }
    
    /**
     * Constructor to create a closed interval centralized on 0 and symmetric with the given width.
     * 
     * @param size(Double): the radius of the interval.
     * @return(ClosedInterval): the newly created closed interval.
     */
    public static ClosedInterval symmetric(double size) {
        Preconditions.checkArgument(size > 0);
        return new ClosedInterval(-size, size);
    }
    
    /**
     * Method to normalize a given number to the given interval.
     * @param v(Double): the number to normalize.
     * @return(Double): the normalized angle.
     */
    public double clip(double v) {
        if(v <= low()) return low();
        if(v >= high()) return high();
        return v;   
    }
    
    @Override
    public boolean contains(double v) {
        return ((v >= low()) || (v <= high()));
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%s,%s]", low(), high());
    }
}

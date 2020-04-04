package ch.epfl.rigel.math;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;

/**
 * Allow to create a closed interval.
 * 
 * @author Theo Houle (312432)
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 *
 */
public final class ClosedInterval extends Interval {

    /**
     * Constructor for the ClosedInterval class used only by the two auxiliary constructors function.
     * @param low: lower boundary of the interval.
     * @param high: upper boundary of the interval.
     */
    private ClosedInterval(double low, double high) {
        super(low, high);
    }
    
    /**
     * Constructor to create a closed interval between to given doubles.
     * @param low: lower boundary of the interval.
     * @param high: upper boundary of the interval.
     * @return: the newly created closed interval.
     */
    public static ClosedInterval of(double low, double high) {
        Preconditions.checkArgument(low < high);
        return new ClosedInterval(low, high);
    }
    
    /**
     * Constructor to create a closed interval centred on 0 and symmetric with the given width.
     * 
     * @param size: the size of the interval.
     * @return: the newly created closed interval.
     */
    public static ClosedInterval symmetric(double size) {
        Preconditions.checkArgument(size > 0);
        return new ClosedInterval(-size/2, size/2);
    }
    
    /**
     * Method to normalise a given number to the given interval.
     * @param v: the number to normalise.
     * @return: the normalised angle.
     */
    public double clip(double v) {
        if(v <= low()) return low();
        if(v >= high()) return high();
        return v;   
    }
    
    @Override
    /**   
     * @param v: the value to be checked for existence
     * @return: whether or not a certain value is contained in the interval
     */
    public boolean contains(double v) {
        return ((v >= low()) && (v <= high()));
    }

    @Override
    // Outputs to console the interval in the correct format
    public String toString() {
        return String.format(Locale.ROOT, "[%s,%s]", low(), high());
    }
}

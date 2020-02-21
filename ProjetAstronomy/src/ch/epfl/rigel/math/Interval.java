package ch.epfl.rigel.math;

/**
 * Representation of a Intervals
 * 
 * @author Theo Houle (312432)
 *
 */

abstract public class Interval {
    private final double low;
    private final double high;
    
    /**
     * @param: the lower bound of the interval.
     * @param: the upper bound of the interval.
     */
    protected Interval(double low, double high) {
        this.low = low;
        this.high = high;
    }
    
    /**
     * @return: the lower bound of the interval.
     */
    public double low() {
        return low;
    }
    
    /**
     * @return: the upper bound of the interval.
     */
    public double high() {
        return high;
    }
    
    /**
     * Allows to create a polynomial in decreasing power.    
     * @return: the size of the interval.
     */
    public double size() {
        return high - low;
    }

    /**
     * @param: value to verify existence in interval   
     * @return: the existence of a certain value
     */
    abstract public boolean contains(double v);
    
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }
}

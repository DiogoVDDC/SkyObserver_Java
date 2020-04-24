package ch.epfl.rigel.math;

/**
 * Representation of a Intervals
 * 
 * @author Theo Houle (312432)
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 *
 */

abstract public class Interval {
    private final double low;
    private final double high;

    /**
     * Constructor for Interval.
     * @param: the lower bound of the interval.
     * @param: the upper bound of the interval.
     */
    protected Interval(double low, double high) {
        this.low = low;
        this.high = high;
    }

    /**
     * Getter for the lower bound of the interval.
     * @return: the lower bound of the interval.
     */
    public double low() {
        return low;
    }

    /**
     * Getter for the upper bound of the interval.
     * @return: the upper bound of the interval.
     */
    public double high() {
        return high;
    }

    /**
     * Allows to know the size of the interval. 
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

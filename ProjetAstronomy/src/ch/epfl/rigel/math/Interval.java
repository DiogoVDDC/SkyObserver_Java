package ch.epfl.rigel.math;

abstract public class Interval {
    private final double low;
    private final double high;
    
    protected Interval(double low, double high) {
        this.low = low;
        this.high = high;
    }
    
    public double low() {
        return low;
    }
    public double high() {
        return high;
    }
    public double size() {
        return high - low;
    }

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

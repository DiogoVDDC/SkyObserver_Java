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
}
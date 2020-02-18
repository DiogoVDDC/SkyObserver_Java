package ch.epfl.rigel.math;

import java.util.Locale;

public final class RightOpenInterval extends Interval {

    private RightOpenInterval(double low, double high) {
        super(low, high);
    }
    
    public static RightOpenInterval of(double low, double high) {
        if (low < high) {
            return new RightOpenInterval(low, high);
        }
        throw new IllegalArgumentException();
    }
    
    public static RightOpenInterval symmetric(double size) {
        if (size > 0) {
            return new RightOpenInterval(-size, size);
        }
        throw new IllegalArgumentException();
    }
    
    public double reduce(double v) {
        return low() + Math.floorMod((int)(v - low()), (int)size());
    }

    @Override
    boolean contains(double v) {
        return ((v >= low()) || (v < high()));
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT,"[%s,%s[", low(), high());
    }
}

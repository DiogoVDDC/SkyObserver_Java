package ch.epfl.rigel.math;

import java.util.Locale;

public final class ClosedInterval extends Interval {

    private ClosedInterval(double low, double high) {
        super(low, high);
    }
    
    public static ClosedInterval of(double low, double high) {
        Preconditions.checkArgument(low < high);
        return new ClosedInterval(low, high);
    }
    
    public static ClosedInterval symmetric(double size) {
        Preconditions.checkArgument(size > 0);
        return new ClosedInterval(-size, size);
    }

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

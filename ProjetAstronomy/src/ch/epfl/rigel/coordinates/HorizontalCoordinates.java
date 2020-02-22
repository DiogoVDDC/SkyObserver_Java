package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

public final class HorizontalCoordinates extends SphericalCoordinates{

    private static final RightOpenInterval azInterval = RightOpenInterval.of(0, 360);
    private static final ClosedInterval altInterval = ClosedInterval.symmetric(180);

    private HorizontalCoordinates(double alt, double az) {
        super(alt, az);
    }

    public static HorizontalCoordinates of(double alt, double az) {    
        return new HorizontalCoordinates(Preconditions.checkInInterval(altInterval, alt), Preconditions.checkInInterval(azInterval, az));
    }

    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        return new HorizontalCoordinates(Angle.ofDeg(Preconditions.checkInInterval(altInterval, altDeg)), Angle.ofDeg(Preconditions.checkInInterval(azInterval, azDeg)));
    }

    public double az() {
        return lon();
    }

    public double azDeg() {
        return lonDeg();
    }

    public String azOctantName()
}

    package ch.epfl.rigel.coordinates;

import java.util.Locale;
import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Representation of coordinates in ecliptic system.
 * @author Theo Houle (312432)
 */

public final class EclipticCoordinates extends SphericalCoordinates{

    // latitude interval in terms of radians, -pi/2 to pi/2 radians
    private final static ClosedInterval latInterval = ClosedInterval.symmetric(Math.PI);
    // longitude interval in terms of radians, 0 to 2pi radians
    private final static RightOpenInterval lonInterval = RightOpenInterval.of(0, 2*Math.PI);
    
    /**
     * Private constructor used only by the auxiliary constructor.
     * @param lat: the latitude of the coordinate in radiant.
     * @param lon: the latitude of the coordinate in radiant.
     */
    private EclipticCoordinates(double lat, double lon) {
        super(lat, lon);
    }

    /**
     * Auxiliary constructor to create an elliptic coordinate.
     * @param lon: the longitude in radiant of the coordinate.
     * @param lat: the latitude in radiant of the coordinate
     * @return: the newly created elliptic coordinates.
     * @throws: If the angles aren't in the correct interval ([-90, 90] for lat and [0, 360[ for lon), throws an IllegalArgumentExcpetion.
     */
    public static EclipticCoordinates of(double lon, double lat) {
        return new EclipticCoordinates(Preconditions.checkInInterval(latInterval, lat), Preconditions.checkInInterval(lonInterval, lon));
    }
    
    /**
     * Getter for the longitude in radian.
     * @return: the longitude in radiant.
     */
    public double lon() {
        return super.lon();
    }
    
    /**
     * Getter for the longitude converted in degrees.
     * @return: the longitude in degrees.
     */
    public double lonDeg() {
        return super.lonDeg();
    }
    
    /**
     * Getter for the latitude in radian.
     * @return: returns the latitude radianst.
     */
    public double lat() {
        return super.lat();
    }
    
    /**
     * Getter for the latitude converted in degrees.
     * @return: the latitude in degrees.
     */
    public double latDeg() {
        return super.latDeg();
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(\u03bb=%.4f°, \u03b2=%.4f°)", lonDeg(), latDeg());
    }
}

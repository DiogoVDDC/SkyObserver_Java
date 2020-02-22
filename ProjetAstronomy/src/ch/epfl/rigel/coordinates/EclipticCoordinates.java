package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

public final class EclipticCoordinates extends SphericalCoordinates {
      
    // CHECK THE INTERVALS NOT CLEAR
    // latitude interval in terms of radians, -pi to pi radians
    private static final ClosedInterval latInterval = ClosedInterval.symmetric(Math.PI);
    // longitude interval in terms of radians, 0 to 2pi radians
    private static final RightOpenInterval lonInterval = RightOpenInterval.of(0, 2*Math.PI);
    
    private EclipticCoordinates(double lat, double lon) {
        super(lat, lon);
    }
    
    /**
     * @param lon: longitude in radians 
     * @param lat: latitude in radians
     * @return: returns new ecliptic coordinates object
     */
    public static EclipticCoordinates of(double lon, double lat) {
        return new EclipticCoordinates(Preconditions.checkInInterval(latInterval, lat),
                Preconditions.checkInInterval(lonInterval, lon));
    }

    /**     
     * @return: returns longitude in radians
     */
    public double lon() {
        return super.lon();
    }
    
    /**     
     * @return: returns longitude in degrees
     */
    public double lonDeg() {
        return super.lonDeg();
    }
    
    /**     
     * @return: returns latitude in radians
     */
    public double lat() {
        return super.lat();
    }
    
    /**     
     * @return: returns latitude in degree
     */
    public double latDeg() {
        return super.latDeg();
    }
}

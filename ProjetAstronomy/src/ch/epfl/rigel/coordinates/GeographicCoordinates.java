package ch.epfl.rigel.coordinates;

import java.util.Locale;

import org.junit.jupiter.api.Order;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public final class GeographicCoordinates extends SphericalCoordinates{

    // longitude interval in terms of radians, 180 to -180 degrees
    private static final RightOpenInterval lonInterval = RightOpenInterval.symmetric(360);
    // latitude interval in terms of radians, 90 to -90 degrees
    private static final ClosedInterval latInterval = ClosedInterval.symmetric(180);
  
    private GeographicCoordinates(double lat, double lon) {
        super(lat, lon);        
    }

    
    /**     
     * @param: longitude in degrees 
     * @param: latitude in degrees 
     * @return: returns a new geographic coordinates object
     */
     public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        return new GeographicCoordinates( Angle.ofDeg(Preconditions.checkInInterval(latInterval, latDeg)),
                Angle.ofDeg(Preconditions.checkInInterval(lonInterval, lonDeg)));
    }

    /**     
     * @param: longitude in degrees 
     * @return: returns true is the longitude is in the correct interval
     */
    public static boolean isValidLonDeg(double lonDeg) {
        Preconditions.checkInInterval(lonInterval, lonDeg);
        return true;
    }

    /**     
     * @param: latitude in degrees 
     * @return: returns true is the latitude is in the correct interval
     */
    public static boolean isValidLatDeg(double latDeg) {
        Preconditions.checkInInterval(lonInterval, latDeg);
        return true;
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
     * @return: returns latitude in degrees
     */
    public double latDeg() {
        return super.latDeg();
    }
   
    @Override
    public String toString(){
        return String.format(Locale.ROOT,"(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg());
    }
}

package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

public final class GeographicCoordinates extends SphericalCoordinates{

    // longitude interval in terms of radiant, 180 to -180 degrees //degrees or radiant??
    private static final RightOpenInterval lonInterval = RightOpenInterval.symmetric(360);
    // latitude interval in terms of radiant, 90 to -90 degrees  //degrees or radiant??
    private static final ClosedInterval latInterval = ClosedInterval.symmetric(180);
  
    private GeographicCoordinates(double lat, double lon) {
        super(lat, lon);        
    }

    //CHECK IF HAS TO BE PUBLIC
    /**     
     * @param: longitude in degrees 
     * @param: latitude in degrees 
     * @return: returns a new geographic coordinates object
     */
     public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) { //Maybe use the isValidLonDeg et isValidLatDeg instead of preconditions?
        return new GeographicCoordinates( Angle.ofDeg(Preconditions.checkInInterval(latInterval, latDeg)),
                Angle.ofDeg(Preconditions.checkInInterval(lonInterval, lonDeg)));
    }

    /**     
     * @param: longitude in degrees 
     * @return: returns true is the longitude is in the correct interval
     */
    public static boolean isValidLonDeg(double lonDeg) {
        try {
            Preconditions.checkInInterval(lonInterval, lonDeg);
        } 
        catch(IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**     
     * @param: latitude in degrees 
     * @return: returns true is the latitude is in the correct interval
     */
    public static boolean isValidLatDeg(double latDeg) {
        try {
            Preconditions.checkInInterval(latInterval, latDeg);
        }
        catch(IllegalArgumentException e) {
            return false;
        }
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
        return String.format(Locale.ROOT,"(lon=%.4f °, lat=%.4f°)", lonDeg(), latDeg());
    }
}

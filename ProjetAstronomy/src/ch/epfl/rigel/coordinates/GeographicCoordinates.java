package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Representation of GeographicCoordinates.
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 * @author Theo Houle (312432)
 *
 */
public final class GeographicCoordinates extends SphericalCoordinates{

    // longitude interval in terms of degrees, 180 to -180 degrees.
    private static final RightOpenInterval lonInterval = RightOpenInterval.symmetric(360);
    // latitude interval in terms of degrees, 90 to -90 degrees.
    private static final ClosedInterval latInterval = ClosedInterval.symmetric(180);
  
    /**
     * Constructor for GeographicCoordinates.
     * @param lat: lattitude of the position.
     * @param lon: longitude of the position.
     */
    private GeographicCoordinates(double lat, double lon) {
        super(lat, lon);        
    }

    
    /**     
     * Allows to create geographic coordinates using degrees.
     * @param: longitude in degrees.
     * @param: latitude in degrees..
     * @return: returns a new geographic coordinates object.
     */
     public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        return new GeographicCoordinates( Angle.ofDeg(Preconditions.checkInInterval(latInterval, latDeg)),
                Angle.ofDeg(Preconditions.checkInInterval(lonInterval, lonDeg)));
    }

   
    /**   
     * Allows to know if the longitude is in the right interval.  
     * @param: longitude in degrees.
     * @return: returns true is the longitude is in the correct interval.
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
     * Allows to know if the lattitude is in the right interval.
     * @param: latitude in degrees.
     * @return: returns true is the latitude is in the correct interval.
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
     * Getter for the longitude in radiant.
     * @return: returns longitude in radiant.
     */
    public double lon() {
        return super.lon();
    }

    /**     
     * Getter got the Longitude in degrees.
     * @return: returns longitude in degrees
     */
    public double lonDeg() {
        return super.lonDeg();
    }

    /**     
     * Getter for the latitude in radiant.
     * @return: returns latitude in radiant.
     */
    public double lat() {
        return super.lat();
    }

    /**
     * Getter for the latitude in degrees.     
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

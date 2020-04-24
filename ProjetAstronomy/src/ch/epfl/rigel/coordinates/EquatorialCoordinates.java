package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Representation of EquatorialCoordinates.
 * @author Diogo Valdivieso Damasio Da Costa (311673) 
 * @author Theo Houle (312432)
 */
public final class EquatorialCoordinates extends SphericalCoordinates{

    //longitude interval expressed in terms of radians, 0 to 2*pi
    private static final RightOpenInterval lonInterval = RightOpenInterval.of(0, Angle.TAU);
    //latitude interval expressed in terms of radians, -pi/2 to pi/2 degrees
    private static final ClosedInterval latInterval = ClosedInterval.symmetric(Math.PI);

    /**
     * Constructor for EquatorialCoordinates.
     * @param lat: latitude of the position
     * @param lon: longitude of the position.
     */
    private EquatorialCoordinates(double lat, double lon) {
        super(lat, lon);        
    }
    
    /** 
     * Allows to create equatorial coordinates from right ascension and declination
     * @param ra: (right ascension) correspond to longitude (given in radians) 
     * @param dec: (declination) correspond to latitude (given in radians)
     * @return: returns new equatorial coordinates
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        return new EquatorialCoordinates(Preconditions.checkInInterval(latInterval, dec), Preconditions.checkInInterval(lonInterval, ra));
    } 
    
    /**    
     * Getter for right ascension (equivalent to longitude)
     * @return:returns the right ascension in radians
     */
    public double ra() {
        return super.lon();
    }

    /**     
     * Getter for right ascension (equivalent to longitude)
     * @return: returns the right ascension in degrees
     */
    public double raDeg() {
        return super.lonDeg();
    }
    
    /**     
     * Getter for right ascension (equivalent to longitude)
     * @return: returns the right ascension (equivalent to longitude) in hours
     */
    public double raHr() {
        return Angle.toHr(super.lon());
    }

    /**     
     * Getter for declination (equivalent to latitude) 
     * @return: the declination in radians
     */
    public double dec() {
        return super.lat();
    }

    /**     
     * Getter for declination (equivalent to latitude) 
     * @return: the declination in degrees
     */
    public double decDeg() {
        return super.latDeg();
    }

    @Override
    public String toString(){
        return String.format(Locale.ROOT,"(ra=%.4fh, dec=%.4f°)", Angle.toHr(lon()), latDeg());
    }

}

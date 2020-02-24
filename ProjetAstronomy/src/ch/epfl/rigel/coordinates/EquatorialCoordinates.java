package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

public final class EquatorialCoordinates extends SphericalCoordinates{

    //longitude interval expressed in terms of radiant, 0 to two pi.
    private static final RightOpenInterval lonInterval = RightOpenInterval.of(0, Math.PI*2);
    //latitude interval expressed in terms of radiant, -pi/2 to pi/2 degrees
    private static final ClosedInterval latInterval = ClosedInterval.symmetric(Math.PI);


    private EquatorialCoordinates(double lat, double lon) {
        super(lat, lon);        
    }
    
    /** 
     * @param ra: (right ascension) correspond to longitude (given in radians)
     * @param dec: (declination) correspond to latitude (given in radians)
     * @return: returns new equatorial coordinates
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        return new EquatorialCoordinates(Preconditions.checkInInterval(latInterval, dec),
                Preconditions.checkInInterval(lonInterval, ra));
    } 
    
    /**     
     * @return: returns the right ascension (equivalent to longitude) in radians
     */
    public double ra() {
        return super.lon();
    }

    /**     
     * @return: returns the right ascension (equivalent to longitude) in degrees
     */
    public double raDeg() {
        return super.lonDeg();
    }
    
    /**     
     * @return: returns the right ascension (equivalent to longitude) in hours
     */
    public double raHr() {
        return Angle.toHr(super.lon());
    }

    /**     
     * @return: the declination (equivalent to latitude) in radians
     */
    public double dec() {
        return super.lat();
    }

    /**     
     * @return: the declination (equivalent to latitude) in degrees
     */
    public double decDeg() {
        return super.latDeg();
    }

    // HOW DO I ADD THE DEGREE SYMBOL
    @Override
    public String toString(){
        return String.format(Locale.ROOT,"(ra=%.4fh, lat=%.4f)", lonDeg(), latDeg());
    }

}

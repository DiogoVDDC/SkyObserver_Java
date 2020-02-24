package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Representation of coordinates in horizontal system.
 * @author Theo Houle (312432)
 *
 */
public final class HorizontalCoordinates extends SphericalCoordinates{

    // Interval in which the azimuth angle must be contained.
    private static final RightOpenInterval azInterval = RightOpenInterval.of(0, Math.PI*2);
    // Interval in which the altitude angle must be contained.
    private static final ClosedInterval altInterval = ClosedInterval.symmetric(Math.PI);
    
    private HorizontalCoordinates(double alt, double az) {
        super(alt, az);
    }
    
    
    /**
     * Auxiliary constructor to create an horizontal coordatine in radians.
     * @param az: the azimuth angle in radians.
     * @param alt: the altitude angle in radians.
     * @return: the newly created horizontal coordinates.
     * @throws: If the given angles aren't in the interval ([-90,90] for alt and [0, 360[ for az), it will
     * throw an IllegalArgumentException.
     */
    public static HorizontalCoordinates of(double az, double alt) {
        return new HorizontalCoordinates(Preconditions.checkInInterval(altInterval, alt), Preconditions.checkInInterval(azInterval, az));
    }
        
    /**
     * Auxiliary constructor to create an horizontal coordinate using angles given in degrees.
     * @param azDeg: the azimuth angle in degrees.
     * @param altDeg: the altitude angle in degrees.
     * @return: the newly created horizontal coordinates.
     * @throws: If the given angles aren't in the interval ([-90,90] for alt and [0, 360[ for az), it will
     * throw an IllegalArgumentException.
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        return new HorizontalCoordinates(Angle.ofDeg(Preconditions.checkInInterval(altInterval, Angle.ofDeg(altDeg))), 
                Angle.ofDeg(Preconditions.checkInInterval(azInterval, Angle.ofDeg(azDeg))));
    }
    
    public double az() {
        return lon();
    }
    
    /**
     * Getter for the azimuth in degrees.
     * @return: the azimuth converted in degrees.
     */
    public double azDeg() {
        return lonDeg();
    }
    
    /**
     * Allows to know in which octant of the 8 cardinals point the azimut angle is in.
     * @param n: the string representation of the north.
     * @param e: the string representation of the east.
     * @param s: the string representation of the south.
     * @param w: the string representation of the west.
     * @return: the corresponding octant of the angle written using the given string representation of the cardinal points.
     */
    public String azOctantName(String n, String e, String s, String w) {
        // Reduce the 360°(two pi) span of a full circle to height equally distributed slices representing the 8 octant.
        int normalizationAz = (int)Math.round((az() * 8) / (Math.PI * 2));
        switch(normalizationAz) {
            case(0):
                return n;
            case(1):
                return n + e;
            case(2):
                return e;
            case(3):
                return s + e;
            case(4):
                return s;
            case(5):
                return s + w;
            case(6):
                return w;
            case(7):
                return n + w;
            default:
                return "Error";
        }
    }

    /**
     * Getter for the altitude.
     * @return: the altitude of the coordinates.
     */
    public double alt() {
        return lat();
    }
    
    /**
     * Getter for the altitude converted in degrees.
     * @return: the altitude converted in degrees.
     */
    public double altDeg() {
        return latDeg();
    }

    /**
     * Allows to know the distance bewtween to coordinates.
     * @param that: the coordinates with which the distance must be computed.
     * @return: the distance between the two coordinates.
     */
    public double angularDistanceTo(HorizontalCoordinates that) {
        return Math.acos(Math.sin(that.alt()) * Math.sin(alt()) + Math.cos(that.alt()) * Math.cos(alt()) * Math.cos(that.az()-az()));
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", azDeg(), altDeg());
    }
}

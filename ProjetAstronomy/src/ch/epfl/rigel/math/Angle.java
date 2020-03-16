package ch.epfl.rigel.math;

/**
 * Representation of a Polynomial
 * 
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 *
 */

public final class Angle {

    public static final double TAU = 2 * Math.PI;

    // Number of second arc per degree.
    private static final double ARC_SECOND_PER_DEG = 1 / 3600.0;

    // Number of arc minute per degree.
    private static final double ARC_MINUTE_PER_DEG = 1 / 60.0;

    // Number of arc second per radian.
    private static final double ARC_SECOND_PER_RAD = Math.toRadians(ARC_SECOND_PER_DEG);    

    // Number of arc hour per radian.
    private static final double ARC_HOUR_PER_RAD = 24/ TAU;

    // Number of radian per arc hour.
    private static final double RAD_PER_ARC_HOUR = TAU / 24;

    private Angle () {}

    /**
     * Normalize an angle in radian to the interval [0,2pi[.
     * 
     * @param rad(Double): angle in radians to normalize.
     * @return (Double): normalized angle.
     */
    public static double normalizePositive(double rad) {   

        double multipleTAU = Math.floor(rad / TAU);

        rad -= TAU  * multipleTAU;
        return rad;

    }

    /**
     * Transform angle from second to radians.
     * 
     * @param sec(Double): angle to conversion.
     * @return(Double): converted angle in radian.
     */
    public static double ofArcsec(double sec) {
        return sec*ARC_SECOND_PER_RAD;
    }

    /**
     * Transform an angle in DMS system to radians.
     * @param deg(Integer): degrees of the angle.
     * @param min(Integer): minutes of the anlge.
     * @param sec(Double): seconds of the angle.
     * @return(Double): the converted angle in radians.
     */
    public static double ofDMS(int deg, int min, double sec) {
        if(  min < 0 || min >= 60 || sec < 0 || sec >= 60) {
            throw new IllegalArgumentException();
        }

        double angle_deg = deg + min* ARC_MINUTE_PER_DEG + sec * ARC_SECOND_PER_DEG;
        return Math.toRadians(angle_deg);
    }

    /**
     * Transform an angle from degrees to radians.
     * @param deg(Double): the angle to convert.
     * @return(Double): the angle converted in radians.
     */
    public static double ofDeg(double deg) {
        return Math.toRadians(deg);
    }

    /**
     * Transform an angle from radians to degrees.
     * @param rad(Double): the angle in radians to be converted.
     * @return(Double): the angle converted in degrees.
     */
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * Transform angle from hours to radians.
     * @param hr(Double): the angle in hours to convert.
     * @return(Double): the angle converted in radians.
     */
    public static double ofHr(double hr) {
        return hr* RAD_PER_ARC_HOUR;
    } 

    /**
     * Transform an angle from radians to hours.
     * @param rad(Double): the angle in radians to convert.
     * @return(Double): the angle converted in hours.
     */
    public static double toHr(double rad) {
        return rad * ARC_HOUR_PER_RAD;
    }
}

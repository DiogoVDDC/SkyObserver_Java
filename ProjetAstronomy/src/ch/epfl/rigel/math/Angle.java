package ch.epfl.rigel.math;

/**
 * Representation of a Polynomial
 * 
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 * @author Theo Houle (312432)
 *
 */
public final class Angle {

    public static final double TAU = 2 * Math.PI;

    // Number of second arc per degree.
    private static final double ARC_SECOND_PER_DEG = 1 / 3600.0;

    // Number of arc minute per degree.
    private static final double ARC_MINUTE_PER_DEG = 1 / 60.0;

    // Number of arc second per radiant.
    private static final double ARC_SECOND_PER_RAD = Math.toRadians(ARC_SECOND_PER_DEG);    

    // Number of arc hour per radiant.
    private static final double ARC_HOUR_PER_RAD = 24/ TAU;

    // Number of radiant per arc hour.
    private static final double RAD_PER_ARC_HOUR = TAU / 24;

    private Angle () {}

    /**
     * Normalise an angle in radiant to the interval [0, tau[.
     * 
     * @param rad: angle in radiant to normalise.
     * @return: normalised angle.
     */
    public static double normalizePositive(double rad) {   

        double multipleTAU = Math.floor(rad / TAU);

        rad -= TAU  * multipleTAU;
        return rad;

    }

    /**
     * Transform angle from second to radiant.
     * 
     * @param sec: angle to conversion.
     * @return: converted angle in radiant.
     */
    public static double ofArcsec(double sec) {
        return sec*ARC_SECOND_PER_RAD;
    }

    /**
     * Transform an angle in DMS system to radiant.
     * @param deg: degrees of the angle.
     * @param min: minutes of the angle.
     * @param sec: seconds of the angle.
     * @return: the converted angle in radiant.
     */
    public static double ofDMS(int deg, int min, double sec) {
        if(  min < 0 || min >= 60 || sec < 0 || sec >= 60) {
            throw new IllegalArgumentException();
        }

        double angle_deg = deg + min* ARC_MINUTE_PER_DEG + sec * ARC_SECOND_PER_DEG;
        return Math.toRadians(angle_deg);
    }

    /**
     * Transform an angle from degrees to radiant.
     * @param deg(Double): the angle to convert.
     * @return(Double): the angle converted in radiant.
     */
    public static double ofDeg(double deg) {
        return Math.toRadians(deg);
    }

    /**
     * Transform an angle from radiant to degrees.
     * @param rad(Double): the angle in radiant to be converted.
     * @return(Double): the angle converted in degrees.
     */
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * Transform angle from hours to radiant.
     * @param hr(Double): the angle in hours to convert.
     * @return(Double): the angle converted in radiant.
     */
    public static double ofHr(double hr) {
        return hr* RAD_PER_ARC_HOUR;
    } 

    /**
     * Transform an angle from radiant to hours.
     * @param rad(Double): the angle in radiant to convert.
     * @return(Double): the angle converted in hours.
     */
    public static double toHr(double rad) {
        return rad * ARC_HOUR_PER_RAD;
    }
}

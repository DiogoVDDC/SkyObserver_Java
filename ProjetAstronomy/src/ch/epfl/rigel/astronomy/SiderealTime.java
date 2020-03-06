package ch.epfl.rigel.astronomy;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

/**
 * Allows to compute the sidereal time of a given location at given date at a given time.
 * @author Theo Houle (312432)
 *
 */
public final class SiderealTime {
    
    // Polynomials used for the computation of the sidereal time. 
    private static final Polynomial polyS0 = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
    private static final Polynomial polyS1 = Polynomial.of(1.002737909, 0);   
    private static final double MILLIS_PER_HOUR = 1/ (1000.0*60.0*60.0);
    
    // The constructor is set to private and does nothing to disallow the instantiation of this class.
    private SiderealTime() {}

    /**
     * Allows to compute the sidereal time of greenwich.
     * @param when: the date and time of the instant.
     * @return: the sidereal time of greenwich at the given moment.
     */
    public static double greenwich(ZonedDateTime when) {
        System.out.println(Epoch.J2000.julianCenturiesUntil(when));
        ZonedDateTime offSettedDate = when.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime truncatedToDay = offSettedDate.truncatedTo(ChronoUnit.DAYS);
        double T = Epoch.J2000.julianCenturiesUntil(truncatedToDay);
        System.out.println(T);
        double t = (truncatedToDay.until(offSettedDate, ChronoUnit.MILLIS)) * MILLIS_PER_HOUR;
        double s0 = polyS0.at(T);
        double s1 = polyS1.at(t);
        return Angle.normalizePositive(Angle.ofHr(s0+s1));
    }
    
    /**
     * Allows to compute the sidereal time of a given location at a certain time.
     * @param when: the date and time of the instant.
     * @param where: the coordinates of the instant.
     * @return: The sidereal time of a the given location at the given time.
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }
    
}

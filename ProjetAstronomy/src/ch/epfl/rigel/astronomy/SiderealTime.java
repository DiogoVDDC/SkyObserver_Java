package ch.epfl.rigel.astronomy;

import java.time.ZoneId;
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
    private static final Polynomial polyS0 = Polynomial.of(0.000025862, 2400.051336, 6.697374554558);
    private static final Polynomial polyS1 = Polynomial.of(1.002737909);
    
    // The constructor is set to private and does nothing to disallow the instantiation of this class.
    private SiderealTime() {}

    /**
     * Allows to compute the sidereal time of greenwich.
     * @param when: the date and time of the instant.
     * @return: the sidereal time of greenwich at the given moment.
     */
    public static double greenwich(ZonedDateTime when) {
        when.getOffset();
        ZonedDateTime offSettedDate = when.withZoneSameInstant(ZoneId.of("UTC"));
        double T = Epoch.J2000.julianCenturiesUntil(offSettedDate.truncatedTo(ChronoUnit.DAYS));
        double t = (offSettedDate.until(when, ChronoUnit.MILLIS))/(1000*60*60);
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
        return greenwich(when) + where.lon();
    }
    
}

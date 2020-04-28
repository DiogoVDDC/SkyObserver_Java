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
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 *
 */
public final class SiderealTime {
    
    // Polynomials used for the computation of the sidereal time. 
    private static final Polynomial POLY_S0 = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
    private static final Polynomial POLY_S1 = Polynomial.of(1.002737909, 0);   
    private static final double MILLIS_PER_HOUR = 1/ (1000.0*60.0*60.0);
    
    // The constructor is set to private and does nothing to disallow the instantiation of this class.
    private SiderealTime() {}

    /**
     * Allows to compute the sidereal time of greenwich.
     * @param when: the date and time of the instant.
     * @return: the sidereal time of greenwich at the given moment.
     */
    public static double greenwich(ZonedDateTime when) {
    	//Convert the given time to be in UTC time.
        ZonedDateTime offSettedDate = when.withZoneSameInstant(ZoneOffset.UTC);
        //Truncate the date to have the begining (at 00h00).
        ZonedDateTime truncatedToDay = offSettedDate.truncatedTo(ChronoUnit.DAYS);
        //Number of julian centuries since the begining of the given date.
        double T = Epoch.J2000.julianCenturiesUntil(truncatedToDay);
        //Number of hours since the begining of the day.
        double t = (truncatedToDay.until(offSettedDate, ChronoUnit.MILLIS)) * MILLIS_PER_HOUR;
        
        //Given the previously calculated values T and t, compute the values s0 and s1 using the specific polynomes.
        double s0 = POLY_S0.at(T);
        double s1 = POLY_S1.at(t);
        //Return the sidereal time normalized between [0, t[.
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

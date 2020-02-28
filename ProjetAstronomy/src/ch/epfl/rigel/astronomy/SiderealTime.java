package ch.epfl.rigel.astronomy;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

public final class SiderealTime {
    
    private static final Polynomial polyS0 = Polynomial.of(0.000025862, 2400.051336, 6.697374554558);
    private static final Polynomial polyS1 = Polynomial.of(1.002737909);
    
    private SiderealTime() {}

    public static double greenwich(ZonedDateTime when) {
        when.getOffset();
        ZonedDateTime offSettedDate = when.withZoneSameInstant(ZoneId.of("UTC"));
        double T = Epoch.J2000.julianCenturiesUntil(offSettedDate.truncatedTo(ChronoUnit.DAYS));
        double t = (offSettedDate.until(when, ChronoUnit.MILLIS))/(1000*60*60);
        double s0 = polyS0.at(T);
        double s1 = polyS1.at(t);
        return Angle.normalizePositive(Angle.ofHr(s0+s1));
    }
    
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        return greenwich(when) + where.lon();
    }
    
}

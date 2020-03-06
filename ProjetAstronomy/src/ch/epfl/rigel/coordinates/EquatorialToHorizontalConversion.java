package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

/**
 * Allows the convertion from equatorial to horizontal coordinates
 * @author Diogo Valdivieso Damasio Da Costa (311673) 
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>{

    private final double cosOfObservLat;
    private final double sinOfObservLat;
    private final double localSideRealTime;

    /**
     * Initializes the constant used to reduce calculation when conversion of coordinates
     * @param when: instance at which the conversion occur
     * @param where: used to calculate the sidereal time
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {        
        cosOfObservLat = Math.cos(where.lat());
        sinOfObservLat = Math.sin(where.lat());
        localSideRealTime = SiderealTime.local(when, where); 
    }

    @Override
    /**
     * Converts equatorial coordinates to horizontal coordinates
     * @param: equatorial coordinates to be converted
     * @returns: new horizontal coordinates after conversion
     */
    public HorizontalCoordinates apply(EquatorialCoordinates equ) { 
        double angleHour = localSideRealTime - equ.ra();
        // Altitude calculation.
        double alt = Math.asin(( Math.sin(equ.dec()) * sinOfObservLat )
                    + ( Math.cos(equ.dec()) * cosOfObservLat * Math.cos(angleHour) ));
        // Quotient of the arctan of azimuth calculation.
        double y = (-Math.cos(equ.dec())) * cosOfObservLat * Math.sin(angleHour);
        // Denominator of the arctan of azimuth calculation.
        double x = Math.sin(equ.dec()) - sinOfObservLat* Math.sin(alt);
        // Calculation and normalization of arctan.
        double az = Angle.normalizePositive(Math.atan2(y, x));
        return  HorizontalCoordinates.of(az, alt);
    }
    
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
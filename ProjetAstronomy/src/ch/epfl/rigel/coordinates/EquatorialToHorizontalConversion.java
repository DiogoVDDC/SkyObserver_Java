package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

/**
 * Allows the convertion from equatorial to horizontal coordinates.
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 * @author Theo Houle (312432)
 *
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>{

	//Cosine of the lattitude of the observer.
    private final double cosOfObservLat;
    //Sine of the lattitude of the osberver.
    private final double sinOfObservLat;
    //Local sidereal time.
    private final double localSideRealTime;

    /**
     * Initializes the constant used to reduce calculation when conversion of coordinates.
     * @param when: instance at which the conversion occur.
     * @param where: used to calculate the sidereal time.
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {        
        cosOfObservLat = Math.cos(where.lat());
        sinOfObservLat = Math.sin(where.lat());
        localSideRealTime = SiderealTime.local(when, where); 
    }

    @Override
    /**
     * Converts equatorial coordinates to horizontal coordinates.
     * @param: equatorial coordinates to be converted.
     * @returns: new horizontal coordinates after conversion.
     */
    public HorizontalCoordinates apply(EquatorialCoordinates equ) { 
    	double sinOfDec = Math.sin(equ.dec());
    	double cosOfDec = Math.cos(equ.dec());
        double angleHour = localSideRealTime - equ.ra();
        // Altitude calculation.
        double alt = ( sinOfDec * sinOfObservLat )
                    + ( cosOfDec * cosOfObservLat * Math.cos(angleHour) );
        // Quotient of the arctan of azimuth calculation.
        double y = (-cosOfDec) * cosOfObservLat * Math.sin(angleHour);
        // Denominator of the arctan of azimuth calculation.
        double x = sinOfDec - sinOfObservLat* alt;
        // Calculation and normalization of arctan.
        double az = Angle.normalizePositive(Math.atan2(y, x));
        return  HorizontalCoordinates.of(az, Math.asin(alt));
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
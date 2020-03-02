package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.SiderealTime;

/**
 * Allows the convertion from equatorial to horizontal coordinates
 * @author Diogo Valdivieso Damasio Da Costa (311673) 
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>{

    private final double cosOfObservationLatitude;
    private final double sinOfObservationLatitude;
    private final double localSideRealTime;

    /**
     * Initialises the constant used to reduce calculation when conversion of coordinates
     * @param when: instance at which the conversion occur
     * @param where: used to calculate the sidereal time
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {        
        cosOfObservationLatitude = Math.cos(where.lat());
        sinOfObservationLatitude = Math.sin(where.lat());
        localSideRealTime = SiderealTime.local(when, where);        
    }

    @Override
    /**
     * Converts
     * @param: equatorial coordinates to be converted
     * @returns: new horizontal coordinates after conversion
     */
    public HorizontalCoordinates apply(EquatorialCoordinates equ) { 
        double angleHour = localSideRealTime - equ.ra();
        
        double alt = Math.asin(Math.sin(equ.dec())*sinOfObservationLatitude 
                + Math.cos(equ.dec()*cosOfObservationLatitude*Math.cos(angleHour)));
        
        
        double az = Math.atan2(-Math.cos(equ.dec())*cosOfObservationLatitude*Math.sin(angleHour)
                , Math.sin(equ.dec()) - sinOfObservationLatitude*Math.sin(alt));
        
        
        return  HorizontalCoordinates.of(az, alt);
    }
}
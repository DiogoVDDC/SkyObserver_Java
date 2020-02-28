package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.SiderealTime;

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>{

    private final double cosOfObservationLatitude;
    private final double sinOfObservationLatitude;
    private final double localSideRealTime;

    
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {        
        cosOfObservationLatitude = Math.cos(where.lat());
        sinOfObservationLatitude = Math.sin(where.lat());
        localSideRealTime = SiderealTime.local(when, where);        
    }

    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) { 
        double angleHour = localSideRealTime - equ.ra();
        
        double alt = Math.asin(Math.sin(equ.dec())*sinOfObservationLatitude 
                + Math.cos(equ.dec()*cosOfObservationLatitude*Math.cos(angleHour)));
        
        
        double az = Math.atan2(-Math.cos(equ.dec())*cosOfObservationLatitude*Math.sin(angleHour)
                , Math.sin(equ.dec()) - sinOfObservationLatitude*Math.sin(alt));
        
        
        return  HorizontalCoordinates.of(az, alt);
    }
}
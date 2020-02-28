package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>{

    private double observationLatitude;
    private double cosOfObservationLatitude;
    private double sinOfObservationLatitude;
    private double angleHour;
    private double cosOfAngleHour;
    private double sinOfAngleHour;
    
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        observationLatitude = where.lat();
        cosOfObservationLatitude = Math.cos(observationLatitude);
        sinOfObservationLatitude = Math.sin(observationLatitude);
     //   angleHour
    }

    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {        
        double az = Math.atan2(-Math.cos(equ.dec())*cosOfObservationLatitude*sinOfAngleHour
                , Math.sin(equ.dec()) - sinOfObservationLatitude*Math.sin());
        double alt = 0;
        return  HorizontalCoordinates.of(az, alt);
    }
}
package ch.epfl.rigel.coordinates;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;

public class EclipticToEquatorialConversionTest {
    @Test
    void EclipticToEquatorialConversionTest1() {
        EclipticCoordinates coord = EclipticCoordinates.of(Angle.ofDMS(139, 41,10), Angle.ofDMS(4,52,31));
        
        EclipticToEquatorialConversion EclipticToEquatorial = new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2009, 07, 6)                
                ,LocalTime.of(0,0), ZoneOffset.UTC));
        
        assertEquals(EclipticToEquatorial.apply(coord).toString() , 
                EquatorialCoordinates.of(Angle.ofHr(9.581478),Angle.ofDMS(19, 32, 6.01)).toString());    
        
        
        EclipticCoordinates coord1 = EclipticCoordinates.of(Angle.ofDMS(30, 10, 2.5), Angle.ofDMS(30, 20, 3.5));
        EclipticToEquatorialConversion EclipticToEquatorial1 = new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2020, 12, 3), LocalTime.of(0, 0), ZoneOffset.UTC));
        
        
        assertEquals(EquatorialCoordinates.of(Angle.ofHr(0.986275)
                   ,Angle.ofDMS(39, 29, 8.99)).toString(), EclipticToEquatorial1.apply(coord1).toString());  
        
        
    
    }
}

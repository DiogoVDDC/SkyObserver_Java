package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

public class ModelSunTest {
    @Test
    void SunPositionTest() {
        ZonedDateTime d1 = ZonedDateTime.of(
                LocalDate.of(2003, Month.JULY, 27),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);
        
       double daysSinceJ2010 = Epoch.J2010.daysUntil(d1);
        
       EclipticToEquatorialConversion conv = new EclipticToEquatorialConversion(d1);
       
       Sun sun1 = SunModel.SUN.at(daysSinceJ2010, conv);
       
       assertEquals(Angle.ofHr(8.39), sun1.equatorialPos().ra(), 1e-6);
       assertEquals(Angle.ofDMS(19, 21, 10), sun1.equatorialPos().dec(), 1e-6);
     
    }
    
    @Test 
    void AngularSizeTest() {
        ZonedDateTime d1 = ZonedDateTime.of(
                LocalDate.of(1988, Month.JULY, 27),
                LocalTime.of(0,0),
                ZoneOffset.UTC);
        
       double daysSinceJ2010 = Epoch.J2010.daysUntil(d1);
        
       EclipticToEquatorialConversion conv = new EclipticToEquatorialConversion(d1);
       
       Sun sun1 = SunModel.SUN.at(daysSinceJ2010, conv);       
      
       assertEquals(Angle.ofDMS(0, 31, 30), sun1.angularSize(), 1e-6);
    }
}

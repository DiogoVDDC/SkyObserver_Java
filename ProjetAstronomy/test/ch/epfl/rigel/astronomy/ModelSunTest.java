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
       
       Sun sun = SunModel.SUN.at(daysSinceJ2010, conv);
       
       assertEquals(8.392682808297808, sun.equatorialPos().raHr(), 1e-13);
       assertEquals(19.35288373097352 , sun.equatorialPos().decDeg(), 1e-13);
       
    }
    
    @Test 
    void AngularSizeTest() {
        ZonedDateTime d1 = ZonedDateTime.of(
                LocalDate.of(1988, Month.JULY, 27),
                LocalTime.of(0,0),
                ZoneOffset.UTC);
        
       double daysSinceJ2010 = Epoch.J2010.daysUntil(d1);
        
       EclipticToEquatorialConversion conv = new EclipticToEquatorialConversion(d1);
       
       Sun sun= SunModel.SUN.at(daysSinceJ2010, conv);       
      
       assertEquals(Angle.ofDMS(0, 31, 30), sun.angularSize(), 1e-6);
    }
    
    
    @Test
    void SunPosTest2() {
    	EclipticToEquatorialConversion conv = new EclipticToEquatorialConversion(ZonedDateTime.of(
                					LocalDate.of(2010, Month.FEBRUARY, 27),
                					LocalTime.of(0,0),
                					ZoneOffset.UTC));
    	assertEquals(5.9325494700300885, SunModel.SUN.at(27 + 31, conv).equatorialPos().ra());
    	
    	assertEquals(8.392682808297806, SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 
    	        27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().raHr());
    	        
    }
}

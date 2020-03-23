package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

public class PlanetModelTest {

    @Test
    void test() {
    	EclipticToEquatorialConversion con = new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22), 
    									LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC));
    	assertEquals(11.18715493470968, PlanetModel.JUPITER.at(-2231.0, con).equatorialPos().raHr());
    	assertEquals(6.3566355066857465, PlanetModel.JUPITER.at(-2231.0, con).equatorialPos().decDeg()); 
    	
    }
    
    @Test
    void test2() {
    	EclipticToEquatorialConversion con = new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22), 
				LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC));
    	assertEquals(16.820074565897148, PlanetModel.MERCURY.at(-2231.0, con).equatorialPos().raHr());
    	assertEquals(-24.500872462861217, PlanetModel.MERCURY.at(-2231.0, con).equatorialPos().decDeg());
    }
    
    @Test
    void testAngularSize() {
    	EclipticToEquatorialConversion con = new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22), 
				LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC));
    	assertEquals(35.11141185362771, Angle.toDeg(PlanetModel.JUPITER.at(-2231.0, con).angularSize() * 3600));
    }
    
    @Test
    void testMagnitude() {
    	EclipticToEquatorialConversion con = new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22), 
				LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC));
    	assertEquals(-1.9885659217834473, PlanetModel.JUPITER.at(-2231.0, con).magnitude());
    }
}

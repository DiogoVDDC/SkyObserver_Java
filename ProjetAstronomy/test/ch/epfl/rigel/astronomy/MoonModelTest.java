package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

public class MoonModelTest {

    @Test
    public void MoonModelAtTest() {
        
        EclipticToEquatorialConversion conv = new EclipticToEquatorialConversion(ZonedDateTime.of(
            LocalDate.of(2003,Month.SEPTEMBER,1),
            LocalTime.of(0,0),
            ZoneOffset.UTC));
        
        
        assertEquals(14.211456457836277, MoonModel.MOON.at(-2313, conv).equatorialPos().raHr());
      
        EclipticToEquatorialConversion conv1 = new EclipticToEquatorialConversion(ZonedDateTime.of(
                LocalDate.of(2003,  Month.SEPTEMBER, 1),
                LocalTime.of(0,0),
                ZoneOffset.UTC));
           
        assertEquals(-0.20114171346019355, MoonModel.MOON.at(-2313, conv1).equatorialPos().dec());
        
    }
}

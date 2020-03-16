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
    void InnerPlanetMercuryAt() {
        ZonedDateTime d1 = ZonedDateTime.of(
                LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0,0),
                ZoneOffset.UTC);
        
        double daysSinceJ2010 = Epoch.J2010.daysUntil(d1);
        
        EclipticToEquatorialConversion conv1 = new EclipticToEquatorialConversion(d1);
        
        Planet jup = PlanetModel.JUPITER.at(daysSinceJ2010, conv1);
        
        assertEquals(Angle.ofHr(11.187222222222222222222), jup.equatorialPos().ra());
        assertEquals(Angle.ofDMS(6, 21, 25), jup.equatorialPos().dec());
    }
    
    @Test
    void OuterPlanetJupiter() {
        ZonedDateTime d1 = ZonedDateTime.of(
                LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0,0),
                ZoneOffset.UTC);
        
        double daysSinceJ2010 = Epoch.J2010.daysUntil(d1);
        
        EclipticToEquatorialConversion conv1 = new EclipticToEquatorialConversion(d1);
        
        Planet mec = PlanetModel.JUPITER.at(daysSinceJ2010, conv1);
        
        assertEquals(Angle.ofHr(16.42), mec.equatorialPos().ra());
        assertEquals(Angle.ofDMS(-24, 30, 9), mec.equatorialPos().dec());
    }
}

package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;

class EquatorialToHorizontalTest {

    @Test
    void test() {
        ZonedDateTime when = ZonedDateTime.of(LocalDate.of(1998, Month.AUGUST, 10), LocalTime.of(23, 10), ZoneOffset.UTC);
        GeographicCoordinates obs = GeographicCoordinates.ofDeg(-1.9166667, 52.5);
        EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(when, obs);
  
        EquatorialCoordinates  where = EquatorialCoordinates.of(Angle.normalizePositive(Angle.ofHr(16.695)), Angle.ofDMS(36, 28, 0));
        HorizontalCoordinates converted = HorizontalCoordinates.ofDeg(269.1467, 49.1689);
        assertEquals(converted.altDeg(), conv.apply(where).altDeg(), 1e-3);
        assertEquals(converted.azDeg(), conv.apply(where).azDeg(), 1e-3);
        
        ZonedDateTime when2 = ZonedDateTime.of(LocalDate.of(1997, Month.MARCH, 14), LocalTime.of(19, 00), ZoneOffset.UTC);
        EquatorialToHorizontalConversion conv2 = new EquatorialToHorizontalConversion(when2, obs);
  
        EquatorialCoordinates  where2 = EquatorialCoordinates.of(Angle.normalizePositive(Angle.ofHr(22.99666666666667)), Angle.ofDMS(42, 43, 0));
        HorizontalCoordinates converted2 = HorizontalCoordinates.ofDeg(311.92258, 22.40100);
        assertEquals(converted2.altDeg(), conv2.apply(where).altDeg(), 1e-3);
        assertEquals(converted2.azDeg(), conv2.apply(where).azDeg(), 1e-3);
    }

}

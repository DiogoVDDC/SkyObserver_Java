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
//        ZonedDateTime when = ZonedDateTime.of(LocalDate.of(1998, Month.AUGUST, 10), LocalTime.of(23, 10), ZoneOffset.UTC);
//        GeographicCoordinates obs = GeographicCoordinates.ofDeg(-1.9166667, 52.5);
//        EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(when, obs);
//  
//        EquatorialCoordinates  where = EquatorialCoordinates.of(Angle.normalizePositive(Angle.ofHr(16.695)), Angle.ofDMS(36, 28, 0));
//        HorizontalCoordinates converted = HorizontalCoordinates.ofDeg(269.1467, 49.1689);
//        assertEquals(converted.altDeg(), conv.apply(where).altDeg(), 1e-3);
//        assertEquals(converted.azDeg(), conv.apply(where).azDeg(), 1e-3);
//        
//        ZonedDateTime when2 = ZonedDateTime.of(LocalDate.of(1997, Month.MARCH, 14), LocalTime.of(19, 00), ZoneOffset.UTC);
//        EquatorialToHorizontalConversion conv2 = new EquatorialToHorizontalConversion(when2, obs);
//  
//        EquatorialCoordinates  where2 = EquatorialCoordinates.of(Angle.normalizePositive(Angle.ofHr(22.99666666666667)), Angle.ofDMS(42, 43, 0));
//        HorizontalCoordinates converted2 = HorizontalCoordinates.ofDeg(311.92258, 22.40100);
//        assertEquals(converted2.altDeg(), conv2.apply(where).altDeg(), 1e-3);
//        assertEquals(converted2.azDeg(), conv2.apply(where).azDeg(), 1e-3);
        ZonedDateTime when3 = ZonedDateTime.of(LocalDate.of(2007, Month.JULY, 22),
                                LocalTime.of(5, 9, 50, (int).3e8), 
                                ZoneOffset.UTC);
        GeographicCoordinates obs3 = GeographicCoordinates.ofDeg(Angle.toDeg(Angle.ofDMS(172, 47, 34.99999799999998)),
                                                Angle.toDeg(Angle.ofDMS(6, 3, 10.999998000000009)));
        System.out.println(obs3.latDeg());
        System.out.println(obs3.lonDeg());
        
        EquatorialToHorizontalConversion conv3 = new EquatorialToHorizontalConversion(when3, obs3);
  
        EquatorialCoordinates  where3 = EquatorialCoordinates.of(Angle.normalizePositive(Angle.ofHr(8.113925)), Angle.ofDMS(20, 14, 47.16));
        HorizontalCoordinates converted3 = HorizontalCoordinates.ofDeg(289.40, 22.28);
        assertEquals(converted3.altDeg(), conv3.apply(where3).altDeg(), 1e-3);
        assertEquals(converted3.azDeg(), conv3.apply(where3).azDeg(), 1e-3);
    }

}

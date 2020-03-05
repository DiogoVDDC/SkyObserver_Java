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
//        EquatorialCoordinates  where = EquatorialCoordinates.of(Angle.normalizePositive(Angle.ofHr(16.695)), Angle.ofDMS(36, 28, 0));
//        HorizontalCoordinates converted = HorizontalCoordinates.ofDeg(269.1467, 49.1689);
//        assertEquals(converted.altDeg(), conv.apply(where).altDeg(), 1e-3);
//        assertEquals(converted.azDeg(), conv.apply(where).azDeg(), 1e-3);
//        
//        ZonedDateTime when2 = ZonedDateTime.of(LocalDate.of(1997, Month.MARCH, 14), LocalTime.of(19, 00), ZoneOffset.UTC);
//        GeographicCoordinates obs2 = GeographicCoordinates.ofDeg(-1.9166667, 52.5);
//        EquatorialToHorizontalConversion conv2 = new EquatorialToHorizontalConversion(when2, obs2);
//        EquatorialCoordinates  where2 = EquatorialCoordinates.of(Angle.normalizePositive(Angle.ofHr(22.99666666666667)), Angle.ofDMS(42, 43, 0));
//        HorizontalCoordinates converted2 = HorizontalCoordinates.ofDeg(311.92258, 22.40100);
//        assertEquals(converted2.altDeg(), conv2.apply(where2).altDeg(), 1e-3);
//        assertEquals(converted2.azDeg(), conv2.apply(where2).azDeg(), 1e-3);
        
        ZonedDateTime when3 = ZonedDateTime.of(LocalDate.of(2020, Month.MAY, 5),
                                LocalTime.of(18, 0), 
                                ZoneOffset.UTC);
        GeographicCoordinates obs3 = GeographicCoordinates.ofDeg(0, Angle.ofDMS(51, 28, 6));
        System.out.println(obs3.latDeg());
        System.out.println(obs3.lonDeg());
        EquatorialToHorizontalConversion conv3 = new EquatorialToHorizontalConversion(when3, obs3);
        EquatorialCoordinates  where3 = EquatorialCoordinates.of(Angle.ofHr(10), Angle.ofDMS(40, 0, 0));
        HorizontalCoordinates converted3 = HorizontalCoordinates.ofDeg((71 + 02/60 + 14.46/3600), (38 + 11/60 + 38.84/3600));
        assertEquals(converted3.altDeg(), conv3.apply(where3).altDeg(), 1e-3);
        assertEquals(converted3.azDeg(), conv3.apply(where3).azDeg(), 1e-3);
    	
//        ZonedDateTime when = ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22), LocalTime.of(18, 36, 51, (int)6.7e8), ZoneOffset.UTC);
//        GeographicCoordinates obs3 = GeographicCoordinates.ofDeg(172, 52);
//        EquatorialToHorizontalConversion conv3 = new EquatorialToHorizontalConversion(when, obs3);
//        HorizontalCoordinates converted2 = HorizontalCoordinates.ofDeg(283.2710273, 19.33434522);
//        EquatorialCoordinates  where = EquatorialCoordinates.of(Angle.normalizePositive(Angle.ofHr(5 + 51/60 + 44/3600)), Angle.ofDMS(23, 13, 10));
//        System.out.println(conv3.apply(where));
//        assertEquals(converted2.altDeg(), conv3.apply(where).altDeg(), 1e-6);
//        assertEquals(converted2.azDeg(), conv3.apply(where).azDeg(), 1e-6);
    	
    }

}

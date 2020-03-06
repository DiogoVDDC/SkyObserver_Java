package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

class EquatorialToHorizontalTest {

    @Test
    void test() {
        
//        ZonedDateTime when3 = ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22),
//                LocalTime.of(18, 36, 51, (int)6.7e8), 
//                ZoneOffset.UTC);
//        GeographicCoordinates obs3 = GeographicCoordinates.ofDeg(0, 52);
//        EquatorialToHorizontalConversion conv3 = new EquatorialToHorizontalConversion(when3, obs3);
//        EquatorialCoordinates  where3 = EquatorialCoordinates.of(Angle.ofHr(10), Angle.ofDMS(23, 13, 10));
//        HorizontalCoordinates converted3 = HorizontalCoordinates.ofDeg(129.44 ,73.87);
//        assertEquals(Angle.ofDMS(19, 20, 3.64), conv3.apply(where3).alt(), 1e-8);
//        System.out.println("az =" + Angle.toDeg(conv3.apply(where3).az()) );
//        assertEquals(Angle.ofDMS(283, 16, 15.7), conv3.apply(where3).az(), 1e-10);
     
    }

}

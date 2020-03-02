package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;

class SiderealTimeTest {

    @Test
    void greenwichTest() {
        ZonedDateTime d1 = ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, 67 * 10^8),
                ZoneOffset.UTC);
        assertEquals(Angle.ofDMS(4, 40, 5.23),SiderealTime.greenwich(d1));
//        ZonedDateTime d2 = ZonedDateTime.of(
//                LocalDate.of(2001, Month.JANUARY, 27),
//                LocalTime.of(12, 0),
//                ZoneOffset.UTC);
//        assertEquals(5.37112916198716,SiderealTime.greenwich(d2));
//        ZonedDateTime d3 = ZonedDateTime.of(
//                LocalDate.of(2001, Month.SEPTEMBER, 11),
//                LocalTime.of(8, 14),
//                ZoneOffset.UTC);
//        assertEquals(0.07288478193571, SiderealTime.greenwich(d3));
//        ZonedDateTime d4 = ZonedDateTime.of(
//                LocalDate.of(2004, Month.SEPTEMBER, 23),
//                LocalTime.of(11, 0),
//                ZoneOffset.UTC);
//        assertEquals(2.47403226856781, SiderealTime.greenwich(d4));
    }

}

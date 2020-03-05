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
                LocalTime.of(14, 36, 51, 67 * 10000000),
                ZoneOffset.UTC);
        assertEquals(Angle.ofHr(4.668120),SiderealTime.greenwich(d1), 1e-6);
        ZonedDateTime d2 = ZonedDateTime.of(
                LocalDate.of(1899, Month.DECEMBER, 31),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);
        assertEquals(1.73133420074,SiderealTime.greenwich(d2), 1e-6);
         ZonedDateTime d3 = ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22),
                 LocalTime.of(14, 36, 51, (int)6.7e8),
                 ZoneOffset.UTC);
         assertEquals(Angle.ofHr(4.668119327), SiderealTime.greenwich(d3), 1e-6);
    }

}

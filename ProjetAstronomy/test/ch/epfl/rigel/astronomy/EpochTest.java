package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

class EpochTest {

    @Test
    void test() {
        ZonedDateTime d1 = ZonedDateTime.of(
                LocalDate.of(2020, Month.FEBRUARY, 28),
                LocalTime.of(12, 0),
                ZoneOffset.UTC);
        assertEquals(7363, Epoch.J2000.daysUntil(d1));
        ZonedDateTime d2 = ZonedDateTime.of(
                LocalDate.of(2001, Month.SEPTEMBER, 11),
                LocalTime.of(8, 45),
                ZoneOffset.UTC);
        assertEquals(618.86458333333, Epoch.J2000.daysUntil(d2), 1e-8);
        ZonedDateTime d3 = ZonedDateTime.of(
                LocalDate.of(2000, Month.NOVEMBER, 13),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);
        assertEquals(316.5, Epoch.J2000.daysUntil(d3));
    }

}

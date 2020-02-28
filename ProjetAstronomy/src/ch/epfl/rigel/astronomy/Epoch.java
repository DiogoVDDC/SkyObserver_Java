
package ch.epfl.rigel.astronomy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public enum Epoch {
    
    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1), LocalTime.of(12, 0),
            ZoneOffset.UTC)),
    J2010(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1), LocalTime.of(0, 0),
            ZoneOffset.UTC));    
    
    
    private ZonedDateTime date;
    
    private Epoch(ZonedDateTime date) {
        this.date = date;
    }
    
    
    public double daysUntil(ZonedDateTime when) {
        double h = this.date.until(when, ChronoUnit.MILLIS);
        return h/(1000*60*60*24);
    }
    
    
    public double julianCenturiesUntil(ZonedDateTime when) {      
        double h = this.date.until(when, ChronoUnit.HOURS);
        return h/24/36525;
    }
}

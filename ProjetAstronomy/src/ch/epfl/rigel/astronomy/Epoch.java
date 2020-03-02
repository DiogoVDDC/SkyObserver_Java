
package ch.epfl.rigel.astronomy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Enumeration of the two reference epoch J2000 & J2010.
 * @author Theo Houle (312432) 
 *
 */
public enum Epoch {
    
    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1), LocalTime.of(12, 0),
            ZoneOffset.UTC)),
    J2010(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1), LocalTime.of(0, 0),
            ZoneOffset.UTC));
    
    // The precise date of the two enums.
    private ZonedDateTime date;
    private Epoch(ZonedDateTime date) {
        this.date = date;
    }
    
    /**
     * Compute the amount of days separating this and the given date.
     * @param when: the date to compare with.
     * @return: the amount of days separating the given date and this.
     */
    public double daysUntil(ZonedDateTime when) {
        // To avoid loosing precision, we use milliseconds instead of hours and then convert back to hours.
        double h = this.date.until(when, ChronoUnit.MILLIS);
        return h/(double)(1000*60*60*24);
        
    }
    
    /**
     * Compute the amount of Julian centuries between this and the given date.
     * @param when: The date to compare with.
     * @return: the amount of julian centuries separating the two dates.
     */
    public double julianCenturiesUntil(ZonedDateTime when) {
        return daysUntil(when)/36525.0;
    }
}

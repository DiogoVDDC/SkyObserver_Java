
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import ch.epfl.rigel.astronomy.Epoch;



public class Main {
    public static void main(String[] args) {
        
        ZonedDateTime d = ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 3),
                LocalTime.of(18, 0),
                ZoneOffset.UTC);
            System.out.println(Epoch.J2000.daysUntil(d));
    }
     

//        Interval interval = RightOpenInterval.of(-5, -2);
//        Preconditions.checkInInterval(interval, -3);
//        System.out.println(interval);
//        
//        GeographicCoordinates g = GeographicCoordinates.ofDeg(45, 45);
//        System.out.println(g);
//        System.out.println(GeographicCoordinates.ofDeg(6.57, 46.52));
//        
//
//        System.out.println(EclipticCoordinates.of(Math.toRadians(350), Math.toRadians(7.2)));
//        Polynomial a = Polynomial.of(1, 4, -1, 2);
//        System.out.println(a.at(2));
//        System.out.println(a);
//        
//        
//        Interval interval = RightOpenInterval.of(-5, -2);
//        Preconditions.checkInInterval(interval, -3);
//        System.out.println(interval);
            


}

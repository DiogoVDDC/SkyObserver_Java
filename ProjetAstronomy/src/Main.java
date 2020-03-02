
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;



public class Main {
    public static void main(String[] args) {
        
        ZonedDateTime d = ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, 670000000),
                ZoneOffset.UTC);
        
           
        System.out.println(Angle.toHr(SiderealTime.greenwich(d)));
        
        ZonedDateTime d1 = ZonedDateTime.of(
                LocalDate.of(2100, Month.JANUARY, 1),
                LocalTime.of(12,00),
                ZoneOffset.UTC);
       // System.out.println(Epoch.J2000.julianCenturiesUntil(d1));
    }
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
            




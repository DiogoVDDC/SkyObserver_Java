
import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;
import ch.epfl.rigel.coordinates.EclipticCoordinates;


public class Main {
    public static void main(String[] args) {

        
        Polynomial a = Polynomial.of(1, 4, -1, 2);
        System.out.println(a.at(2));
        System.out.println(a);
        
        
        Interval interval = RightOpenInterval.of(-5, -2);
        Preconditions.checkInInterval(interval, -3);
        System.out.println(interval);
        
        GeographicCoordinates g = GeographicCoordinates.ofDeg(45, 45);
        System.out.println(g);
        System.out.println(GeographicCoordinates.ofDeg(6.57, 46.52));
        

        System.out.println(EclipticCoordinates.of(Math.toRadians(350), Math.toRadians(7.2)));
//        Polynomial a = Polynomial.of(1, 4, -1, 2);
//        System.out.println(a.at(2));
//        System.out.println(a);
//        
//        
//        Interval interval = RightOpenInterval.of(-5, -2);
//        Preconditions.checkInInterval(interval, -3);
//        System.out.println(interval);

    }
}

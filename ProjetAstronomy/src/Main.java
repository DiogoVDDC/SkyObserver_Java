import ch.epfl.rigel.coordinates.EclipticCoordinates;

public class Main {
    public static void main(String[] args) {
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

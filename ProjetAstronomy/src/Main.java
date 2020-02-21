import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

public class Main {
    public static void main(String[] args) {
        Polynomial a = Polynomial.of(1, 2, 2);
        System.out.println(a.at(2));
        System.out.println(a);
        
        
        Interval interval = RightOpenInterval.of(-5, -2);
        System.out.println(interval);
    }
}

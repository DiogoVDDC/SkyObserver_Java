import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

public class Main {
    public static void main(String[] args) {
       System.out.println(Angle.ofArcsec(1));
       System.out.println(Angle.ofDMS(85, 18, 30));
    }
}

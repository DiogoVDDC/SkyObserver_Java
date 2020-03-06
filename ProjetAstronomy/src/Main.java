import ch.epfl.rigel.astronomy.Moon;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public class Main {
    public static void main(String[] args) {
        Moon lu = new Moon(EquatorialCoordinates.of(1, 1), 10, 10, 0.3752f);
        System.out.println(lu);
    }
}
       




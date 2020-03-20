import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public class Main {
    public static void main(String[] args) {
        Star s = new Star(123, "Diogo", EquatorialCoordinates.of(0, 0), 1f , -0.03f);
        System.out.println(s.colorTemperature());
    }
}
       




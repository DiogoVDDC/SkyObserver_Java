package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public class StarTest {
    
    @Test
    void starColorTest() {
    
    Star r = new Star(24436, "Rigel", EquatorialCoordinates.of(0, 0), 0, -0.03f);
    
    assertEquals(10515, r.colorTemperature());


    Star b = new Star(27989, "Betelgeuse", EquatorialCoordinates.of(0, 0), 0, 1.50f);
    
    assertEquals(3793, b.colorTemperature());
    }
}

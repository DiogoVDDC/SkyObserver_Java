package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

class MoonTest1 {

    @Test
    void legalArgTest() {
        Moon moon = new Moon(EquatorialCoordinates.of(2, 1), 2, 10, (float).5);
        assertEquals(2, moon.angularSize());
        assertEquals(10, moon.magnitude());
        assertEquals("Lune", moon.name());
        assertEquals(2, moon.equatorialPos().ra());
        assertEquals(1, moon.equatorialPos().dec());
        assertEquals("Lune (50.0%)", moon.info());
    }
    
    @Test
    void illegalArgTest() {
        assertThrows(IllegalArgumentException.class, () ->{
            new Moon(EquatorialCoordinates.of(2, 1), 2, 10, (float)1.1);
        });
    }

}

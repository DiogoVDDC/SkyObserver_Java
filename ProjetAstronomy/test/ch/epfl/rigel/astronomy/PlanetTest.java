package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

class PlanetTest {

    @Test
    void legalValuesTest() {
        Planet planet = new Planet("cul", EquatorialCoordinates.of(2, 1), 2, 10);
        assertEquals("cul", planet.name());
        assertEquals(2, planet.angularSize());
        assertEquals(10, planet.magnitude());
        assertEquals(2, planet.equatorialPos().ra());
        assertEquals(1, planet.equatorialPos().dec());
        assertEquals("cul", planet.info());
    }
    @Test 
    void throwsOnIllegalValues() {
        assertThrows(IllegalArgumentException.class, () ->{
           new Planet("poulet", EquatorialCoordinates.of(1, 1), -2, 10); 
        });
        assertThrows(NullPointerException.class, () ->{
            new Planet("cul", null, -2, 10); 
         });
        assertThrows(NullPointerException.class, () ->{
            new Planet(null, EquatorialCoordinates.of(1, 1), -2, 10); 
         });
    }
    
    
    
}

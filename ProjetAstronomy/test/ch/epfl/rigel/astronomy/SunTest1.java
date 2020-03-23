package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

class SunTest1 {

    @Test
    void legalArgTest() {
        Sun sun = new Sun(EclipticCoordinates.of(2, .5), EquatorialCoordinates.of(2, 1), 2, 20);
        assertEquals(2, sun.angularSize());
        assertEquals(20, sun.meanAnomaly());
        assertEquals(2, sun.eclipticPos().lon());
        assertEquals(.5, sun.eclipticPos().lat());
        assertEquals(2, sun.equatorialPos().ra());
        assertEquals(1, sun.equatorialPos().dec());
        assertEquals("Soleil", sun.name());
        assertEquals((float)-26.7, (float)sun.magnitude());
    }
    
    @Test
    void throwsWithNullCoord() {
        assertThrows(NullPointerException.class, () ->{
            new Sun(null, EquatorialCoordinates.of(2,1), 2, 20);
        });
    }

}

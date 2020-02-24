package ch.epfl.coordinates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

public class EquatorialCoordinatesTest {
    
    @Test
    void instantiationWithLegalArg() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(4, 1);
        assertEquals(4, coord.ra());
        assertEquals(Angle.toDeg(4), coord.raDeg());
        assertEquals(Angle.toHr(4), coord.raHr());
        assertEquals(1, coord.dec());
        assertEquals(Angle.toDeg(1), coord.decDeg());
        
        EquatorialCoordinates coord2 = EquatorialCoordinates.of(0, Math.PI/2);
        assertEquals(0, coord2.ra());
        assertEquals(0, coord2.raDeg());
        assertEquals(0, coord2.raHr());
        assertEquals(Math.PI/2, coord2.dec());
        assertEquals(90, coord2.decDeg());
        
        EquatorialCoordinates coord3 = EquatorialCoordinates.of(0, -Math.PI/2);
        assertEquals(0, coord3.ra());
        assertEquals(0, coord3.raDeg());
        assertEquals(0, coord3.raHr());
        assertEquals(-Math.PI/2, coord3.dec());
        assertEquals(-90, coord3.decDeg());
    }
    
    @Test
    void instantiationWithIllegalArg() {
        assertThrows(IllegalArgumentException.class, () ->{
            EquatorialCoordinates.of(Math.PI*2, 0);
        });
        assertThrows(IllegalArgumentException.class, () ->{
            EquatorialCoordinates.of(1, Math.PI*2);
        });
        assertThrows(IllegalArgumentException.class, () ->{
            EquatorialCoordinates.of(1, Math.PI + 0.0001);
        });
        assertThrows(IllegalArgumentException.class, () ->{
            EquatorialCoordinates.of(Math.PI*3, 0);
        });
        assertThrows(IllegalArgumentException.class, () ->{
            EquatorialCoordinates.of(0, -Math.PI-0.0001);
        });
    }
}

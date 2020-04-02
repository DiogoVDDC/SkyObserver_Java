package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;

public class EclipticCoordinatesTest {

    @Test
    void instantiationWithLegalArg() {
        EclipticCoordinates coord = assertDoesNotThrow(() -> EclipticCoordinates.of(0, Math.PI/2));
        assertEquals(0, coord.lon());
        assertEquals(0, coord.lonDeg());
        assertEquals(Math.PI/2, coord.lat());
        assertEquals(90, coord.latDeg());
        
        EclipticCoordinates coord2 = assertDoesNotThrow(() ->EclipticCoordinates.of(4, -Math.PI/2));
        assertEquals(4, coord2.lon());
        assertEquals(Angle.toDeg(4), coord2.lonDeg());
        assertEquals(-Math.PI/2, coord2.lat());
        assertEquals(-90, coord2.latDeg());
        
        EclipticCoordinates coord3 = assertDoesNotThrow(() ->EclipticCoordinates.of(Math.PI*2-0.001, Math.PI/4));
        assertEquals(Math.PI*2-0.001, coord3.lon());
        assertEquals(Angle.toDeg(Math.PI*2-0.001), coord3.lonDeg());
        assertEquals(Math.PI/4, coord3.lat());
        assertEquals(45, coord3.latDeg());
    }
    
    @Test
    void instantiationWithIllegalArg() { 
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(0, Math.PI);
        });
        
        assertThrows(IllegalArgumentException.class, () ->{
            EclipticCoordinates.of(Math.PI*2, 0);
        });
        
        assertThrows(IllegalArgumentException.class, () ->{
            EclipticCoordinates.of(-1, 0);
        });

        assertThrows(IllegalArgumentException.class, () ->{
            EclipticCoordinates.of(0, -Math.PI/2-0.00001);
        });
               
        assertDoesNotThrow(() -> EclipticCoordinates.of(0, 1));
        assertDoesNotThrow(() -> EclipticCoordinates.of(0, -Math.PI/2));
       
    

        assertDoesNotThrow(() -> EclipticCoordinates.of(0, -Math.PI/2));
        assertDoesNotThrow(() -> EclipticCoordinates.of(0, Math.PI/2));
    }

}



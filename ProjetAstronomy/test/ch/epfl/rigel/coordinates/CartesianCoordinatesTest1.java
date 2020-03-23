package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CartesianCoordinatesTest1 {

    @Test
    void instantiationTest() {
        CartesianCoordinates coord = CartesianCoordinates.of(10, 20);
        assertEquals(10, coord.x());
        assertEquals(20, coord.y());
        
        CartesianCoordinates coord2 = CartesianCoordinates.of(4.6578, -0.20);
        assertEquals(4.6578, coord2.x());
        assertEquals(-0.20, coord2.y());
    }
    
    @Test 
    void hashEqualThrowTest() {
        CartesianCoordinates coord = CartesianCoordinates.of(10, 20);
        CartesianCoordinates coord2 = CartesianCoordinates.of(4.6578, -0.20);
        assertThrows(UnsupportedOperationException.class, () ->{
            coord.equals(coord2);
        });
        
        assertThrows(UnsupportedOperationException.class, () ->{
            coord.hashCode();
        });
    }
    
    @Test 
    void toStringTest() {
        CartesianCoordinates coord = CartesianCoordinates.of(4.6578, -0.20);
        assertEquals("(x=4.6578, y=-0.2000)", coord.toString());
    }

}

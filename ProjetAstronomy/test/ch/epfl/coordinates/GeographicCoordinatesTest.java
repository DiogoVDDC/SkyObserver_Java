package ch.epfl.coordinates;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;

public class GeographicCoordinatesTest {

    @Test
    void withLegalArgumentsTests() {
        GeographicCoordinates coord = GeographicCoordinates.ofDeg(150, 50);
        assertEquals(150, coord.lonDeg());
        assertEquals(50, coord.latDeg());
        
        assertEquals(Angle.ofDeg(150), coord.lon());
        assertEquals(Angle.ofDeg(50), coord.lat());
        
        
        GeographicCoordinates coord_2 = GeographicCoordinates.ofDeg(179, 50);
        assertEquals(179, coord_2.lonDeg());
        assertEquals(50, coord_2.latDeg());
        
        GeographicCoordinates coord_3 = GeographicCoordinates.ofDeg(150, 90);
        assertEquals(150, coord_3.lonDeg());
        assertEquals(90, coord_3.latDeg());
        
        GeographicCoordinates coord_4 = GeographicCoordinates.ofDeg(150, -90);
        assertEquals(150, coord_4.lonDeg());
        assertEquals(-90, coord_4.latDeg());
    }
        
    @Test
    void withIllegalArgumentsTests() {
        assertThrows(IllegalArgumentException.class, () ->{
            GeographicCoordinates.ofDeg(180, 50); 
        });
        assertThrows(IllegalArgumentException.class, () ->{
            GeographicCoordinates.ofDeg(150, 91);
        });
        assertThrows(IllegalArgumentException.class, () ->{
            GeographicCoordinates.ofDeg(150, -91);
        });
        
        assertThrows(IllegalArgumentException.class, () ->{
            GeographicCoordinates.ofDeg(400, 120); 
        });
    }
    
    @Test
    void isValidLonDegTests() {
        assertTrue(GeographicCoordinates.isValidLonDeg(-160));
        assertTrue(GeographicCoordinates.isValidLonDeg(-34.5478));
        assertTrue(GeographicCoordinates.isValidLonDeg(-10));
        assertTrue(GeographicCoordinates.isValidLonDeg(160));
        assertTrue(GeographicCoordinates.isValidLonDeg(113.5739));
        assertTrue(GeographicCoordinates.isValidLonDeg(0));
        assertTrue(GeographicCoordinates.isValidLonDeg(179));
        assertTrue(GeographicCoordinates.isValidLonDeg(-180));
        assertTrue(GeographicCoordinates.isValidLonDeg(179.9999));
        
        assertFalse(GeographicCoordinates.isValidLonDeg(-190));
        assertFalse(GeographicCoordinates.isValidLonDeg(-180.001));
        assertFalse(GeographicCoordinates.isValidLonDeg(-200));
        assertFalse(GeographicCoordinates.isValidLonDeg(180));
        assertFalse(GeographicCoordinates.isValidLonDeg(192.3628));
        assertFalse(GeographicCoordinates.isValidLonDeg(200.1234));
    }

    @Test
    void isValidLatDegTests() {
        assertTrue(GeographicCoordinates.isValidLatDeg(-30));
        assertTrue(GeographicCoordinates.isValidLatDeg(30));
        assertTrue(GeographicCoordinates.isValidLatDeg(-21.234324));
        assertTrue(GeographicCoordinates.isValidLatDeg(60.43627));
        assertTrue(GeographicCoordinates.isValidLatDeg(-90));
        assertTrue(GeographicCoordinates.isValidLatDeg(90));
        assertTrue(GeographicCoordinates.isValidLatDeg(0));
        
        assertFalse(GeographicCoordinates.isValidLatDeg(90.0001));
        assertFalse(GeographicCoordinates.isValidLatDeg(100));
        assertFalse(GeographicCoordinates.isValidLatDeg(-100));
        assertFalse(GeographicCoordinates.isValidLatDeg(-90.0001));
    }
}

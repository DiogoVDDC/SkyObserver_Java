package ch.epfl.coordinates;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.Angle;

public class HorizontalCoordinatesTest {
   

    @Test
    void isValidAngularDistanceTest() {
        HorizontalCoordinates h1 = HorizontalCoordinates.ofDeg(6.5682, 46.5183);
        HorizontalCoordinates h2 = HorizontalCoordinates.ofDeg(8.5476, 47.3763);        
        assertEquals(0.0279, h1.angularDistanceTo(h2), 1e-3);
        
        HorizontalCoordinates h3 = HorizontalCoordinates.of(Math.PI/6, Math.PI/2);
        HorizontalCoordinates h4 = HorizontalCoordinates.of(Math.PI, Math.PI/8);        
        assertEquals(1.1780, h3.angularDistanceTo(h4), 1e-3);       
    }
    
    @Test
    void HorizontalIllegalArgumentsTests(){        
        
        assertDoesNotThrow(() -> HorizontalCoordinates.of(0, -Math.PI/2));
        
        assertDoesNotThrow(() -> HorizontalCoordinates.of(0, Math.PI/2));   
        
        
        assertThrows(IllegalArgumentException.class, () ->{
            HorizontalCoordinates.of(2*Math.PI, 0);
        });
        
        assertThrows(IllegalArgumentException.class, () ->{
            HorizontalCoordinates.of(-Math.PI, 0);
        });
        
        assertThrows(IllegalArgumentException.class, () ->{
            HorizontalCoordinates.of(0, 2*Math.PI);
        });
        
        assertThrows(IllegalArgumentException.class, () ->{
            HorizontalCoordinates.of(0, 2*Math.PI); 
        });
        
        assertDoesNotThrow(() -> HorizontalCoordinates.ofDeg(0, -90));
        
        assertDoesNotThrow(() -> HorizontalCoordinates.ofDeg(0, 90)); 
        
        assertThrows(IllegalArgumentException.class, () ->{
            HorizontalCoordinates.ofDeg(360, 0);
        });
        
        assertThrows(IllegalArgumentException.class, () ->{
            HorizontalCoordinates.ofDeg(-180, 0);
        });
        
        assertThrows(IllegalArgumentException.class, () ->{
            HorizontalCoordinates.ofDeg(0, 360);
        });
        
        assertThrows(IllegalArgumentException.class, () ->{
            HorizontalCoordinates.of(0, 360); 
        });
    }
    
    @Test
    void withLegalArgumentsTests() {
        HorizontalCoordinates coord = HorizontalCoordinates.ofDeg(150, 50);
        assertEquals(150, coord.azDeg());
        assertEquals(50, coord.altDeg());
        
        assertEquals(Angle.ofDeg(150), coord.az());
        assertEquals(Angle.ofDeg(50), coord.alt());
        
        
        HorizontalCoordinates coord_2 = HorizontalCoordinates.ofDeg(179, 50);
        assertEquals(179, coord_2.azDeg());
        assertEquals(50, coord_2.altDeg());
        
        HorizontalCoordinates coord_3 = HorizontalCoordinates.ofDeg(150, 90);
        assertEquals(150, coord_3.azDeg());
        assertEquals(90, coord_3.altDeg());
        
        HorizontalCoordinates coord_4 = HorizontalCoordinates.ofDeg(150, -90);
        assertEquals(150, coord_4.azDeg());
        assertEquals(-90, coord_4.altDeg());
        
        
        HorizontalCoordinates coord_5 = HorizontalCoordinates.of(Angle.ofDeg(150), Angle.ofDeg(50));
        assertEquals(150, coord_5.azDeg());
        assertEquals(50, coord_5.altDeg());
        
        assertEquals(Angle.ofDeg(150), coord_5.az());
        assertEquals(Angle.ofDeg(50), coord_5.alt());
        
        
        HorizontalCoordinates coord_6 = HorizontalCoordinates.of(Angle.ofDeg(179), Angle.ofDeg(50));
        assertEquals(179, coord_6.azDeg());
        assertEquals(50, coord_6.altDeg());
        
        HorizontalCoordinates coord_7 = HorizontalCoordinates.of(Angle.ofDeg(150), Angle.ofDeg(90));
        assertEquals(150, coord_7.azDeg());
        assertEquals(90, coord_7.altDeg());
        
        HorizontalCoordinates coord_8 = HorizontalCoordinates.of(Angle.ofDeg(150), Angle.ofDeg(-90));
        assertEquals(150, coord_8.azDeg());
        assertEquals(-90, coord_8.altDeg());
    }
    
    @Test
    void validArgumentworksOctantName() {       
        assertEquals("NO", HorizontalCoordinates.ofDeg(335, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("NE", HorizontalCoordinates.ofDeg(45, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("N", HorizontalCoordinates.ofDeg(355, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("E", HorizontalCoordinates.ofDeg(95, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("SE", HorizontalCoordinates.ofDeg(112.5, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("S", HorizontalCoordinates.ofDeg(170, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("S", HorizontalCoordinates.ofDeg(170, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("S", HorizontalCoordinates.ofDeg(195, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("SO", HorizontalCoordinates.ofDeg(245, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("SO", HorizontalCoordinates.ofDeg(210, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("O", HorizontalCoordinates.ofDeg(270, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("NO", HorizontalCoordinates.ofDeg(300, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("NO", HorizontalCoordinates.ofDeg(292.76, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("NO", HorizontalCoordinates.ofDeg(292.76, 0)
                .azOctantName("N", "E", "S", "O"));     
                
        
        assertEquals("N", HorizontalCoordinates.ofDeg(337.5, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("NE", HorizontalCoordinates.ofDeg(22.5, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("E", HorizontalCoordinates.ofDeg(67.5, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("SE", HorizontalCoordinates.ofDeg(90 + 22.5, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("S", HorizontalCoordinates.ofDeg(135 + 22.5, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("SO", HorizontalCoordinates.ofDeg(180 + 22.5, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("O", HorizontalCoordinates.ofDeg(225 + 22.5, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("NO", HorizontalCoordinates.ofDeg(270 + 22.5, 0)
                .azOctantName("N", "E", "S", "O"));
        
        assertEquals("N", HorizontalCoordinates.ofDeg(315 + 22.5, 0)
                .azOctantName("N", "E", "S", "O"));
        
            
    }
}

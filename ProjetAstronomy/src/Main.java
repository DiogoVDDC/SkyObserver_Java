
import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;


public class Main {
    public static void main(String[] args) {
        EclipticCoordinates e = EclipticCoordinates.of(Math.PI/2, Math.PI/2);
        System.out.println(e);
        
        EquatorialCoordinates e1 = EquatorialCoordinates.of(Math.PI/2, Math.PI/2);
        System.out.println(e1);
        
        GeographicCoordinates e2 = GeographicCoordinates.ofDeg(90, 80);
        System.out.println(e2);
        
        HorizontalCoordinates e3 = HorizontalCoordinates.of(Math.PI/2, Math.PI/2);
        System.out.println(e3);
        
    
      
    }
}

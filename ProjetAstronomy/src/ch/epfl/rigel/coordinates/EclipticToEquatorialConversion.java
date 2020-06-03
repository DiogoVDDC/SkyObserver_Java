package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

/**
 * Allows the convertion from ecliptic to equatorial coordinates
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 * @author Theo Houle (312432)
 *
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates>{

	//Elliptic obliquity.
    private final double eclipticObliquity;
    //Cosine of the elliptic obliquity
    private final double cosOfEclipticObliquity;
    //Sine of the eccliptic obliquity.
    private final double sinOfEclipticObliquity;
    
    /**
     * Initialises the constant used to reduce calculation when conversion of coordinates
     * @param when: instance at which the conversion occur
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        // calculates ecliptic obliquity using polynimial approximation
        eclipticObliquity = Polynomial.of(Angle.ofArcsec(0.00181), -Angle.ofArcsec(0.0006)
                , -Angle.ofArcsec(46.815), Angle.ofDMS(23, 26, 21.45)).at(Epoch.J2000.julianCenturiesUntil(when));
   
        cosOfEclipticObliquity = Math.cos(eclipticObliquity);
        sinOfEclipticObliquity = Math.sin(eclipticObliquity);
    }
    
    /**
     * Transforms elliptic coordinates to equatorial coordinates
     * @param: ecliptic coordinates to be converted
     * @returns: new equatorial coordinates after conversion
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {       
    	double sinOfLon = Math.sin(ecl.lon());
        double ra = Angle.normalizePositive(Math.atan2((sinOfLon*cosOfEclipticObliquity 
                - Math.tan(ecl.lat())*sinOfEclipticObliquity), Math.cos(ecl.lon())));
        
        double dec = Math.asin(Math.sin(ecl.lat())*cosOfEclipticObliquity 
                + Math.cos(ecl.lat())*sinOfEclipticObliquity*sinOfLon);
        return  EquatorialCoordinates.of(ra, dec);
    }
    
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }
}

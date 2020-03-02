package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

/**
 * Allows the convertion from ecliptic to equatorial coordinates
 * @author Diogo Valdivieso Damasio Da Costa (311673) 
 */

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates>{

    private final double eclipticObliquity;
    private final double cosOfEclipticObliquity;
    private final double sinOfEclipticObliquity;
    
    @Override
    /**
     * 
     * @param: ecliptic coordinates to be converted
     * @returns: new equatorial coordinates after conversion
     */
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {          
        double ra = Math.atan2((Math.sin(ecl.lat())*cosOfEclipticObliquity 
                - Math.tan(ecl.lon())*sinOfEclipticObliquity), Math.cos(ecl.lat()));
        
        double dec= Math.asin(Math.sin(ecl.lat())*cosOfEclipticObliquity 
                + Math.cos(ecl.lat())*sinOfEclipticObliquity*Math.sin(ecl.lon()));
        return  EquatorialCoordinates.of(ra, dec);
    }
    
    /**
     * Initialises the constant used to reduce calculation when conversion of coordinates
     * @param when: instance at which the conversion occur
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        // calculates ecliptic obliquity using polynimial approximation
        eclipticObliquity = Polynomial.of(Angle.ofArcsec(0.00181), Angle.ofArcsec(0.0006)
                , Angle.ofArcsec(46.815), Angle.ofDMS(23, 26, 21.45)).at(Epoch.J2000.julianCenturiesUntil(when));
        
        cosOfEclipticObliquity = Math.cos(eclipticObliquity);
        sinOfEclipticObliquity = Math.sin(eclipticObliquity);
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

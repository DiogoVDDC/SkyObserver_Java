package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates>{

    private double eclipticObliquity;
    private double cosOfEclipticObliquity;
    private double sinOfEclipticObliquity;
    @Override
    // retourne equatorial
    //ra: (right ascension) correspond to longitude (given in radians) 
    // dec: (declination) correspond to latitude (given in radians)
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {
        //right sa
        double ra = Math.atan2((Math.sin(ecl.lat())*cosOfEclipticObliquity - Math.tan(ecl.lon())*sinOfEclipticObliquity), Math.cos(ecl.lat()));
        double dec= Math.asin(Math.sin(ecl.lat())*cosOfEclipticObliquity + Math.cos(ecl.lat())*sinOfEclipticObliquity*Math.sin(ecl.lon()));
        return  EquatorialCoordinates.of(ra, dec);
    }
    
    
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        
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

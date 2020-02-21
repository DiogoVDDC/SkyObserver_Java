package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

public final class GeographicCoordinates extends SphericalCoordinates{

      private static final RightOpenInterval lonInterval = RightOpenInterval.symmetric(360);
      private static final ClosedInterval latInterval = ClosedInterval.symmetric(180);
//    GeographicCoordinates ofDeg(double lonDeg, double latDeg),
//    qui retourne les coordonnées géographiques dont la longitude en degrés est égale à lonDeg et la latitude en degrés est égale à latDeg,
//    ou lève IllegalArgumentException si l'une ou l'autre de ces composantes est invalide (voir §2.2).
//   
//
//    boolean isValidLonDeg(double lonDeg), qui retourne vrai ssi l'angle qui lui est passé représente une longitude valide en degrés,
//    boolean isValidLatDeg(double latDeg), qui retourne vrai ssi l'angle qui lui est passé représente une latitude valide en degrés.
//    
//
//    double lon(), qui retourne la longitude,
//    double lonDeg(), qui retourne la longitude en degrés,
//    double lat(), qui retourne la latitude,
//    double latDeg(), qui retourne la latitude en degrés.
    
    
    private GeographicCoordinates(double lat, double lon) {
        super(lat, lon);        
    }
    
    //CHECK IF HAS TO BE PUBLIC
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        return new GeographicCoordinates(Angle.ofDeg(Preconditions.checkInInterval(lonInterval, lonDeg)), Angle.ofDeg(Preconditions.checkInInterval(latInterval, latDeg)));
    }
}

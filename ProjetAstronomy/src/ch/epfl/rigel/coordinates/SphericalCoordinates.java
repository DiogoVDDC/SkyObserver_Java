package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

abstract class SphericalCoordinates {
    private double lat;
    private double lon;
    
    SphericalCoordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
    
      double lon() {
          return lon;
      }
      double lonDeg() {
          return Angle.toDeg(lon);
      }
      
      double lat() {
          return lat;
      }
      
      double latDeg() {
          return Angle.toDeg(lat);
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


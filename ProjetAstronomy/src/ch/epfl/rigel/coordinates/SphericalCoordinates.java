package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

abstract class SphericalCoordinates {
    private double latitude;
    private double longitude;
    
    SphericalCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
      double lon() {
          return longitude;
      }
      double lonDeg() {
          return Angle.toDeg(longitude);
      }
      
      double lat() {
          return latitude;
      }
      
      double latDeg() {
          return Angle.toDeg(latitude);
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


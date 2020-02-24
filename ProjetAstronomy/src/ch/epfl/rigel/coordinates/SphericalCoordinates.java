package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
abstract class SphericalCoordinates {
    
    private double lat;
    private double lon;

    /**
     * @param: latitude in radians
     * @param: longitude in radians
     */
    SphericalCoordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * @return: returns longitude in radians
     */
    double lon() {
        return lon;
    }

    /**
     * @return: returns longitude in degrees
     */
    double lonDeg() {
        return Angle.toDeg(lon);
    }

    /**
     * @return: return latitude in radians
     */
    double lat() {
        return lat;
    }

    /**
     * @return: return latitude in degrees
     */
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

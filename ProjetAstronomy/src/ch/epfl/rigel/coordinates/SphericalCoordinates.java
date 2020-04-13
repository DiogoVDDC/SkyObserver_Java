package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Abstract representation of spherical coordinates
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 * @author Theo Houle (312432)
 *
 */
abstract class SphericalCoordinates {
    
	//Lattitude of the position.
    private double lat;
    //Longitude of the position.
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
     * Getter for the longitude in radians.
     * @return: returns longitude in radians.
     */
    double lon() {
        return lon;
    }

    /**
     * Getter for the longitude in degrees.
     * @return: returns longitude in degrees.
     */
    double lonDeg() {
        return Angle.toDeg(lon);
    }

    /**
     * Getter for the latitude in radians.
     * @return: return latitude in radians.
     */
    double lat() {
        return lat;
    }

    /**
     * Getter for latitude in degrees
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

package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * Representation of cartesian coordinates
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public final class CartesianCoordinates {
    
    private double x;
    private double y;

    private CartesianCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Auxiliary constructor to create an cartesian coordinate.
     * @param x: x coordinates.
     * @param y: y coordinates
     */
    public static CartesianCoordinates of(double x, double y) {
        return new CartesianCoordinates(x, y);
    }    
    
    public double x() {
        return x;
    }
    
    public double  y() {
        return y;
    }
    
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * String representation of cartesian coordinates
     */
    @Override
    public String toString() {
       return String.format(Locale.ROOT, "(x=%.4f, y=%.4f)", x, y);
    }
    
    
}

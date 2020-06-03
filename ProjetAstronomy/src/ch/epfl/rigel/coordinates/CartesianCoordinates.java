package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * Representation of Cartesian coordinates
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 * @author Theo Houle (312432)
 */
public final class CartesianCoordinates {
    
	//X coordinate of the position.
    private final double x;
    //Y coordinate of the position.
    private final double y;

    /**
     * Constructor for cartesian coordinates.
     * @param x: x coordinate.
     * @param y: y coorinate.
     */
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
    
    /**
     * Getter for the x coordinate.
     * @return: returns x coordinates
     */
    public double x() {
        return x;
    }
    
    /**
     * Getter for the y coordinate.
     * @return: returns y coordinates
     */
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
   
    @Override
    public String toString() {
       return String.format(Locale.ROOT, "(x=%.4f, y=%.4f)", x, y);
    }
    
    
}

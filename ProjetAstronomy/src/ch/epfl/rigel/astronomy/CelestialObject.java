package ch.epfl.rigel.astronomy;

import java.util.Objects;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * abstract class representing a celestial object.
 * @author Theo Houle (312432)
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 *
 */
public abstract class CelestialObject {
    //name of the object.
    private final String name;
    //equatorial position of the object.
    private final EquatorialCoordinates equatorialPos;
    //magnitude of the object(float because takes less space in memory).
    private final float magnitude;
    //Angular size of the object(float because takes less space in memory).
    private final float angularSize;
    
    /**
     * Constructor for celestial object.
     * @param name: name of the object.
     * @param equatorialPos: equatorial position of the object.
     * @param angularSize: angular size of the object.
     * @param magnitude: magnitude of the object.
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude){
        //Insures that the position and the name aren't null.
    	this.name = Objects.requireNonNull(name);
    	this.equatorialPos = Objects.requireNonNull(equatorialPos);
        this.angularSize = angularSize;
        //Insure that the angular size isn't negative.
        Preconditions.checkArgument(angularSize >= 0);
        this.magnitude = magnitude;
    }
    
    /**
     * Getter for the name 
     * @return: name of the object.
     */
    public String name() {
        return name;
    }
    /**
     * Getter for the angular size.
     * @return : angular size in double.
     */
    public double angularSize() {
        return angularSize;
    }
    /**
     * Getter for the magnitude.
     * @return: the magnitude of the object.
     */
    public double magnitude() {
        return magnitude;
    }
    /**
     * Getter for the equatorial position.
     * @return: equatorial position of the object.
     */
    public EquatorialCoordinates equatorialPos() {
        return equatorialPos;
    }
    /**
     * Allows to get an informative text about the object.
     * @return: the text to print.
     */
    public String info() {
        return name;
    }
    
    @Override
    public String toString() {
        return info();
    }
}

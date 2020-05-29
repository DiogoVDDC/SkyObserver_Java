 package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * Representation of different planet
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 * @author Theo Houle (312432)
 *
 */
public final class Star extends CelestialObject{
    
	//Hipparcos ID of the star.
    private int hipparcosId;
    //Colour temperature of the star.
    private final int colorTemperature;
    /**
     * Constructor for a Star
     * @param hipparcosId: unique id to identify a star
     * @param name: name to identify a star
     * @param equatorialPos: the position of the star in equatorial coordinates
     * @param magnitude: magnitude of the star
     * @param colorIndex: color of the star
     * @throws IllegalArgumentException if the hipparcosId is less than 0 or
     * if the colorIndex isn't in the interval [-0.5, 5.5].
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude , float colorIndex) {
        super(name, equatorialPos, 0, magnitude); 
        //Check that the hipparcosID is positive.
        Preconditions.checkArgument(hipparcosId >= 0);
        //Check if the colorIndex is in the right interval.
        Preconditions.checkInInterval(ClosedInterval.of(-0.5, 5.5), colorIndex);
        this.colorTemperature = (int)Math.floor(4600*(1/(0.92*colorIndex + 1.7) + 1/(0.92*colorIndex + 0.62)));
        this.hipparcosId = hipparcosId;
    }
    
    /**
     * Getter for hipparcosId
     * @return hipparcosId: returns the unique id associated to the star
     */
    public int hipparcosId() {
        return hipparcosId;
    }
    
    /**
     * Getter for colortemperature
     * @return colortemperature: returns the temperature in kelvins rounded to 
     */
    public int colorTemperature() {
        return colorTemperature;
    }

}

package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * Representation of different planet
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public final class Star extends CelestialObject{
    
    private int hipparcosId;
    private float colorIndex;
    
    /**
     * Constructor for a Star
     * @param hipparcosId: unique id to identify a star
     * @param name: name to identify a star
     * @param equatorialPos: the position of the star in equatorial coordinates
     * @param magnitude: magnitude of the star
     * @param colorIndex: color of the star 
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude , float colorIndex) {
        super(name, equatorialPos, 0, magnitude);     
        this.colorIndex = colorIndex;
        this.hipparcosId = hipparcosId;
    }
    
    /**
     * @return hipparcosId: returns the unique id associated to the star
     */
    public int hipparcosId() {
        return (int) Preconditions.checkInInterval(ClosedInterval.of(-0.5, 0.5), hipparcosId);
    }
    
    /**
     * @return colortemperature: returns the temperature in kelvins rounded to 
     */
    public int colorTemperature() {
        return (int)Math.floor(4600*(1/(0.92*colorIndex + 1.7) + 1/(0.92*colorIndex + 0.62)));
    }

}

package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
//representation of star
public final class Star extends CelestialObject{
    
    private int hipparcosId;
    private int colorIndex;
    
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude , float colorIndex) {
        super(name, equatorialPos, 0, magnitude);        
    }
    
    public int hipparcosId() {
        return (int) Preconditions.checkInInterval(ClosedInterval.of(-0.5, 0.5), hipparcosId);
    }
    
    public int colorTemperature() {
        return (int) Math.floor(4600*(1/(0.92*colorIndex + 1.7) + 1/(0.92*colorIndex + 0.62)));
    }

}

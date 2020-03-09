package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public final class Star extends CelestialObject{
    
    private int hipparcosId;
    private int colorIndex;
    
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude , float colorIndex) {
        super(name, equatorialPos, 0, magnitude);        
    }
    
    public int hipparcosId() {
        return hipparcosId;
    }
    
    public float colorTemperature() {
        return colorIndex;
    }

}

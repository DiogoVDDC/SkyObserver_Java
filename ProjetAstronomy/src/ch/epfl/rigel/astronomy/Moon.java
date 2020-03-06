package ch.epfl.rigel.astronomy;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * Class representing the moon.
 * @author Theo Houle  $(312432)
 *
 */
public class Moon extends CelestialObject{

    //Interval in which the phase must be contained.
    private final static ClosedInterval phaseInterval = ClosedInterval.of(0, 1);
    //Phase of the moon.
    private final float phase;
    
    /**
     * Constructor for the moon.
     * @param equatorialPos: equtorial position of the moon.
     * @param angularSize: angular size of the moon.
     * @param magnitude: magnitude of the moon.
     * @param phase: phase of the moon.
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize,
            float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        Preconditions.checkInInterval(phaseInterval, phase);
        this.phase = phase;
    }
    @Override
    public String info() {
        return String.format(Locale.ROOT, "Lune (%.1f%%)", phase*100);
    }
}

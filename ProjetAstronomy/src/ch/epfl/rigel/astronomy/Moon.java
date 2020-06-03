package ch.epfl.rigel.astronomy;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * Class representing the moon.
 * @author Theo Houle (312432)
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 *
 */
public final class Moon extends CelestialObject{

    //Interval in which the phase must be contained.
    private final static ClosedInterval PHASE_INTERVAL = ClosedInterval.of(0, 1);
    //Phase of the moon.
    private final float phase;
    
    /**
     * Constructor for the moon.
     * @param equatorialPos: equtorial position of the moon.
     * @param angularSize: angular size of the moon.
     * @param magnitude: magnitude of the moon.
     * @param phase: phase of the moon.
     * @throws IllegalArgumentException if  the phase isn't in the interval [0,1].
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize,
            float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        this.phase = (float) Preconditions.checkInInterval(PHASE_INTERVAL, phase);
    }
    
    @Override
    public String info() {
        return String.format(Locale.ROOT, "%s (%.1f%%)", name(), phase*100);
    }
}

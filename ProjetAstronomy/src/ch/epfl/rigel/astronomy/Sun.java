package ch.epfl.rigel.astronomy;

import java.util.Objects;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Class representing the sun.
 * @author Theo Houle (312432)
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 *
 */
public final class Sun extends CelestialObject{

    // ecliptic position of the sun
    private final EclipticCoordinates eclipticPos;
    //mean anomaly of the sun.
    private final float meanAnomaly;
    
    /**
     * Constructor for the sun
     * @param eclipticPos: ecliptic position of the sun.
     * @param equatorialPos: equatorial position of the sun.
     * @param angularSize: angular size of the sun.
     * @param meanAnomaly: mean anomaly of the sun.
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize, -26.7f);
        Objects.requireNonNull(eclipticPos);
        this.eclipticPos = eclipticPos;
        this.meanAnomaly = meanAnomaly;
    }
    
    /**
     * Getter for the ecliptic position.
     * @return: ecliptic position of the sun.
     */
    public EclipticCoordinates eclipticPos() {
        return eclipticPos;
    }
    
    /**
     * Getter for the mean anomaly.
     * @return: mean anomally of the sun.
     */
    public float meanAnomaly() {
        return meanAnomaly;
    }

}

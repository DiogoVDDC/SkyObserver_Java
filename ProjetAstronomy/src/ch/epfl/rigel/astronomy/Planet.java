package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Representation of a planet
 * @author Theo Houle (312432)
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 *
 */
public final class Planet extends CelestialObject{
    /**
     * Constructor for the planet class
     * @param name: name of the planet.
     * @param equatorialPos: equatorial position of the planet.
     * @param angularSize: angular size of the planet.
     * @param magnitude: magnitude of the planet.
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize,
            float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }

}

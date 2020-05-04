package ch.epfl.rigel.astronomy;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ch.epfl.rigel.Preconditions;

/**
 * Class representating an asterism.
 * @author Theo Houle (312432)
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public final class Asterism {
    
    // list of stars in the asterism
    private final List<Star> asterism;
    
    /**
     * Constructor for an asterism.
     * @param stars: the list of star contained in the asterism.
     */
    public Asterism(List<Star> stars){       
        Objects.requireNonNull(stars);
        Preconditions.checkArgument(!stars.isEmpty());
        asterism = List.copyOf(stars);
    }
    
    /**
     * Getter for the stars of the asterism
     * @return: list of stars.
     */
    public List<Star> stars(){
        return asterism;
    }
}

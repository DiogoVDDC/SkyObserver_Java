package ch.epfl.rigel.astronomy;

import java.util.List;
import java.util.Objects;

import ch.epfl.rigel.Preconditions;

public final class Asterism {
    
    private final List<Star> asterism;
    
    public Asterism(List<Star> stars){
        Objects.requireNonNull(stars);
        Preconditions.checkArgument(!stars.isEmpty());
        
        
    }
}

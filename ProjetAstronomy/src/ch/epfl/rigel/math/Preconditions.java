package ch.epfl.rigel.math;
/**
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */

public final class Preconditions {
    private Preconditions () {}
    
    public static void checkArgument(boolean isTrue) {
        if(!isTrue) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkInInterval(Interval interval, double value) {
        if(interval.contains(value)) {
            throw new IllegalArgumentException();
        }
   }
}

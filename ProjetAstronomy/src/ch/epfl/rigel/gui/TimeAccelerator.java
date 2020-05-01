package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Allow to create a time accelerator.
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
@FunctionalInterface
public interface TimeAccelerator {
    
    /**   
     * @param initialTime: initial simulated time
     * @param realTimeElapsed: the amount of real time elapsed since the start of the animation in nanoseconds
     * @return returns the simulated time after a certain real time elapsed 
     */
    ZonedDateTime adjust(ZonedDateTime initialTime, long realTimeElapsed);
    
    /**
     * @param accelerationFactor: factor at which the simulated time elapses compared to real time
     * @return returns the simulated time after a certain time elapsed
     */
    public static TimeAccelerator continuous(int accelerationFactor) {
        return (initialTime, timeElapsed) -> {           
            return initialTime.plusNanos(accelerationFactor * timeElapsed);            
        };
    }
    
    /**
     * @param stepFrequency: frequency of the number of steps taken per second
     * @param stepLength: the size of the steps taken
     * @return returns the simulated time after a certain time elapsed
     */
    public static TimeAccelerator discrete(int stepFrequency, Duration stepLength) {
        return (initialTime, timeElapsed) -> {
        	//The step frequency is divided by 1e9 because it's initially a frequency per seconds 
        	//and timeElapsed is in nano seconds.
            return initialTime.plusNanos((long) (Math.floor(stepFrequency/1e9 * timeElapsed))
            		* stepLength.toNanos());
        };
    }
}

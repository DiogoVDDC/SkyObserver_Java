package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * Represent the different time accelerators
 * 
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public enum NamedTimeAccelerator {

    // Different time accelerator
    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000x", TimeAccelerator.continuous(3000)),
    DAY("jour", TimeAccelerator.discrete(60, Duration.ofHours(24))),
    SIDEREAL_DAY("jour sidéral",TimeAccelerator.discrete(60, Duration.ofHours(23).plusMinutes(56).plusSeconds(4)));

    private String name;
    private TimeAccelerator timeAccelerator;

    private NamedTimeAccelerator(String name, TimeAccelerator timeAccelerator) {
        this.name = name;
        this.timeAccelerator = timeAccelerator;
    }

    /**
     * Getter for name 
     * @return: returns the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for time accelerator 
     * @return: returns time accelerator
     */
    public TimeAccelerator getAccelerator() {
        return timeAccelerator;
    }

    @Override
    public String toString() {
        return name;
    }
}

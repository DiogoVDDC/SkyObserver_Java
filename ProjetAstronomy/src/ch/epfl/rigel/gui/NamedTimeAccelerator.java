package ch.epfl.rigel.gui;

import java.util.Locale;

import ch.epfl.rigel.math.Angle;

public enum NamedTimeAccelerator {

    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000x", TimeAccelerator.continuous(3000));
    
    private String name;
    private TimeAccelerator timeAccelerator;
    
    
    private NamedTimeAccelerator(String name, TimeAccelerator timeAccelerator) {
        this.name = name;
        this.timeAccelerator = timeAccelerator;
    }
    
    
    public String getName() {
        return this.name;
    }
    
    public TimeAccelerator getAccelerator() {
        return timeAccelerator;
    }
    
    @Override
    public String toString() {
        return name;        
    }
}

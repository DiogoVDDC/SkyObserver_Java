package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;

public final class ObserverLocationBean {
    
    //The longitude property.
    private final DoubleProperty lonDeg;
    //The latitude property.
    private final DoubleProperty latDeg;
    //The center property
    private final ObjectProperty<GeographicCoordinates> coordinates;
    
    private ObservableObjectValue<GeographicCoordinates> observableCoodinatesValue;
    
    
    public ObserverLocationBean() {
        this.lonDeg = new SimpleDoubleProperty();
        this.latDeg = new SimpleDoubleProperty();    
        this.coordinates = new SimpleObjectProperty<GeographicCoordinates>(null);        
        
        observableCoodinatesValue = Bindings.createObjectBinding(
                () -> coordinates.getValue().ofDeg(lonDeg.get(), latDeg.get())
                , coordinates, lonDeg, latDeg);
      
    }
    
    
    
}

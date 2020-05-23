package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableObjectValue;


/**
 * Class representing a viewable observer location properties 
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public final class ObserverLocationBean {    
    //The longitude property.
    private final DoubleProperty lonDeg;
    //The latitude property.
    private final DoubleProperty latDeg;
    //Observable geographic coordinates.
    private final ObservableObjectValue<GeographicCoordinates> coordinates;
    
    
    public ObserverLocationBean() {
        this.lonDeg = new SimpleDoubleProperty();
        this.latDeg = new SimpleDoubleProperty(); 
        // create binding where coordinates depends of lon and lat
        coordinates = Bindings.createObjectBinding(
                () -> GeographicCoordinates.ofDeg(lonDeg.get(), latDeg.get())
                ,lonDeg, latDeg);      
    }
    
    /**
     * Getter for the lonDeg property.
     * @return the lonDeg property.
     */
    public DoubleProperty lonDegProperty(){
        return lonDeg;
    }
    
    /**
     * Getter for the lonDeg value.
     * @return the lonDeg value.
     */
    public double getLonDeg() {
        return lonDeg.getValue();
    }

    /**
     * Setter for the LonDeg propertie's value.
     * @param newLonDeg: the new longitude in degrees.
     */
    public void setLonDeg(double newLonDeg) {
        lonDeg.set(newLonDeg);
    }
    
    
    /**
     * Getter for the latDeg property.
     * @return the latDeg property.
     */
    public DoubleProperty latDegProperty(){
        return latDeg;
    }
    
    /**
     * Getter for the latDeg value.
     * @return the latDeg value.
     */
    public double getLatDeg() {
        return latDeg.getValue();
    }

    /**
     * Setter for the latDeg propertie's value.
     * @param newLatDeg: the new latitude in degrees.
     */
    public void setLatDeg(double newLatDeg) {
        latDeg.set(newLatDeg);
    }
    
    /**
     * Getter for the coordinate property.
     * @return the observable geographic coordinates.
     */
    public ObservableObjectValue<GeographicCoordinates>  coordinatesProperty(){
        return coordinates;
    }
    
    /**
     * Getter for the coordinate value.
     * @return the observable geographic coordinates value.
     */
    public GeographicCoordinates getCoordinates() {
        return coordinates.getValue();
    }
    
    /**
     * Setter for the properties, latDeg and lonDeg. 
     * @param newCoords: the new coordinates.
     */
    public void setCoordinates(GeographicCoordinates newCoords) {    
        setLonDeg(newCoords.lonDeg());
        setLatDeg(newCoords.latDeg());
    }
}

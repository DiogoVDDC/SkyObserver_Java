package ch.epfl.rigel.gui;

import java.time.ZonedDateTime;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableObjectValue;

public final class ObserverLocationBean {
    
    //The longitude property.
    private final DoubleProperty lonDeg;
    //The latitude property.
    private final DoubleProperty latDeg;
    //Observable geographic coordinates.
    private final ObservableObjectValue<GeographicCoordinates> observableCoordinatesValue;
    
    
    public ObserverLocationBean() {
        this.lonDeg = new SimpleDoubleProperty();
        this.latDeg = new SimpleDoubleProperty();        
        observableCoordinatesValue = Bindings.createObjectBinding(
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
     * Getter for the observable geographic coordinates.
     * @return the observable geographic coordinates.
     */
    public ObservableObjectValue<GeographicCoordinates>  getObservableObjectCoordinates(){
        return observableCoordinatesValue;
    }
    
    /**
     * Getter for the observable geographic coordinates value.
     * @return the observable geographic coordinates value.
     */
    public GeographicCoordinates getCoordinatesValue() {
        return observableCoordinatesValue.getValue();
    }
    
    /**
     * Setter for the properties, latDeg and lonDeg. 
     * @param latDeg: the latitude of the coordinate in degrees
     * @param lonDeg: the longitude of the coordinate in degrees
     */
    public void setZonedDateTime(double latDeg, double lonDeg) {    
        setLonDeg(lonDeg);
        setLatDeg(latDeg);
    }
}

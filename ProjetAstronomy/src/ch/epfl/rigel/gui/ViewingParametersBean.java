package ch.epfl.rigel.gui;
import com.sun.javafx.collections.SetListenerHelper;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class ViewingParametersBean {
    
    // Interval in which the azimuth angle must be contained.
    private static final RightOpenInterval AZ_INTERVAL = RightOpenInterval.of(0,Angle.TAU);
    // Interval in which the altitude angle must be contained.
    private static final ClosedInterval ALT_INTERVAL = ClosedInterval.symmetric(Math.PI);
    
    //The field of view property.
    private final DoubleProperty fieldOfView;
    //The center property
    private final ObjectProperty<HorizontalCoordinates> center;

    // Viewing Parameters Bean constructor
    public ViewingParametersBean() {
        this.fieldOfView = new SimpleDoubleProperty();
        this.center = new SimpleObjectProperty<HorizontalCoordinates>(null);
    }   

    /**
     * Getter for the fieldOfView property.
     * @return: the fieldOfView property.
     */
    public DoubleProperty fieldOfViewProperty(){
        return fieldOfView;
    }
    
    /**
     * Getter for the fieldOfView value.
     * @return: the fielOfView value.
     */
    public double getFieldOfView() {
        return fieldOfView.getValue();
    }

    /**
     * Setter for the fieldOfView propertie's value.
     * @param newFieldOfView: the new field of view.
     */
    public void setFieldOfView(double newFieldOfView) {
        fieldOfView.set(newFieldOfView);
    }
    
    /**
     * Getter for the horizontal coordinate property.
     * @return: the horizontal coordinates of the center property.
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty(){
        return center;
    }  
    
    /**
     * Getter for the horizontal coordinates
     * @return: returns the center horizontal coordinates
     */
    public HorizontalCoordinates getCenter() {
        return center.getValue();
    }
    
    /**
     * Setter for the horizontal coordinates
     * @param newHorizontalCoordinates: the new horizontal coordinates
     */
    public void setCenter(HorizontalCoordinates newHorizontalCoordinates) {
        center.set(newHorizontalCoordinates);
    } 
    
    /**
     * Setter for the FOV and the horizontal coodinates
     * @param fieldOfView: the field of view
     * @param horizontal: the horizontal coordinates
     */
    public void setParameters(double fieldOfView, HorizontalCoordinates horizontal) {    
        setFieldOfView(fieldOfView);
        setCenter(horizontal);
    }
    
    /**
     * Allows to nudge the altitude of the center coordinates.
     * @param delta: the amount to add to the altitude (in degrees).
     */
    public void changeAlt(double delta) {
    	double newAlt = center.get().altDeg() + delta;
    	setCenter(HorizontalCoordinates.ofDeg(center.get().azDeg(), Angle.toDeg(ALT_INTERVAL.clip(Angle.ofDeg(newAlt)))));
    }
    
    /**
     * Allows to nudge the azimuth of the center coordinates.
     * @param delta: amount to add to the current azimuth angle (in degrees).
     */
    public void changeAz(double delta) {
    	double newAz = center.get().azDeg() + delta;
    	setCenter(HorizontalCoordinates.ofDeg(Angle.toDeg(AZ_INTERVAL.reduce(Angle.ofDeg(newAz))), center.get().altDeg()));
    }
}

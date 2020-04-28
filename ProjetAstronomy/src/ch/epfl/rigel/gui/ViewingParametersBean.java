package ch.epfl.rigel.gui;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class ViewingParametersBean {
    
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
    public DoubleProperty fieldOfView(){
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
    public ObjectProperty<HorizontalCoordinates> horizontalCoordinateProperty(){
        return center;
    }  
    
    /**
     * Getter for the horizontal coordinates
     * @return: returns the center horizontal coordinates
     */
    public HorizontalCoordinates getHorizontalCoordinates() {
        return center.getValue();
    }
    
    /**
     * Setter for the horizontal coordinates
     * @param newHorizontalCoordinates: the new horizontal coordinates
     */
    public void setHorizontalCoordinates(HorizontalCoordinates newHorizontalCoordinates) {
        center.set(newHorizontalCoordinates);
    } 
        
    /**
     * Setter for the FOV and the horizontal coodinates
     * @param fieldOfView: the field of view
     * @param horizontal: the horizontal coordinates
     */
    public void setParameters(double fieldOfView, HorizontalCoordinates horizontal) {    
        setFieldOfView(fieldOfView);
        setHorizontalCoordinates(horizontal);
    }
}

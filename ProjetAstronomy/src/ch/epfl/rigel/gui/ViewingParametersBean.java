package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Class representing an viewable parameter such as fov or center of observation
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public final class ViewingParametersBean {

    // Interval in which the altitude angle must be contained.
    private static final ClosedInterval ALT_INTERVAL = ClosedInterval.of(6, 90);

    // The field of view property.
    private final DoubleProperty fieldOfView;
    //Enables the lon/lat lines
    private final BooleanProperty enLonLatLines;
    // Enable the writing of the planets, sun and moon names.
    private final BooleanProperty enWriteNames;
    // The center property
    private final ObjectProperty<HorizontalCoordinates> center;

    // Viewing Parameters Bean constructor
    public ViewingParametersBean() {
        this.fieldOfView = new SimpleDoubleProperty();
        this.center = new SimpleObjectProperty<HorizontalCoordinates>(null);
        this.enLonLatLines = new SimpleBooleanProperty(false);
        this.enWriteNames = new SimpleBooleanProperty(false);
    }

    /**
     * Getter for the fieldOfView property.
     * @return: the fieldOfView property.
     */
    public DoubleProperty fieldOfViewProperty() {
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
     * @param newFieldOfView:
     *            the new field of view.
     */
    public void setFieldOfView(double newFieldOfView) {
        fieldOfView.set(newFieldOfView);
    }

    /**
     * Getter for the horizontal coordinate property.
     * @return: the horizontal coordinates of the center property.
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty() {
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
     * Getter for the enabling of lat/lon lines.
     * @return
     */
    public boolean isLatLonEnable() {
    	return enLonLatLines.get();
    }
    
    /**
     * Getter for the porperty of the enabling of lat/lon lines.
     * @return
     */
    public BooleanProperty enLatLonLinesProperty() {
    	return enLonLatLines;
    }
    
    /**
     * Setter for the enabling of lat/lon lines.
     * @param val
     */
    public void setEnableLatLonline(boolean val) {
    	enLonLatLines.set(val);
    }
    
    /**
     * Getter for the enabling of name writing
     * @return
     */
    public boolean isWriteName() {
    	return enWriteNames.get();
    }
    
    /**
     * Getter for the property of the enabling of name writing.
     * @return
     */
    public BooleanProperty enWriteNamesProperty() {
    	return enWriteNames;
    }
    
    /**
     * Setter for the enabling of name writing.
     * @param val
     */
    public void setEnWriteNames(boolean val) {
    	enWriteNames.set(val);
    }
    
    /**
     * Setter for the horizontal coordinates
     * @param newHorizontalCoordinates:
     *            the new horizontal coordinates
     */
    public void setCenter(HorizontalCoordinates newHorizontalCoordinates) {
        center.set(newHorizontalCoordinates);
    }

    /**
     * Setter for the FOV and the horizontal coodinates
     * @param fieldOfView:
     *            the field of view
     * @param horizontal:
     *            the horizontal coordinates
     */
    public void setParameters(double fieldOfView,
            HorizontalCoordinates horizontal) {
        setFieldOfView(fieldOfView);
        setCenter(horizontal);
    }

    /**
     * Allows to nudge the altitude of the center coordinates.
     * @param delta:
     *            the amount to add to the altitude (in degrees).
     */
    public void changeAlt(double delta) {
        double newAlt = ALT_INTERVAL.clip(center.get().altDeg() + delta);
        setCenter(HorizontalCoordinates.ofDeg(center.get().azDeg(), newAlt));
    }

    /**
     * Allows to nudge the azimuth of the center coordinates.
     * @param delta:
     *            amount to add to the current azimuth angle (in degrees).
     */
    public void changeAz(double delta) {
        double newAz = Angle.toDeg(Angle
                .normalizePositive(Angle.ofDeg(center.get().azDeg() + delta)));
        setCenter(HorizontalCoordinates.ofDeg(newAz, center.get().altDeg()));
    }
}

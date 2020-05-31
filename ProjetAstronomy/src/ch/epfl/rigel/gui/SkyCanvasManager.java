package ch.epfl.rigel.gui;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Optional;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Transform;

public final class SkyCanvasManager {

    // Interval in which the altitude angle must be contained.
    private static final ClosedInterval FOV_INTERVAL = ClosedInterval.of(30, 150);
    //property containing the current stereographic projection.
    private final ObservableObjectValue<StereographicProjection> projection;
    // Property contaning the current plane to canvas transformation.
    private final ObservableObjectValue<Transform> planeToCanvas;
    // Property containing the current representation of the observed sky.
    private final ObservableObjectValue<ObservedSky> observedSky;
    //Property containing the position of the mouse at any time.
    private final ObjectProperty<Point2D> mousePosition;
    //Property contaning the horizontal coord corresponding to the current mouse position.
    private final ObservableObjectValue<HorizontalCoordinates> mouseHorizontalCoords;
    // Property containing the mouse's azimuth of it's horizontal coord
    private final ObservableDoubleValue mouseAzDeg;
    // Property containing the mouse's altitude of it's horizontal coord
    private final ObservableDoubleValue mouseAltDeg;
    // Property containing the closest celestial object to the current mouse's position.
    private final ObservableObjectValue<CelestialObject> objectUnderMouse;
    // Canvas on which the sky is drawn.
    private final Canvas sky;
    // Painter used to draw onto the canvas.
    private final SkyCanvasPainter painter;
    private double anchorX;
    private double anchorY;

    /**
     * SkyCanvasManager constructor.
     * @param catalogue: the catalogue of stars to use to draw the sky.
     * @param dateTimeBean: date and time of the observation.
     * @param observerLocBean: location of the observer.
     * @param viewingParamBean: center of observation and fov information.
     */
    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dateTimeBean,
            ObserverLocationBean observerLocBean,
            ViewingParametersBean viewingParamBean) {
    	//initialising canvas and painter.
        sky = new Canvas();
        painter = new SkyCanvasPainter(sky);
        
        // Binding the projection to it's corresponding variables.
        projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(viewingParamBean.getCenter()),
                viewingParamBean.centerProperty());

        // Binding the transformation to it's corresponding variables.
        planeToCanvas = Bindings.createObjectBinding(() -> {
        	//Compute the dilatation factor.
            double dilatationFactor = sky.getWidth()
                    / projection.getValue().applyToAngle(
                            Angle.ofDeg(viewingParamBean.getFieldOfView()));
            return Transform.affine(dilatationFactor, 0, 0, -dilatationFactor,
                    sky.getWidth() / 2, sky.getHeight() / 2);
        }, projection, viewingParamBean.fieldOfViewProperty(),
                sky.widthProperty(), sky.heightProperty());
        
        // Binding the observed sky to it's corresponding variables.
        observedSky = Bindings.createObjectBinding(() -> new ObservedSky(
                        ZonedDateTime.of(dateTimeBean.getDate(),
                        dateTimeBean.getTime(), dateTimeBean.getZone()),
                        observerLocBean.getCoordinates(), projection.getValue(),
                        catalogue),
                observerLocBean.coordinatesProperty(),
                dateTimeBean.dateProperty(), dateTimeBean.timeProperty(),
                dateTimeBean.zoneProperty(), projection);

        // Initialising the mouse position property at (0,0)
        mousePosition = new SimpleObjectProperty<Point2D>(new Point2D(0,0));

        // Updates the mouse position whenever the mouse moves.
        sky.setOnMouseMoved(e ->{
            mousePosition.setValue(new Point2D(e.getX(), e.getY()));
         });
       
        // Binding the horizontal coords to the position of the mouse.
        mouseHorizontalCoords = Bindings.createObjectBinding(() ->{
        	// Try and catch to avoid having an error of non invertible transform exception at initialisation.
        	try {
        		Point2D transformedMousePos =
                planeToCanvas.get().inverseTransform(mousePosition.get());
        		return projection.get().inverseApply(CartesianCoordinates.of(transformedMousePos.getX(),
            		transformedMousePos.getY()));
        	} catch(Exception e) {
        		return HorizontalCoordinates.of(0, 0);
        	}

            }, projection, mousePosition, planeToCanvas);
        
         // Binding the mouse alt and az to the mouse's horizontal coords.
         mouseAltDeg = Bindings.createDoubleBinding(() ->
             mouseHorizontalCoords.get().altDeg(), mouseHorizontalCoords);
         mouseAzDeg = Bindings.createDoubleBinding(() ->
             mouseHorizontalCoords.get().azDeg(), mouseHorizontalCoords);
        
         // Binding the object under mouse to it's corresponding variables.
         objectUnderMouse = Bindings.createObjectBinding(() ->{
        	// Try and catch to avoid having an error of non invertible transform exception at initialisation.
        	 try {
             Point2D transformedMousePos =
                     planeToCanvas.get().inverseTransform(mousePosition.get());
             
             // Get the closest object in a certain radius
             Optional<CelestialObject> closest = observedSky.get().objectClosestTo(
                     CartesianCoordinates.of(transformedMousePos.getX(),
                     transformedMousePos.getY()), planeToCanvas.get().inverseDeltaTransform(10, 0).magnitude());
             
             return closest.isEmpty() ? null : closest.get();
        	 } catch(Exception e1) {
        	     // returns null if the nonInvertibleTransformException is caught
                 return null;
        	 }
         }, observedSky, mousePosition, planeToCanvas);
        
         // Makes sure the sky is redrawn whenever the observed sky changes.
         observedSky.addListener((o, oV, nV) -> {
        	 drawCanvas();
         });
         
         //Makes sure the sky is redrawn whenever the plane to canvas trasnformatio changes.
         planeToCanvas.addListener((o, oV, nV) -> {
            drawCanvas();
         });
         
         //Adjust the fov when zooming in or out using mouse's wheel of trackpad.
         sky.setOnScroll(e -> {
        	 if (Math.abs(e.getDeltaX()) > Math.abs(e.getDeltaY())) {
        		 viewingParamBean.setFieldOfView(FOV_INTERVAL.clip(
        				 viewingParamBean.getFieldOfView() - 
        				 e.getDeltaX()*viewingParamBean.getFieldOfView()/100.0));
             } else {
            	 viewingParamBean.setFieldOfView(FOV_INTERVAL.clip(
                         viewingParamBean.getFieldOfView() -
                         e.getDeltaY()*viewingParamBean.getFieldOfView()/100.0));
             }
         });
         
         // Requests the focus when left clicking on the canvas.
         sky.setOnMousePressed(e ->{
        	 if (e.isPrimaryButtonDown()) sky.requestFocus();
        	 anchorX = e.getX();
        	 anchorY = e.getY();
         });
         
         // Allows to move around using the mouse drag
         sky.setOnMouseDragged(e ->{
        	 // The azimuth and altitude are chnaged by the difference bewteen the two
        	 // positions and scaled down by 10 to make the mouvement smaller.
         	 viewingParamBean.changeAz((anchorX - e.getX())/10);
         	 viewingParamBean.changeAlt((e.getY() - anchorY)/10); 
         	 anchorX = e.getX();
         	 anchorY = e.getY();
         });
         
         // Moves the centre of projection according the the keyboard arrows.
         sky.setOnKeyPressed(e -> {
             if (e.getCode() == KeyCode.UP) {
                 viewingParamBean.changeAlt(5);
             }
             if (e.getCode() == KeyCode.DOWN) {
                 viewingParamBean.changeAlt(-5);
             }
             if (e.getCode() == KeyCode.LEFT) {
                 viewingParamBean.changeAz(-5);
             }
             if (e.getCode() == KeyCode.RIGHT) {
                viewingParamBean.changeAz(5);
            }
            e.consume();
        });    
    }

    /**
     * Allows to draw all elements of the canvas using a single command.
     */
    private void drawCanvas() {
        painter.clear();
        try {
            painter.drawSky(observedSky.get(), projection.get(),
                    planeToCanvas.get());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Getter for the objectUnderMouseProperty.
     * @return: objectUnderMouse property
     */
    public ObservableObjectValue<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

    /**
     * Getter for the canvas
     * @return: the canvas
     */
    public Canvas canvas() {
        return sky;
    }

    /**
     * Getter for the projection property.
     * @return: the projection property.
     */
    public ObservableObjectValue<StereographicProjection> projectionProperty() {
        return projection;
    }

    /**
     * Getter for the stereographic projection.
     * @return: the stereographic projection.
     */
    public StereographicProjection getProjection() {
        return projection.get();
    }

    /**
     * Getter for the planeToCanvas transformation property.
     * @return: planeToCanvas property.
     */
    public ObservableObjectValue<Transform> planeToCanvasProperty() {
        return planeToCanvas;
    }

    /**
     * Getter for the plane to canvas transformation.
     * @return: the plane to canvas transformation.
     */
    public Transform getPlaneToCanvas() {
        return planeToCanvas.get();
    }

    /**
     * Getter for the observedSky property.
     * @return: the observedSky property.
     */
    public ObservableObjectValue<ObservedSky> observedSkyProperty() {
        return observedSky;
    }

    /**
     * Getter for the current observedSky.
     * @return: the current observedSky.
     */
    public ObservedSky getObservedSky() {
        return observedSky.get();
    }
    
    /**
     * Getter for the mouseAzDeg property.
     * @return: the mouseAzDeg property.
     */
    public ObservableDoubleValue mouseAzDegProperty() {
    	return mouseAzDeg;
    }
    
    /**
     * Getter for the mouse's azimuth value.
     * @return: the mouse's azimuth value.
     */
    public double getMouseAzDeg() {
    	return mouseAzDeg.get();
    }
    
    /**
     * Getter for the mouseAltDeg property. 
     * @return: the mouseAltDeg property.
     */
    public ObservableDoubleValue mouseAltDegProperty() {
    	return mouseAltDeg;
    }
    
    /**
     * Getter for the mouse's altitude value.
     * @return: the mouse's altitude value.
     */
    public double getMouseAltDeg() {
    	return mouseAltDeg.get();
    }
}

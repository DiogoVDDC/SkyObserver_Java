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

    private final ObservableObjectValue<StereographicProjection> projection;
    private final ObservableObjectValue<Transform> planeToCanvas;
    private final ObservableObjectValue<ObservedSky> observedSky;
    private final ObjectProperty<Point2D> mousePosition;
    private final ObservableObjectValue<HorizontalCoordinates> mouseHorizontalCoords;
    private final ObservableDoubleValue mouseAzDeg;
    private final ObservableDoubleValue mouseAltDeg;
    private final ObservableObjectValue<CelestialObject> objectUnderMouse;

    private final Canvas sky;
    private final SkyCanvasPainter painter;

    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dateTimeBean,
            ObserverLocationBean observerLocBean,
            ViewingParametersBean viewingParamBean) {

        sky = new Canvas();
        painter = new SkyCanvasPainter(sky);
        
        projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(viewingParamBean.getCenter()),
                viewingParamBean.centerProperty());

        planeToCanvas = Bindings.createObjectBinding(() -> {
            double dilatationFactor = sky.getWidth()
                    / projection.getValue().applyToAngle(
                            Angle.ofDeg(viewingParamBean.getFieldOfView()));
            return Transform.affine(dilatationFactor, 0, 0, -dilatationFactor,
                    sky.getWidth() / 2, sky.getHeight() / 2);
        }, projection, viewingParamBean.fieldOfViewProperty(),
                sky.widthProperty(), sky.heightProperty());

        observedSky = Bindings.createObjectBinding(() -> new ObservedSky(
                        ZonedDateTime.of(dateTimeBean.getDate(),
                        dateTimeBean.getTime(), dateTimeBean.getZone()),
                        observerLocBean.getCoordinates(), projection.getValue(),
                        catalogue),
                observerLocBean.coordinatesProperty(),
                dateTimeBean.dateProperty(), dateTimeBean.timeProperty(),
                dateTimeBean.zoneProperty(), projection);

         mousePosition = new SimpleObjectProperty<Point2D>(new Point2D(0,0));
         
        

         mouseHorizontalCoords = Bindings.createObjectBinding(() ->{
             Point2D transformedMousePos = planeToCanvas.get().transform(mousePosition.get());             
             return projection.get().inverseApply(CartesianCoordinates.of(transformedMousePos.getX(),
                 transformedMousePos.getY()));
            
         }, projection, mousePosition, planeToCanvas);
         
         
        
         mouseAltDeg = Bindings.createDoubleBinding(() ->
             mouseHorizontalCoords.get().altDeg(), mouseHorizontalCoords);
         mouseAzDeg = Bindings.createDoubleBinding(() ->
             mouseHorizontalCoords.get().azDeg(), mouseHorizontalCoords);
        
         objectUnderMouse = Bindings.createObjectBinding(() ->{
             Point2D transformedMousePos =
                     planeToCanvas.get().transform(mousePosition.get());
             Optional<CelestialObject> closest = observedSky.get().objectClosestTo(
                     CartesianCoordinates.of(transformedMousePos.getX(),
                     transformedMousePos.getY()), 10d);
             return closest.isEmpty() ? null : closest.get();
         }, observedSky, mousePosition, planeToCanvas);
        

        observedSky.addListener((o, oV, nV) -> {
            drawCanvas();
        });

        planeToCanvas.addListener((o, oV, nV) -> {
            drawCanvas();
        });

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
        
         sky.setOnMousePressed(e ->{
         if (e.isPrimaryButtonDown()) sky.requestFocus();
         });
        
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
        
        
        sky.setOnMouseMoved(e ->{
            // position x y sur le canvas
            mousePosition.set(new Point2D(e.getX(), e.getY()));
            System.out.println(mousePosition.get().toString());
           System.out.println(mouseHorizontalCoords.getValue().toString());
        });
    }

    private void drawCanvas() {
        painter.clear();
        try {
            painter.drawSky(observedSky.get(), projection.get(),
                    planeToCanvas.get());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public ObservableObjectValue<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

    public Canvas canvas() {
        return sky;
    }

    public ObservableObjectValue<StereographicProjection> projectionProperty() {
        return projection;
    }

    public StereographicProjection getProjection() {
        return projection.get();
    }

    public ObservableObjectValue<Transform> planeToCanvasProperty() {
        return planeToCanvas;
    }

    public Transform getPlaneToCanvas() {
        return planeToCanvas.get();
    }

    public ObservableObjectValue<ObservedSky> observedSkyPropert() {
        return observedSky;
    }

    public ObservedSky getOservedSky() {
        return observedSky.get();
    }
}

package ch.epfl.rigel.gui;

import java.io.IOException;
import java.time.ZonedDateTime;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
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
			ObserverLocationBean observerLocBean, ViewingParametersBean viewingParamBean) {
		
		sky = new Canvas();
		painter = new SkyCanvasPainter(sky);
		
		projection = Bindings.createObjectBinding(() -> 
			new StereographicProjection(viewingParamBean.getCenter()),
			viewingParamBean.centerProperty());
		
		planeToCanvas = Bindings.createObjectBinding(() -> {
			double dilatationFactor = sky.getWidth() / (2 * Math.tan(viewingParamBean.getFieldOfView() / 4));
			return Transform.affine(dilatationFactor, 0, 0, -dilatationFactor, sky.getWidth()/2, sky.getHeight()/2);
			},
			projection, viewingParamBean.fieldOfViewProperty(), sky.widthProperty(), sky.heightProperty());
		
		observedSky = Bindings.createObjectBinding(() ->
			new ObservedSky(ZonedDateTime.of(dateTimeBean.getDate(), dateTimeBean.getTime(),
			dateTimeBean.getZone()), observerLocBean.getCoordinates(), projection.getValue(), catalogue)
			, observerLocBean.coordinatesProperty(), dateTimeBean.dateProperty(), dateTimeBean.timeProperty(),
			dateTimeBean.zoneProperty());
		
		mousePosition = new SimpleObjectProperty<Point2D>();
		sky.setOnMouseMoved(e ->{
			 mousePosition.set(new Point2D(e.getX(), e.getY()));
		});
		
		mouseHorizontalCoords = Bindings.createObjectBinding(() ->{
			Point2D transformedMousePos = planeToCanvas.get().transform(mousePosition.get());
			return projection.get().inverseApply(CartesianCoordinates.of(transformedMousePos.getX(),
					transformedMousePos.getY()));
		}, projection, mousePosition, planeToCanvas);
		
		mouseAltDeg = Bindings.createDoubleBinding(() -> mouseHorizontalCoords.get().altDeg(), mouseHorizontalCoords);
		mouseAzDeg = Bindings.createDoubleBinding(() -> mouseHorizontalCoords.get().azDeg(), mouseHorizontalCoords);
		
		objectUnderMouse = Bindings.createObjectBinding(() ->{
			Point2D transformedMousePos = planeToCanvas.get().transform(mousePosition.get());
			
			return observedSky.get().objectClosestTo(CartesianCoordinates.of(transformedMousePos.getX(), transformedMousePos.getY()), 10d).get();
		}, observedSky, mousePosition, planeToCanvas);
		
		projection.addListener((o, oV, nV) -> {
			drawCanvas();
		});
		
		observedSky.addListener((o, oV, nV) ->{
			drawCanvas();
		});
		
		planeToCanvas.addListener((o, oV, nV) ->{
			drawCanvas();
		});
		
		sky.setOnScroll(e -> {
			viewingParamBean.setFieldOfView(Math.max(e.getDeltaX(), e.getDeltaY()));
		});
		
		sky.setOnMousePressed(e ->{
			if (e.isPrimaryButtonDown()) sky.requestFocus(); 
		});
		
		sky.setOnKeyPressed(e ->{
			if (e.getCode() == KeyCode.UP) 
				viewingParamBean.changeAlt(5);
			if (e.getCode() == KeyCode.DOWN) 
				viewingParamBean.changeAlt(-5);
			if (e.getCode() == KeyCode.LEFT)
				viewingParamBean.changeAz(10);
			if (e.getCode() == KeyCode.RIGHT)
				viewingParamBean.changeAz(-10);
		});
		
	}
	
	private void drawCanvas() {
		painter.clear();
		try {
			painter.drawAllCelestialObjects(observedSky.get(), projection.get(), planeToCanvas.get());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public ObservableObjectValue<CelestialObject> objectUnderMouseProperty(){
		return objectUnderMouse;
	}
	
	public Canvas canvas() {
		return sky;
	}
	
	public ObservableObjectValue<StereographicProjection> projectionProperty(){
		return projection;
	}
	
	public StereographicProjection getProjection() {
		return projection.get();
	}
	
	public ObservableObjectValue<Transform> planeToCanvasProperty(){
		return planeToCanvas;
	}
	
	public Transform getPlaneToCanvas() {
		return planeToCanvas.get();
	}
	
	public ObservableObjectValue<ObservedSky> observedSkyPropert(){
		return observedSky;
	}
	
	public ObservedSky getOservedSky() {
		return observedSky.get();
	}
	
}

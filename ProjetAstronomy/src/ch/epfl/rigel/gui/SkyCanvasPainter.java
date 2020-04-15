package ch.epfl.rigel.gui;

import java.io.IOException;

import javax.swing.GroupLayout.Alignment;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

/**
 * Class representing a sky painter.
 * @author Theo Houle (312432)
 *
 */
public final class SkyCanvasPainter {
	
	private Canvas canvas; //Final ??
	private final GraphicsContext ctx;
	private static final ClosedInterval MAGNITUDE_INTERVAL = ClosedInterval.of(-2,5);
	
	public SkyCanvasPainter(Canvas canvas) {
		this.canvas = canvas;
		this.ctx = canvas.getGraphicsContext2D();
	}
	
	public void clear() {
		ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		ctx.setFill(Color.BLACK);
		ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
	}
	
	public void drawStars(ObservedSky observedSky, StereographicProjection projection, Transform transform) throws IOException {
		//Transforming all stars positions accoridng to the affine transformation.
		double[] transformedCoords = new double[observedSky.starPositions().length];
		transform.transform2DPoints(observedSky.starPositions(), 0, transformedCoords, 0, transformedCoords.length/2);		
		
		//Drawing the asterisms.
		
		//Set stroke color to blue.
		ctx.setStroke(Color.BLUE);
		//Set stroke width to 1.
		ctx.setLineWidth(1);
		
		Bounds bound = canvas.getBoundsInLocal();
		
		for (Asterism asterism : observedSky.asterisms()) {
		    boolean previousIscontained = false;
		    ctx.beginPath();
		    for (Star star : asterism.stars()) {
		        Double starX = transformedCoords[observedSky.stars().indexOf(star)*2];
		        Double starY = transformedCoords[observedSky.stars().indexOf(star)*2+ 1];
		        if(bound.contains(new Point2D(starX, starY)) || previousIscontained) {
		            ctx.lineTo(starX, starY);
		            previousIscontained = bound.contains(new Point2D(starX, starY));
		        } else {
                    ctx.moveTo(starX, starY);
                    previousIscontained = false;
                }
		    }
		    ctx.stroke();
		}
		
		//Drawing the stars.
		
		for (Star star: observedSky.stars()) {
			//Defining the star's disk size based on it's magnitude.
			Double clippedMag = MAGNITUDE_INTERVAL.clip(star.magnitude());
			Double f = (99 - 17 * clippedMag) / 140.0;
			Double d = f * 2 * Math.tan(Angle.ofDeg(0.5) / 4.0);
			
			//Transforming the disk size according to the affine transformation.
			Point2D transformedSize = transform.deltaTransform(0, d);
			
			//Defining the star coordinates.
			Double ajustedStarX = ajustedCoordinate(transformedCoords[observedSky.stars().indexOf(star)*2], transformedSize.magnitude());
			Double ajustedStarY = ajustedCoordinate(transformedCoords[observedSky.stars().indexOf(star)*2+ 1], transformedSize.magnitude());

			//Set the color to the star's one based on it's color temperature.
			ctx.setFill(BlackBodyColor.colorForTemperature(star.colorTemperature()));
			
			//Drawing the star.
			ctx.fillOval(ajustedStarX, ajustedStarY, transformedSize.magnitude(), transformedSize.magnitude());
		}
	}
	
	public void drawPlanets(ObservedSky observedSky, StereographicProjection projection, Transform transform) {
		//Transforming all stars' coordinates according to affine transformation.
		double[] transformedCoords = new double[observedSky.planetPositions().length];
		//System.out.println(Arrays.toString(transformedCoords));
		//System.out.println(transformedCoords.length/2);
		transform.transform2DPoints(observedSky.planetPositions(), 0, transformedCoords, 0, transformedCoords.length/2);
		//System.out.println(Arrays.toString(transformedCoords));
		
		//Setting the planets' color.
		ctx.setFill(Color.LIGHTGRAY);
		
		for(Planet planet: observedSky.planets()) {
			//Defining the planet's disk size based on it's magnitude.
			Double clippedMag = MAGNITUDE_INTERVAL.clip(planet.magnitude());
			Double f = (99 - 17 * clippedMag) / 140.0;
			Double d = f * 2 * Math.tan(Angle.ofDeg(0.5) / 4.0);
			
			
			//Transforming the disk size according to the affine transformation.
			Point2D transformedSize = transform.deltaTransform(0,d);
			
			//Defining the planet's coordinates.
			Double ajustedPlanetX = ajustedCoordinate(transformedCoords[observedSky.planets().indexOf(planet) * 2], transformedSize.magnitude());
			Double ajustedPlanetY = ajustedCoordinate(transformedCoords[observedSky.planets().indexOf(planet) * 2 + 1], transformedSize.magnitude());
			
			//Drawing the planet on the canvas.
			ctx.fillOval(ajustedPlanetX, ajustedPlanetY, transformedSize.magnitude(), transformedSize.magnitude());
		}
		
		
	}
	
	public void drawSun(ObservedSky observedSky, StereographicProjection projection, Transform transform) {
		Point2D sunPos = transform.transform(observedSky.sunPosition().x(), observedSky.sunPosition().y());
		double sunSize = projection.applyToAngle(observedSky.sun().angularSize()); 
		double relSunSize = transform.deltaTransform(sunSize,0).magnitude();
		
		ctx.setFill(Color.YELLOW.deriveColor(0, 1, 1, 0.25));
		ctx.fillOval(sunPos.getX() - relSunSize*1.1, sunPos.getY() - relSunSize*1.1, relSunSize*2.2, relSunSize*2.2);

	    ctx.setFill(Color.YELLOW);
        ctx.fillOval(sunPos.getX() - (relSunSize+2)/2 , sunPos.getY() - (relSunSize+2)/2, relSunSize+2, relSunSize+2);

		ctx.setFill(Color.WHITE);
		ctx.fillOval(sunPos.getX() - relSunSize/2, sunPos.getY() - relSunSize/2, relSunSize, relSunSize);
	}
	
	public void drawMoon(ObservedSky observedSky, StereographicProjection projection, Transform transform) {
		Point2D moonPos = transform.transform(observedSky.moonPosition().x(), observedSky.moonPosition().y());
		double moonSize = projection.applyToAngle(observedSky.moon().angularSize());
		Point2D relativeMoonSize = transform.deltaTransform(moonSize, 0);
		double ajustedPosx = ajustedCoordinate(moonPos.getX(), relativeMoonSize.magnitude());
		double ajustedPosy = ajustedCoordinate(moonPos.getY(), relativeMoonSize.magnitude());
        
		ctx.setFill(Color.WHITE);
		ctx.fillOval(ajustedPosx, ajustedPosy, relativeMoonSize.magnitude(), relativeMoonSize.magnitude());
	}
	
	public void drawHorizon(ObservedSky observedSky, StereographicProjection projection, Transform transform) {
	    HorizontalCoordinates horizon = HorizontalCoordinates.ofDeg(0, 0);
	    
	    CartesianCoordinates horizonCoords = projection.circleCenterForParallel(horizon);
	    Point2D transHorizonCoords = transform.transform(horizonCoords.x(), horizonCoords.y());
	    
	    double horizonRadius = projection.circleRadiusForParallel(horizon);
	    double transHorizonRadius = transform.deltaTransform(horizonRadius, 0).magnitude();
	    
	    ctx.setStroke(Color.RED);
	    ctx.setLineWidth(2);
	    ctx.strokeOval(transHorizonCoords.getX() - transHorizonRadius, transHorizonCoords.getY() - transHorizonRadius,
	            transHorizonRadius*2, transHorizonRadius*2);
	    
	    
	    
	    ctx.setTextAlign(TextAlignment.CENTER);
	    ctx.setTextBaseline(VPos.BASELINE.TOP);
	    ctx.setLineWidth(1);
	    
	    for(cardinalPoints cardPoint : cardinalPoints.values()) {
	        CartesianCoordinates coord = cardPoint.getCartesianCoord(projection);
	        Point2D transCoord = transform.transform(coord.x(), coord.y());
	        ctx.strokeText(cardPoint.getFrenchAbrev(), transCoord.getX(), transCoord.getY());
	    }
	}
	
	private double ajustedCoordinate(double coordinate, double ovalWidth) {
	    return coordinate - ovalWidth/2;
	}
	
	private enum cardinalPoints{
	    NORTH("N", HorizontalCoordinates.ofDeg(0, -.5)),
	    NORT_EAST("NE", HorizontalCoordinates.ofDeg(45, -.5)),
	    EAST("E", HorizontalCoordinates.ofDeg(90, -.5)),
	    SOUTH_EAST("SE", HorizontalCoordinates.ofDeg(135, -.5)),
	    SOUTH("S", HorizontalCoordinates.ofDeg(180, -.5)),
	    SOUTH_WEST("SO", HorizontalCoordinates.ofDeg(225, -.5)),
	    WEST("O", HorizontalCoordinates.ofDeg(270, -.5)),
	    NORTH_WEST("NO", HorizontalCoordinates.ofDeg(315, -.5));
	    
	    private final HorizontalCoordinates horCoord;
	    private final String frenchName;
	    
	    private cardinalPoints(String frenchAbrev, HorizontalCoordinates horCoord) {
	        this.horCoord = horCoord;
	        this.frenchName = frenchAbrev;
        }
	    
	    protected String getFrenchAbrev() {
	        return frenchName;
	    }
	    protected CartesianCoordinates getCartesianCoord(StereographicProjection projection) {
	        return projection.apply(horCoord);
	    }
	}
}

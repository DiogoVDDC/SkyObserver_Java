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
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public final class SkyCanvasPainter {
	
	private Canvas canvas; 
	private final GraphicsContext ctx;
	private static final ClosedInterval MAGNITUDE_INTERVAL = ClosedInterval.of(-2,5);
	
	public SkyCanvasPainter(Canvas canvas) {
		this.canvas = canvas;
		this.ctx = canvas.getGraphicsContext2D();
	}
	
	/**
	 * Clears the canvas and sets the background to black
	 */
	public void clear() {
		ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		ctx.setFill(Color.BLACK);
		ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());		
	}
	
	/**
	 * Draws stars and asterisms to the canvas
	 * @param observedSky: the observed sky from which we will draw the stars and asterism
	 * @param projection: projection of the point which we will draw arround
	 * @param transform: plane to canvas affine transform 
	 * @throws IOException:  throws IO exception if black body colour throws an exception
	 */
	public void drawStars(ObservedSky observedSky, StereographicProjection projection, Transform transform) throws IOException {
		double[] transformedCoords = new double[observedSky.starPositions().length];
	    //Transforming all stars positions accoridng to the affine transformation.
		transform.transform2DPoints(observedSky.starPositions(), 0, transformedCoords, 0, transformedCoords.length/2);
		
		//Set stroke color to blue.
		ctx.setStroke(Color.BLUE);
		//Set stroke width to 1.
		ctx.setLineWidth(1);
		// Defining the canvas bounds
		Bounds bound = canvas.getBoundsInLocal();
		
		// Drawing the lines the stars in an asterism
		for (Asterism asterism : observedSky.asterisms()) {
		    boolean isPreviousContained = false;
		    // begin a path for every asterism
		    ctx.beginPath();
		    for (Star star : asterism.stars()) {
		        // Define star position
		        Double starX = transformedCoords[observedSky.stars().indexOf(star)*2];
		        Double starY = transformedCoords[observedSky.stars().indexOf(star)*2+ 1];
		        
		        // only draw a line if the current or the previous star was contained on the canvas
		        if(bound.contains(new Point2D(starX, starY)) || isPreviousContained) {
		            ctx.lineTo(starX, starY);
		            
		            isPreviousContained = bound.contains(new Point2D(starX, starY));
		        } else {
		            // moves to the next star without drawing a line
                    ctx.moveTo(starX, starY);
                    isPreviousContained = false;
                }
		    }
		    ctx.stroke();
		}
		
		//Drawing the stars		
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
	
	/**
     * Draws planets to the canvas
     * @param observedSky: the observed sky from which we will draw the planets
     * @param projection: projection of the point which we will draw arround
     * @param transform: plane to canvas affine transform    
     */
	public void drawPlanets(ObservedSky observedSky, StereographicProjection projection, Transform transform) {
		double[] transformedCoords = new double[observedSky.planetPositions().length];
	    //Transforming all stars positions accoridng to the affine transformation.
		transform.transform2DPoints(observedSky.planetPositions(), 0, transformedCoords, 0, transformedCoords.length/2);
	
		
		//Setting the planet's color.
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
	
	/**
     * Draws sun to the canvas
     * @param observedSky: the observed sky from which we will draw the sun
     * @param projection: projection of the point which we will draw arround
     * @param transform: plane to canvas affine transform    
     */
	public void drawSun(ObservedSky observedSky, StereographicProjection projection, Transform transform) {
	    // Defining the sun position as a point on the affine transform
		Point2D sunPos = transform.transform(observedSky.sunPosition().x(), observedSky.sunPosition().y());
		// Finding the diameter of the sun using the angular size
		double sunSize = projection.applyToAngle(observedSky.sun().angularSize()); 
		// Finding the relative size which depends of the affine transform
		double relativeSunSize = transform.deltaTransform(sunSize,0).magnitude();
		
		// Drawing the sun halo
		ctx.setFill(Color.YELLOW.deriveColor(0, 1, 1, 0.25));
		ctx.fillOval(sunPos.getX() - relativeSunSize*1.1, sunPos.getY() - relativeSunSize*1.1, relativeSunSize*2.2, relativeSunSize*2.2);

		// Drawing the sun outer core
	    ctx.setFill(Color.YELLOW);
        ctx.fillOval(sunPos.getX() - (relativeSunSize+2)/2 , sunPos.getY() - (relativeSunSize+2)/2, relativeSunSize+2, relativeSunSize+2);

        // Drawing the sun core
		ctx.setFill(Color.WHITE);
		ctx.fillOval(sunPos.getX() - relativeSunSize/2, sunPos.getY() - relativeSunSize/2, relativeSunSize, relativeSunSize);
	}
	
	/**
     * Draws moon to the canvas
     * @param observedSky: the observed sky from which we will draw the moon
     * @param projection: projection of the point which we will draw arround
     * @param transform: plane to canvas affine transform    
     */
	public void drawMoon(ObservedSky observedSky, StereographicProjection projection, Transform transform) {
	    // Defining the moon position as a point on the affine transform 
		Point2D moonPos = transform.transform(observedSky.moonPosition().x(), observedSky.moonPosition().y());
        // Finding the diameter of the sun using the angular size
		double moonSize = projection.applyToAngle(observedSky.moon().angularSize());
	    // Finding the relative size which depends of the affine transform
		Point2D relativeMoonSize = transform.deltaTransform(moonSize, 0);
		
		// Ajusting the x and y such that it represents the top left conner, which is required by fill oval
		double ajustedPosx = ajustedCoordinate(moonPos.getX(), relativeMoonSize.magnitude());
		double ajustedPosy = ajustedCoordinate(moonPos.getY(), relativeMoonSize.magnitude());
        
		// Drawing the moon 
		ctx.setFill(Color.WHITE);
		ctx.fillOval(ajustedPosx, ajustedPosy, relativeMoonSize.magnitude(), relativeMoonSize.magnitude());
	}
	
	/**
     * Draws the horizon and cardinal points
     * @param observedSky: the observed sky from which we will draw the horizon
     * @param projection: projection of the point which we will draw around
     * @param transform: plane to canvas affine transform    
     */
	public void drawHorizon(ObservedSky observedSky, StereographicProjection projection, Transform transform) {
	    // Defining the horizon object
	    HorizontalCoordinates horizon = HorizontalCoordinates.ofDeg(0, 0);
	    
	    // Projecting the center of the circle
	    CartesianCoordinates horizonCoords = projection.circleCenterForParallel(horizon);
	    // Creating a point from the centre of the circle
	    Point2D transHorizonCoords = transform.transform(horizonCoords.x(), horizonCoords.y());
	    
	    // Defining the the radius of the circle
	    double horizonRadius = projection.circleRadiusForParallel(horizon);
	    // Ajusting the radius to the affine transform
	    double transHorizonRadius = transform.deltaTransform(horizonRadius, 0).magnitude();
	    
	    // Drawing the horizon circle
	    ctx.setStroke(Color.RED);
	    ctx.setLineWidth(2);
	    ctx.strokeOval(transHorizonCoords.getX() - transHorizonRadius, transHorizonCoords.getY() - transHorizonRadius,
	            transHorizonRadius*2, transHorizonRadius*2);
	    
	    
	    // Setting the parameters for the cardinal point text
	    ctx.setTextAlign(TextAlignment.CENTER);
	    ctx.setTextBaseline(VPos.BASELINE.TOP);
	    ctx.setLineWidth(1);
	    
	    // Drawing the text of the every cardinal points on the horizon circle 
	    for(CardinalPoints cardPoint : CardinalPoints.values()) {
	        // Getting the coordinates at which the text will be drawn
	        CartesianCoordinates coord = cardPoint.getCartesianCoord(projection);
	        // Ajusting the coordinates to the affine transform
	        Point2D transCoord = transform.transform(coord.x(), coord.y());
	        // Drawing the cardinal point text
	        ctx.strokeText(cardPoint.getFrenchAbrev(), transCoord.getX(), transCoord.getY());
	    }
	}
	
	// ajustes the coordinates such that the center moves to the upper left corner
	private double ajustedCoordinate(double coordinate, double ovalWidth) {
	    return coordinate - ovalWidth/2;
	}
	
	 // Creating all the cardinal points with appropriate name, and coordinates
	private enum CardinalPoints{	   
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
	    
	    private CardinalPoints(String frenchAbrev, HorizontalCoordinates horCoord) {
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

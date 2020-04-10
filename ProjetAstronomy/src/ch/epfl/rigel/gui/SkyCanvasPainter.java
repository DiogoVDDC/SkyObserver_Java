package ch.epfl.rigel.gui;

import java.io.IOException;
import java.util.Arrays;

import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	public void drawStars(ObservedSky observedSky, StereographicProjection projection, Transform transform) throws IOException {
		//Transforming all stars positions accoridng to the affine transformation.
		double[] transformedCoords = new double[observedSky.starPositions().length];
		transform.transform2DPoints(observedSky.starPositions(), 0, transformedCoords, 0, transformedCoords.length/2);
		
		//Set the background black and display it.
		ctx.setFill(Color.BLACK);
		ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		for (Star star: observedSky.stars()) {
			//Defining the star's disk size based on it's magnitude.
			Double clippedMag = MAGNITUDE_INTERVAL.clip(star.magnitude());
			Double f = (99 - 17 * clippedMag) / 140.0;
			Double d = f * 2 * Math.tan(Angle.ofDeg(0.5) / 4.0);
			
			//Transforming the disk size according to the affine transformation.
			Point2D transformedSize = transform.deltaTransform(d, d);
			
			//Defining the star coordinates.
			Double starX = transformedCoords[observedSky.stars().indexOf(star)*2];
			Double starY = transformedCoords[observedSky.stars().indexOf(star)*2+ 1];

			//Set the color to the star's one based on it's color temperature.
			ctx.setFill(BlackBodyColor.colorForTemperature(star.colorTemperature()));
			
			//Drawing the star.
			ctx.fillOval(starX, starY, transformedSize.magnitude(), transformedSize.magnitude());
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
			
			
			System.out.println("D : " + d);
			//Transforming the disk size according to the affine transformation.
			Point2D transformedSize = transform.deltaTransform(d,d);
			
			//Defining the planet's coordinates.
			System.out.println(observedSky.planets().indexOf(planet));
			Double planetX = transformedCoords[observedSky.planets().indexOf(planet) * 2];
			System.out.println(planetX);
			Double planetY = transformedCoords[observedSky.planets().indexOf(planet) * 2 + 1];
	         System.out.println(planetY);
			
			//Drawing the planet on the canvas.
			ctx.fillOval(planetX, planetY, transformedSize.magnitude(), transformedSize.magnitude());
		}
		
		
	}
	
	public void drawSun(ObservedSky observedSky, StereographicProjection projection, Transform transform) {
		Point2D sunPos = transform.transform(observedSky.sunPosition().x(), observedSky.sunPosition().y());
		Double sunSize = projection.applyToAngle(observedSky.sun().angularSize()) * (transform.getMxx() * 2);
		Point2D relativeSunSize = transform.deltaTransform(sunSize,sunSize);
		
		ctx.setFill(Color.YELLOW);
        ctx.setGlobalAlpha(.25);
		ctx.fillOval(sunPos.getX(), sunPos.getY(), relativeSunSize.magnitude()*2.2, relativeSunSize.magnitude()*2.2);

        ctx.setGlobalAlpha(1);
        ctx.fillOval(sunPos.getX(), sunPos.getY(), relativeSunSize.magnitude()+2, relativeSunSize.magnitude()+2);

		
		ctx.setFill(Color.WHITE);
		ctx.fillOval(sunPos.getX(), sunPos.getY(), relativeSunSize.magnitude(), relativeSunSize.magnitude());
	}
	
	public void drawMoon(ObservedSky observedSky, StereographicProjection projection, Transform transform) {
		Point2D moonPos = transform.transform(observedSky.moonPosition().x(), observedSky.moonPosition().y());
		Double moonSize = projection.applyToAngle(observedSky.moon().angularSize());
		Point2D relativeMoonSize = transform.deltaTransform(moonSize,moonSize);
		
		System.out.println(moonSize);
        
		ctx.setFill(Color.WHITE);
		ctx.fillOval(moonPos.getX(), moonPos.getY(), relativeMoonSize.magnitude(), relativeMoonSize.magnitude());
	}
}

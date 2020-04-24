package ch.epfl.rigel.gui;

import java.io.IOException;

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
			double clippedMag = MAGNITUDE_INTERVAL.clip(star.magnitude());
			double f = (99 - 17 * clippedMag) / 140;
			double d = f * 2 * Math.tan(Angle.ofDeg(0.5) / 4.);
			
			//Transforming the disk size according to the affine transformation.
			double transformedSize = transform.deltaTransform(0, d).magnitude();
			//Defining the star coordinates. 
			double starX = transformedCoords[observedSky.stars().indexOf(star)*2] - transformedSize/2;
			double starY = transformedCoords[observedSky.stars().indexOf(star)*2+ 1] - transformedSize/2;

			//Set the color to the star's one based on it's color temperature.
			ctx.setFill(BlackBodyColor.colorForTemperature(star.colorTemperature()));
			
			//Drawing the star.
			ctx.fillOval(starX, starY, transformedSize, transformedSize);
		}
	}
	
	
	public void drawPlanets(ObservedSky observedSky, StereographicProjection projection, Transform transform) {
		//Transforming all stars' coordinates according to affine transformation.
		double[] transformedCoords = new double[observedSky.planetPositions().length];
		transform.transform2DPoints(observedSky.planetPositions(), 0, transformedCoords, 0, transformedCoords.length/2);
		
		//Setting the planets' color.
		ctx.setFill(Color.LIGHTGRAY);
		
		for(Planet planet: observedSky.planets()) {
			//Defining the planet's disk size based on it's magnitude.
			double clippedMag = MAGNITUDE_INTERVAL.clip(planet.magnitude());
			double f = (99 - 17 * clippedMag) / 140;
			double d = f * 2 * Math.tan(Angle.ofDeg(0.5) / 4.);
			
			//Transforming the disk size according to the affine transformation.
			double transformedSize = transform.deltaTransform(0,d).magnitude();
			
			//Defining the planet's coordinates.
			double planetX = transformedCoords[observedSky.planets().indexOf(planet)] - transformedSize/2;
			double planetY = transformedCoords[observedSky.planets().indexOf(planet) + 1] - transformedSize/2;
			
			//Drawing the planet on the canvas.
			ctx.fillOval(planetX, planetY, transformedSize, transformedSize);
		}
		
		
	}
	
	public void drawSun(ObservedSky observedSky, StereographicProjection projection, Transform transform) {
		Point2D sunPos = transform.transform(observedSky.sunPosition().x(), observedSky.sunPosition().y());
		double sunSize = projection.applyToAngle(observedSky.sun().angularSize());// 2 * Math.tan(.5/4.);
		double sunSizetransf = transform.deltaTransform(0, sunSize).magnitude();
		ctx.setFill(Color.WHITE);
		ctx.fillOval(sunPos.getX(), sunPos.getY(), sunSizetransf, sunSizetransf);
		ctx.setFill(Color.YELLOW);
		ctx.fillOval(sunPos.getX()+1, sunPos.getY()+1, sunSizetransf+2, sunSizetransf+2);
		ctx.setGlobalAlpha(.25);
		ctx.fillOval(sunPos.getX()- sunSizetransf*.55, sunPos.getY()- sunSizetransf *.55, sunSizetransf*2.2,	 sunSizetransf*2.2);
		ctx.setGlobalAlpha(1);
	}
	
	public void drawMoon(ObservedSky observedSky, StereographicProjection projection, Transform transform) {
		Point2D moonPos = transform.transform(observedSky.moonPosition().x(), observedSky.moonPosition().x());
		double moonSize = projection.applyToAngle(observedSky.moon().angularSize());
		double moonSizeTransf = transform.deltaTransform(0, moonSize).magnitude();
		ctx.setFill(Color.WHITE);
		ctx.fillOval(moonPos.getX(), moonPos.getY(), moonSizeTransf, moonSizeTransf);
	}
}

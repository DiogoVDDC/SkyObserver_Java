package ch.epfl.rigel.gui;

import java.io.IOException;

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
 * 
 * @author Theo Houle (312432)
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public final class SkyCanvasPainter {

	// Offset used to slightly move the names of the objects down.
    private static final int NAMES_OFFSET = 3;
	private Canvas canvas;
    private final GraphicsContext ctx;
    private static final ClosedInterval MAGNITUDE_INTERVAL = ClosedInterval
            .of(-2, 5);

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
     * @param observedSky:
     *            the observed sky from which we will draw the stars and asterism
     * @param projection:
     *            projection of the point which we will draw arround
     * @param transform:
     *            plane to canvas affine transform
     * @throws IOException:
     *             throws IO exception if black body colour throws an exception
     */
    public void drawStars(ObservedSky observedSky,
            StereographicProjection projection, Transform transform)
            throws IOException {
        
        double[] transformedCoords = new double[observedSky.starPositions().length];
        // Transforming all stars positions accoridng to the affine
        // transformation.
        transform.transform2DPoints(observedSky.starPositions(), 0,
                transformedCoords, 0, transformedCoords.length / 2);

        // Set stroke color to blue.
        ctx.setStroke(Color.BLUE);
        // Set stroke width to 1.
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
                Double starX = transformedCoords[observedSky.stars()
                        .indexOf(star) * 2];
                Double starY = transformedCoords[observedSky.stars()
                        .indexOf(star) * 2 + 1];

                // only draw a line if the current or the previous star was
                // contained on the canvas
                if (bound.contains(new Point2D(starX, starY))
                        || isPreviousContained) {
                    
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

        // Drawing the stars
        for (Star star : observedSky.stars()) {
            // Defining the star's disk size based on it's magnitude.
            double clippedMag = MAGNITUDE_INTERVAL.clip(star.magnitude());
            double f = (99 - 17 * clippedMag) / 140;
            double d = f * 2 * Math.tan(Angle.ofDeg(0.5) / 4.);

            // Transforming the disk size according to the affine
            // transformation.
            double transformedSize = transform.deltaTransform(0, d).magnitude();
            // Defining the star coordinates and finding top left coordinates.
            double starX = transformedCoords[observedSky.stars().indexOf(star) * 2];
            double starY = transformedCoords[observedSky.stars().indexOf(star) * 2 + 1];
            Point2D starPos = adjustCoordinate(new Point2D(starX, starY),
                    transformedSize / 2);
            // Set the color to the star's one based on it's color temperature.
            ctx.setFill(BlackBodyColor
                    .colorForTemperature(star.colorTemperature()));

            // Drawing the star.
            ctx.fillOval(starPos.getX(), starPos.getY(), transformedSize,
                    transformedSize);
        }
    }

    /**
     * Draws planets to the canvas
     * 
     * @param observedSky:
     *            the observed sky from which we will draw the planets
     * @param projection:
     *            projection of the point which we will draw arround
     * @param transform:
     *            plane to canvas affine transform
     */
    public void drawPlanets(ObservedSky observedSky, StereographicProjection projection, 
    					Transform transform, boolean writeName) {
        double[] transformedCoords = new double[observedSky
                .planetPositions().length];
        // Transforming all stars positions accoridng to the affine
        // transformation.
        transform.transform2DPoints(observedSky.planetPositions(), 0,
                transformedCoords, 0, transformedCoords.length / 2);

        // Setting the planet's color.
        ctx.setFill(Color.LIGHTGRAY);

        for (Planet planet : observedSky.planets()) {
            // Defining the planet's disk size based on it's magnitude.
            double clippedMag = MAGNITUDE_INTERVAL.clip(planet.magnitude());
            double f = (99 - 17 * clippedMag) / 140;
            double d = f * 2 * Math.tan(Angle.ofDeg(0.5) / 4.);

            // Transforming the disk size according to the affine
            // transformation.
            double transformedSize = transform.deltaTransform(0, d).magnitude();

            // Defining the planet's coordinates and finding top left corner
            // coordinates.
            double planetX = transformedCoords[observedSky.planets()
                    .indexOf(planet)];
            double planetY = transformedCoords[observedSky.planets()
                    .indexOf(planet) + 1];
            Point2D planetPos = adjustCoordinate(new Point2D(planetX, planetY),
                    transformedSize / 2);

            // Drawing the planet on the canvas.
            ctx.fillOval(planetPos.getX(), planetPos.getY(), transformedSize,
                    transformedSize);
            
            //Write the name of the planet if option is enabled.
            if(writeName)
            	ctx.fillText(planet.name(), planetX, planetY + NAMES_OFFSET); 
        }

    }

    /**
     * Draws sun to the canvas
     * @param observedSky:
     *            the observed sky from which we will draw the sun
     * @param projection:
     *            projection of the point which we will draw arround
     * @param transform:
     *            plane to canvas affine transform
     */
    public void drawSun(ObservedSky observedSky, StereographicProjection projection, 
    				Transform transform, boolean writeName) {
        // Finding the radius of the sun using the angular size.
        double sunSize = projection
                .applyToAngle(observedSky.sun().angularSize());
        // Finding the relative size which depends of the affine transform
        double relativeSunSize = transform.deltaTransform(sunSize, 0)
                .magnitude();
        // Defining the sun position as a point on the affine transform and.
        Point2D sunPos = transform.transform(observedSky.sunPosition().x(),
                observedSky.sunPosition().y());
        
        // Drawing the sun halo
        Point2D haloPos = adjustCoordinate(sunPos, relativeSunSize * 1.1);
        ctx.setFill(Color.YELLOW.deriveColor(0, 1, 1, 0.25));
        ctx.fillOval(haloPos.getX(), haloPos.getY(), relativeSunSize * 2.2,
                relativeSunSize * 2.2);

        // Drawing the sun outer core
        Point2D outterCorePos = adjustCoordinate(sunPos,
                (relativeSunSize + 2) / 2);
        ctx.setFill(Color.YELLOW);
        ctx.fillOval(outterCorePos.getX(), outterCorePos.getY(),
                relativeSunSize + 2, relativeSunSize + 2);

        // Drawing the sun core
        Point2D adjustedSunPos = adjustCoordinate(sunPos, relativeSunSize / 2);
        ctx.setFill(Color.WHITE);
        ctx.fillOval(adjustedSunPos.getX(), adjustedSunPos.getY(),
                relativeSunSize, relativeSunSize);
        
        //Writes the name of the sun next to it if enabled.
        if(writeName) {
        	ctx.fillText(observedSky.sun().name(), sunPos.getX(), sunPos.getY() + NAMES_OFFSET);
        }

    }

    /**
     * Draws moon to the canvas
     * @param observedSky:
     *            the observed sky from which we will draw the moon
     * @param projection:
     *            projection of the point which we will draw arround
     * @param transform:
     *            plane to canvas affine transform
     */
    public void drawMoon(ObservedSky observedSky, StereographicProjection projection,
    					Transform transform, boolean writeName) {

        // Finding the diameter of the sun using the angular size
        double moonSize = projection
                .applyToAngle(observedSky.moon().angularSize());
        
        // Finding the relative size which depends of the affine transform
        double relativeMoonSize = transform.deltaTransform(moonSize, 0)
                .magnitude();
        
        // Defining the moon position as a point on the affine transform and
        // offsetting the center point.
        Point2D moonPos = transform.transform(observedSky.moonPosition().x(),
                observedSky.moonPosition().y());
        Point2D adjustedPos = adjustCoordinate(moonPos, relativeMoonSize/2);

        // Drawing the moon
        ctx.setFill(Color.WHITE);
        ctx.fillOval(adjustedPos.getX(), adjustedPos.getY(), relativeMoonSize,
                relativeMoonSize);
        
        // Draws the name if option is enabled.
        if(writeName) {
        	ctx.fillText(observedSky.moon().name(), moonPos.getX(), moonPos.getY() + NAMES_OFFSET);
        }
    }

    /**
     * Draws the horizon and cardinal points
     * @param observedSky:
     *            the observed sky from which we will draw the horizon
     * @param projection:
     *            projection of the point which we will draw around
     * @param transform:
     *            plane to canvas affine transform
     */
    public void drawHorizon(ObservedSky observedSky,
            StereographicProjection projection, Transform transform) {
        
        // Defining the horizon object
        HorizontalCoordinates horizon = HorizontalCoordinates.ofDeg(0, 0);

        // Projecting the center of the circle
        CartesianCoordinates horizonCoords = projection
                .circleCenterForParallel(horizon);
        // Creating a point from the centre of the circle
        Point2D transHorizonCoords = transform.transform(horizonCoords.x(),
                horizonCoords.y());

        // Defining the the radius of the circle
        double horizonRadius = projection.circleRadiusForParallel(horizon);
        // Ajusting the radius to the affine transform
        double transHorizonRadius = transform.deltaTransform(horizonRadius, 0)
                .magnitude();

        // Drawing the horizon circle
        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2);
        ctx.strokeOval(transHorizonCoords.getX() - transHorizonRadius,
                transHorizonCoords.getY() - transHorizonRadius,
                transHorizonRadius * 2, transHorizonRadius * 2);
        
        // Setting the parameters for the cardinal point text
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(VPos.TOP);
        ctx.setLineWidth(1);
        ctx.setStroke(Color.YELLOW);

        for(int i = 0; i<360; i+=45) {
        	HorizontalCoordinates coords = HorizontalCoordinates.ofDeg(i, -0.5);
        	// Getting the coordinates at which the text will be drawn
        	CartesianCoordinates cartCoords = projection.apply(coords);
        	// Ajusting the coordinates to the affine transform
        	Point2D transCoord = transform.transform(cartCoords.x(), cartCoords.y());
        	// Drawing the cardinal point text
        	ctx.strokeText(coords.azOctantName("N", "E", "S", "W"), transCoord.getX(), transCoord.getY());
        }
    }
    
    public void drawMeridians(ObservedSky observedSky,
            StereographicProjection projection, Transform transform) {
        
        
        for(int i = -90; i<90; i+=15) {
            HorizontalCoordinates meridian = HorizontalCoordinates.ofDeg(0, i);
         
            // Projecting the center of the circle
            CartesianCoordinates meridianCoords = projection
                    .circleCenterForMeridian(meridian);
            
            double radius = projection.circleRadiusForMeridian(meridian);
            
            double transMeridianRadius = transform.deltaTransform(radius, 0)
                    .magnitude();
            
            
            Point2D transMeridianCoords = transform.transform(meridianCoords.x(),
                    meridianCoords.y());          
           

        
            ctx.setStroke(Color.SKYBLUE);
            ctx.setLineWidth(0.2);
            ctx.strokeOval(transMeridianCoords.getX() - transMeridianRadius,
                    transMeridianCoords.getY() - transMeridianRadius,
                    transMeridianRadius * 2, transMeridianRadius * 2);
        }
    }
    
    public void drawLatitudeCircle(ObservedSky observedSky,
            StereographicProjection projection, Transform transform) {
        
        for(int i = -90; i<90; i+=15) {
            HorizontalCoordinates latitude = HorizontalCoordinates.ofDeg(0, i);
         
            // Projecting the center of the circle
            CartesianCoordinates latitudeCoords = projection
                    .circleCenterForParallel(latitude);
            
            double radius = projection.circleRadiusForParallel(latitude);
            
            double transLatitudeRadius = transform.deltaTransform(radius, 0)
                    .magnitude();
            
            
            Point2D transLatitudeCoords = transform.transform(latitudeCoords.x(),
                    latitudeCoords.y());          
           
            ctx.setStroke(Color.SKYBLUE);
            ctx.setLineWidth(0.2);
            ctx.strokeOval(transLatitudeCoords.getX() - transLatitudeRadius,
                    transLatitudeCoords.getY() - transLatitudeRadius,
                    transLatitudeRadius * 2, transLatitudeRadius * 2);
        }
    }

    /**
     * Allows to draw all celestial object of the sky.
     * @param observedSky:
     *            the observed sky
     * @param projection:
     *            the stereographique projection.
     * @param transform:
     *            the plane to canvas projection.
     * @throws IOException:
     *             if there an issue while the painter draws the stars.
     */
    public void drawSky(ObservedSky observedSky, StereographicProjection projection,
    							Transform transform, boolean writeName, boolean drawLonLatLines)
            throws IOException {
        drawStars(observedSky, projection, transform);
        drawPlanets(observedSky, projection, transform, writeName);
        drawSun(observedSky, projection, transform, writeName);
        drawMoon(observedSky, projection, transform, writeName);
        drawHorizon(observedSky, projection, transform);
        if(drawLonLatLines) {
        	drawMeridians(observedSky, projection, transform);
        	drawLatitudeCircle(observedSky, projection, transform);
        }
    }

    /**
     * Given the center coordinates of the circle, returns the top left
     * coordinates of the square in which the circle is inscribed.
     * 
     * @param center:
     *            center coordinates of the circle.
     * @param radius:
     *            the radius of the circle.
     * @return: 
     *          returns point ajusted by the radius
     */
    private Point2D adjustCoordinate(Point2D center, double radius) {
        return new Point2D(center.getX() - radius, center.getY() - radius);
    }
}

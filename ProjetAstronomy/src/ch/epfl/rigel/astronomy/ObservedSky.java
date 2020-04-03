package ch.epfl.rigel.astronomy;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;

/**
 * Representation of the sky at a give time and position.
 * @author Theo Houle (312432)
 *
 */
public class ObservedSky {
	
	//Model of the sun at the given time.
	private final Sun sun;
	//Position in Cartesian coordinates of the sun.
	private final CartesianCoordinates sunPosition;
	//Model of the moon at the given time.
	private final Moon moon;
	//Position in Cartesian coordinates of the moon
	private final CartesianCoordinates moonPosition;
	//Models of the 7 planets at a given time.
	private final Planet[] planets;
	/**
	 * Positions in Cartesian coordinates of the planets. Each star has two slot,
	 * one for it's x coordinate and the second for the y coordinate.
	 */
	private final Double[] planetPositions;
	//
	private final Star[] stars;
	//Position of the stars stored the same way as the planets' positions.
	private final Double[] starPositions;
	//Catalogue of stars and asterisms.
	private final StarCatalogue catalogue;
	//List containing every celestial objects
	private final Map<CartesianCoordinates, CelestialObject> all;
	
	/**
	 * Constructor of ObservedSky. Given the time, position, stars, and stereographique projection, 
	 * computes the specific models and position of the different celestial objects in the sky (sun,
	 * moon, planets, stars).
	 * @param when: date and time of the observation.
	 * @param where: position of the observer.
	 * @param projection: stereographic projection used to project celestial objects positions
	 * on the Cartesian plane.
	 * @param catalogue: Catalogue containing the stars and it's asterism.
	 */
	public ObservedSky(ZonedDateTime when, GeographicCoordinates where, 
			StereographicProjection projection, StarCatalogue catalogue) {
		
		this.catalogue = catalogue;
		
		//Number of days since J2010 for the given date "when".
		double daysSinceJ2010 = Epoch.J2010.daysUntil(when);
		
		//Useful conversion classes allowing to find horizontal coordinates to later convert to cartesian coordinates.
		EclipticToEquatorialConversion convEclToEqu = new EclipticToEquatorialConversion(when);
		EquatorialToHorizontalConversion convEquToHor = new EquatorialToHorizontalConversion(when, where);
		
		//Initialization of the map which will contain every celestial objects and their positions.
		all = new HashMap<>();
		
		//Modelization of the moon and it's position in Cartesian coordinates.
		moon = MoonModel.MOON.at(daysSinceJ2010, convEclToEqu);
		moonPosition = projection.apply(convEquToHor.apply(moon.equatorialPos()));
		//Adding the moon and it's position in the map containing all object and their positions.
		all.put(moonPosition, moon);
		
		//Modelization of the sun and it's position in Cartesian coordinates.
		sun = SunModel.SUN.at(daysSinceJ2010, convEclToEqu);
		sunPosition = projection.apply(convEquToHor.apply(sun.equatorialPos()));
		//Adding the sun and it's position in the map containing all object and their positions.
		all.put(sunPosition, sun);
		
		//Modelization of the planets and their positions in Cartesian coordinates.
		planets = new Planet[7];
		planetPositions = new Double[14];
		for (PlanetModel planetModel : PlanetModel.ALL) {
			//Modelization of the planet at the given moment.
			Planet planet = planetModel.at(daysSinceJ2010, convEclToEqu);
			//Conversion to cartesian coordinates of the planet.
			CartesianCoordinates planetPos = projection.apply(convEquToHor.apply(planet.equatorialPos()));
			planets[planetModel.ordinal()] = planet;
			planetPositions[2 * planetModel.ordinal()] = planetPos.x();
			planetPositions[2 * planetModel.ordinal() + 1] = planetPos.y();
			//Adding the planet and it's position in the map containing all object and their positions.
			all.put(planetPos, planet);
		}
		
		stars = (Star[]) catalogue.stars().toArray();
		//Defining the stars positions in Cartiesian coordinates.
		starPositions = new Double[catalogue.stars().size()*2];
		for (int i = 0; i < stars.length; i++) {
			Star star = catalogue.stars().get(i);
			CartesianCoordinates starPos = projection.apply(convEquToHor.apply(star.equatorialPos()));
			starPositions[2 * i] = starPos.x();
			starPositions[2 * i + 1] = starPos.y();
			//Adding the star and it's position in the map containing all object and their positions.
			all.put(starPos, star);
		}
	}
	
	/**
	 * Getter for the model of the sun.
	 * @return: model of the sun.
	 */
	public Sun sun() {
		return sun;
	}
	
	/**
	 * Getter for the position of the sun in Cartesian coordinates.
	 * @return: position of the sun.
	 */
	public CartesianCoordinates sunPosition() {
		return sunPosition;
	}
	
	/**
	 * Getter for the model of the moon.
	 * @return: the moon model.
	 */
	public Moon moon() {
		return moon;
	}
	
	/**
	 * Getter for the position of the moon in Cartesian coordinates.
	 * @return: the moon's position.
	 */
	public CartesianCoordinates moonPosition() {
		return moonPosition;
	}
	
	/**
	 * Getter for the models of the planets.
	 * @return: table containing each model of the planets.
	 */
	public Planet[] planets() {
		return planets.clone();
	}
	
	/**
	 * Getter for the position of the planets
	 * @return: table containing the Cartesian coordinates of the planets.
	 * Each planet has to slot, one for it's x coord and the other for the y
	 * coord.
	 */
	public Double[] planetPositions() {
		return planetPositions.clone();
	}
	
	/**
	 * Getter for the stars.
	 * @return: table containing each stars.
	 */
	public Star[] stars() {
		return stars.clone();
	}
	
	/**
	 * Getter for the stars' positions.
	 * @return: table containing each star's position arranged the same way
	 * as the positions of the planets.
	 */
	public Double[] starPositions() {
		return starPositions.clone();
	}
	
	/**
	 * Getter for the set of asterism in the catalogue.
	 * @return: a Set of asterism.
	 */
	public Set<Asterism> asterisms(){
		return Set.copyOf(catalogue.asterisms());
	}
	
	/**
	 * Getter for the list of indices of the star in the given asterism.
	 * @param asterism: asterism for which we need to find the list of indices.
	 * @return: List containing the list of indices of the stars in the same
	 * order as in the asterism.
	 */
	public List<Integer> starIndices(Asterism asterism){
		return List.copyOf(catalogue.asterismIndices(asterism));
	}
	
	/**
	 * Getter for the closest celestial object to the given position in a given radius.
	 * @param pos
	 * @param maxRange
	 * @return
	 */
	public Optional<CelestialObject> objectClosestTo(CartesianCoordinates pos, Double maxRange) {
		//Map containing all the objects that are in the range before comparing the distances.
		Map<CartesianCoordinates, CelestialObject> inRadiusObjects = new HashMap<>();
		for (Entry<CartesianCoordinates, CelestialObject> obj : all.entrySet()) {
			CartesianCoordinates objPos = obj.getKey();
			if (Math.abs(objPos.x()-pos.x()) <= maxRange  || Math.abs(objPos.y()-pos.y()) <= maxRange) {
				inRadiusObjects.put(objPos, obj.getValue());
			}
		}
		/**
		 * given the map preivously created, compute the distance and if smaller than the current smallest,
		 * updates the distance and the closest object.
		 */
		double minDist = maxRange;
		//Null by default to be able to know wether the for loop found a an object in the given range or not.
		CelestialObject closest = null;
		for (CartesianCoordinates coord : all.keySet()) {
			double dist = getDistance(pos, coord);
			/**
			 * Checks if the range is also respected because the map contains object that are within the square of length 
			 * 2 times maxRange around the given position meaning that this map may contain objects that are at a greater
			 * distance than maxRange.
			 */
			if (dist < minDist && dist <= maxRange) {
				minDist = getDistance(pos, coord);
				closest = all.get(coord);
			}
		}
		/*
		 * If the closest object it still null, meaning that there aren't any object within the radius, returns an empty 
		 * cell. Else return the closest object.
		 */
		if(closest == null) return Optional.empty();
		else return Optional.of(closest);
		
	}
	
	/**
	 * Method to get the distance between to points.
	 * @param c1: first coordinate
	 * @param c2: second coordinate.
	 * @return: the distance between the 2 points.
	 */
	private double getDistance(CartesianCoordinates c1, CartesianCoordinates c2) {
		double x = c2.x() - c1.x();
		double y = c2.y() - c1.y();
		return Math.sqrt(x*x + y*y);
	}
	
	
}

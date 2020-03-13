package ch.epfl.rigel.astronomy;

import java.util.List;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

public enum PlanetModel implements CelestialObjectModel<Planet> {

	MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
	        0.387098, 7.0051, 48.449, 6.74, -0.42),
	VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
	      0.723329, 3.3947, 76.769, 16.92, -4.40),
	EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
	      0.999985, 0, 0, 0, 0),
	MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
	     1.523689, 1.8497, 49.632, 9.36, -1.52),
	JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
	        5.20278, 1.3035, 100.595, 196.74, -9.40),
	SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
	       9.51134, 2.4873, 113.752, 165.60, -8.88),
	URANUS("Uranus", 84.039492, 271.063148, 172.884833, 0.046321,
	       19.21814, 0.773059, 73.926961, 65.80, -7.19),
	NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
	        30.1985, 1.7673, 131.879, 62.20, -6.87);
	
	// French name of the planet.
	private final String frenchName;
	// Revolution period.
	private final double revoPeriod;
	//Longitude of the planet at J2010.
	private final double lonAtJ2010;
	//Longitude of the planet at Perigee.
	private final double lonAtPerigee;
	//Eccentricity of the orbit.
	private final double orbitEccentricity;
	// Semi-major axis of the orbit.
	private final double  semiMajorAxis;
	// Orbit's tilt at the ecliptic.
	private final double orbitTiltAtEcliptic;
	// Longitude of the orbital node.
	private final double lonOrbitalNode;
	//Angular size of the planet.
	private final double angularSize;
	// Magnitude of the planet.
	private final double magnitude;
	// Immutable list containing all the object of this enum.
	public static List<PlanetModel> ALL = List.of(PlanetModel.MERCURY, PlanetModel.VENUS, PlanetModel.EARTH,
				PlanetModel.MARS, PlanetModel.JUPITER, PlanetModel.SATURN, PlanetModel.URANUS, PlanetModel.NEPTUNE);
	
	private PlanetModel(String frenchName, double revoPeriod, double lonAtJ2010, double lonAtPerigee, double orbitEccentricity,
						double semiMajorAxis, double orbitTiltAtEcliptic, double lonOrbitalNode, double angularSize, double magnitude) {
		this.frenchName = frenchName;
		this.revoPeriod = revoPeriod;
		this.lonAtJ2010 = Angle.ofDeg(lonAtJ2010);
		this.lonAtPerigee = Angle.ofDeg(lonAtPerigee);
		this.orbitEccentricity = orbitEccentricity;
		this.semiMajorAxis = semiMajorAxis;
		this.orbitTiltAtEcliptic = Angle.ofDeg(orbitTiltAtEcliptic);
		this.lonOrbitalNode = Angle.toDeg(lonOrbitalNode);
		this.angularSize = angularSize;
		this.magnitude = magnitude;
	}
	
	private double trueAnomaly(double daysSinceJ2010, PlanetModel planet) {
		//Mean anomaly
		double M = ( Angle.TAU / 365.242191 ) * (daysSinceJ2010 / planet.revoPeriod) + planet.lonAtJ2010 - planet.lonAtPerigee;
		//True anomaly
		return M + (2 * planet.orbitEccentricity * Math.sin(M));
	}
	
	@Override
	public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
		//Radiuses of the planet and the earth.
		double trueAnom = trueAnomaly(daysSinceJ2010, this);
		double trueAnomEarth = trueAnomaly(daysSinceJ2010, EARTH);
		double r = (semiMajorAxis * (1 - Math.pow(orbitEccentricity, 2))) / (1 + orbitEccentricity * Math.cos(trueAnom));
		double rEarth = (EARTH.semiMajorAxis * (1 - Math.pow(EARTH.orbitEccentricity, 2))) / (1 + EARTH.orbitEccentricity * Math.cos(trueAnomEarth));
		
		// Longitude of the planet and earth  in the plan of it's own orbit.
		double lonInOwnOrbit = trueAnom + lonAtPerigee;
		double lonInOwnOrbitEarth = trueAnomEarth + EARTH.lonAtPerigee;
		
		// Heliocentric ecliptic latitude of the planet.
		double helioCenEclipticLat = Math.asin(Math.sin(lonInOwnOrbit - lonOrbitalNode) * Math.sin(orbitTiltAtEcliptic));
		// The radius of the planet projected on ecliptic plan.
		double rProj = r * Math.cos(helioCenEclipticLat);
		//Longitude projected on ecliptic plan.
		double lonInOwnOrbitProj = Math.atan2((Math.sin(lonInOwnOrbit - lonOrbitalNode) * Math.cos(orbitTiltAtEcliptic)),
									Math.cos(lonInOwnOrbit - lonOrbitalNode));
		
		double eclipticLon;
		switch(frenchName) {
			case "Mercure":
			case "Vénus":
				//Calculus of longitude for inner planets.
				eclipticLon = Math.PI + lonInOwnOrbitEarth + Math.atan2(rProj * Math.sin(lonInOwnOrbitEarth - lonInOwnOrbitProj),
									rEarth - rProj * Math.cos(lonInOwnOrbitEarth - lonInOwnOrbitProj));
				break;
			default:
				//Calculus of longitude for outer planets.
				eclipticLon = lonInOwnOrbitProj + Math.atan2(rEarth * Math.sin(lonInOwnOrbitProj - lonInOwnOrbitEarth),
								rProj - rEarth * Math.cos(lonInOwnOrbitProj - lonInOwnOrbitEarth));
				break;
		}
		
		//Latitude of the planet
		double eclipticLat = Math.atan2((rProj * Math.tan(helioCenEclipticLat) * Math.sin(eclipticLon - lonInOwnOrbitProj)),
						rEarth * Math.sin(lonInOwnOrbitProj) - lonInOwnOrbitEarth);
				
		//Ecliptic coordinates of the planet
		EclipticCoordinates eclipticPos = EclipticCoordinates.of(eclipticLon, eclipticLat);
		
		return new Planet(frenchName, eclipticToEquatorialConversion.apply(eclipticPos), (float)angularSize, (float)magnitude);
	}

}

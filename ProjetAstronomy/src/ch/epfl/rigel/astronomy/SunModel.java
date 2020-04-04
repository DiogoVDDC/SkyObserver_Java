package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

/**
 * Class allowing to model a sun at a given time.
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 * @author Theo Houle (312432)
 *
 */
public enum SunModel implements CelestialObjectModel<Sun>{
	
	SUN(279.557208, 283.112438, 0.016705);
	//Longitude of the sun at J2010.
	private final double lonAtJ2010;
	//Longitude of the sun at Perigee.
	private final double lonAtPerigee;
	//Orbit eccentricity of the sun.
	private final double orbitEccentricity;
	
	/**
	 * Constructor for SunModel.
	 * @param lonAtJ2010: longitude at J2010 (in degrees).
	 * @param lonAtPerigee: lonitude at the Perigee (in degrees).
	 * @param orbitEccentricity: Orbit eccentricity.
	 */
	private SunModel(double lonAtJ2010, double lonAtPerigee, double orbitEccentricity) {
		this.lonAtJ2010 = Angle.ofDeg(lonAtJ2010);
		this.lonAtPerigee = Angle.ofDeg(lonAtPerigee);
		this.orbitEccentricity = orbitEccentricity;
	}
	
	@Override
	public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
		//Mean anomaly of the sun.
		double M = (Angle.TAU /365.242191) * daysSinceJ2010 + lonAtJ2010 - lonAtPerigee;
		//True anomaly of the sun.
		double v = M + 2 * orbitEccentricity * Math.sin(M);
		//Ecliptic longitude of the sun.
		double eclipticLon = Angle.normalizePositive(v + lonAtPerigee);
		//Ecliptic lattitude of the sun.
		double eclipticLat = 0;
		//Ecliptic coordinates of the sun given the previously calculated longitude and lattitude.
		EclipticCoordinates eclipticPos = EclipticCoordinates.of(eclipticLon, eclipticLat);
		//Angular size of the sun.
		double angularSize = Angle.ofDeg(0.533128 * ( (1 + orbitEccentricity * Math.cos(v)) / (1 - Math.pow(orbitEccentricity, 2))));
		
		return new Sun(eclipticPos, eclipticToEquatorialConversion.apply(eclipticPos), (float)angularSize, (float)Angle.normalizePositive(M));
	}
	

}

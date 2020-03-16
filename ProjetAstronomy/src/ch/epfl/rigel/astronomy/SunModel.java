package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

public enum SunModel implements CelestialObjectModel<Sun>{
	
	SUN(279.557208, 283.112438, 0.016705);
	private final double lonAtJ2010;
	private final double lonAtPerigee;
	private final double orbitEccentricity;
	
	
	private SunModel(double lonAtJ2010, double lonAtPerigee, double orbitEccentricity) {
		this.lonAtJ2010 = Angle.ofDeg(lonAtJ2010);
		this.lonAtPerigee = Angle.ofDeg(lonAtPerigee);
		this.orbitEccentricity = orbitEccentricity;
	}
	
	@Override
	public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
		//Mean anomaly of the sun.
		double M = (Angle.TAU / 365.242191) * daysSinceJ2010 + lonAtJ2010 - lonAtPerigee;
		//True anomaly of the sun.
		double v = M + 2 * orbitEccentricity * Math.sin(M);
		double eclipticLon = Angle.normalizePositive(v + lonAtPerigee);
		double eclipticLat = 0;
		
		EclipticCoordinates eclipticPos = EclipticCoordinates.of(eclipticLon, eclipticLat);
		
		double angularSize = Angle.ofDeg(0.533128 * ( (1 + orbitEccentricity * Math.cos(v)) / (1 - Math.pow(orbitEccentricity, 2))));
		
		return new Sun(eclipticPos, eclipticToEquatorialConversion.apply(eclipticPos), (float)angularSize, (float)Angle.normalizePositive(M));
	}
	

}

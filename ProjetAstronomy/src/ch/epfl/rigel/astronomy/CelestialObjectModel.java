package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

public interface CelestialObjectModel<O> {
	
	/**
	 * Return the object modelised via the model for the given number of days past J2010.
	 * @param daysSinceJ2010: Number of days bewteen J2010 and the wanted day of representation.
	 * @param eclipticToEquatorialConversion : the corresponding ecliptic to equatorial conversion.
	 * @return: the modelised object.
	 */
	public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
	
}


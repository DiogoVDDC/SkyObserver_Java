package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * Enum allowing to create a model of the moon at a given time.
 * @author Theo Houle (312432)
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 *
 */
public enum MoonModel implements CelestialObjectModel<Moon>{
    // in degrees 
    MOON(91.929336,130.143076,291.682547,5.145396,0.0549);
    
	//Mean longitude
    private final double averageLon;
    //Mean longitude at Perigee
    private final double averageLonPerigee;
    //Longitude of ascending node
    private final double lonAscendNode;
    //Orbital Tilt
    private final double orbitTilt;
    //Eccentricity of the orbit
    private final double orbitEccen;
    private ClosedInterval phaseInterval = ClosedInterval.of(0, 1);

    /**
     * MoonModel constructor.
     * @param averageLon: Mean longitude of the moon (in degrees).
     * @param averagePerigreeLon: Mean longitude at Perigee (in degrees).
     * @param nodeLon: Longitude of the ascending node (in degrees).
     * @param orbitTilt: Tilt angle of the orbit (in degrees).
     * @param orbitEccentricity: Eccentricity of the orbit
     */
    MoonModel(double averageLon, double averagePerigreeLon, double nodeLon, double orbitTilt, double orbitEccentricity) {
        this.averageLon = Angle.ofDeg(averageLon);
        this.averageLonPerigee = Angle.ofDeg(averagePerigreeLon);
        this.lonAscendNode = Angle.ofDeg(nodeLon);
        this.orbitTilt = Angle.ofDeg(orbitTilt);
        this.orbitEccen = orbitEccentricity;
    }

    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        Sun sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        
        //Mean orbital longitude.
        double meanOrbitLon = Angle.normalizePositive(Angle.ofDeg(13.1763966) * daysSinceJ2010 + averageLon);
        
        //Moon's mean anomaly
        double meanAnomaly = Angle.normalizePositive(meanOrbitLon - Angle.ofDeg(0.1114041) * daysSinceJ2010 - averageLonPerigee);
        
        //Correcting value based on the sun's influence on the moon.
        double evection = Angle.ofDeg(1.2739) * Math.sin(2*(meanOrbitLon - sun.eclipticPos().lon()) - meanAnomaly);
        double annualCorrection = Angle.ofDeg(0.1858) * Math.sin(sun.meanAnomaly());
        double correction3 = Angle.ofDeg(0.37) * Math.sin(sun.meanAnomaly());
        
        //True anomaly of the moon.
        double correctedAnomaly = meanAnomaly + evection - annualCorrection - correction3;
        
        //Correcting terms for orbital longitude
        double centerCorrection = Angle.ofDeg(6.2886) * Math.sin(correctedAnomaly);
        double correction4 = Angle.ofDeg(0.214) * Math.sin(2*correctedAnomaly);
        
        //Corrected orbital longitude.
        double correctedOrbitLon = meanOrbitLon + evection + centerCorrection - annualCorrection + correction4;
        
        //Correcting value for the true orbital lon
        double variation = Angle.ofDeg(0.6583) * Math.sin( 2 * ( correctedOrbitLon - sun.eclipticPos().lon() ) );
        
        //Corrected value of the true orbitalLon
        double trueOrbitLon = correctedOrbitLon + variation;
        
        //Average longitude of ascending node
        double averageNodeLon = Angle.normalizePositive(lonAscendNode - Angle.ofDeg(0.0529539) * daysSinceJ2010);
        
        //Corrected average longitude of ascending node
        double correctedNodeLon = averageNodeLon - Angle.ofDeg(0.16) * Math.sin(sun.meanAnomaly());
        
        //Ecliptic longitude of the moon
        double x = Math.sin(trueOrbitLon - correctedNodeLon) * Math.cos(orbitTilt);
        double y = Math.cos(trueOrbitLon - correctedNodeLon);
        double eclipticLon = Angle.normalizePositive(Math.atan2(x, y) + correctedNodeLon);
        
        //Ecliptic lattitude of the moon
        double eclipticLat = Math.asin(Math.sin(trueOrbitLon - correctedNodeLon) * Math.sin(orbitTilt));
        
        //Ecliptic coordinates of the moon based on the previously calculated longitude and lattitude.
        EclipticCoordinates eclipticCoord = EclipticCoordinates.of(eclipticLon, eclipticLat);        
        
        //Phase of the moon.
        double phase = phaseInterval.clip(((1 - Math.cos(trueOrbitLon - sun.eclipticPos().lon()))/2));
        
        //Distance between earth and the moon.
        double rho = (1 - Math.pow(orbitEccen,2))/(1 + orbitEccen*Math.cos(correctedAnomaly + centerCorrection));
        
        //Angular size of the moon.
        double angularSize = Angle.ofDeg(0.5181/rho);
                
        return new Moon(eclipticToEquatorialConversion.apply(eclipticCoord), (float)angularSize, 0, (float)phase);
    }
}

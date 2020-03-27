package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;

public enum MoonModel implements CelestialObjectModel<Moon>{
    // in degrees 
    MOON(91.929336,130.143076,291.682547,5.145396,0.0549);
    
    private final double averageLon;
    private final double averagePerigreeLon;
    private final double nodeLon;
    private final double orbitTilt;
    private final double orbitEccentricity;
    private ClosedInterval phaseInterval = ClosedInterval.of(0, 1);

    MoonModel(double averageLon, double averagePerigreeLon, double nodeLon, double orbitTilt, double orbitEccentricity) {
        this.averageLon = averageLon;
        this.averagePerigreeLon = averagePerigreeLon;
        this.nodeLon = nodeLon;
        this.orbitTilt = orbitTilt;
        this.orbitEccentricity = orbitEccentricity;
    }

    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        Sun sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        
        double averageOrbitLon = Angle.ofDeg(13.1763966) * daysSinceJ2010 + Angle.ofDeg(averageLon);
        
        double averageAnomaly = averageOrbitLon - Angle.ofDeg(0.1114041) * daysSinceJ2010 - Angle.ofDeg(averagePerigreeLon);
        
        double evection = Angle.ofDeg(1.2739) * Math.sin(2*(averageOrbitLon - sun.equatorialPos().ra()) - averageAnomaly);
        
        double annualCorrection = Angle.ofDeg(0.1858) * Math.sin(sun.meanAnomaly());
        
        double correction3 = Angle.ofDeg(0.37) * Math.sin(sun.meanAnomaly());
        
        double correctedAnomaly = averageAnomaly + evection - annualCorrection - correction3;
        
        double centerCorrection = Angle.ofDeg(6.2886) * Math.sin(correctedAnomaly);
        
        double correction4 = Angle.ofDeg(0.214) * Math.sin(2*correctedAnomaly);
        
        double correctedOrbitLon = averageOrbitLon + evection + centerCorrection - annualCorrection + correction4;
        
        double variation = Angle.ofDeg(0.6583) * Math.sin(2 * (correctedOrbitLon - sun.equatorialPos().ra()));
        
        double trueOrbitLon = correctedOrbitLon + variation;
        
        
        double averageLon = Angle.ofDeg(nodeLon) - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        
        double correctedNodeLon = averageLon - Angle.ofDeg(0.16) * Math.sin(sun.meanAnomaly());
        
        double eclipticLon = Angle.normalizePositive(Math.atan2(Math.sin(trueOrbitLon - correctedNodeLon) * Math.cos(Angle.ofDeg(orbitTilt))                
                , Math.cos(trueOrbitLon - correctedNodeLon)) + correctedNodeLon);
        
        double eclipticLat = Math.asin(Math.sin(trueOrbitLon - correctedNodeLon) * Math.asin(Angle.ofDeg(orbitTilt)));
        
        EclipticCoordinates eclipticCoord = EclipticCoordinates.of(eclipticLon, eclipticLat);        
        
        double phase = phaseInterval.clip(((1 - Math.cos(trueOrbitLon - sun.equatorialPos().ra()))/2));
        
        double rho = (1 - Math.pow(orbitEccentricity,2))/(1 + orbitEccentricity*Math.cos(correctedAnomaly + centerCorrection));
        
        double angularSize = Angle.ofDeg(0.5181/rho);
                
        return new Moon(eclipticToEquatorialConversion.apply(eclipticCoord), (float)angularSize, 0, (float)phase);
    }
}

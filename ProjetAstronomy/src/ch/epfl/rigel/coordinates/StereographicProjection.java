package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

import ch.epfl.rigel.math.Angle;

/**
 * Enables Stereographic Projection of coordinates
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {
    
	//Cosine of the latitude of the centre position.
    private final double cosOfCenterLat;
    //Sine of the latitude of the centre position.
    private final double sinOfCenterLat;
    //Tan of the latitude of the centre position.
    private final double tanOfCenterLat;
    //Longitude of the centre position.
    private final double centerLon;
    //Latitude of the centre position.
    private final double centerLat;

    /**
     * Initialises constants to facilitate calculations and calculation load.
     * @param center: centre at which the projection will be .
     */
    public StereographicProjection(HorizontalCoordinates center) {
        cosOfCenterLat = Math.cos(center.lat());
        sinOfCenterLat = Math.sin(center.lat());
        tanOfCenterLat = Math.atan(center.lat());
        centerLon = center.lon();
        centerLat = center.lat();
    }   
    
   
    /**
     * Calculates the center of the circle after projection
     * @param hor: point for which 
     * parallel line pass through.
     * @return returns cartesian coordinates of the circle corresponding to the projection.
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {                          
        return CartesianCoordinates.of(0, cosOfCenterLat/(Math.sin(hor.alt()) + sinOfCenterLat));
    }
    
    /**
     * Calculates radius of the circle correspoding to the projection
     * @param parallel: coordinates for which the radius of the projection will be evaluated.
     * @return returns the radius of the circle corresponding to the projection.
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        return Math.cos(parallel.alt())/(Math.sin(parallel.alt()) + sinOfCenterLat);
    }
    
    
    public double circleRadiusForMeridian(HorizontalCoordinates meridian) {
        return 1/(cosOfCenterLat * Math.sin(meridian.alt() - centerLon));
    }
    
    public CartesianCoordinates circleCenterForMeridian(HorizontalCoordinates meridian) {                          
        return CartesianCoordinates.of(-1/(cosOfCenterLat * Math.tan(meridian.alt() - centerLon)),
                   -tanOfCenterLat);
    }
    
    
    /**
     * Calculates the diameter of a projected sphere
    * @param rad: angular size of the sphere.
    * @return returns diameter of a projected sphere of angular size rad.
    */
    public double applyToAngle(double rad) {
        return 2*Math.tan(rad/4);
    }
    
    /**
     * Calculates the projection of horizontal coordinates to cartesian system
     * @param azAlt: horizontal coordinates which will be projected to cartesian coordinates.
     * @return: returns cartesian coordinates of the projection of the horizontal coordinates.
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double sinLat = Math.sin(azAlt.alt());
        double cosLat = Math.cos(azAlt.alt());
        double deltaLamba = azAlt.lon() - centerLon;
        double d = 1/(1+ sinLat * sinOfCenterLat + cosLat * cosOfCenterLat * Math.cos(deltaLamba));
        double x = d * cosLat * Math.sin(deltaLamba);
        double y = d * (sinLat * cosOfCenterLat - cosLat * sinOfCenterLat * Math.cos(deltaLamba));
        return CartesianCoordinates.of(x, y);
    }
    
    /**
     * Calculates the horizontal coordinates from the cartesian projection
     * @param xy: cartesian coordinates which will be projected to horizontal coordinates.
     * @return: returns horizontal coordinates from the projection of the cartesian coordinates. 
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        // return the center of projection, if the point is the origin 
        if(xy.x() == 0 && xy.y() == 0) {
            return HorizontalCoordinates.of(centerLon, centerLat);
        }
        
        double rho = Math.sqrt( xy.x()*xy.x() + xy.y()*xy.y() );
        double sinC =  (2*rho)/(rho*rho + 1);
        double cosC =  (1 - rho*rho)/(rho*rho + 1);
        double az = Angle.normalizePositive((Math.atan2( xy.x() * sinC,
                    rho * cosOfCenterLat * cosC - xy.y() * sinOfCenterLat * sinC)) + centerLon);
        double alt = Math.asin(cosC * sinOfCenterLat + (xy.y() * sinC * cosOfCenterLat) / rho);
        
        return HorizontalCoordinates.of(az, alt);
    }    
    
    /**
     * @return: returns string representation of the center of the projection
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT,"StereographicProjection: (center lon=%.4f°, center lat=%.4f°)", centerLon, centerLat);
    }
    
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }
}

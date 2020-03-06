package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

/**
 * Enables Stereographic Projection of coordinates
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {
    
    private final double cosOfCenterLatitude;
    private final double sinOfCenterLatitude;
    private final double centerLongitude;
    private final double centerLatitude;

    /**
     * Initialises constants to facilitate calculations and calculation load
     * @param center: center at which the projection will be centered
     */
    public StereographicProjection(HorizontalCoordinates center) {
        cosOfCenterLatitude = Math.cos(center.lat());
        sinOfCenterLatitude = Math.sin(center.lat());
        centerLongitude = center.lon();
        centerLatitude = center.lat();
    }   
    
   
    /**
     * @param hor: point for which parallel line pass through
     * @return returns cartesian coordinates of the circle corresponding to the projection
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {                          
        return CartesianCoordinates.of(0, cosOfCenterLatitude/(Math.sin(hor.alt()) + sinOfCenterLatitude));
    }
    
    /**
     * @param parallel: coordinates for which the radius of the projection will be evaluated
     * @return returns the radius of the circle corresponding to the projection
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        return Math.cos(parallel.alt())/(Math.sin(parallel.alt()) + sinOfCenterLatitude);
    }
    
    
    /**
    * @param rad: angular size of the sphere
    * @return returns diameter of a projected sphere of angular size rad.
    */
    public double applyToAngle(double rad) {
        return 2*Math.tan(rad/4);
    }
    
    /**
     * @param azAlt: horizontal coordinates which will be projected to cartesian coordinates
     * @return: returns cartesian coordinates of the projection of the horizontal coordinates 
     */
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double lonVariation = azAlt.alt() - centerLongitude;
        double d = 1/(1+ Math.sin(azAlt.alt()*sinOfCenterLatitude + Math.cos(azAlt.alt())*cosOfCenterLatitude*Math.cos(lonVariation)));
        
        return CartesianCoordinates.of(d*Math.cos(azAlt.alt())*Math.sin(lonVariation)
                    , d*(Math.sin(azAlt.alt())*cosOfCenterLatitude - Math.cos(azAlt.alt())*sinOfCenterLatitude*Math.cos(lonVariation)));
    }
    
    /**
     * @param xy: cartesian coordinates which will be projected to horizontal coordinates
     * @return: returns horizontal coordinates from the projection of the cartesian coordinates 
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        double rho = Math.sqrt(Math.pow(xy.x(), 2) + Math.pow(xy.y(), 2));
        double sinC =  (2*rho)/(Math.pow(rho, 2) + 1);
        double cosC =  (1 - Math.pow(rho, 2))/(Math.pow(rho, 2) + 1);
        double alt = (Math.atan2(xy.x()*sinC, rho*cosOfCenterLatitude 
                            * cosC - xy.y()*sinOfCenterLatitude * sinC)) + centerLongitude;
        
        double az = Math.asin(cosC*sinOfCenterLatitude + (xy.y()*sinC*cosOfCenterLatitude)/rho);
        
        return HorizontalCoordinates.of(az, alt);
    }    
    
    /**
     * @return string representation of the center of the projection
     */
    public String toString(){
        return String.format(Locale.ROOT,"(center_lon=%.4f°, center_lat=%.4f°)", centerLongitude, centerLatitude);
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

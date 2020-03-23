package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

import ch.epfl.rigel.math.Angle;

/**
 * Enables Stereographic Projection of coordinates
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {
    
    private final double cosOfCenterLat;
    private final double sinOfCenterLat;
    private final double centerLon;
    private final double centerLat;

    /**
     * Initialises constants to facilitate calculations and calculation load
     * @param center: center at which the projection will be centered
     */
    public StereographicProjection(HorizontalCoordinates center) {
        cosOfCenterLat = Math.cos(center.lat());
        sinOfCenterLat = Math.sin(center.lat());
        centerLon = center.lon();
        centerLat = center.lat();
    }   
    
   
    /**
     * @param hor: point for which parallel line pass through
     * @return returns cartesian coordinates of the circle corresponding to the projection
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {                          
        return CartesianCoordinates.of(0, cosOfCenterLat/(Math.sin(hor.alt()) + sinOfCenterLat));
    }
    
    /**
     * @param parallel: coordinates for which the radius of the projection will be evaluated
     * @return returns the radius of the circle corresponding to the projection
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        return Math.cos(parallel.alt())/(Math.sin(parallel.alt()) + sinOfCenterLat);
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
        double lat = azAlt.alt();
        double deltaLamba = azAlt.lon() - centerLon;
        double d = 1/(1+ Math.sin(lat) * sinOfCenterLat + Math.cos(lat) * cosOfCenterLat * Math.cos(deltaLamba));
        double x = d * Math.cos(lat) * Math.sin(deltaLamba);
        double y = d * (Math.sin(lat) * cosOfCenterLat - Math.cos(lat) * sinOfCenterLat * Math.cos(deltaLamba));
        return CartesianCoordinates.of(x, y);
    }
    
    /**
     * @param xy: cartesian coordinates which will be projected to horizontal coordinates
     * @return: returns horizontal coordinates from the projection of the cartesian coordinates 
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        double rho = Math.sqrt( xy.x()*xy.x() + xy.y()*xy.y() );
        double sinC =  (2*rho)/(rho*rho + 1);
        double cosC =  (1 - rho*rho)/(rho*rho + 1);
        double az = Angle.normalizePositive((Math.atan2( xy.x() * sinC,
                    rho * cosOfCenterLat * cosC - xy.y() * sinOfCenterLat * sinC)) + centerLon);
        double alt = Math.asin(cosC * sinOfCenterLat + (xy.y() * sinC * cosOfCenterLat) / rho);
        
        return HorizontalCoordinates.of(az, alt);
    }    
    
    /**
     * @return string representation of the center of the projection
     */
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

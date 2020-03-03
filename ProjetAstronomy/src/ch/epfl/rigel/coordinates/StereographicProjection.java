package ch.epfl.rigel.coordinates;

import java.util.function.Function;

public class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {
    
    private final double cosOfProjectionCenterLatitude;
    private final double sinOfProjectionCenterLatitude;
    private final double projectionCenterLongitude;

    public StereographicProjection(HorizontalCoordinates center) {
        cosOfProjectionCenterLatitude = Math.cos(center.lat());
        sinOfProjectionCenterLatitude = Math.sin(center.lat());
        projectionCenterLongitude = center.lon();
    }   
    
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {                          
        return CartesianCoordinates.of(0, cosOfProjectionCenterLatitude/(Math.sin(hor.alt()) + sinOfProjectionCenterLatitude));
    }
    
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        return Math.cos(parallel.alt())/(Math.sin(parallel.alt()) + sinOfProjectionCenterLatitude);
    }
    
    public double applyToAngle(double rad) {
        return 2*Math.tan(rad/4);
    }
    
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double lonVariation = azAlt.alt() - projectionCenterLongitude;
        double d = 1/(1+ Math.sin(azAlt.alt()*sinOfProjectionCenterLatitude + Math.cos(azAlt.alt())*cosOfProjectionCenterLatitude*Math.cos(lonVariation)));
        
        return CartesianCoordinates.of(d*Math.cos(azAlt.alt())*Math.sin(lonVariation)
                    , d*(Math.sin(azAlt.alt())*cosOfProjectionCenterLatitude - Math.cos(azAlt.alt())*sinOfProjectionCenterLatitude*Math.cos(lonVariation)));
    }
    
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        return null;
    }
    
    
    public String toString(){
        return "";
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

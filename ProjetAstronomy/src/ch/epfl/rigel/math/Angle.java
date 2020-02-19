package ch.epfl.rigel.math;

public final class Angle {
    
    private static final double TAU = 2 * Math.PI;
    
    private static final double ARC_SECOND_PER_DEG = 1 / 3600.0;
    
    private static final double ARC_MINUTE_PER_DEG = 1 / 60.0;
   
    private static final double ARC_SECOND_PER_RAD = Math.toRadians(ARC_SECOND_PER_DEG);    
    
    private static final double ARC_HOUR_PER_RAD = TAU / 24;
    
    private static final double RAD_PER_ARC_HOUR = 24/ TAU;
    
    
    private Angle () {}
    
    // normalise l'angle rad en le réduisant à l'intervalle [0,τ[,
    public static double normalizePositive(double rad) {   
        
        double multipleTAU = Math.floor(rad / TAU);       
        
        rad -= TAU  * multipleTAU;       
        
        return rad;
      
    }
    
    public static double ofArcsec(double sec) {
        return sec*ARC_SECOND_PER_RAD;
    }
    
    public static double ofDMS(int deg, int min, double sec) {
        if( (deg < 0 && deg >= 60) || (min < 0 && min >= 60)) {
            throw new IllegalArgumentException();
        }
        
        double angle_deg = deg + min* ARC_MINUTE_PER_DEG + sec * ARC_SECOND_PER_DEG;
        
        return Math.toRadians(angle_deg);
    }
    
    public static double ofDeg(double deg) {
        return Math.toRadians(deg);
    }
    
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);
    }
    
    public static double ofHr(double hr) {
        return hr* RAD_PER_ARC_HOUR;
    } 
    
    
    public static double toHr(double rad) {
      return rad * ARC_HOUR_PER_RAD;
    }
    
    
  
    
    
  
}

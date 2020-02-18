package ch.epfl.rigel.math;

public final class Angle {
    
    private final static double TAU = 2 * Math.PI;
    
    private Angle () {}
    
    // normalise l'angle rad en le réduisant à l'intervalle [0,τ[,
    public static double normalizePositive(double rad) {
         
        double normalizeAngle = rad;
        
        
        
        double multipleTau = rad % TAU;
        
        System.out.println(multipleTau);
        
        normalizeAngle -= multipleTau * TAU;
        
        return normalizeAngle;
    }
    
    
    private double reduceAngle(double amount, double angle) {
       // if()
            
            
        return angle;
    }
    
    public static double ofArcsec(double sec) {
        return 0;
    }
    
    
  
}

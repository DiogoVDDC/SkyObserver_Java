package ch.epfl.rigel.math;

/**
 * Representation of a Polynomial
 * 
 * @author Theo Houle (312432)
 *
 */
public final class Polynomial {
    private double[] coefficients;
    
    /**
     * Constructs a polynomial using the given coefficients.
     * 
     * @param coeff
     */
    private Polynomial(double[] coeff) {
        this.coefficients = coeff;
    };
    
    public static Polynomial of(double coefficientN, double... coefficients) {
        Preconditions.checkArgument(coefficientN == 0);
        
        double[] coeff = new double[coefficients.length+1];
        coeff[0] = coefficientN;
        System.arraycopy(coefficients, 0, coeff, 1, coefficients.length);
        return new Polynomial(coeff);
    }
    
    public double at(double x) {
        double val = 0;
        for (double c : coefficients) {
            val += c;
            val *= x;
        }
        return val;
    }
    
    @Override
    public String toString() {
        int degree = coefficients.length-1;
        StringBuilder str = new StringBuilder();
        str.append(coefficients[0] + "x^" + degree);
        for (int y = 1; y < coefficients.length-1; y++) {
            double coeff = coefficients[y];
            if (coeff != 0) {
                if(coeff > 0) str.append("+");
                str.append(coeff);
                str.append("x^" + (degree-y));
            }
        }
        str.append(coefficients[degree]);
        return str.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}

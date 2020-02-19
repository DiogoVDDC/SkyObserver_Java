package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Representation of a Polynomial
 * 
 * @author Theo Houle (312432)
 *
 */
public final class Polynomial {
    private double[] coefficients;
    
    /**
     * Constructs a polynomial using the given coefficients used only by the auxiliary constructor function.
     * 
     * @param coeff(Double[]): the list of the coefficients.
     */
    private Polynomial(double[] coeff) {
        this.coefficients = coeff;
    };
    
    /**
     * Allows to create a polynomial in decreasing power.
     * @param coefficientN(Double): the first coefficient of the polynomial.
     * @param coefficients(Double): following coefficient.
     * @return(Polynomial): the newly created polynomial.
     */
    public static Polynomial of(double coefficientN, double... coefficients) {
        Preconditions.checkArgument(coefficientN != 0);
        
        double[] coeff = new double[coefficients.length+1];
        coeff[0] = coefficientN;
        System.arraycopy(coefficients, 0, coeff, 1, coefficients.length);
        return new Polynomial(coeff);
    }
    
    /**
     * Evaluate the polynomial at a given x.
     * @param x(Double): the x value to evaluate.
     * @return(Double): the result of the evaluated polynomial.
     */
    public double at(double x) {
        double val = 0;
        for (double c : coefficients) {
            val += c;
            val *= x;
        }
        return val;
    }
    
    @Override
    // outputs to console the polynomial in the correct format
    public String toString() {
        int degree = coefficients.length-1;
        StringBuilder str = new StringBuilder();
        str.append(coefficients[0] + " x^" + degree);
        for (int i = 1; i < coefficients.length-1; i++) {
            double coeff = coefficients[i];
            if (coeff != 0 && coeff > 0) {
                str.append(" + ");
                str.append(coeff);
                str.append(" x^" + (degree-i));
            }
        }
        str.append(" + " + coefficients[degree]);
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

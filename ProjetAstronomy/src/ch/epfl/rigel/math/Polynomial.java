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
     * @param coeff: the list of the coefficients.
     */
    private Polynomial(double[] coeff) {
        this.coefficients = coeff;
    };

    /**
     * Allows to create a polynomial in decreasing power.
     * @param coefficientN: the first coefficient of the polynomial.
     * @param coefficients: following coefficient.
     * @return: the newly created polynomial.
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
     * @param x: the x value to evaluate.
     * @return: the result of the evaluated polynomial.
     */
    public double at(double x) {
        double val = 0;
        for (int i = 0; i < coefficients.length-1; i++) {
            val += coefficients[i];
            val *= x;
        }
        // The last coeff is added separetely to prevent the for loop to multiply by x one time to many the polynomial.
        val += coefficients[coefficients.length-1];
        return val;
    }

    @Override
    // outputs to console the polynomial in the correct format
    public String toString() {
        int degree = coefficients.length-1;
        StringBuilder str = new StringBuilder();

        for (int y = 0; y < coefficients.length-1; y++) {
            double coeff = coefficients[y];
            //If the coefficient is null, nothing must be written.
            if (coeff != 0) {
                //If the coefficient is -1, there must be a minus sign and the one disappear since 1*x is x.
                if(coeff == -1) {
                    str.append("-");
                }
                // The one must disappear since +-1*x = +-x.
                if (coeff != 1 && coeff != -1) {
                    str.append(coeff);
                }
                //if the degree is 1, the x must not be raised since x^1 = x.
                if (degree > 1) {
                    str.append("x^" + degree);
                } else {
                    str.append("x");
                }
                //If the next coefficient is positive, a plus sign must be added, 
                //else, since a negative value already has a minus sign, we don't need to add it.
                if (coefficients[y+1] > 0) {
                    str.append("+");
                }
            }
            degree --;
        }

        //Adding the last coefficient separately to avoid having it multiplied by x.

        if(coefficients[coefficients.length-1] !=0) {
            str.append(coefficients[coefficients.length-1]);
        }
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

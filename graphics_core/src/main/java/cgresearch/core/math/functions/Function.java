package cgresearch.core.math.functions;

/**
 * Representation of a n-dimensional function: R^n -> R
 * 
 * @author Philipp Jenke
 *
 */
public abstract class Function {

	/**
	 * Return the dimension of the function.
	 */
	public abstract int getDimension();

	/**
	 * Cached array for the gradient
	 */
	protected double[] gradient = null;
	protected double[] xh = null;

	/**
	 * Evaluate the function at the position x. x must have length
	 * getDimension()
	 */
	public abstract double eval(double[] x);

	/**
	 * Return the gradient of the function. The gradient is a vector of
	 * dimension getDimension(). In this implementation the gradient is computed
	 * using finite differences. If a closed representation is available
	 * overwrite this implementation.
	 */
	public double[] getGradient(double[] x) {
		double h = 1e-5;

		if (gradient == null) {
			gradient = new double[getDimension()];
			xh = new double[getDimension()];
		}

		for (int i = 0; i < getDimension(); i++) {
			System.arraycopy(x, 0, xh, 0, x.length);
			x[i] += h;
			gradient[i] = (eval(xh) - eval(x)) / h;
		}
		return gradient;
	}
}

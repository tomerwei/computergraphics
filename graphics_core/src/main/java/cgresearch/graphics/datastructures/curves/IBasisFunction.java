package cgresearch.graphics.datastructures.curves;

/**
 * Interface for basis functions.
 * 
 * @author Philipp Jenke
 *
 */
public interface IBasisFunction {
	/**
	 * Evaluate the i'th basis function at position t.
	 * 
	 * @param i
	 *            Index of the basis function.
	 * @param t
	 *            Parameter value
	 * @return Value of the basis function.
	 */
	public double eval(int index, double t);
}

package cgresearch.core.math;

/**
 * Interface of a general vector of arbitrary dimension.
 * 
 * @author Philipp Jenke
 */
public interface IVector {

	public int getDimension();

	public double get(int index);

	public void set(int index, double value);

	/**
	 * Subtract other vector, return result as new vector.
	 * 
	 * @param other
	 *            Vector to be subtracted.
	 * @return New vector containing the result.
	 */
	public IVector subtract(IVector other);

	/**
	 * Return the eucledian norm of the vector.
	 * 
	 * @return Norm.
	 */
	public double getNorm();

	/**
	 * Return the squared Eucledian norm of the vector.
	 * 
	 * @return Norm.
	 */
	public double getSqrNorm();

	public double multiply(IVector other);

	public IVector multiply(double alpha);

	public IVector add(IVector multiply);

	// Erweitert von Vitalij Kagaidj
	/**
	 * Compute the vector product of the two vectors
	 */
	IMatrix vectorProduct(IVector d, int dimension);

}

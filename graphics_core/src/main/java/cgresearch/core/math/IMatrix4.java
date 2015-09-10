/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

/**
 * Interface class for matrices in 4-space.
 * 
 * @author Philipp Jenke
 * 
 */
public interface IMatrix4 {
	/**
	 * Set the row 'index'. Attentions: sets the reference, no copy!
	 */
	public void setRow(final int index, IVector4 row);

	/**
	 * Compute the product of this matrix and a vector.
	 */
	public IVector4 multiply(final IVector4 other);

	/**
	 * Getter.
	 */
	public IVector4 getRow(final int index);

	/**
	 * Sets the value at row rowIndex and column columnIndex.
	 */
	public void setRow(int rowIndex, int columnIndex, double value);

	/**
	 * Return the value at row rowIndex and column columnIndex.
	 */
	public double get(int rowIndex, int columnIndex);

	/**
	 * Return an array representation of the matrix.
	 */
	public double[] toArray();

	/**
	 * Return the transposed of the matrix.
	 */
	public IMatrix4 getTransposed();

	/**
	 * Return an array of row-first values.
	 */
	public double[] data();

	/**
	 * Multiply matrix with another matrix, return result.
	 */
	public IMatrix4 multiply(IMatrix4 other);

	/**
	 * Return the i'th column
	 */
	public IVector4 getColumn(int i);

	/**
	 * Set the value at (rown, column).
	 */
	public void set(int row, int column, double value);
}

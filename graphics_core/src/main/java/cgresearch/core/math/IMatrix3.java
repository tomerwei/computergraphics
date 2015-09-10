/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

/**
 * Interface class for matrices in 3-space.
 * 
 * @author Philipp Jenke
 * 
 */
public interface IMatrix3 {
	/**
	 * Set the row 'index'. Attentions: sets the reference, no copy!
	 */
	public void setRow(final int rowIndex, IVector3 row);

	/**
	 * Compute the product of this matrix and a vector.
	 */
	public IVector3 multiply(final IVector3 other);

	/**
	 * Getter.
	 */
	public IVector3 getRow(final int rowIndex);

	/**
	 * Getter
	 */
	public IVector3 getColumn(int columnIndex);

	/**
	 * Return the double array representing the matrix. Use with caution. Use
	 * read-only.
	 * 
	 * @return Array representation of the matrix.
	 */
	public double[] data();

	/**
	 * Return the double array representing the matrix transformed into a
	 * homogenous 4x4 matrix.
	 * 
	 * @return Array representation of the matrix.
	 */
	public double[] data4x4();

	/**
	 * Create a 4x4 homogenious matrix from a 3x3 matrix.
	 * 
	 * @return
	 */
	public IMatrix4 makeHomogenious();

	/**
	 * Return the transposed of the matrix.
	 */
	public IMatrix3 getTransposed();

	/**
	 * Set the value at the specified row and column index.
	 * 
	 * @param columnIndex
	 *            Index of the column.
	 * @param rowIndex
	 *            Index of the row.
	 * @param d
	 *            New value.
	 */
	public void set(int rowIndex, int columnIndex, double d);

	/**
	 * Add another matrix, return the result.
	 */
	public IMatrix3 add(IMatrix3 vectorProduct);

	/**
	 * Getter for an individual values.
	 * 
	 * @param colIndex
	 *            Index of the column.
	 * @param rowIndex
	 *            Index of the row.
	 * @return Value.
	 */
	public double get(int rowIndex, int columnIndex);

	/**
	 * Copy from other matrix
	 * 
	 * @param rotation
	 */
	public void copy(IMatrix3 rotation);

	/**
	 * Matrix multiplication.
	 */
	public IMatrix3 multiply(IMatrix3 other);
}

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
public interface IMatrix4 extends IMatrix {
  /**
   * Set the row 'index'. Attentions: sets the reference, no copy!
   */
  public void setRow(final int index, IVector4 row);

  /**
   * Compute the product of this matrix and a vector.
   */
  public IVector4 multiply(final IVector other);

  /**
   * Sets the value at row rowIndex and column columnIndex.
   */
  public void set(int rowIndex, int columnIndex, double value);

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
  public IMatrix4 multiply(IMatrix other);

  /**
   * Return the i'th column
   */
  public IVector4 getColumn(int i);

  /**
   * Returns the inverse of the matrix, returns null if the computation failed.
   */
  public IMatrix4 getInverse();
}

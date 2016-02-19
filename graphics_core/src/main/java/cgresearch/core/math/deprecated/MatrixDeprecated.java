package cgresearch.core.math.deprecated;

import cgresearch.core.math.Vector;
import cgresearch.core.math.Matrix;

/**
 * Interface of a general matrix of arbitrary dimensions.
 * 
 * @author Philipp Jenke
 */
public interface MatrixDeprecated {

  public int getNumberOfRows();

  public int getNumberOfColumns();

  public double get(int rowIndex, int columnIndex);

  public void set(int rowIndex, int columnIndex, double value);

  /**
   * Multiply with a vector return new vector: m x n (this) * n (other) = m.
   * 
   * @param other
   *          Vector to multiply with (matrix from the left)
   * @return New Vector object containing the multiplication result.
   */
  public Vector multiply(Vector other);

  /**
   * Multiply with a matrix return new matrix: m x n (this) * n x l (other) = m
   * x l (result). Notation: #rows x #columns.
   * 
   * @param other
   *          Matrix to multiply with (matrix from the left)
   * @return New Matrix object containing the multiplication result.
   */
  public Matrix multiply(Matrix other);

  /**
   * Add another matrix, return the result.
   */
  public Matrix add(Matrix other);

  /**
   * Compute the determinant of the matrix.
   */
  public double getDeterminant();

  /**
   * Returns the inverse of the matrix, returns null if the computation failed.
   */
  public Matrix getInverse();

  public Matrix getTransposed();

  public double[] data();

  public Matrix multiply(double d);
}

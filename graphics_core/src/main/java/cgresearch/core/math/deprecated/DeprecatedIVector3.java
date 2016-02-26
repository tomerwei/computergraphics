/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math.deprecated;

import cgresearch.core.math.Vector;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;

/**
 * 
 * Generic interface for a vector of a given type in 3D.
 * 
 * @author Philipp Jenke
 * 
 */
public interface DeprecatedIVector3 {

  /**
   * Copy all values.
   * 
   * @param other
   *          Copy source.
   */
  void copy(final Vector other);

  /**
   * Multiply vector with a scalar, return result.
   */
  public Vector multiply(final double s);

  /**
   * Multiply vector with a scalar, result is saved in this
   */
  void multiplySelf(double s);

  /**
   * Scalar product
   */
  public double multiply(final Vector other);

  /**
   * Return the squared norm of the vector
   */
  public double getSqrNorm();

  /**
   * Return the norm of a vector.
   */
  public double getNorm();

  /**
   * Setter.
   */
  public void set(final int index, final double value);

  /**
   * Getter.
   */
  public double get(final int index);

  /**
   * Compute the cross product of two vectors. Only works for 3-dimensional
   * vectors.
   */
  public Vector cross(final Vector other);

  /**
   * Subtract vector from this vector, return result.
   */
  public Vector subtract(Vector other);

  /**
   * Add this vector other to this vector, result result
   */
  public Vector add(Vector other);

  /**
   * Add this vector other to this vector, the result overrides self
   */
  public void addSelf(Vector other);

  /**
   * Normalize the vector (vector length = 1)
   */
  public Vector getNormalized();

  /**
   * Return the an array containing the values. Use with caution. Violates the
   * OOP principle.
   */
  public double[] data();

  /**
   * Return the vector content as a float array.
   */
  public float[] floatData();

  /**
   * Make sure the vector has length 1.
   */
  void normalize();

  /**
   * Create a homogenious 4-vector from a 3-vector.
   */
  public Vector makeHomogenious();

  /**
   * Compute the vector product of the two vectors
   */
  public Matrix innerProduct(Vector d);

  /**
   * Additional toString method with a given precision.
   */
  public String toString(int precision);

  /**
   * Return a homogenious version of the vector.
   */
  public Vector getHomogenious();

  /**
   * Set all three coordinates.
   */
  public void set(double x, double y, double z);
}

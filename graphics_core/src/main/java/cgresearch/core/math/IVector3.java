/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

import java.io.Serializable;

/**
 * 
 * Generic interface for a vector of a given type in 3D.
 * 
 * @author Philipp Jenke
 * 
 */
public interface IVector3 extends Serializable {

  /**
   * Copy all values.
   * 
   * @param other
   *          Copy source.
   */
  void copy(final IVector3 other);

  /**
   * Multiply vector with a scalar, return result.
   */
  public IVector3 multiply(final double s);

  /**
   * Multiply vector with a scalar, result is saved in this
   */
  void multiplySelf(double s);

  /**
   * Scalar product
   */
  public double multiply(final IVector3 other);

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
  public IVector3 cross(final IVector3 other);

  /**
   * Subtract vector from this vector, return result.
   */
  public IVector3 subtract(IVector3 other);

  /**
   * Add this vector other to this vector, result result
   */
  public IVector3 add(IVector3 other);

  /**
   * Add this vector other to this vector, the result overrides self
   */
  public void addSelf(IVector3 other);

  /**
   * Normalize the vector (vector length = 1)
   */
  public IVector3 getNormalized();

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
  IVector4 makeHomogenious();

  /**
   * Compute the vector product of the two vectors
   */
  IMatrix3 vectorProduct(IVector3 d);

  /**
   * Additional toString method with a given precision.
   */
  public String toString(int precision);

  /**
   * Return a homogenious version of the vector.
   */
  public IVector4 getHomogenious();

  /**
   * Set all three coordinates.
   */
  void set(double x, double y, double z);
}

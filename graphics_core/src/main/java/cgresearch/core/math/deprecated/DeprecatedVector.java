package cgresearch.core.math.deprecated;

import java.io.Serializable;

import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;

/**
 * Interface of a general vector of arbitrary dimension.
 * 
 * @author Philipp Jenke
 */
public interface DeprecatedVector extends Serializable {

  public int getDimension();

  public double get(int index);

  public void set(int index, double value);

  /**
   * Subtract other vector, return result as new vector.
   * 
   * @param other
   *          Vector to be subtracted.
   * @return New vector containing the result.
   */
  public Vector subtract(Vector other);

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

  public double multiply(Vector other);

  public Vector multiply(double alpha);

  public Vector add(Vector multiply);

  /**
   * Compute the vector product of the two vectors
   */
  public Matrix innerProduct(Vector d);

  public float[] floatData();

  public void copy(Vector other);

  Vector getNormalized();

}

///**
// * Prof. Philipp Jenke
// * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
// * Lecture demo program.
// */
//package cgresearch.core.math.deprecated;
//
//import cgresearch.core.math.Vector;
//import cgresearch.core.math.Vector;
//import cgresearch.core.math.Vector;
//
///**
// * Generic interface for a vector of a given type in 4D.
// * 
// * @author Philipp Jenke
// * 
// */
//public interface VectorDeprecated extends Vector {
//  /**
//   * Multiply vector with a scalar, return result.
//   */
//  public Vector multiply(final double s);
//
//  /**
//   * Scalar product
//   */
//  public double multiply(final Vector other);
//
//  /**
//   * Return the squared norm of the vector
//   */
//  public double getSqrNorm();
//
//  /**
//   * Return the norm of a vector.
//   */
//  public double getNorm();
//
//  /**
//   * Setter.
//   */
//  public void set(final int index, final double value);
//
//  /**
//   * Getter.
//   */
//  public double get(final int index);
//
//  /**
//   * Subtract vector from this vector, return result.
//   */
//  public Vector subtract(Vector other);
//
//  /**
//   * Add this vector other to this vector, result result
//   */
//  public Vector add(Vector other);
//
//  /**
//   * Normalize the vector (vector length = 1)
//   */
//  public Vector getNormalized();
//
//  /**
//   * Return the an array containing the values. Use with caution. Violates the
//   * OOP principle.
//   */
//  public double[] data();
//
//  /**
//   * Return the vector content as a float array.
//   */
//  public float[] floatData();
//
//  /**
//   * Create a 3-dimensional vector - discard w-component.
//   */
//  public Vector toVector();
//}

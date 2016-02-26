/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

/**
 * Factory class for vectors.
 * 
 * @author Philipp Jenke
 */
public class VectorFactory {

  /**
   * Create vector of arbitrary dimension.
   * 
   * @param dimension
   *          Dimension of the vector.
   * @return New vector.
   */
  public static Vector createVector(int dimension) {
    return new Vector(dimension);
  }

  /**
   * Copy-constructor of arbitrary vector.
   * 
   * @param other
   *          Vector to be copied from.
   * @return New instance.
   */
  public static Vector createVector(Vector other) {
    return new Vector(other);
  }

  /**
   * Create a new instance for a 3-vector.
   * 
   * @param x
   *          Initial value for x-coordinate.
   * 
   * @param y
   *          Initial value for y-coordinate.
   * @param z
   *          Initial value for z-coordinate.
   * @return New instance.
   */
  public static Vector createVector3(double x, double y, double z) {
    return new Vector(x, y, z);
  }

  /**
   * Create a new instance for a 4-vector.
   * 
   * @param x
   *          Initial value for x-coordinate.
   * @param y
   *          Initial value for y-coordinate.
   * @param z
   *          Initial value for z-coordinate.
   * @param w
   *          Initial value for w-coordinate.
   * @return new instance.
   */
  public static Vector createVector4(double x, double y, double z, double w) {
    return new Vector(x, y, z, w);
  }

  /**
   * Create a 3-space vector from a 4-space vector by discarding the
   * w-coordinate.
   * 
   * @param v
   *          4-space vector.
   * @return New 3-space vector.
   */
  public static Vector create3spaceFrom4spaceVector(Vector v) {
    if (v.getDimension() != 4) {
      throw new IllegalArgumentException();
    }
    return VectorFactory.createVector3(v.get(0), v.get(1), v.get(2));
  }

  /**
   * Create a 4-space vector from a 3-space vector by add a w=1 coordinate.
   * 
   * @param v
   *          3-space vector.
   * @return New 4-space vector.
   */
  public static Vector createHomogeniousFor3spaceVector(Vector v) {
    if (v.getDimension() != 3) {
      throw new IllegalArgumentException();
    }
    return VectorFactory.createVector4(v.get(0), v.get(1), v.get(2), 1);
  }
}

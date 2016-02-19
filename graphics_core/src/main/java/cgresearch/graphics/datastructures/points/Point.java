/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.points;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Representation of a point in a @see PointCloud.
 * 
 * @author Philipp Jenke
 * 
 */
public class Point {

  /**
   * Position of the point in 3-space.
   */
  private Vector position = VectorMatrixFactory.newVector(3);

  /**
   * Color of the point.
   */
  private Vector color = VectorMatrixFactory.newVector(3);

  /**
   * Point normal
   */
  private Vector normal = VectorMatrixFactory.newVector(3);

  /**
   * Texture coordinate
   */
  private Vector texCoord = VectorMatrixFactory.newVector(3);

  /**
   * Constructor.
   * 
   * @param position
   *          Initial value for the position.
   */
  public Point(Vector position, Vector color, Vector normal) {
    this.position.copy(position);
    this.color.copy(color);
    this.normal.copy(normal);
  }

  /**
   * Constructor.
   */
  public Point(Vector position) {
    this(position, VectorMatrixFactory.newVector(3), VectorMatrixFactory.newVector(3));
  }

  /**
   * Constructor.
   */
  public Point(Vector position, Vector normal) {
    this(position, normal, VectorMatrixFactory.newVector(3));
  }

  /**
   * Constructor.
   */
  public Point() {
    this(VectorMatrixFactory.newVector(3), VectorMatrixFactory.newVector(3), VectorMatrixFactory.newVector(3));
  }

  /**
   * Getter.
   */
  public Vector getPosition() {
    return position;
  }

  /**
   * Getter.
   */
  public Vector getColor() {
    return color;
  }

  /**
   * Getter.
   */
  public Vector getNormal() {
    return normal;
  }

  /**
   * Setter.
   */
  public void setNormal(Vector normal) {
    this.normal.copy(normal);
  }

  /**
   * Setter.
   */
  public void setColor(Vector color) {
    this.color.copy(color);
  }

  public Point clone() {
    Point clone = new Point(position, color, normal);
    return clone;
  }

  /**
   * Getter.
   */
  public Vector getTexCoord() {
    return texCoord;
  }

}

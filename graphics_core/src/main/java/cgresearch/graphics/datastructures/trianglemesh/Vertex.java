/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.trianglemesh;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Representation of a vertex.
 * 
 * @author Philipp Jenke
 * 
 */
public class Vertex implements IVertex {

  /**
   * 3D position of the vertex.
   */
  private final Vector position = VectorMatrixFactory.newVector(3);

  /**
   * (Normalized) normal direction of the vertex.
   */
  private Vector normal = VectorMatrixFactory.newVector(1, 0, 0);

  /**
   * Color
   */
  // private Vector color = VectorMatrixFactory.newVector(0, 0, 0);

  /**
   * Constructor.
   */
  public Vertex() {
  }

  /**
   * Constructor.
   * 
   * @param position
   *          Initial value for position.
   */
  public Vertex(Vector position) {
    this.position.copy(position);
    this.normal = VectorMatrixFactory.newVector(1, 0, 0);
  }

  /**
   * Constructor.
   * 
   * @param position
   *          Initial value for position.
   * @param normal
   *          Initial value for normal.
   */
  public Vertex(Vector position, Vector normal) {
    this.position.copy(position);
    this.normal = normal;
  }

  /**
   * Copy constructor
   */
  public Vertex(IVertex vertex) {
    this(VectorMatrixFactory.newVector(vertex.getPosition()), VectorMatrixFactory.newVector(vertex.getNormal()));
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
  public Vector getNormal() {
    return normal;
  }

  /**
   * Getter.
   */
  // public Vector getColor() {
  // return color;
  // }

  /**
   * @param normal
   */
  public void setNormal(Vector normal) {
    this.normal = normal;
  }

  /**
   * @param color
   */
  // public void setColor(Vector color) {
  // this.color.copy(color);
  // }
}
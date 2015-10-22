package cgresearch.graphics.datastructures.trianglemesh;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

public class HalfEdgeVertex implements IVertex {

  /**
   * 3D position of the vertex.
   */
  private final IVector3 position = VectorMatrixFactory.newIVector3(0, 0, 0);

  /**
   * (Normalized) normal direction of the vertex.
   */
  private IVector3 normal = VectorMatrixFactory.newIVector3(1, 0, 0);

  /**
   * Color value at the vertex
   */
  private IVector3 color = VectorMatrixFactory.newIVector3(0, 0, 0);

  /**
   * Reference to one of the outgoing half edges.
   */
  private HalfEdge halfEgde = null;

  /**
   * Constructor.
   * 
   * @param position
   *          Initial value for position.
   */
  public HalfEdgeVertex(IVector3 position) {
    this.position.copy(position);
  }

  /**
   * Constructor.
   * 
   * @param position
   *          Initial value for position.
   * @param normal
   *          Initial value for normal.
   */
  public HalfEdgeVertex(IVector3 position, IVector3 normal) {
    this.position.copy(position);
    this.normal.copy(normal);
  }

  /**
   * Constructor.
   * 
   * @param position
   *          Initial value for position.
   * @param normal
   *          Initial value for normal.
   */
  public HalfEdgeVertex(IVector3 position, IVector3 normal, IVector3 color) {
    this.position.copy(position);
    this.normal.copy(normal);
    this.color.copy(color);
  }

  public IVector3 getPosition() {
    return position;
  }

  public IVector3 getNormal() {
    return normal;
  }

  public IVector3 getColor() {
    return color;
  }

  public void setNormal(IVector3 normal) {
    this.normal.copy(normal);
  }

  public void setColor(IVector3 color) {
    this.color.copy(color);
  }

  public HalfEdge getHalfEdge() {
    return halfEgde;
  }

  public void setHalfEgde(HalfEdge halfEgde) {
    this.halfEgde = halfEgde;
  }

  @Override
  public String toString() {
    return "Vertex";
  }
}

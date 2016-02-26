package cgresearch.graphics.datastructures.trianglemesh;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.GenericEdge;
import cgresearch.graphics.datastructures.GenericVertex;

/**
 * A half edge has references to the next edge within the current facet, the
 * opposite edge, its start vertex and the facet it belongs to.
 * 
 * @author Philipp Jenke
 *
 */
public class HalfEdge implements GenericEdge {

  /**
   * Reference to the next edge in the mesh.
   */
  private HalfEdge next;

  /**
   * Reference to the opposite edge in the mesh.
   */
  private HalfEdge opposite;

  /**
   * Start vertex of the half edge.
   */
  private HalfEdgeVertex startVertex;

  /**
   * The half edge belongs to this facet.
   */
  private HalfEdgeTriangle facet;

  /**
   * Color for the edge (used for debugging)
   */
  private Vector color = VectorFactory.createVector(3);

  public HalfEdge getNext() {
    return next;
  }

  public void setNext(HalfEdge next) {
    this.next = next;
  }

  public HalfEdge getOpposite() {
    return opposite;
  }

  public void setOpposite(HalfEdge opposite) {
    this.opposite = opposite;
  }

  public HalfEdgeVertex getStartVertex() {
    return startVertex;
  }

  public void setStartVertex(IVertex startVertex) {
    if (startVertex != null && !(startVertex instanceof HalfEdgeVertex)) {
      throw new IllegalArgumentException("Can only work with HalfEdgeVertex!");
    }
    this.startVertex = (HalfEdgeVertex) startVertex;
  }

  public HalfEdgeTriangle getFacet() {
    return facet;
  }

  public void setFacet(HalfEdgeTriangle facet) {
    this.facet = facet;
  }

  @Override
  public String toString() {
    return "Half Edge";
  }

  @Override
  public void setColor(Vector color) {
    this.color.copy(color);
  }

  @Override
  public GenericVertex getEndVertex() {
    return getNext().getStartVertex();
  }
}

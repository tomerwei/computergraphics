package cgresearch.graphics.datastructures.polygon;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.GenericEdge;

/**
 * An edge connects two points in a polygon.
 * 
 * @author Philipp Jenke
 */
public class PolygonEdge implements GenericEdge {

  /**
   * Start vertex of the edge.
   */
  private PolygonVertex startVertex = null;

  /**
   * End vertex of the edge.
   */
  private PolygonVertex endVertex = null;

  /**
   * Color used for debugging.
   */
  private Vector color = VectorFactory.createVector3(0, 0, 0);

  public PolygonEdge(PolygonVertex start, PolygonVertex end) {
    this.startVertex = start;
    this.endVertex = end;
    start.setOutgoingEdge(this);
    end.setIncomingEdge(this);
  }

  public PolygonVertex getStartVertex() {
    return startVertex;
  }

  public PolygonVertex getEndVertex() {
    return endVertex;
  }

  public Vector getColor() {
    return color;
  }

  public void setColor(Vector color) {
    this.color = color;
  }

  public void setStartVertex(PolygonVertex startVertex) {
    this.startVertex = startVertex;
  }

  public void setEndVertex(PolygonVertex endVertex) {
    this.endVertex = endVertex;
  }

  @Override
  public String toString() {
    return startVertex.toString() + " -> " + endVertex.toString();
  }
}

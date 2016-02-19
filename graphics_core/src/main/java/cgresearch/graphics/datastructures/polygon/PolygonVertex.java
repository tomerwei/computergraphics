package cgresearch.graphics.datastructures.polygon;

import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.GenericVertex;

/**
 * A polygon vertex can have up to two edges (incoming, outgoing).
 * 
 * @author Philipp Jenke
 *
 */
public class PolygonVertex implements GenericVertex {

  /**
   * Point position.
   */
  private final Vector position;

  /**
   * Incoming edge.
   */
  private PolygonEdge incomingEdge = null;

  /**
   * Outgoing edge.
   */
  private PolygonEdge outgoingEdge = null;

  public PolygonVertex(Vector position) {
    this.position = position;

  }

  public PolygonEdge getIncomingEdge() {
    return incomingEdge;
  }

  public void setIncomingEdge(PolygonEdge incomingEdge) {
    this.incomingEdge = incomingEdge;
  }

  public PolygonEdge getOutgoingEdge() {
    return outgoingEdge;
  }

  public void setOutgoingEdge(PolygonEdge outgoingEdge) {
    this.outgoingEdge = outgoingEdge;
  }

  public Vector getPosition() {
    return position;
  }

  @Override
  public String toString(){
    return position.toString();
  }
}

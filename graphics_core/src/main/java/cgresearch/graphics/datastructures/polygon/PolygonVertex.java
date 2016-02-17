package cgresearch.graphics.datastructures.polygon;

import cgresearch.core.math.IVector3;
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
  private final IVector3 position;

  /**
   * Incoming edge.
   */
  private PolygonEdge incomingEdge = null;

  /**
   * Outgoing edge.
   */
  private PolygonEdge outgoingEdge = null;

  public PolygonVertex(IVector3 position) {
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

  public IVector3 getPosition() {
    return position;
  }

  @Override
  public String toString(){
    return position.toString();
  }
}

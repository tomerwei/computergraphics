package cgresearch.graphics.algorithms;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.polygon.Polygon;
import cgresearch.graphics.datastructures.polygon.PolygonVertex;

/**
 * Compute subdivision on a 2D polygon.
 * 
 * @author Philipp Jenke
 */
public class Subdivision2D {
  /**
   * This polygon gets subdivided
   */
  private final Polygon polygon;

  public Subdivision2D(Polygon polygon) {
    this.polygon = polygon;
  }

  /**
   * Average the positions of all points.
   */
  public void average() {
    if (polygon == null) {
      return;
    }
    List<Vector> newPositions = new ArrayList<Vector>();
    for (int pointIndex = 0; pointIndex < polygon.getNumPoints(); pointIndex++) {
      PolygonVertex p = polygon.getPoint(pointIndex);
      PolygonVertex q = p.getOutgoingEdge().getEndVertex();
      Vector newPosition = computeNewPosition(p.getPosition(), q.getPosition());
      newPositions.add(newPosition);
    }
    for (int i = 0; i < polygon.getNumPoints(); i++) {
      polygon.getPoint(i).getPosition().copy(newPositions.get(i));
    }
  }

  /**
   * Compute the averaged position.
   */
  private Vector computeNewPosition(Vector p1, Vector p2) {
    Vector result = p1.add(p2);
    result.multiplySelf(0.5);
    return result;
  }

  /**
   * Split polygon, create doubled number of vertices.
   */
  public void split() {
    if (polygon == null) {
      return;
    }
    int numberOfEgdes = polygon.getNumEdges();
    for (int edgeIndex = 0; edgeIndex < numberOfEgdes; edgeIndex++) {
      polygon.splitEdge(polygon.getEdge(edgeIndex));
    }
  }

  /**
   * Call split() and average()
   */
  public void subdivide() {
    split();
    average();
  }
}

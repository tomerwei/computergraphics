package cgresearch.graphics.algorithms;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.IVector3;
import cgresearch.graphics.datastructures.Polygon;

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
    List<IVector3> newPositions = new ArrayList<IVector3>();
    for (int i = 0; i < polygon.getNumPoints(); i++) {
      IVector3 newPosition =
          computeNewPosition(polygon.getPoint(i), polygon.getPoint((i + 1) % polygon.getNumPoints()));
      newPositions.add(newPosition);
    }
    for (int i = 0; i < polygon.getNumPoints(); i++) {
      polygon.getPoint(i).copy(newPositions.get(i));
    }
  }

  /**
   * Compute the averaged position.
   */
  private IVector3 computeNewPosition(IVector3 p1, IVector3 p2) {
    IVector3 result = p1.add(p2);
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
    int numberOfPoints = polygon.getNumPoints();
    for (int i = 0; i < numberOfPoints; i++) {
      IVector3 newPoint = polygon.getPoint(i * 2).add(polygon.getPoint((i * 2 + 1) % polygon.getNumPoints()));
      newPoint.multiplySelf(0.5);
      polygon.insertPointAt(2 * i + 1, newPoint);
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

package cgresearch.graphics.datastructures;

import java.util.LinkedList;
import java.util.List;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * Representation of a polygon in 3-space.
 * 
 * @author Philipp Jenke
 */
public class Polygon extends ICgNodeContent {

  private List<IVector3> points = new LinkedList<IVector3>();

  public void addPoint(IVector3 p) {
    points.add(p);
  }

  public int getNumPoints() {
    return points.size();
  }

  public IVector3 getPoint(int index) {
    return points.get(index);
  }

  @Override
  public BoundingBox getBoundingBox() {
    BoundingBox bbox = new BoundingBox();
    for (IVector3 p : points) {
      bbox.add(p);
    }
    return bbox;
  }

  /**
   * Insert new point at specified index.
   */
  public void insertPointAt(int index, IVector3 point) {
    points.add(index, point);
  }

  public void clear() {
    points.clear();
  }
}

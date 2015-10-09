package cgresearch.graphics.datastructures.linesegments;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * A line connects two points, this datastructure contains a list of such lines.
 * Multiple lines can share then same point.
 * 
 * @author Philipp Jenke
 *
 */
public class LineSegments extends ICgNodeContent {

  /**
   * Container for the points.
   */
  private List<IVector3> points = new ArrayList<IVector3>();

  /**
   * Container for the lines (pairs of indices).
   */
  private List<LinePointIndices> lines = new ArrayList<LinePointIndices>();

  public LineSegments() {
  }

  public void addPoint(IVector3 p) {
    points.add(p);
  }

  public void addLine(int startIndex, int endIndex) {
    lines.add(new LinePointIndices(startIndex, endIndex));
  }

  public int getNumberOfPoints() {
    return points.size();
  }

  public int getNumberOfLines() {
    return lines.size();
  }

  public IVector3 getPoint(int pointIndex) {
    return points.get(pointIndex);
  }

  public int getLineStartPointIndex(int lineIndex) {
    return lines.get(lineIndex).startIndex;
  }

  public int getLineEndPointIndex(int lineIndex) {
    return lines.get(lineIndex).endIndex;
  }

  public IVector3 getLineStartPoint(int lineIndex) {
    return points.get(lines.get(lineIndex).startIndex);
  }

  public IVector3 getLineEndPoint(int lineIndex) {
    return points.get(lines.get(lineIndex).endIndex);
  }

  @Override
  public BoundingBox getBoundingBox() {
    BoundingBox bbox = new BoundingBox();
    for (IVector3 p : points) {
      bbox.add(p);
    }
    return bbox;
  }

}

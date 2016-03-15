package cgresearch.graphics.datastructures.linesegments;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
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
  private List<Vector> points = new ArrayList<Vector>();

  /**
   * Container for the lines (pairs of indices).
   */
  private List<LinePointIndices> lines = new ArrayList<LinePointIndices>();

  private List<Vector> lineColors = new ArrayList<Vector>();

  public LineSegments() {
  }

  public int addPoint(Vector p) {
    points.add(p);
    return points.indexOf(p);
  }

  public void addLine(int startIndex, int endIndex) {
    lines.add(new LinePointIndices(startIndex, endIndex));
    lineColors.add(VectorFactory.createVector3(0.7, 0.7, 0.7));
  }

  public int getNumberOfPoints() {
    return points.size();
  }

  public int getNumberOfLines() {
    return lines.size();
  }

  public Vector getPoint(int pointIndex) {
    return points.get(pointIndex);
  }

  public int getLineStartPointIndex(int lineIndex) {
    return lines.get(lineIndex).startIndex;
  }

  public int getLineEndPointIndex(int lineIndex) {
    return lines.get(lineIndex).endIndex;
  }

  public Vector getLineStartPoint(int lineIndex) {
    return points.get(lines.get(lineIndex).startIndex);
  }

  public Vector getLineEndPoint(int lineIndex) {
    return points.get(lines.get(lineIndex).endIndex);
  }

  @Override
  public BoundingBox getBoundingBox() {
    BoundingBox bbox = new BoundingBox();
    for (Vector p : points) {
      bbox.add(p);
    }
    return bbox;
  }

  public Vector getLineColor(int lineIndex) {
    return lineColors.get(lineIndex);
  }

  public void setLineColor(int lineIndex, Vector color) {
    lineColors.get(lineIndex).copy(color);
  }
}

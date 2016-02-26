package cgresearch.graphics.datastructures.linesegments;

/**
 * This POJO contains the indices of the end points of a line.
 * 
 * @author Philipp Jenke
 */
public class LinePointIndices {

  public LinePointIndices(int startIndex, int endIndex) {
    this.startIndex = startIndex;
    this.endIndex = endIndex;
  }

  public int startIndex, endIndex;
}

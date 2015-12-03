package cgresearch.graphics.datastructures.tree;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * A node in an octree datastructure.
 * 
 * @author PHilipp Jenke
 */
public class OctreeNode<T> extends ICgNodeContent {

  /**
   * Elements (not children!) in the node.
   */
  private List<T> elements = new ArrayList<T>();

  /**
   * The children array can either be null or must have length 8.
   */
  private List<OctreeNode<T>> children = null;

  /**
   * Lower left corner of the octree cell.
   */
  private final IVector3 lowerLeftCorner;

  /**
   * Length of the octree cell.
   */
  private final double length;

  /**
   * Constructor.
   */
  public OctreeNode(IVector3 lowerLeftCorner, double length) {
    this.lowerLeftCorner = lowerLeftCorner;
    this.length = length;
  }

  /**
   * Subdivide the node - create the children. This operation is only valid if
   * the children array is null.
   */
  public void subdivide() {
    if (children != null) {
      Logger.getInstance().error("Octree:subdivide(): children array must be null.");
      return;
    }

    // Create 8 children
    children = new ArrayList<OctreeNode<T>>();
    for (int i = 0; i < 8; i++) {
      children.add(null);
    }
    double childLength = length / 2.0;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        for (int k = 0; k < 2; k++) {
          IVector3 childLowerLeftCorner =
              lowerLeftCorner.add(VectorMatrixFactory.newIVector3(i * childLength, j * childLength, k * childLength));
          OctreeNode<T> childNode = new OctreeNode<T>(childLowerLeftCorner, childLength);
          children.set(getChildIndex(i, j, k), childNode);
        }
      }
    }

  }

  /**
   * Get the index of the child node. i, j, k must either be 0 (smaller) or 1
   * (larger)
   */
  public static int getChildIndex(int i, int j, int k) {
    return i * 4 + j * 2 + k;
  }

  /**
   * Getter.
   */
  public int getNumberOfElements() {
    return elements.size();
  }

  /**
   * Getter.
   */
  public T getElement(int index) {
    return elements.get(index);
  }

  /**
   * Setter.
   */
  public void addElement(T element) {
    elements.add(element);
  }

  /**
   * Getter
   */
  public int getNumberOfChildren() {
    return (children == null) ? 0 : 8;
  }

  /**
   * Getter.
   */
  public OctreeNode<T> getChild(int index) {
    return children.get(index);
  }

  /**
   * Getter.
   */
  public OctreeNode<T> getChild(int i, int j, int k) {
    return getChild(getChildIndex(i, j, k));
  }

  /**
   * Getter.
   */
  public IVector3 getLowerLeft() {
    return lowerLeftCorner;
  }

  /**
   * Getter.
   */
  public double getLength() {
    return length;
  }

  /**
   * Remove all elements from the node.
   */
  public void clearElements() {
    elements.clear();

  }

  /**
   * Return a list of corner points.
   * 
   * @return List of corner points
   */
  public List<IVector3> computeCornerPoints() {
    List<IVector3> corners = new ArrayList<IVector3>();
    corners.add(getLowerLeft());
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(getLength(), 0, 0)));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(getLength(), getLength(), 0)));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(0, getLength(), 0)));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(0, 0, getLength())));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(getLength(), 0, getLength())));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(getLength(), getLength(), getLength())));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(0, getLength(), getLength())));
    return corners;
  }

  @Override
  public BoundingBox getBoundingBox() {
    return new BoundingBox(lowerLeftCorner,
        lowerLeftCorner.add(VectorMatrixFactory.newIVector3(length, length, length)));
  }
}

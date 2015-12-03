/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.core.math;

import java.util.ArrayList;
import java.util.List;

import cgresearch.graphics.scenegraph.Transformation;

public class BoundingBox {
  /**
   * Lower left corner.
   */
  private IVector3 ll = VectorMatrixFactory.newIVector3();

  /**
   * Upper right corner
   */
  private IVector3 ur = VectorMatrixFactory.newIVector3();

  /**
   * This flag is false for invalid bounding boxes.
   */
  private boolean isInitialized = false;

  /**
   * Constructor.
   */
  public BoundingBox() {
    this(VectorMatrixFactory.newIVector3(), VectorMatrixFactory.newIVector3());
    isInitialized = false;
  }

  /**
   * Constructor.
   */
  public BoundingBox(IVector3 ll, IVector3 ur) {
    isInitialized = true;
    this.ll.copy(ll);
    this.ur.copy(ur);
  }

  /**
   * Unite two bounding boxes. Result is saved in this object and returned.
   */
  public BoundingBox unite(BoundingBox other) {
    // Self is uninitialized
    if (!isInitialized) {
      ll = other.ll;
      ur = other.ur;
      isInitialized = other.isInitialized;
      return this;
    }

    // Other is uninitialized
    if (!other.isInitialized) {
      return this;
    }

    // Both bounding boxes are initialized
    for (int i = 0; i < MathHelpers.DIMENSION_3; i++) {
      ll.set(i, Math.min(ll.get(i), other.ll.get(i)));
      ur.set(i, Math.max(ur.get(i), other.ur.get(i)));
    }
    return this;
  }

  public IVector3 getCenter() {
    return ll.add(ur).multiply(0.5);
  }

  public double getDiameter() {
    IVector3 diagonal = ur.subtract(ll);
    return diagonal.getNorm();
  }

  public IVector3 getLowerLeft() {
    return ll;
  }

  public IVector3 getUpperRight() {
    return ur;
  }

  public void setInitialized(boolean b) {
    isInitialized = b;
  }

  public boolean isInitialized() {
    return isInitialized;
  }

  public void setLowerLeft(IVector3 ll) {
    isInitialized = true;
    this.ll = ll;
  }

  public void setUpperRight(IVector3 ur) {
    isInitialized = true;
    this.ur = ur;
  }

  public void add(IVector3 p) {

    if (!isInitialized) {
      ll.copy(p);
      ur.copy(p);
      isInitialized = true;
    } else {
      ll = VectorMatrixFactory.newIVector3(Math.min(ll.get(MathHelpers.INDEX_0), p.get(MathHelpers.INDEX_0)),
          Math.min(ll.get(MathHelpers.INDEX_1), p.get(MathHelpers.INDEX_1)),
          Math.min(ll.get(MathHelpers.INDEX_2), p.get(MathHelpers.INDEX_2)));

      ur = VectorMatrixFactory.newIVector3(Math.max(ur.get(MathHelpers.INDEX_0), p.get(MathHelpers.INDEX_0)),
          Math.max(ur.get(MathHelpers.INDEX_1), p.get(MathHelpers.INDEX_1)),
          Math.max(ur.get(MathHelpers.INDEX_2), p.get(MathHelpers.INDEX_2)));
    }
  }

  /**
   * Rescale the bounding box.
   */
  public void scale(double d) {
    IVector3 offset = ll.subtract(ur).multiply(d / 2.0);
    ll = ll.add(offset);
    ur.subtract(offset);
  }

  /**
   * Return the length of the longest edge of the bounding box.
   */
  public double getMaxExtend() {
    IVector3 diagonal = getUpperRight().subtract(getLowerLeft());
    return Math.max(diagonal.get(0), Math.max(diagonal.get(1), diagonal.get(2)));
  }

  /**
   * Transform the bounding box with R * bbox + t.
   */
  public void transform(IMatrix3 rotation, IVector3 translation) {
    IVector3 extend = ur.subtract(ll);
    List<IVector3> points = new ArrayList<IVector3>();
    for (int x = 0; x < 2; x++) {
      for (int y = 0; y < 2; y++) {
        for (int z = 0; z < 2; z++) {
          points.add(ll.add(VectorMatrixFactory.newIVector3(x * extend.get(0), y * extend.get(1), z * extend.get(2))));
        }
      }
    }
    for (int i = 0; i < points.size(); i++) {
      IVector3 p = points.get(i);
      if (translation != null) {
        p = p.add(translation);
      }
      if (rotation != null) {
        p = rotation.multiply(p);
      }
      points.get(i).copy(p);
    }
    isInitialized = false;
    for (IVector3 p : points) {
      add(p);
    }
  }

  public void transform(Transformation transformation) {

    if (transformation == null) {
      return;
    }

    IVector3 extend = ur.subtract(ll);
    List<IVector3> points = new ArrayList<IVector3>();
    for (int x = 0; x < 2; x++) {
      for (int y = 0; y < 2; y++) {
        for (int z = 0; z < 2; z++) {
          points.add(ll.add(VectorMatrixFactory.newIVector3(x * extend.get(0), y * extend.get(1), z * extend.get(2))));
        }
      }
    }
    for (int i = 0; i < points.size(); i++) {
      IVector4 p = points.get(i).makeHomogenious();
      points.get(i).copy(transformation.getTransformation().multiply(p).toVector3());
    }
    isInitialized = false;
    for (IVector3 p : points) {
      add(p);
    }

  }

  @Override
  public String toString() {
    return ll + " -> " + ur;
  }

  /**
   * Returns the half sidelength of the bbox in each direction.
   * 
   * @return
   */
  public IVector3 getExtent() {
    return ur.subtract(ll).multiply(0.5);
  }

  /**
   * Return a list of corner points.
   * 
   * @return List of corner points
   */
  public List<IVector3> computeCornerPoints() {
    List<IVector3> corners = new ArrayList<IVector3>();
    IVector3 length = getExtent().multiply(2);
    corners.add(getLowerLeft());
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(length.get(0), 0, 0)));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(length.get(0), length.get(1), 0)));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(0, length.get(1), 0)));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(0, 0, length.get(2))));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(length.get(0), 0, length.get(2))));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(length.get(0), length.get(1), length.get(2))));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newIVector3(0, length.get(1), length.get(2))));
    return corners;
  }
}

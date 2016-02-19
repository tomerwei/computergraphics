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
  private Vector ll = VectorMatrixFactory.newVector(3);

  /**
   * Upper right corner
   */
  private Vector ur = VectorMatrixFactory.newVector(3);

  /**
   * This flag is false for invalid bounding boxes.
   */
  private boolean isInitialized = false;

  /**
   * Constructor.
   */
  public BoundingBox() {
    this(VectorMatrixFactory.newVector(3), VectorMatrixFactory.newVector(3));
    isInitialized = false;
  }

  /**
   * Constructor.
   */
  public BoundingBox(Vector ll, Vector ur) {
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

  public Vector getCenter() {
    return ll.add(ur).multiply(0.5);
  }

  public double getDiameter() {
    Vector diagonal = ur.subtract(ll);
    return diagonal.getNorm();
  }

  public Vector getLowerLeft() {
    return ll;
  }

  public Vector getUpperRight() {
    return ur;
  }

  public void setInitialized(boolean b) {
    isInitialized = b;
  }

  public boolean isInitialized() {
    return isInitialized;
  }

  public void setLowerLeft(Vector ll) {
    isInitialized = true;
    this.ll = ll;
  }

  public void setUpperRight(Vector ur) {
    isInitialized = true;
    this.ur = ur;
  }

  public void add(Vector p) {

    if (!isInitialized) {
      ll.copy(p);
      ur.copy(p);
      isInitialized = true;
    } else {
      ll = VectorMatrixFactory.newVector(Math.min(ll.get(MathHelpers.INDEX_0), p.get(MathHelpers.INDEX_0)),
          Math.min(ll.get(MathHelpers.INDEX_1), p.get(MathHelpers.INDEX_1)),
          Math.min(ll.get(MathHelpers.INDEX_2), p.get(MathHelpers.INDEX_2)));

      ur = VectorMatrixFactory.newVector(Math.max(ur.get(MathHelpers.INDEX_0), p.get(MathHelpers.INDEX_0)),
          Math.max(ur.get(MathHelpers.INDEX_1), p.get(MathHelpers.INDEX_1)),
          Math.max(ur.get(MathHelpers.INDEX_2), p.get(MathHelpers.INDEX_2)));
    }
  }

  /**
   * Rescale the bounding box.
   */
  public void scale(double d) {
    Vector offset = ll.subtract(ur).multiply(d / 2.0);
    ll = ll.add(offset);
    ur.subtract(offset);
  }

  /**
   * Return the length of the longest edge of the bounding box.
   */
  public double getMaxExtend() {
    Vector diagonal = getUpperRight().subtract(getLowerLeft());
    return Math.max(diagonal.get(0), Math.max(diagonal.get(1), diagonal.get(2)));
  }

  /**
   * Transform the bounding box with R * bbox + t.
   */
  public void transform(Matrix rotation, Vector translation) {
    Vector extend = ur.subtract(ll);
    List<Vector> points = new ArrayList<Vector>();
    for (int x = 0; x < 2; x++) {
      for (int y = 0; y < 2; y++) {
        for (int z = 0; z < 2; z++) {
          points.add(ll.add(VectorMatrixFactory.newVector(x * extend.get(0), y * extend.get(1), z * extend.get(2))));
        }
      }
    }
    for (int i = 0; i < points.size(); i++) {
      Vector p = points.get(i);
      if (translation != null) {
        p = p.add(translation);
      }
      if (rotation != null) {
        p = rotation.multiply(p);
      }
      points.get(i).copy(p);
    }
    isInitialized = false;
    for (Vector p : points) {
      add(VectorMatrixFactory.newVector(p.get(0), p.get(1), p.get(2)));
    }
  }

  public void transform(Transformation transformation) {

    if (transformation == null) {
      return;
    }

    Vector extend = ur.subtract(ll);
    List<Vector> points = new ArrayList<Vector>();
    for (int x = 0; x < 2; x++) {
      for (int y = 0; y < 2; y++) {
        for (int z = 0; z < 2; z++) {
          points.add(ll.add(VectorMatrixFactory.newVector(x * extend.get(0), y * extend.get(1), z * extend.get(2))));
        }
      }
    }
    for (int i = 0; i < points.size(); i++) {
      Vector p = VectorMatrixFactory.dim3toDim4(points.get(i));
      Vector v = transformation.getTransformation().multiply(p);
      points.get(i).copy(VectorMatrixFactory.newVector(v.get(0), v.get(1), v.get(2)));
    }
    isInitialized = false;
    for (Vector p : points) {
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
  public Vector getExtent() {
    return ur.subtract(ll).multiply(0.5);
  }

  /**
   * Return a list of corner points.
   * 
   * @return List of corner points
   */
  public List<Vector> computeCornerPoints() {
    List<Vector> corners = new ArrayList<Vector>();
    Vector length = getExtent().multiply(2);
    corners.add(getLowerLeft());
    corners.add(getLowerLeft().add(VectorMatrixFactory.newVector(length.get(0), 0, 0)));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newVector(length.get(0), length.get(1), 0)));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newVector(0, length.get(1), 0)));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newVector(0, 0, length.get(2))));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newVector(length.get(0), 0, length.get(2))));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newVector(length.get(0), length.get(1), length.get(2))));
    corners.add(getLowerLeft().add(VectorMatrixFactory.newVector(0, length.get(1), length.get(2))));
    return corners;
  }
}

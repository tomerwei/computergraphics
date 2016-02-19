/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.primitives;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Representation of a plane in 3-space.
 * 
 * @author Philipp Jenke
 * 
 */
public class Plane extends IPrimitive {

  /**
   * A point in the plane;
   */
  private Vector point = VectorMatrixFactory.newVector(0, 0, 0);

  /**
   * Normal vector of the plane;
   */
  private Vector normal = VectorMatrixFactory.newVector(0, 0, 0);

  /**
   * Cached precomputed coordinate frame of the plane: tangent in u-direction
   */
  private Vector tangentU;

  /**
   * Cached precomputed coordinate frame of the plane: tangent in v-direction
   */
  private Vector tangentV;

  /**
   * Cached precomputed coordinate frame of the plane: coordinate frame
   */
  private Matrix planeCoordinateSystem;

  private static final double PARALLEL_THRESHOLD = 0.99;

  /**
   * Constructor.
   */
  public Plane() {
    this(VectorMatrixFactory.newVector(0, 0, 0), VectorMatrixFactory.newVector(0, 1, 0));
  }

  /**
   * Constructor with initialization.
   */
  public Plane(Vector point, Vector normal) {
    this.point.copy(point);
    this.normal.copy(normal);
    precomputeCoordinateFrame();
  }

  /**
   * Precompute the coordinate frame.
   */
  private void precomputeCoordinateFrame() {
    tangentU = VectorMatrixFactory.newVector(1, 0, 0);
    if (Math.abs(tangentU.multiply(getNormal())) > 0.95) {
      tangentU = VectorMatrixFactory.newVector(0, 1, 0);
    }
    tangentV = getNormal().cross(tangentU).getNormalized();
    tangentU = tangentV.cross(getNormal()).getNormalized();
    planeCoordinateSystem = VectorMatrixFactory.newMatrix(tangentU, tangentV, getNormal()).getTransposed();
  }

  /**
   * Getter.
   */
  public Vector getPoint() {
    return point;
  }

  /**
   * Setter.
   */
  public void setPoint(Vector point) {
    this.point = point;
  }

  /**
   * Getter.
   */
  public Vector getNormal() {
    return normal;
  }

  /**
   * Setter.
   */
  public void setNormal(Vector normal) {
    this.normal = normal;
    precomputeCoordinateFrame();
  }

  /**
   * Compute the signed distance between the given point and the plane.
   * Attention: normal must be normalized for correct values!
   */
  public double computeSignedDistance(Vector p) {

    // DEBBUGGING CODE - REMOVE LATER!
    if (Math.abs(normal.getSqrNorm() - 1.0) > 1e-5) {
      Logger.getInstance().error("Attention - normal must be normalized for Plane.computeSignedDistance()");
    }

    return (p.subtract(getPoint()).multiply(getNormal()));
  }

  /**
   * Compute the absolute distance between the given point and the plane.
   * Attention: normal must be normalized for correct values!
   */
  public double computeDistance(Vector p) {
    return Math.abs(computeSignedDistance(p));
  }

  /**
   * Compute a checkerboard color for the plane at the given point.
   */
  public Vector getReflectionDiffuseCheckerBoard(double checkerBoardCellSize, Vector point) {
    Vector pointInPlaneCoordinateSytem = planeCoordinateSystem.multiply(point.subtract(getPoint()));
    double u = pointInPlaneCoordinateSytem.get(0);
    double v = pointInPlaneCoordinateSytem.get(1);
    if (u < 0) {
      u = -u + checkerBoardCellSize;
    }
    if (v < 0) {
      v = -v + checkerBoardCellSize;
    }
    int indexI = (int) (u / checkerBoardCellSize);
    int indexJ = (int) (v / checkerBoardCellSize);
    if (indexI % 2 == indexJ % 2) {
      return getMaterial().getReflectionDiffuse();
    } else {
      return getMaterial().getReflectionDiffuse().multiply(0.5);
    }
  }

  /**
   * Return true if the point is in the positive halfspace of the plane.
   */
  public boolean isInPositiveHalfSpace(Vector p) {
    double distance = getDistance(p);
    return distance >= 0;
  }

  /**
   * Project the point p onto the plane.
   */
  public Vector project(Vector p) {
    return p.subtract(normal.multiply(getDistance(p)));
  }

  /**
   * Compute the distance of the point from the plane.
   */
  public double getDistance(Vector p) {
    Vector v = p.subtract(point);
    return v.multiply(normal);
  }

  public Vector getTangentU() {
    Vector helper = VectorMatrixFactory.newVector(1, 0, 0);
    if (Math.abs(helper.multiply(normal)) > PARALLEL_THRESHOLD) {
      helper = VectorMatrixFactory.newVector(0, 1, 0);
    }
    return normal.cross(helper).getNormalized();
  }

  /**
   * Return a tangent vector which is perpendicular to normal and getTangentU().
   */
  public Vector getTangentV() {
    return normal.cross(getTangentU());
  }
}

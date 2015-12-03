/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.primitives;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;
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
  private IVector3 point = VectorMatrixFactory.newIVector3(0, 0, 0);

  /**
   * Normal vector of the plane;
   */
  private IVector3 normal = VectorMatrixFactory.newIVector3(0, 0, 0);

  /**
   * Cached precomputed coordinate frame of the plane: tangent in u-direction
   */
  private IVector3 tangentU;

  /**
   * Cached precomputed coordinate frame of the plane: tangent in v-direction
   */
  private IVector3 tangentV;

  /**
   * Cached precomputed coordinate frame of the plane: coordinate frame
   */
  private IMatrix3 planeCoordinateSystem;

  private static final double PARALLEL_THRESHOLD = 0.99;

  /**
   * Constructor.
   */
  public Plane() {
    this(VectorMatrixFactory.newIVector3(0, 0, 0), VectorMatrixFactory.newIVector3(0, 1, 0));
  }

  /**
   * Constructor with initialization.
   */
  public Plane(IVector3 point, IVector3 normal) {
    this.point.copy(point);
    this.normal.copy(normal);
    precomputeCoordinateFrame();
  }

  /**
   * Precompute the coordinate frame.
   */
  private void precomputeCoordinateFrame() {
    tangentU = VectorMatrixFactory.newIVector3(1, 0, 0);
    if (Math.abs(tangentU.multiply(getNormal())) > 0.95) {
      tangentU = VectorMatrixFactory.newIVector3(0, 1, 0);
    }
    tangentV = getNormal().cross(tangentU).getNormalized();
    tangentU = tangentV.cross(getNormal()).getNormalized();
    planeCoordinateSystem = VectorMatrixFactory.newIMatrix3(tangentU, tangentV, getNormal()).getTransposed();
  }

  /**
   * Getter.
   */
  public IVector3 getPoint() {
    return point;
  }

  /**
   * Setter.
   */
  public void setPoint(IVector3 point) {
    this.point = point;
  }

  /**
   * Getter.
   */
  public IVector3 getNormal() {
    return normal;
  }

  /**
   * Setter.
   */
  public void setNormal(IVector3 normal) {
    this.normal = normal;
    precomputeCoordinateFrame();
  }

  /**
   * Compute the signed distance between the given point and the plane.
   * Attention: normal must be normalized for correct values!
   */
  public double computeSignedDistance(IVector3 p) {

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
  public double computeDistance(IVector3 p) {
    return Math.abs(computeSignedDistance(p));
  }

  /**
   * Compute a checkerboard color for the plane at the given point.
   */
  public IVector3 getReflectionDiffuseCheckerBoard(double checkerBoardCellSize, IVector3 point) {
    IVector3 pointInPlaneCoordinateSytem = planeCoordinateSystem.multiply(point.subtract(getPoint()));
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
  public boolean isInPositiveHalfSpace(IVector3 p) {
    double distance = getDistance(p);
    return distance >= 0;
  }

  /**
   * Project the point p onto the plane.
   */
  public IVector3 project(IVector3 p) {
    return p.subtract(normal.multiply(getDistance(p)));
  }

  /**
   * Compute the distance of the point from the plane.
   */
  public double getDistance(IVector3 p) {
    IVector3 v = p.subtract(point);
    return v.multiply(normal);
  }

  public IVector3 getTangentU() {
    IVector3 helper = VectorMatrixFactory.newIVector3(1, 0, 0);
    if (Math.abs(helper.multiply(normal)) > PARALLEL_THRESHOLD) {
      helper = VectorMatrixFactory.newIVector3(0, 1, 0);
    }
    return normal.cross(helper).getNormalized();
  }

  /**
   * Return a tangent vector which is perpendicular to normal and getTangentU().
   */
  public IVector3 getTangentV() {
    return normal.cross(getTangentU());
  }
}

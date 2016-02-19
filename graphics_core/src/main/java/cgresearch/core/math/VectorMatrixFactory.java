/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

/**
 * Factory class for matrices and vectors.
 * 
 * @author Philipp Jenke
 * 
 */
public class VectorMatrixFactory {

  /**
   * Create a new instance for a 3-vector.
   * 
   * @return new instance.
   */
  public static IVector3 newIVector3() {
    return new Vector3();
  }

  /**
   * Create a new instance for a 3-vector. Copy values from other vector.
   * 
   * @param other
   *          Vector to be copied from.
   * @return new instance.
   */
  public static IVector3 newIVector3(IVector3 other) {
    return new Vector3(other);
  }

  /**
   * Create a new instance for a 3-vector.
   * 
   * @param x
   *          Initial value for x-coordinate.
   * 
   * @param y
   *          Initial value for y-coordinate.
   * @param z
   *          Initial value for z-coordinate.
   * @return new instance.
   */
  public static IVector3 newIVector3(double x, double y, double z) {
    return new Vector3(x, y, z);
  }

  /**
   * Create a new instance for a 4-vector.
   * 
   * @return new instance.
   */
  public static IVector4 newIVector4() {
    return new Vector4();
  }

  /**
   * Create a new instance for a 4-vector.
   * 
   * @param x
   *          Initial value for x-coordinate.
   * @param y
   *          Initial value for y-coordinate.
   * @param z
   *          Initial value for z-coordinate.
   * @param w
   *          Initial value for w-coordinate.
   * @return new instance.
   */
  public static IVector4 newIVector4(double x, double y, double z, double w) {
    return new Vector4(x, y, z, w);
  }

  /**
   * Create a new instance for a 3x3-matrix.
   * 
   * @return new instance.
   */
  public static IMatrix3 newIMatrix3() {
    return new Matrix3();
  }

  /**
   * Create a new instance of a 3x3-matrix.
   * 
   * @param row0
   *          Initial value for the first row.
   * @param row1
   *          Initial value for the second row.
   * @param row2
   *          Initial value for the third row.
   * @return new instance.
   */
  public static IMatrix3 newIMatrix3(IVector3 row0, IVector3 row1, IVector3 row2) {
    return new Matrix3(row0, row1, row2);
  }

  /**
   * Create a new instance of a 3x3-matrix.
   */
  public static IMatrix3 newIMatrix3(double v00, double v01, double v02, double v10, double v11, double v12, double v20,
      double v21, double v22) {
    return new Matrix3(v00, v01, v02, v10, v11, v12, v20, v21, v22);
  }

  /**
   * Create a new instance for a 4x4-matrix.
   * 
   * @return new instance.
   */
  public static IMatrix4 newIMatrix4() {
    return new Matrix4();
  }

  /**
   * Create a new instance of a 4x4-matrix.
   * 
   * @param row0
   *          Initial value for the first row.
   * @param row1
   *          Initial value for the second row.
   * @param row2
   *          Initial value for the third row.
   * @param row3
   *          Initial value for the forth row.
   * @return new instance.
   */
  public static IMatrix4 newIMatrix4(IVector4 row0, IVector4 row1, IVector4 row2, IVector4 row3) {
    return new Matrix4(row0, row1, row2, row3);
  }

  /**
   * Create a rotation matrix around an axis with a given angle.
   */
  public static IMatrix3 getRotationMatrix(IVector3 axis, double angle) {
    double s = Math.sin(angle);
    double c = Math.cos(angle);
    double t = 1.0 - c;

    return new Matrix3(
        VectorMatrixFactory.newIVector3(t * axis.get(0) * axis.get(0) + c,
            t * axis.get(0) * axis.get(1) + s * axis.get(2), t * axis.get(0) * axis.get(2) - s * axis.get(1)),
        VectorMatrixFactory.newIVector3(t * axis.get(0) * axis.get(1) - s * axis.get(2),
            t * axis.get(1) * axis.get(1) + c, t * axis.get(1) * axis.get(2) + s * axis.get(0)),
        VectorMatrixFactory.newIVector3(t * axis.get(0) * axis.get(2) + s * axis.get(1),
            t * axis.get(2) * axis.get(2) - s * axis.get(0), t * axis.get(2) * axis.get(2) + c));
  }

  /**
   * Create a new coordinate orthogonal coordinate system where the y-direction
   * points into the newY-direction.
   * 
   * @param newY
   *          New direction for the second base vector.
   * @return Coordinate system in matrix representation.
   */
  public static IMatrix3 createCoordinateFrameY(IVector3 newY) {
    newY.normalize();
    if (Math.abs(newY.multiply(newIVector3(1, 0, 0))) < 0.95) {
      IVector3 newX = newIVector3(1, 0, 0);
      IVector3 newZ = newX.cross(newY);
      newX = newY.cross(newZ);
      newX.normalize();
      newZ.normalize();
      return newIMatrix3(newX, newY, newZ).getTransposed();
    } else {
      IVector3 newZ = newIVector3(0, 0, 1);
      IVector3 newX = newY.cross(newZ);
      newZ = newX.cross(newY);
      newX.normalize();
      newZ.normalize();
      return newIMatrix3(newX, newY, newZ).getTransposed();
    }
  }

  /**
   * Create a new coordinate orthogonal coordinate system where the y-direction
   * points into the newY-direction.
   * 
   * @param newY
   *          New direction for the second base vector.
   * @return Coordinate system in matrix representation.
   */
  public static IMatrix3 createCoordinateFrameX(IVector3 oldX) {
    IVector3 newX = VectorMatrixFactory.newIVector3(oldX);
    newX.normalize();
    if (Math.abs(newX.multiply(newIVector3(0, 1, 0))) < 0.95) {
      IVector3 newY = newIVector3(0, 1, 0);
      IVector3 newZ = newX.cross(newY);
      newY = newZ.cross(newX);
      newY.normalize();
      newZ.normalize();
      return newIMatrix3(newX, newY, newZ).getTransposed();
    } else {
      IVector3 newZ = newIVector3(0, 0, 1);
      IVector3 newY = newZ.cross(newX);
      newZ = newX.cross(newY);
      newY.normalize();
      newZ.normalize();
      return newIMatrix3(newX, newY, newZ).getTransposed();
    }
  }

  /**
   * Create a new coordinate orthogonal coordinate system where the y-direction
   * points into the newY-direction.
   * 
   * @param newY
   *          New direction for the second base vector.
   * @return Coordinate system in matrix representation.
   */
  public static IMatrix3 createCoordinateFrameZ(IVector3 oldZ) {
    IVector3 newZ = VectorMatrixFactory.newIVector3(oldZ);
    newZ.normalize();
    if (Math.abs(newZ.multiply(newIVector3(0, 1, 0))) < 0.95) {
      IVector3 newY = newIVector3(0, 1, 0);
      IVector3 newX = newY.cross(newZ);
      newY = newZ.cross(newX);
      newX.normalize();
      newY.normalize();
      return newIMatrix3(newX, newY, newZ).getTransposed();
    } else {
      IVector3 newX = newIVector3(0, 0, 1);
      IVector3 newY = newZ.cross(newX);
      newX = newY.cross(newZ);
      newX.normalize();
      newY.normalize();
      return newIMatrix3(newX, newY, newZ).getTransposed();
    }
  }

  /**
   * Create a 4x4 matrix by providing all coordinates.
   */
  public static IMatrix4 newIMatrix4(double i, double j, double k, double l, double m, double n, double o, double p,
      double q, double r, double s, double t, double u, double v, double w, double x) {
    return newIMatrix4(VectorMatrixFactory.newIVector4(i, j, k, l), VectorMatrixFactory.newIVector4(m, n, o, p),
        VectorMatrixFactory.newIVector4(q, r, s, t), VectorMatrixFactory.newIVector4(u, v, w, x));
  }

  /**
   * Create a 4x identity matrix.
   */
  public static IMatrix4 newIMatrix4Identity() {
    return newIMatrix4(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }

  /**
   * Create a 4x4-matrix from a 3x3 matrix.
   */
  public static IMatrix4 newIMatrix4(IMatrix3 mat3) {
    return newIMatrix4(mat3.get(0, 0), mat3.get(0, 1), mat3.get(0, 2), 0, mat3.get(1, 0), mat3.get(1, 1),
        mat3.get(1, 2), 0, mat3.get(2, 0), mat3.get(2, 1), mat3.get(2, 2), 0, 0, 0, 0, 1);
  }

  /**
   * Create a 4x4-matrix from a 3x3 matrix.
   */
  public static IMatrix4 newIMatrix4(IMatrix4 other) {
    IMatrix4 result = newIMatrix4();
    for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
      for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
        result.set(rowIndex, columnIndex, other.get(rowIndex, columnIndex));
      }
    }
    return result;
  }

  /**
   * Create a 4x4-matrix from a 3x3 matrix.
   */
  public static IMatrix4 newIMatrix4(IMatrix other) {
    if (other.getNumberOfRows() != 4 || other.getNumberOfColumns() != 4) {
      throw new IllegalArgumentException();
    }
    IMatrix4 result = newIMatrix4();
    for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
      for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
        result.set(rowIndex, columnIndex, other.get(rowIndex, columnIndex));
      }
    }
    return result;
  }

  /**
   * Copy-constructor.
   * 
   * @param other
   * @return
   */
  public static IMatrix3 newIMatrix3(IMatrix other) {
    IMatrix3 matrix = VectorMatrixFactory.newIMatrix3();
    matrix.copy(other);
    return matrix;
  }

  /**
   * Make a homogenious translation matrix.
   */
  public static IMatrix4 makeTranslationMatrix(IVector3 translation) {
    IMatrix4 M = VectorMatrixFactory.newIMatrix4Identity();
    M.set(0, 3, translation.get(0));
    M.set(1, 3, translation.get(1));
    M.set(2, 3, translation.get(2));
    return M;
  }

  /**
   * Make a homogenious scale matrix.
   */
  public static IMatrix4 makeHomogeniousScaleMatrix(double scale) {
    IMatrix4 M = VectorMatrixFactory.newIMatrix4Identity();
    M.set(0, 0, scale);
    M.set(1, 1, scale);
    M.set(2, 2, scale);
    return M;
  }
}

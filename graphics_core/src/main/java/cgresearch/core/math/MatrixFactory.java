package cgresearch.core.math;

/**
 * Factory for matrices.
 * 
 * @author Philipp Jenke
 */
public class MatrixFactory {

  /**
   * Copy-create new matrix from existing matrix.
   * 
   * @return new instance.
   */
  public static Matrix createMatrix(Matrix other) {
    return new Matrix(other);
  }

  /**
   * Create a new matrix with given dimensions.
   * 
   * @return new instance.
   */
  public static Matrix createMatrix(int numberOfRows, int numberOfColumns) {
    return new Matrix(numberOfRows, numberOfColumns);
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
  public static Matrix createMatrix3(Vector row0, Vector row1, Vector row2) {
    return new Matrix(row0.get(0), row0.get(1), row0.get(2), row1.get(0), row1.get(1), row1.get(2), row2.get(0),
        row2.get(1), row2.get(2));
  }

  /**
   * Create a new instance of a 3x3-matrix.
   * 
   * @return new instance.
   */
  public static Matrix createMatrix3(double v00, double v01, double v02, double v10, double v11, double v12, double v20,
      double v21, double v22) {
    return new Matrix(v00, v01, v02, v10, v11, v12, v20, v21, v22);
  }

  /**
   * Create a 4x4 matrix by providing all coordinates.
   */
  public static Matrix createMatrix4(double i, double j, double k, double l, double m, double n, double o, double p,
      double q, double r, double s, double t, double u, double v, double w, double x) {
    return new Matrix(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
  }

  /**
   * Create a rotation matrix around an axis with a given angle.
   */
  public static Matrix createRotationMatrix(Vector axis, double angle) {
    double s = Math.sin(angle);
    double c = Math.cos(angle);
    double t = 1.0 - c;

    return createMatrix3(
        VectorFactory.createVector3(t * axis.get(0) * axis.get(0) + c, t * axis.get(0) * axis.get(1) + s * axis.get(2),
            t * axis.get(0) * axis.get(2) - s * axis.get(1)),
        VectorFactory.createVector3(t * axis.get(0) * axis.get(1) - s * axis.get(2), t * axis.get(1) * axis.get(1) + c,
            t * axis.get(1) * axis.get(2) + s * axis.get(0)),
        VectorFactory.createVector3(t * axis.get(0) * axis.get(2) + s * axis.get(1),
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
  public static Matrix createCoordinateFrameX(Vector oldX) {
    Vector newX = VectorFactory.createVector(oldX);
    newX.normalize();
    if (Math.abs(newX.multiply(VectorFactory.createVector3(0, 1, 0))) < 0.95) {
      Vector newY = VectorFactory.createVector3(0, 1, 0);
      Vector newZ = newX.cross(newY);
      newY = newZ.cross(newX);
      newY.normalize();
      newZ.normalize();
      return createMatrix3(newX, newY, newZ).getTransposed();
    } else {
      Vector newZ = VectorFactory.createVector3(0, 0, 1);
      Vector newY = newZ.cross(newX);
      newZ = newX.cross(newY);
      newY.normalize();
      newZ.normalize();
      return createMatrix3(newX, newY, newZ).getTransposed();
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
  public static Matrix createCoordinateFrameY(Vector newY) {
    newY.normalize();
    if (Math.abs(newY.multiply(VectorFactory.createVector3(1, 0, 0))) < 0.95) {
      Vector newX = VectorFactory.createVector3(1, 0, 0);
      Vector newZ = newX.cross(newY);
      newX = newY.cross(newZ);
      newX.normalize();
      newZ.normalize();
      return createMatrix3(newX, newY, newZ).getTransposed();
    } else {
      Vector newZ = VectorFactory.createVector3(0, 0, 1);
      Vector newX = newY.cross(newZ);
      newZ = newX.cross(newY);
      newX.normalize();
      newZ.normalize();
      return createMatrix3(newX, newY, newZ).getTransposed();
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
  public static Matrix createCoordinateFrameZ(Vector oldZ) {
    Vector newZ = VectorFactory.createVector(oldZ);
    newZ.normalize();
    if (Math.abs(newZ.multiply(VectorFactory.createVector3(0, 1, 0))) < 0.95) {
      Vector newY = VectorFactory.createVector3(0, 1, 0);
      Vector newX = newY.cross(newZ);
      newY = newZ.cross(newX);
      newX.normalize();
      newY.normalize();
      return createMatrix3(newX, newY, newZ).getTransposed();
    } else {
      Vector newX = VectorFactory.createVector3(0, 0, 1);
      Vector newY = newZ.cross(newX);
      newX = newY.cross(newZ);
      newX.normalize();
      newY.normalize();
      return createMatrix3(newX, newY, newZ).getTransposed();
    }
  }

  /**
   * Create a 4x identity matrix.
   */
  public static Matrix createIdentityMatrix4() {
    return createMatrix4(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }

  /**
   * Make a homogenious translation matrix.
   */
  public static Matrix createHomogeniousTranslationMatrix(Vector translation) {
    Matrix M = createIdentityMatrix4();
    M.set(0, 3, translation.get(0));
    M.set(1, 3, translation.get(1));
    M.set(2, 3, translation.get(2));
    return M;
  }

  /**
   * Make a homogenious scale matrix.
   */
  public static Matrix createHomogeniousScaleMatrix(double scale) {
    Matrix M = createIdentityMatrix4();
    M.set(0, 0, scale);
    M.set(1, 1, scale);
    M.set(2, 2, scale);
    return M;
  }

  /**
   * Create homogenious matrix for a 3-space matrix by adding a row and a column
   * and setting the lower right corner to 1.
   * 
   * @param other
   *          Input matrix.
   * @return New homogenious 4-space matrix.
   */
  public static Matrix createHomogeniousFor3spaceMatrix(Matrix other) {
    if (other.getNumberOfRows() != 3 || other.getNumberOfColumns() != 3) {
      throw new IllegalArgumentException();
    }
    Matrix result = new Matrix(4, 4);
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        result.set(row, col, other.get(row, col));
      }
    }
    result.set(3, 3, 1);
    return result;
  }

}

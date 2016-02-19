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

  public static Vector newVector(int dimension) {
    return new Vector(dimension);
  }

  /**
   * Create a new instance for a 3-vector. Copy values from other vector.
   * 
   * @param other
   *          Vector to be copied from.
   * @return new instance.
   */
  public static Vector newVector(Vector other) {
    return new Vector(other);
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
  public static Vector newVector(double x, double y, double z) {
    return new Vector(x, y, z);
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
  public static Vector newVector(double x, double y, double z, double w) {
    return new Vector(x, y, z, w);
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
  public static Matrix newMatrix(Vector row0, Vector row1, Vector row2) {
    return new Matrix(row0.get(0), row0.get(1), row0.get(2), row1.get(0), row1.get(1), row1.get(2), row2.get(0),
        row2.get(1), row2.get(2));
  }

  /**
   * Create a new instance of a 3x3-matrix.
   */
  public static Matrix newMatrix(double v00, double v01, double v02, double v10, double v11, double v12, double v20,
      double v21, double v22) {
    return new Matrix(v00, v01, v02, v10, v11, v12, v20, v21, v22);
  }

  /**
   * Create a new instance for a matrix.
   * 
   * @return new instance.
   */
  public static Matrix newMatrix(Matrix other) {
    return new Matrix(other);
  }

  /**
   * Create a new instance for a matrix.
   * 
   * @return new instance.
   */
  public static Matrix newMatrix(int numberOfRows, int numberOfColumns) {
    return new Matrix(numberOfRows, numberOfColumns);
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
  // public static Matrix newMatrix(Vector row0, Vector row1, Vector
  // row2, Vector row3) {
  // return new Matrix(row0, row1, row2, row3);
  // }

  /**
   * Create a rotation matrix around an axis with a given angle.
   */
  public static Matrix getRotationMatrix(Vector axis, double angle) {
    double s = Math.sin(angle);
    double c = Math.cos(angle);
    double t = 1.0 - c;

    return VectorMatrixFactory.newMatrix(
        VectorMatrixFactory.newVector(t * axis.get(0) * axis.get(0) + c,
            t * axis.get(0) * axis.get(1) + s * axis.get(2), t * axis.get(0) * axis.get(2) - s * axis.get(1)),
        VectorMatrixFactory.newVector(t * axis.get(0) * axis.get(1) - s * axis.get(2),
            t * axis.get(1) * axis.get(1) + c, t * axis.get(1) * axis.get(2) + s * axis.get(0)),
        VectorMatrixFactory.newVector(t * axis.get(0) * axis.get(2) + s * axis.get(1),
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
  public static Matrix createCoordinateFrameY(Vector newY) {
    newY.normalize();
    if (Math.abs(newY.multiply(newVector(1, 0, 0))) < 0.95) {
      Vector newX = newVector(1, 0, 0);
      Vector newZ = newX.cross(newY);
      newX = newY.cross(newZ);
      newX.normalize();
      newZ.normalize();
      return newMatrix(newX, newY, newZ).getTransposed();
    } else {
      Vector newZ = newVector(0, 0, 1);
      Vector newX = newY.cross(newZ);
      newZ = newX.cross(newY);
      newX.normalize();
      newZ.normalize();
      return newMatrix(newX, newY, newZ).getTransposed();
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
  public static Matrix createCoordinateFrameX(Vector oldX) {
    Vector newX = VectorMatrixFactory.newVector(oldX);
    newX.normalize();
    if (Math.abs(newX.multiply(newVector(0, 1, 0))) < 0.95) {
      Vector newY = newVector(0, 1, 0);
      Vector newZ = newX.cross(newY);
      newY = newZ.cross(newX);
      newY.normalize();
      newZ.normalize();
      return newMatrix(newX, newY, newZ).getTransposed();
    } else {
      Vector newZ = newVector(0, 0, 1);
      Vector newY = newZ.cross(newX);
      newZ = newX.cross(newY);
      newY.normalize();
      newZ.normalize();
      return newMatrix(newX, newY, newZ).getTransposed();
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
    Vector newZ = VectorMatrixFactory.newVector(oldZ);
    newZ.normalize();
    if (Math.abs(newZ.multiply(newVector(0, 1, 0))) < 0.95) {
      Vector newY = newVector(0, 1, 0);
      Vector newX = newY.cross(newZ);
      newY = newZ.cross(newX);
      newX.normalize();
      newY.normalize();
      return newMatrix(newX, newY, newZ).getTransposed();
    } else {
      Vector newX = newVector(0, 0, 1);
      Vector newY = newZ.cross(newX);
      newX = newY.cross(newZ);
      newX.normalize();
      newY.normalize();
      return newMatrix(newX, newY, newZ).getTransposed();
    }
  }

  /**
   * Create a 4x4 matrix by providing all coordinates.
   */
  public static Matrix newMatrix(double i, double j, double k, double l, double m, double n, double o, double p,
      double q, double r, double s, double t, double u, double v, double w, double x) {
    return new Matrix(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
  }

  /**
   * Create a 4x identity matrix.
   */
  public static Matrix newMatrixIdentity() {
    return newMatrix(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }

  /**
   * Make a homogenious translation matrix.
   */
  public static Matrix makeTranslationMatrix(Vector translation) {
    Matrix M = VectorMatrixFactory.newMatrixIdentity();
    M.set(0, 3, translation.get(0));
    M.set(1, 3, translation.get(1));
    M.set(2, 3, translation.get(2));
    return M;
  }

  /**
   * Make a homogenious scale matrix.
   */
  public static Matrix makeHomogeniousScaleMatrix(double scale) {
    Matrix M = VectorMatrixFactory.newMatrixIdentity();
    M.set(0, 0, scale);
    M.set(1, 1, scale);
    M.set(2, 2, scale);
    return M;
  }

  public static Matrix dim3toDim4(Matrix other) {
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

  public static Vector dim4toDim3(Vector v) {
    if (v.getDimension() != 4) {
      throw new IllegalArgumentException();
    }
    return VectorMatrixFactory.newVector(v.get(0), v.get(1), v.get(2));
  }

  public static Vector dim3toDim4(Vector v) {
    if (v.getDimension() != 3) {
      throw new IllegalArgumentException();
    }
    return VectorMatrixFactory.newVector(v.get(0), v.get(1), v.get(2), 1);
  }
}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

import Jama.Matrix;

/**
 * Implementation of a matrix in 4-space.
 * 
 * @author Philipp Jenke
 * 
 */
public class Matrix4 implements IMatrix4 {
  /**
   * Entries of the matrix
   */
  private final double[][] rows = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };

  /**
   * Constructor.
   */
  public Matrix4() {
  }

  /**
   * Constructor with initial values.
   */
  public Matrix4(IVector4 row0, IVector4 row1, IVector4 row2, IVector4 row3) {
    setRow(MathHelpers.INDEX_0, row0);
    setRow(MathHelpers.INDEX_1, row1);
    setRow(MathHelpers.INDEX_2, row2);
    setRow(MathHelpers.INDEX_3, row3);
  }

  @Override
  public void setRow(final int index, IVector4 row) {
    for (int i = 0; i < 4; i++) {
      set(index, i, row.get(i));
    }
  }

  @Override
  public IVector4 multiply(final IVector other) {
    if (other.getDimension() != 4) {
      throw new IllegalArgumentException();
    }
    IVector4 result = VectorMatrixFactory.newIVector4();
    for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
      double d = 0;
      for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
        d += get(rowIndex, columnIndex) * other.get(columnIndex);
      }
      result.set(rowIndex, d);
    }
    return result;
  }

  @Override
  public void set(int rowIndex, int columnIndex, double value) {
    rows[rowIndex][columnIndex] = value;
  }

  @Override
  public double get(int rowIndex, int columnIndex) {
    return rows[rowIndex][columnIndex];
  }

  @Override
  public double[] toArray() {
    double[] doubleArray = { get(0, 0), get(0, 1), get(0, 2), get(0, 3), get(1, 0), get(1, 1), get(1, 2), get(1, 3),
        get(2, 0), get(2, 1), get(2, 2), get(2, 3), get(3, 0), get(3, 1), get(3, 2), get(3, 3) };
    return doubleArray;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.haw.cg.math.IMatrix4#getTransposed()
   */
  @Override
  public IMatrix4 getTransposed() {
    return VectorMatrixFactory.newIMatrix4(get(0, 0), get(1, 0), get(2, 0), get(3, 0), get(0, 1), get(1, 1), get(2, 1),
        get(3, 1), get(0, 2), get(1, 2), get(2, 2), get(3, 2), get(0, 3), get(1, 3), get(2, 3), get(3, 3));
  }

  @Override
  public double[] data() {
    double[] data = { rows[0][0], rows[0][1], rows[0][2], rows[0][3], rows[1][0], rows[1][1], rows[1][2], rows[1][3],
        rows[2][0], rows[2][1], rows[2][2], rows[2][3], rows[3][0], rows[0][1], rows[0][2], rows[0][3] };
    return data;
  }

  @Override
  public int hashCode() {
    return rows[0].hashCode() + rows[1].hashCode() + rows[2].hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof Matrix4)) {
      return false;
    }
    Matrix4 otherMatrix = (Matrix4) other;
    for (int i = 0; i < 4; i++) {
      if (!rows[i].equals(otherMatrix.rows[i])) {
        return false;
      }
    }
    return true;
  }

  @Override
  public IMatrix4 multiply(IMatrix other) {
    if (other.getNumberOfColumns() != 4 || other.getNumberOfRows() != 4) {
      throw new IllegalArgumentException();
    }
    IMatrix4 result = VectorMatrixFactory.newIMatrix4();
    for (int row = 0; row < 4; row++) {
      for (int column = 0; column < 4; column++) {
        double d = 0;
        for (int i = 0; i < 4; i++) {
          d += get(row, i) * other.get(i, column);
        }
        result.set(row, column, d);
      }
    }
    return result;
  }

  @Override
  public IVector4 getColumn(int i) {
    return VectorMatrixFactory.newIVector4(get(0, i), get(1, i), get(2, i), get(3, i));
  }

  @Override
  public String toString() {
    int precision = 5;
    return String.format(
        "/ %." + precision + "f, %." + precision + "f, %." + precision + "f, %." + precision + "f \\\n| %." + precision
            + "f, %." + precision + "f, %." + precision + "f, %." + precision + "f |\n| %." + precision + "f, %."
            + precision + "f, %." + precision + "f, %." + precision + "f |\n\\ %." + precision + "f, %." + precision
            + "f, %." + precision + "f, %." + precision + "f /",
        get(0, 0), get(0, 1), get(0, 2), get(0, 3), get(1, 0), get(1, 1), get(1, 2), get(1, 3), get(2, 0), get(2, 1),
        get(2, 2), get(2, 3), get(3, 0), get(3, 1), get(3, 2), get(3, 3));

  }

  @Override
  public int getNumberOfRows() {
    return 4;
  }

  @Override
  public int getNumberOfColumns() {
    return 4;
  }

  @Override
  public IMatrix4 add(IMatrix other) {
    if (other.getNumberOfColumns() != 4 || other.getNumberOfRows() != 4) {
      throw new IllegalArgumentException();
    }
    IMatrix4 result = VectorMatrixFactory.newIMatrix4();
    for (int row = 0; row < 4; row++) {
      for (int column = 0; column < 4; column++) {
        result.set(row, column, get(row, column) + other.get(row, column));
      }
    }
    return result;
  }

  @Override
  public double getDeterminant() {
    Matrix M = new Matrix(rows);
    return M.det();
  }

  @Override
  public IMatrix4 getInverse() {
    Matrix M = new Matrix(rows);
    Matrix invM = M.inverse();
    IMatrix4 result = VectorMatrixFactory.newIMatrix4();
    for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
      for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
        result.set(rowIndex, columnIndex, invM.getArray()[rowIndex][columnIndex]);
      }
    }
    return result;
  }
};

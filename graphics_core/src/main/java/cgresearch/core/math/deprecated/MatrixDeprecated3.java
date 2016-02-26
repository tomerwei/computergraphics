///**
// * Prof. Philipp Jenke
// * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
// * Lecture demo program.
// */
//package cgresearch.core.math.deprecated;
//
//import cgresearch.core.math.Matrix;
//import cgresearch.core.math.Vector;
//import cgresearch.core.math.Vector;
//import cgresearch.core.math.MathHelpers;
//import cgresearch.core.math.Matrix;
//import cgresearch.core.math.VectorMatrixFactory;
//
///**
// * Implementation of a 3-dimensional matrix.
// * 
// * @author Philipp Jenke
// * 
// */
//public class MatrixDeprecated3 {
//
//  /**
//   * Entries of the matrix
//   */
//  private final Vector[] rows =
//      { VectorMatrixFactory.newVector(), VectorMatrixFactory.newVector(), VectorMatrixFactory.newVector() };
//
//  /**
//   * Constructor.
//   */
//  public MatrixDeprecated3() {
//  }
//
//  /**
//   * Constructor with initial values.
//   */
//  public MatrixDeprecated3(double v00, double v01, double v02, double v10, double v11, double v12, double v20,
//      double v21, double v22) {
//    rows[0] = VectorMatrixFactory.newVector(v00, v01, v02);
//    rows[1] = VectorMatrixFactory.newVector(v10, v11, v12);
//    rows[2] = VectorMatrixFactory.newVector(v20, v21, v22);
//  }
//
//  /**
//   * Constructor with initial values.
//   */
//  public MatrixDeprecated3(Vector row1, Vector row2, Vector row3) {
//    rows[MathHelpers.INDEX_0] = row1;
//    rows[MathHelpers.INDEX_1] = row2;
//    rows[MathHelpers.INDEX_2] = row3;
//  }
//
//  public void setRow(final int index, Vector row) {
//    rows[index] = row;
//  }
//
//  public Vector multiply(final Vector other) {
//    Vector result = VectorMatrixFactory.newVector();
//    for (int i = 0; i < MathHelpers.DIMENSION_3; i++) {
//      result.set(i, getRow(i).multiply(other));
//    }
//
//    return result;
//  }
//
//  public Vector getRow(final int index) {
//    return rows[index];
//  }
//
//  public double[] data() {
//    double[] data = { rows[0].get(0), rows[0].get(1), rows[0].get(2), rows[1].get(0), rows[1].get(1), rows[1].get(2),
//        rows[2].get(0), rows[2].get(1), rows[2].get(2) };
//    return data;
//  }
//
//  public Matrix getTransposed() {
//    return new Matrix(get(0, 0), get(1, 0), get(2, 0), get(0, 1), get(1, 1), get(2, 1), get(0, 2), get(1, 2),
//        get(2, 2));
//  }
//
//  public double get(int rowIndex, int columnIndex) {
//    return rows[rowIndex].get(columnIndex);
//  }
//
////  public boolean equals(Object other) {
////    if (other == null || !(other instanceof Matrix)) {
////      return false;
////    }
////    Matrix otherMatrix = (Matrix) other;
////    for (int i = 0; i < 3; i++) {
////      if (!rows[i].equals(otherMatrix.rows[i])) {
////        return false;
////      }
////    }
////    return true;
////  }
//
//  public void set(int rowIndex, int columnIndex, double d) {
//    rows[rowIndex].set(columnIndex, d);
//  }
//
//  // public Matrix add(Matrix other) {
//  // if (other.getNumberOfColumns() != 3 || other.getNumberOfColumns() != 3) {
//  // throw new IllegalArgumentException();
//  // }
//  // Matrix m = VectorMatrixFactory.newMatrix();
//  // for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
//  // for (int columnIndex = 0; columnIndex < 3; columnIndex++) {
//  // m.set(rowIndex, columnIndex, get(rowIndex, columnIndex) +
//  // other.get(rowIndex, columnIndex));
//  // }
//  // }
//  // return m;
//  // }
//
//  public String toString() {
//    int precision = 5;
//    return String.format(
//        "/ %." + precision + "f, %." + precision + "f, %." + precision + "f \\\n| %." + precision + "f, %." + precision
//            + "f, %." + precision + "f |\n\\ %." + precision + "f, %." + precision + "f, %." + precision + "f /",
//        get(0, 0), get(0, 1), get(0, 2), get(1, 0), get(1, 1), get(1, 2), get(2, 0), get(2, 1), get(2, 2));
//
//  }
//
//  public int hashCode() {
//    return rows[0].hashCode() + rows[1].hashCode() + rows[2].hashCode();
//  }
//
//  public void copy(Matrix other) {
//    if (other.getNumberOfColumns() != 3 || other.getNumberOfRows() != 3) {
//      throw new IllegalArgumentException();
//    }
//    for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
//      for (int columnIndex = 0; columnIndex < 3; columnIndex++) {
//        set(rowIndex, columnIndex, other.get(rowIndex, columnIndex));
//      }
//    }
//  }
//
////  @Override
////  public double[] data4x4() {
//////    double[] data = { rows[0].get(0), rows[0].get(1), rows[0].get(2), 0, rows[1].get(0), rows[1].get(1), rows[1].get(2),
//////        0, rows[2].get(0), rows[2].get(1), rows[2].get(2), 0, 0, 0, 0, 1 };
//////    return data;
////  }
//
////  public Matrix multiply(Matrix other) {
////    if (other.getNumberOfRows() != 3 || other.getNumberOfColumns() != 3) {
////      throw new IllegalArgumentException();
////    }
////    Matrix result = new Matrix();
////    for (int row = 0; row < 3; row++) {
////      for (int column = 0; column < 3; column++) {
////        double d = 0;
////        for (int i = 0; i < 3; i++) {
////          d += get(row, i) * other.get(i, column);
////        }
////        result.set(row, column, d);
////      }
////    }
////    return result;
////  }
//
//  public Vector getColumn(int columnIndex) {
//    return VectorMatrixFactory.newVector(get(0, columnIndex), get(1, columnIndex), get(2, columnIndex));
//  }
//
////  public Matrix getInverse() {
////    double det = getDeterminant();
////    double a = get(0, 0);
////    double b = get(0, 1);
////    double c = get(0, 2);
////    double d = get(1, 0);
////    double e = get(1, 1);
////    double f = get(1, 2);
////    double g = get(2, 0);
////    double h = get(2, 1);
////    double i = get(2, 2);
////    Matrix inverse = VectorMatrixFactory.newMatrix(e * i - f * h, c * h - b * i, b * f - c * e, f * g - d * i,
////        a * i - c * g, c * d - a * f, d * h - e * g, b * g - a * h, a * e - b * d).multiply(1.0 / det);
////    return inverse;
////  }
//
//  public double getDeterminant() {
//    return get(0, 0) * get(1, 1) * get(2, 2) + get(0, 1) * get(1, 2) * get(2, 0) + get(0, 2) * get(1, 0) * get(2, 1)
//        - get(0, 2) * get(1, 1) * get(2, 0) - get(0, 1) * get(1, 0) * get(2, 2) - get(0, 0) * get(1, 2) * get(2, 1);
//  }
//
////  public Matrix multiply(double factor) {
////    Matrix result = new Matrix();
////    for (int row = 0; row < 3; row++) {
////      for (int column = 0; column < 3; column++) {
////        result.set(row, column, get(row, column) * factor);
////      }
////    }
////    return result;
////  }
//
//  public int getNumberOfRows() {
//    return 3;
//  }
//
//  public int getNumberOfColumns() {
//    return 3;
//  }
//}

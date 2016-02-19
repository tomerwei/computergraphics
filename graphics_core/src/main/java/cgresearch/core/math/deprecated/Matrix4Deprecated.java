///**
// * Prof. Philipp Jenke
// * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
// * Lecture demo program.
// */
//package cgresearch.core.math.deprecated;
//
//import Jama.Matrix;
//import cgresearch.core.math.Vector;
//import cgresearch.core.math.Vector;
//import cgresearch.core.math.MathHelpers;
//import cgresearch.core.math.VectorMatrixFactory;
//
///**
// * Implementation of a matrix in 4-space.
// * 
// * @author Philipp Jenke
// * 
// */
//public class Matrix4Deprecated {
//  /**
//   * Entries of the matrix
//   */
//  private final double[][] rows = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
//
//  /**
//   * Constructor.
//   */
//  public Matrix4Deprecated() {
//  }
//
//  /**
//   * Constructor with initial values.
//   */
//  public Matrix4Deprecated(Vector row0, Vector row1, Vector row2, Vector row3) {
//    setRow(MathHelpers.INDEX_0, row0);
//    setRow(MathHelpers.INDEX_1, row1);
//    setRow(MathHelpers.INDEX_2, row2);
//    setRow(MathHelpers.INDEX_3, row3);
//  }
//
//  public void setRow(final int index, Vector row) {
//    for (int i = 0; i < 4; i++) {
//      set(index, i, row.get(i));
//    }
//  }
//
//  public Vector multiply(final Vector other) {
//    if (other.getDimension() != 4) {
//      throw new IllegalArgumentException();
//    }
//    Vector result = VectorMatrixFactory.newVector();
//    for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
//      double d = 0;
//      for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
//        d += get(rowIndex, columnIndex) * other.get(columnIndex);
//      }
//      result.set(rowIndex, d);
//    }
//    return result;
//  }
//
//  public void set(int rowIndex, int columnIndex, double value) {
//    rows[rowIndex][columnIndex] = value;
//  }
//
//  public double get(int rowIndex, int columnIndex) {
//    return rows[rowIndex][columnIndex];
//  }
//
//  public double[] toArray() {
//    double[] doubleArray = { get(0, 0), get(0, 1), get(0, 2), get(0, 3), get(1, 0), get(1, 1), get(1, 2), get(1, 3),
//        get(2, 0), get(2, 1), get(2, 2), get(2, 3), get(3, 0), get(3, 1), get(3, 2), get(3, 3) };
//    return doubleArray;
//  }
//
//  /*
//   * (nicht-Javadoc)
//   * 
//   * @see edu.haw.cg.math.Matrix#getTransposed()
//   */
//
////  public Matrix getTransposed() {
////    return VectorMatrixFactory.newMatrix(get(0, 0), get(1, 0), get(2, 0), get(3, 0), get(0, 1), get(1, 1), get(2, 1),
////        get(3, 1), get(0, 2), get(1, 2), get(2, 2), get(3, 2), get(0, 3), get(1, 3), get(2, 3), get(3, 3));
////  }
//
//  public double[] data() {
//    double[] data = { rows[0][0], rows[0][1], rows[0][2], rows[0][3], rows[1][0], rows[1][1], rows[1][2], rows[1][3],
//        rows[2][0], rows[2][1], rows[2][2], rows[2][3], rows[3][0], rows[0][1], rows[0][2], rows[0][3] };
//    return data;
//  }
//
//  public int hashCode() {
//    return rows[0].hashCode() + rows[1].hashCode() + rows[2].hashCode();
//  }
//
//  public boolean equals(Object other) {
//    throw new IllegalArgumentException();
//    // if (other == null || !(other instanceof Matrix)) {
//    // return false;
//    // }
//    // Matrix otherMatrix = (Matrix) other;
//    // for (int i = 0; i < 4; i++) {
//    // if (!rows[i].equals(otherMatrix.rows[i])) {
//    // return false;
//    // }
//    // }
//    // return true;
//  }
////
////  public Matrix multiply(Matrix other) {
////    if (other.getNumberOfColumns() != 4 || other.getNumberOfRows() != 4) {
////      throw new IllegalArgumentException();
////    }
////    Matrix result = VectorMatrixFactory.newMatrix();
////    for (int row = 0; row < 4; row++) {
////      for (int column = 0; column < 4; column++) {
////        double d = 0;
////        for (int i = 0; i < 4; i++) {
////          d += get(row, i) * other.get(i, column);
////        }
////        result.set(row, column, d);
////      }
////    }
////    return result;
////  }
//
//  public Vector getColumn(int i) {
//    return VectorMatrixFactory.newVector(get(0, i), get(1, i), get(2, i), get(3, i));
//  }
//
//  @Override
//  public String toString() {
//    int precision = 5;
//    return String.format(
//        "/ %." + precision + "f, %." + precision + "f, %." + precision + "f, %." + precision + "f \\\n| %." + precision
//            + "f, %." + precision + "f, %." + precision + "f, %." + precision + "f |\n| %." + precision + "f, %."
//            + precision + "f, %." + precision + "f, %." + precision + "f |\n\\ %." + precision + "f, %." + precision
//            + "f, %." + precision + "f, %." + precision + "f /",
//        get(0, 0), get(0, 1), get(0, 2), get(0, 3), get(1, 0), get(1, 1), get(1, 2), get(1, 3), get(2, 0), get(2, 1),
//        get(2, 2), get(2, 3), get(3, 0), get(3, 1), get(3, 2), get(3, 3));
//
//  }
//
//  public int getNumberOfRows() {
//    return 4;
//  }
//
//  public int getNumberOfColumns() {
//    return 4;
//  }
//
////  public Matrix add(Matrix other) {
////    if (other.getNumberOfColumns() != 4 || other.getNumberOfRows() != 4) {
////      throw new IllegalArgumentException();
////    }
////    Matrix result = VectorMatrixFactory.newMatrix();
////    for (int row = 0; row < 4; row++) {
////      for (int column = 0; column < 4; column++) {
////        result.set(row, column, get(row, column) + other.get(row, column));
////      }
////    }
////    return result;
////  }
//
//  public double getDeterminant() {
//    Matrix M = new Matrix(rows);
//    return M.det();
//  }
//
//  // public Matrix getInverse() {
//  // Matrix M = new Matrix(rows);
//  // Matrix invM = M.inverse();
//  // Matrix result = VectorMatrixFactory.newMatrix();
//  // for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
//  // for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
//  // result.set(rowIndex, columnIndex, invM.getArray()[rowIndex][columnIndex]);
//  // }
//  // }
//  // return result;
//  // }
//};

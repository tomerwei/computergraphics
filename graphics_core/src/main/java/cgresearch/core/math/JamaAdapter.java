package cgresearch.core.math;

/**
 * Conversion between Jama data types and the cg framework data types.
 * 
 * @author Philipp Jenke
 *
 */
public class JamaAdapter {

  /**
   * Convert Jama matrix to cg framework matrix.
   */
  public static Matrix jama2matrix(Jama.Matrix jamaMatrix) {
    Matrix matrix = VectorMatrixFactory.newMatrix(3, 3);
    for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
      for (int colIndex = 0; colIndex < 3; colIndex++) {
        matrix.set(rowIndex, colIndex, jamaMatrix.get(rowIndex, colIndex));
      }
    }
    return matrix;
  }

  /**
   * Convert cg framework matrix to Jama matrix.
   */
  public static Jama.Matrix matrix2jama(Matrix matrix) {
    Jama.Matrix jamaMatrix = new Jama.Matrix(3, 3);
    for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
      for (int colIndex = 0; colIndex < 3; colIndex++) {
        jamaMatrix.set(rowIndex, colIndex, matrix.get(rowIndex, colIndex));
      }
    }
    return jamaMatrix;
  }
}

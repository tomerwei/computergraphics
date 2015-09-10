package cgresearch.core.math;

import cgresearch.core.math.jama.Matrix;

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
	public static IMatrix3 jama2matrix(Matrix jamaMatrix) {
		IMatrix3 matrix = VectorMatrixFactory.newIMatrix3();
		for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
			for (int colIndex = 0; colIndex < 3; colIndex++) {
				matrix.set(rowIndex, colIndex,
						jamaMatrix.get(rowIndex, colIndex));
			}
		}
		return matrix;
	}

	/**
	 * Convert cg framework matrix to Jama matrix.
	 */
	public static Matrix matrix2jama(IMatrix3 matrix) {
		Matrix jamaMatrix = new Matrix(3, 3);
		for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
			for (int colIndex = 0; colIndex < 3; colIndex++) {
				jamaMatrix.set(rowIndex, colIndex,
						matrix.get(rowIndex, colIndex));
			}
		}
		return jamaMatrix;
	}
}

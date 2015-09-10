/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.jama.EigenvalueDecomposition;
import cgresearch.core.math.jama.Matrix;

/**
 * Apply a principle component analysis on a list of points in 3-space.
 * 
 * @author Philipp Jenke
 * 
 */
public class PrincipalComponentAnalysis {

	/**
	 * Tangent in u-direction.
	 */
	private IVector3 tangentU = null;

	/**
	 * Tangent in v-direction.
	 */
	private IVector3 tangentV = null;

	/**
	 * Normal
	 */
	private IVector3 normal = null;

	/**
	 * Centroid
	 */
	private IVector3 centroid = null;

	/**
	 * Eigenvalues
	 */
	private IVector3 eigenValues = null;

	/**
	 * Container for the points.
	 */
	List<IVector3> points = new ArrayList<IVector3>();

	/**
	 * Constructor
	 */
	public PrincipalComponentAnalysis() {
	}

	/**
	 * Add an additional point.
	 */
	public void add(IVector3 point) {
		points.add(point);
	}

	/**
	 * Apply the PCA, compute tangentU, tangentV and normal.
	 */
	public void applyPCA() {

		if (points.size() < 3) {
			Logger.getInstance().error("Need a least 3 points for PCA");
			return;
		}

		// Compute centroid
		centroid = VectorMatrixFactory.newIVector3(0, 0, 0);
		for (IVector3 p : points) {
			centroid = centroid.add(p);
		}
		centroid = centroid.multiply(1.0 / points.size());

		// Compute the covariance matrix
		IMatrix3 M = VectorMatrixFactory.newIMatrix3(0, 0, 0, 0, 0, 0, 0, 0, 0);
		for (IVector3 p : points) {
			IVector3 d = p.subtract(centroid);
			M = M.add(d.vectorProduct(d));
		}

		// Singular value decomposition
		Matrix jamaM = new Matrix(3, 3);
		for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
			for (int colIndex = 0; colIndex < 3; colIndex++) {
				jamaM.set(colIndex, rowIndex, M.get(colIndex, rowIndex));
			}
		}
		EigenvalueDecomposition e = jamaM.eig();
		Matrix V = e.getV();
		Matrix D = e.getD();

		normal = VectorMatrixFactory.newIVector3(V.get(0, 0), V.get(1, 0),
				V.get(2, 0));
		tangentU = VectorMatrixFactory.newIVector3(V.get(0, 1), V.get(1, 1),
				V.get(2, 1));
		tangentV = VectorMatrixFactory.newIVector3(V.get(0, 2), V.get(1, 2),
				V.get(2, 2));
		eigenValues = VectorMatrixFactory.newIVector3(D.get(0, 0), V.get(1, 0),
				V.get(2, 0));

	}

	/**
	 * Getter.
	 */
	public IVector3 getTangentU() {
		return tangentU;
	}

	/**
	 * Getter.
	 */
	public IVector3 getTangentV() {
		return tangentV;
	}

	/**
	 * Getter.
	 */
	public IVector3 getNormal() {
		return normal;
	}

	/**
	 * Getter.
	 */
	public IVector3 getCentroid() {
		return centroid;
	}

	/**
	 * Getter.
	 */
	public IVector3 getEigenValues() {
		return eigenValues;
	}

	/**
	 * Clear list of points.
	 */
	public void clear() {
		points.clear();
	}

}

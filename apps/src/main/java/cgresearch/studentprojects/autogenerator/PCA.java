package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IMatrix;
import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.core.math.jama.EigenvalueDecomposition;
import cgresearch.core.math.Matrix;

public class PCA {

	private int dimension;

	/**
	 * Tangent in u-direction.
	 */
	private IVector tangentU = null;

	/**
	 * Tangent in v-direction.
	 */
	private IVector tangentV = null;

	/**
	 * Normal
	 */
	private IVector normal = null;

	/**
	 * Centroid
	 */
	private IVector centroid = null;

	/**
	 * Eigenvalues
	 */
	private IVector eigenValues = null;

	/**
	 * Container for the points.
	 */
	List<IVector> points = new ArrayList<IVector>();

	/**
	 * Constructor
	 */
	public PCA(int dimension) {
		this.dimension = dimension;
	}

	/**
	 * Add an additional point.
	 */
	public void add(IVector point) {
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
		centroid = new Vector(dimension); // VectorMatrixFactory.newIVector3(0,
											// 0, 0);
		for (IVector p : points) {
			centroid = centroid.add(p);
		}
		centroid = centroid.multiply(1.0 / points.size());

		// Compute the covariance matrix
		// IMatrix3 M = VectorMatrixFactory.newIMatrix3(0, 0, 0, 0, 0, 0, 0, 0,
		// 0);

		IMatrix M = new Matrix(dimension, dimension);
		for (IVector p : points) {
			IVector d = p.subtract(centroid);
			M = M.add(d.vectorProduct(d, dimension));
		}

		// Singular value decomposition
		cgresearch.core.math.jama.Matrix jamaM = new cgresearch.core.math.jama.Matrix(dimension, dimension);
		for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
			for (int colIndex = 0; colIndex < dimension; colIndex++) {
				jamaM.set(colIndex, rowIndex, M.get(colIndex, rowIndex));
			}
		}
		EigenvalueDecomposition e = jamaM.eig();
		cgresearch.core.math.jama.Matrix V = e.getV();
		cgresearch.core.math.jama.Matrix D = e.getD();

		normal = new Vector(dimension);
		for (int i = 0; i < dimension; i++)
			normal.set(i, V.get(i, 0));
		// VectorMatrixFactory.newIVector3(V.get(0, 0), V.get(1, 0), V.get(2,
		// 0));

		tangentU = new Vector(dimension);
		for (int i = 0; i < dimension; i++)
			tangentU.set(i, V.get(i, 1));
		// VectorMatrixFactory.newIVector3(V.get(0, 1), V.get(1, 1), V.get(2,
		// 1));

		tangentV = new Vector(dimension);
		for (int i = 0; i < dimension; i++)
			tangentV.set(i, V.get(i, 2));
		// VectorMatrixFactory.newIVector3(V.get(0, 2), V.get(1, 2), V.get(2,
		// 2));

		eigenValues = new Vector(dimension);
		for (int i = 0; i < dimension; i++)
			eigenValues.set(i, D.get(i, 0));
		// VectorMatrixFactory.newIVector3(D.get(0, 0), V.get(1, 0), V.get(2,
		// 0));

	}

	/**
	 * Getter.
	 */
	public IVector getTangentU() {
		return tangentU;
	}

	/**
	 * Getter.
	 */
	public IVector getTangentV() {
		return tangentV;
	}

	/**
	 * Getter.
	 */
	public IVector getNormal() {
		return normal;
	}

	/**
	 * Getter.
	 */
	public IVector getCentroid() {
		return centroid;
	}

	/**
	 * Getter.
	 */
	public IVector getEigenValues() {
		return eigenValues;
	}

	/**
	 * Clear list of points.
	 */
	public void clear() {
		points.clear();
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.scenegraph;

import cgresearch.core.math.*;

/**
 * Tranformation representation for transformations in the scene graph.
 * 
 * @author Philipp Jenke
 * 
 */
public class Transformation extends ICgNodeContent {

	/**
	 * Representation of the transformation as homogenious 4x4 matrix
	 */
	private IMatrix4 transformation = VectorMatrixFactory.newIMatrix4Identity();

	/**
	 * Transposed transformation.
	 */
	private IMatrix4 transposedTransformation = VectorMatrixFactory
			.newIMatrix4Identity();

	/**
	 * Constructor
	 */
	public Transformation() {
	}

	/**
	 * Reset to identity.
	 */
	public void makeIdentity() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i == j) {
					transformation.setRow(i, j, 1);
				} else {
					transformation.setRow(i, j, 0);
				}
			}
		}
		updateTransposedTransformation();
	}

	/**
	 * Update the cached transposed transformation.
	 */
	private void updateTransposedTransformation() {
		transposedTransformation = transformation.getTransposed();
	}

	/**
	 * Convenience method for makeIdentity().
	 */
	public void reset() {
		makeIdentity();
	}

	/**
	 * Include a translation.
	 */
	public void addTranslation(IVector3 translation) {
		transformation = transformation.multiply(VectorMatrixFactory
				.makeTranslationMatrix(translation));
		updateTransposedTransformation();
	}

	/**
	 * Include a scaling factor.
	 */
	public void addScale(double scale) {
		transformation = transformation.multiply(VectorMatrixFactory
				.makeHomogeniousScaleMatrix(scale));
		updateTransposedTransformation();
	}

	/**
	 * Include a transformation
	 */
	public void addTransformation(IMatrix3 transform) {
		IMatrix4 homogeniousMatrix = transform.makeHomogenious();
		transformation = transformation.multiply(homogeniousMatrix);
		updateTransposedTransformation();
	}

	public void multiplyTransformation(IMatrix4 transform) {
		transformation = transformation.multiply(transform);
		updateTransposedTransformation();
	}

	/**
	 * @return
	 */
	public IMatrix4 getTransformation() {
		return transformation;
	}

	/**
	 * Return the transposed transformation
	 */
	public IMatrix4 getTransposedTransformation() {
		return transposedTransformation;
	}

	/**
	 * Returns the transformed vector as a Vector 4
     */
	public IVector4 getTransformedVector4(IVector3 vector3) {
		return transformation.multiply(vector3.getHomogenious());
	}

	/**
	 * Returns the transformed vector as a Vector 3
	 */
	public IVector3 getTransformedVector3(IVector3 vector3) {
		return getTransformedVector4(vector3).toVector3();
	}
}

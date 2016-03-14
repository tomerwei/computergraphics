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
  private Matrix transformation = MatrixFactory.createIdentityMatrix4();

  /**
   * Transposed transformation.
   */
  private Matrix transposedTransformation = MatrixFactory.createIdentityMatrix4();

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
          transformation.set(i, j, 1);
        } else {
          transformation.set(i, j, 0);
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
  public void addTranslation(Vector translation) {
    transformation = transformation.multiply(MatrixFactory.createHomogeniousTranslationMatrix(translation));
    updateTransposedTransformation();
  }

  /**
   * Include a scaling factor.
   */
  public void addScale(double scale) {
    transformation = transformation.multiply(MatrixFactory.createHomogeniousScaleMatrix(scale));
    updateTransposedTransformation();
  }

  /**
   * Include a transformation
   */
  public void addTransformation(Matrix transform) {
    Matrix homogeniousMatrix = MatrixFactory.createHomogeniousFor3spaceMatrix(transform);
    transformation = transformation.multiply(homogeniousMatrix);
    updateTransposedTransformation();
  }

  public void multiplyTransformation(Matrix transform) {
    transformation = transformation.multiply(transform);
    updateTransposedTransformation();
  }

  /**
   * @return
   */
  public Matrix getTransformation() {
    return transformation;
  }

  /**
   * Return the transposed transformation
   */
  public Matrix getTransposedTransformation() {
    return transposedTransformation;
  }

  /**
   * Returns the transformed vector as a Vector 4
   */
  public Vector getTransformedVector(Vector other) {
    return transformation.multiply(VectorFactory.createHomogeniousFor3spaceVector(other));
  }

  public void setTransformation(Matrix transformation) {
    this.transformation.copy(transformation);
    updateTransposedTransformation();
  }
}

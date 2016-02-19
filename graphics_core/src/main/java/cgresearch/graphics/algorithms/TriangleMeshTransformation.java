/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.algorithms;

import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;

/**
 * Apply an affine transformation on a triangle mesh (more precisely its
 * vertices).
 * 
 * @author Philipp Jenke
 * 
 */
public class TriangleMeshTransformation {

	/**
	 * Transform all vertices of the mesh with a given transformation matrix.
	 * 
	 * @param mesh
	 *            Input mesh (will be modified).
	 * @param transformation
	 *            Affine transformation.
	 */
	public static void transform(ITriangleMesh mesh, Matrix transformation) {
		for (int vertexIndex = 0; vertexIndex < mesh.getNumberOfVertices(); vertexIndex++) {
			mesh.getVertex(vertexIndex)
					.getPosition()
					.copy(transformation.multiply(mesh.getVertex(vertexIndex)
							.getPosition()));
		}
	}

	/**
	 * Translate all vertices of the mesh.
	 * 
	 * @param mesh
	 *            Input mesh (will be modified).
	 * @param translation
	 *            Translation vector.
	 */
	public static void translate(ITriangleMesh mesh, Vector translation) {
		for (int vertexIndex = 0; vertexIndex < mesh.getNumberOfVertices(); vertexIndex++) {
			mesh.getVertex(vertexIndex)
					.getPosition()
					.copy(mesh.getVertex(vertexIndex).getPosition()
							.add(translation));
		}
	}

	/**
	 * Scale all vertices in the mesh.
	 * 
	 * @param mesh
	 *            Input mesh (will be modified).
	 * @param scale
	 *            Scaling factors in x-, y- and z-direction.
	 */
	public static void scale(ITriangleMesh mesh, Vector scale) {
		transform(mesh, VectorMatrixFactory.newMatrix(
				VectorMatrixFactory.newVector(scale.get(0), 0, 0),
				VectorMatrixFactory.newVector(0, scale.get(1), 0),
				VectorMatrixFactory.newVector(0, 0, scale.get(2))));
	}

	/**
	 * Scale all vertices in the mesh.
	 * 
	 * @param mesh
	 *            Input mesh (will be modified).
	 * @param scale
	 *            Scaling factors for all directions
	 */
	public static void scale(ITriangleMesh mesh, double scale) {
		scale(mesh, VectorMatrixFactory.newVector(scale, scale, scale));
	}

	/**
	 * Multiply all vertices with a matrix.
	 * 
	 * @param mesh
	 *            Input mesh (will be modified).
	 * @param matrix
	 *            Transformation matrix.
	 */
	public static void multiply(ITriangleMesh mesh, Matrix matrix) {
		transform(mesh, matrix);

	}
}

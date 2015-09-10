/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.points;

import java.util.Random;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;

/**
 * Creates a point cloud from a triangle mesh.
 * 
 * @author Philipp Jenke
 * 
 */
public class TriangleMeshSampler {

	/**
	 * Sample random points on all triangles of the mesh.
	 */
	public static IPointCloud sample(ITriangleMesh mesh, int numberOfSamples) {
		IPointCloud pointCloud = new PointCloud();
		int RANDOM_INT = 100000;
		for (int i = 0; i < numberOfSamples; i++) {
			int triangleIndex = new Random().nextInt(mesh
					.getNumberOfTriangles());
			Triangle t = mesh.getTriangle(triangleIndex);
			IVector3 va = mesh.getVertex(t.getA()).getPosition();
			IVector3 vb = mesh.getVertex(t.getB()).getPosition();
			IVector3 vc = mesh.getVertex(t.getC()).getPosition();
			float alpha = new Random().nextInt(RANDOM_INT) / (float) RANDOM_INT;
			float beta = 1;
			if (alpha > 0.99) {
				beta = 0;
			} else {
				while (alpha + beta > 1) {
					beta = new Random().nextInt(RANDOM_INT)
							/ (float) RANDOM_INT;
				}
			}
			float gamma = 1 - alpha - beta;
			IVector3 position = va.multiply(alpha).add(
					vb.multiply(beta).add(vc.multiply(gamma)));
			pointCloud.addPoint(new Point(position, VectorMatrixFactory
					.newIVector3(0.5, 0.5, 0.5), t.getNormal()));
		}
		return pointCloud;
	}

	/**
	 * Convert a triangle mesh to a point cloud. Keep the vertices only.
	 */
	public static IPointCloud convert(ITriangleMesh mesh) {
		IPointCloud pointCloud = new PointCloud();
		for (int vertexIndex = 0; vertexIndex < mesh.getNumberOfVertices(); vertexIndex++) {
			Vertex vertex = mesh.getVertex(vertexIndex);
			pointCloud.addPoint(new Point(vertex.getPosition(),
					VectorMatrixFactory.newIVector3(0, 0, 0), vertex
							.getNormal()));
		}
		return pointCloud;
	}
}

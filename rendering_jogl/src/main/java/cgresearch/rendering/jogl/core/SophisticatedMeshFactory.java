/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.primitives.Line3D;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshFactory;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Factory class for the mesh to highlight edges and vertices.
 * 
 * @author Philipp Jenke
 * 
 */
public class SophisticatedMeshFactory {

	/**
	 * Create a triangle mesh for the geometry of the sophisticated mesh.
	 */
	private static ITriangleMesh createSophisticatedMeshMesh(
			ITriangleMesh triangleMesh) {

		float scale = 1.0f;

		ITriangleMesh sophisticatedMesh = new TriangleMesh();
		// Vertices
		for (int i = 0; i < triangleMesh.getNumberOfVertices(); i++) {
			sophisticatedMesh
					.unite(TriangleMeshFactory.createSphere(triangleMesh
							.getVertex(i).getPosition(), 0.05f * scale, 10));
		}
		// Edges
		for (int i = 0; i < triangleMesh.getNumberOfTriangles(); i++) {
			Triangle triangle = triangleMesh.getTriangle(i);
			for (int j = 0; j < 3; j++) {
				int v0Index = triangle.get(j);
				int v1Index = triangle.get((j + 1) % 3);
				if (v0Index > v1Index) {
					// Line v0 -> v1
					Line3D line = new Line3D(triangleMesh.getVertex(v0Index)
							.getPosition(), triangleMesh.getVertex(v1Index)
							.getPosition());
					sophisticatedMesh.unite(TriangleMeshFactory.createLine3D(
							line, 10, 0.015));
				}
			}
		}

		return sophisticatedMesh;
	}

	/**
	 * Create a render node for the geometry of the sophisticated mesh.
	 */
	public static JoglRenderNode createSophisticatedMeshNode(ITriangleMesh mesh) {
		ITriangleMesh sophisticatedMesh = createSophisticatedMeshMesh(mesh);
		CgNode cgNode = new CgNode(sophisticatedMesh, "sophisticated mesh");
		RenderContentTriangleMesh renderContent = new RenderContentTriangleMesh(
				sophisticatedMesh);
		JoglRenderNode renderNode = new JoglRenderNode(cgNode, renderContent);
		sophisticatedMesh.getMaterial().setReflectionDiffuse(
				VectorMatrixFactory.newIVector3(0.6, 0.6, 0.9));
		sophisticatedMesh.getMaterial().setRenderMode(
				Material.Normals.PER_FACET);
		sophisticatedMesh.getMaterial().setShowSophisticatesMesh(false);
		return renderNode;
	}
}

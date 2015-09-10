package cgresearch.rendering.jogl.core;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Factory class for disc objects to render points
 * 
 * @author Philipp Jenke
 *
 */
public class PointsAsDiscsMeshFactory {
	/**
	 * Create a triangle mesh for the discs.
	 */
	private static ITriangleMesh createPointsAsDiscMesh(IPointCloud pointCloud) {

		float scale = 0.0015f;
		int resolution = 10;
		double deltaAngle = Math.PI * 2.0 / (resolution);

		ITriangleMesh mesh = new TriangleMesh();

		for (int pointIndex = 0; pointIndex < pointCloud.getNumberOfPoints(); pointIndex++) {
			Point p = pointCloud.getPoint(pointIndex);
			IMatrix3 T = VectorMatrixFactory.createCoordinateFrameX(p
					.getNormal());
			// Create vertices
			int centerIndex = mesh.addVertex(new Vertex(p.getPosition()));
			for (int i = 0; i < resolution; i++) {
				IMatrix3 R = VectorMatrixFactory.getRotationMatrix(
						p.getNormal(), deltaAngle * i);
				IVector3 v = p.getPosition()
						.add(R.multiply(T.getTransposed().getRow(1)).multiply(
								scale));
				// System.out.println(v);
				mesh.addVertex(new Vertex(v, p.getNormal()));
			}
			// Create triangles
			for (int i = 0; i < resolution; i++) {
				mesh.addTriangle(new Triangle(centerIndex, centerIndex + i + 1,
						centerIndex + (i + 1) % resolution + 1));
			}
		}

		mesh.computeTriangleNormals();
		mesh.computeVertexNormals();

		Logger.getInstance().message(
				"Created points-as-discs mesh with "
						+ mesh.getNumberOfVertices() + " vertices and "
						+ mesh.getNumberOfTriangles() + " triangles.");

		return mesh;
	}

	/**
	 * Create a render node for the geometry of the sophisticated mesh.
	 */
	public static JoglRenderNode createPointsAsDiscsNode(
			IPointCloud pointCloud, IVector3 color) {
		ITriangleMesh mesh = createPointsAsDiscMesh(pointCloud);
		CgNode cgNode = new CgNode(mesh, "points-as-discs mesh");
		RenderContentTriangleMesh renderContent = new RenderContentTriangleMesh(
				mesh);
		JoglRenderNode renderNode = new JoglRenderNode(cgNode, renderContent);
		mesh.getMaterial().setReflectionDiffuse(color);
		mesh.getMaterial().setRenderMode(Material.Normals.PER_FACET);
		mesh.getMaterial().setShowSophisticatesMesh(false);
		renderNode.getCgNode().setVisible(true);
		mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
		return renderNode;
	}
}
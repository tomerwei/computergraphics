package smarthomevis.groundplan;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;

public class MeshTests extends CgApplication
{

	public static void main(String[] args)
	{
		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		CgApplication app = new MeshTests();
		JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
		appLauncher.create(app);
		appLauncher.setRenderSystem(RenderSystem.JOGL);
		appLauncher.setUiSystem(UI.JOGL_SWING);
	}

	public MeshTests()
	{
		createComplexPolygon();
		createSimplePolygon();
	}

	private void createSimplePolygon()
	{
		ITriangleMesh mesh = createSimpleObject();
		mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
		mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR1));
		getCgRootNode().addChild(new CgNode(mesh, "simpleMesh"));
	}

	private ITriangleMesh createSimpleObject()
	{
		ITriangleMesh mesh = new TriangleMesh();

		int a = mesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(1.5, 0, 0)));
		int b = mesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(1.5, 0, 1)));
		int c = mesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(2.5, 0, 0.5)));

		mesh.addTriangle(new Triangle(a, b, c));

		mesh.computeTriangleNormals();
		mesh.computeVertexNormals();
		return mesh;
	}

	private void createComplexPolygon()
	{
		// ITriangleMesh mesh = TriangleMeshFactory.createCube();
		ITriangleMesh mesh = createComplexObject();
		mesh.getMaterial().setShaderId(Material.SHADER_GOURAUD_SHADING);
		mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR0));
		getCgRootNode().addChild(new CgNode(mesh, "testmesh"));

	}

	private ITriangleMesh createComplexObject()
	{
		ITriangleMesh triangleMesh = new TriangleMesh();
		int a = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(-0.5, 0, -0.5)));
		int b = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(0.5, 0, -0.5)));
		int c = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(-0.5, 0, 0.5)));
		int d = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(0.5, 0, 0.5)));

		int e = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(-0.5, -1, 0.5)));
		int f = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(0.5, -1, 0.5)));

		int g = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(-0.5, 1, 0.5)));
		int h = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(0.5, 1, 0.5)));

		int i = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(-0.5, 0, 1.5)));
		int j = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(0.5, 0, 1.5)));

		// punkte fuer die spitzen
		int l = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(1, 0, 0.5)));
		int m = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(-1, 0, 0.5)));

		triangleMesh.addTriangle(new Triangle(a, b, c));
		triangleMesh.addTriangle(new Triangle(b, c, d));

		triangleMesh.addTriangle(new Triangle(c, d, e));
		triangleMesh.addTriangle(new Triangle(d, e, f));

		triangleMesh.addTriangle(new Triangle(c, d, g));
		triangleMesh.addTriangle(new Triangle(d, g, h));

		triangleMesh.addTriangle(new Triangle(c, d, i));
		triangleMesh.addTriangle(new Triangle(d, i, j));

		// erste Spitze ansetzen
		triangleMesh.addTriangle(new Triangle(b, d, l));
		triangleMesh.addTriangle(new Triangle(d, f, l));
		triangleMesh.addTriangle(new Triangle(d, j, l));
		triangleMesh.addTriangle(new Triangle(d, h, l));

		// zweite Spitze ansetzen
		triangleMesh.addTriangle(new Triangle(a, c, m));
		triangleMesh.addTriangle(new Triangle(e, c, m));
		triangleMesh.addTriangle(new Triangle(i, c, m));
		triangleMesh.addTriangle(new Triangle(g, c, m));

		// Stirnflaechen eine Seite
		triangleMesh.addTriangle(new Triangle(l, b, f));
		triangleMesh.addTriangle(new Triangle(l, f, j));
		triangleMesh.addTriangle(new Triangle(l, j, h));
		triangleMesh.addTriangle(new Triangle(l, h, b));

		// Stirnflaechen andere Seite
		triangleMesh.addTriangle(new Triangle(m, e, i));
		triangleMesh.addTriangle(new Triangle(m, i, g));
		triangleMesh.addTriangle(new Triangle(m, g, a));
		triangleMesh.addTriangle(new Triangle(m, a, e));

		triangleMesh.computeTriangleNormals();
		triangleMesh.computeVertexNormals();
		return triangleMesh;
	}

}

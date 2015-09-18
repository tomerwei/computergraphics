/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.studentprojects.urbanreconstruction;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;

/**
 * Central frame for the urban reconstruction.
 * 
 * @author Philipp Jenke
 * 
 */
public class UrbanReconstruction extends CgApplication {

	/**
	 * Constructor
	 */
	//
	// private static IPointCloud source;
	// private static List<Point> next;
	// private static List<Point> remove;
	// private static IPointCloud result;
	// private static List<IPointCloud> planes = new ArrayList<IPointCloud>();

	public UrbanReconstruction() {

		// Add a point cloud
		// IPointCloud pointCloud = PointCloudFactory.createDummyPointCloud();
		// pointCloud.getMaterial().setLighting(false);
		// CgNode node = new CgNode(pointCloud, "Point cloud");
		// getCgRootNode().addChild(node);
		//
		// // Add an OBJ mesh
		// ObjFileReader reader = new ObjFileReader();
		// List<ITriangleMesh> meshes = reader.readObjFile("meshes/bunny.obj");
		// for (ITriangleMesh mesh : meshes) {
		// mesh.fitToUnitBox();
		// getCgRootNode().addChild(new CgNode(mesh, "OBJ Mesh"));
		// }

		// My Code
		// int i = 0;
		// for (IPointCloud pc : planes) {
		// i++;
		// pc.getMaterial().setLighting(false);
		// CgNode node = new CgNode(pc, "Point cloud " + i);
		// // CgNode node = new CgNode(source, "Point cloud ");
		// getCgRootNode().addChild(node);
		// }
	}

	/**
	 * Program entry point.
	 */
	public static void main(String[] args) {
		// PlyConverter pc = new PlyConverter();
		// Ransac ran = new Ransac();
		// source = pc.getPointCloud();
		//
		// next = copyListOfPoints(source);
		//
		// int numberPlanes = 0;
		// while (next.size() > 800) {
		//
		// ran.runRansac(next);
		//
		// result = ran.getResult(next);
		// if (result.getNumberOfPoints() > 500) {
		// planes.add(result);
		// numberPlanes++;
		// }
		// remove = copyListOfPoints(result);
		// next.removeAll(remove);
		//
		// }
		//
		// System.out.println("Number of Planes = " + numberPlanes);

		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		UrbanReconstruction app = new UrbanReconstruction();
		JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
   

		// IVector3 u = VectorMatrixFactory.newIVector3(1, -2, 6);
		// IVector3 v = VectorMatrixFactory.newIVector3(-4, 3, 3);
		// IVector3 p = VectorMatrixFactory.newIVector3(2, 5, -1);
		// IVector3 n = u.cross(v);
		// edu.haw.cg.math.IMatrix3 R = VectorMatrixFactory
		// .createCoordinateFrameZ(n);
		//
		// List<IVector3> pointsWorldCoords = new ArrayList<IVector3>();
		// for (int i = 0; i < 5; i++) {
		// double alpha = new java.util.Random().nextDouble();
		// double beta = new java.util.Random().nextDouble();
		// pointsWorldCoords.add(p.add(u.multiply(alpha))
		// .add(v.multiply(beta)));
		// }
		//
		// edu.haw.cg.math.IMatrix3 R_T = R.getTransposed();
		//
		// List<IVector3> pointsInPlaneCoords = new ArrayList<IVector3>();
		// for (int i = 0; i < pointsWorldCoords.size(); i++) {
		// IVector3 sub = pointsWorldCoords.get(i).subtract(p);
		// sub = R_T.multiply(sub);
		// pointsInPlaneCoords.add(sub);
		//
		// // pointsInPlaneCoords.add(R_T.multiply(pointsWorldCoords.get(i)
		// // .subtract(p)));
		// }
		//
		// System.out.println("--- World coords ---");
		// for (IVector3 x : pointsWorldCoords) {
		// System.out.println(x);
		// }
		//
		// System.out.println("--- Plane coords ---");
		// for (IVector3 x : pointsInPlaneCoords) {
		// System.out.println(x);
		// }
		//
		// pointsWorldCoords.clear();
		// for (int i = 0; i < pointsInPlaneCoords.size(); i++) {
		//
		// IVector3 res = R.multiply(pointsInPlaneCoords.get(i));
		// res = res.add(p);
		//
		// pointsWorldCoords.add(res);
		// }
		//
		// System.out.println("--- World coords ---");
		// for (IVector3 x : pointsWorldCoords) {
		// System.out.println(x);
		// }

	}

	// private static List<Point> copyListOfPoints(IPointCloud pc) {
	// List<Point> res = new ArrayList<Point>();
	//
	// for (int i = 0; i < pc.getNumberOfPoints(); i++) {
	// res.add(pc.getPoint(i));
	// }
	//
	// return res;
	// }
}

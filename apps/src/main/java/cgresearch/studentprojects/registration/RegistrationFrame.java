package cgresearch.studentprojects.registration;

import java.util.List;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.apps.trianglemeshes.ObjTriangleMesh;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.ConsoleLogger;
import cgresearch.core.logging.Logger.VerboseMode;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.Matrix4;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.datastructures.points.PointCloudFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.jogl.ui.JoglFrame;
import cgresearch.rendering.jogl.ui.JoglSwingUserInterface;
import cgresearch.studentprojects.brickbuilder.math.IVectorInt3;
import cgresearch.studentprojects.shapegrammar.gui.menu.BuilderMenu;

/**
 * Initial frame for the registration project (Jäckel)
 *
 */
public class RegistrationFrame extends CgApplication {
	
	public static IPointCloud pointCloud = new PointCloud();
	public static IPointCloud pointCloud2 = new PointCloud();
	/**
	 * Constructor
	 */
	public RegistrationFrame() {
		// Testing code: load a triangle mesh and display it
//		ObjFileReader reader = new ObjFileReader();
//		List<ITriangleMesh> meshes = reader.readFile("meshes/bunny.obj");
//		getCgRootNode().addChild(new CgNode(meshes.get(0), "mesh"));
		loadIPointCloud();
		
	}
	
	public void loadIPointCloud(){
		IVector3 position1 = VectorMatrixFactory.newIVector3(1,1,1);
		IVector3 position2 = VectorMatrixFactory.newIVector3(0,0,0);
		IVector3 position3 = VectorMatrixFactory.newIVector3(2,2,2);
		
		IVector3 position4 = VectorMatrixFactory.newIVector3(0,3,0);
		IVector3 position5 = VectorMatrixFactory.newIVector3(1,4,1);
		IVector3 position6 = VectorMatrixFactory.newIVector3(2,5,2);
		IVector3 color = VectorMatrixFactory.newIVector3(Math.random(),
				Math.random(), Math.random());
		IVector3 normal = VectorMatrixFactory.newIVector3(Math.random(),
				Math.random(), Math.random());
		
//		IPointCloud pointCloud = new PointCloud();
		pointCloud.addPoint(new Point(position1, color, normal));
		pointCloud.addPoint(new Point(position2, color, normal));
		pointCloud.addPoint(new Point(position3, color, normal));
		
		
//		IPointCloud pointCloud2 = new PointCloud();
		pointCloud2.addPoint(new Point(position4, color, normal));
		pointCloud2.addPoint(new Point(position5, color, normal));
		pointCloud2.addPoint(new Point(position6, color, normal));

		
//		IcpDistanceFunction icp =  new IcpDistanceFunction(pointCloud);
//		icp.startAlgorithm(pointCloud2);
//		ObjFileReader reader = new ObjFileReader();
//		List<ITriangleMesh> meshes = reader.readFile("meshes/square.obj");
//		IPointCloud pointCloud3 = PointCloudFactory.createDummyPointCloud();
////		ITriangleMesh mesh = meshes.get(0);
////		IPointCloud pointCloud3 = MeshSampler.sample(mesh, 5000);
//		
		CgNode pointCloudNode = new CgNode(pointCloud, "pointCloud1");
		getCgRootNode().addChild(pointCloudNode);
		CgNode pointCloudNode2 = new CgNode(pointCloud2, "pointCloud2");
		getCgRootNode().addChild(pointCloudNode2);
		


		
	}

	public static void main(String[] args) {
//		ResourcesLocator.getInstance().parseIniFile("resources.ini");
//		RegistrationFrame app = new RegistrationFrame();
//		JoglFrame frame = new JoglFrame(app);
//		new JoglSwingUserInterface(app, frame);
		new ConsoleLogger(VerboseMode.NORMAL);
		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		RegistrationFrame app = new RegistrationFrame();
		JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
//		RegistrationButton button = new RegistrationButton();
		appLauncher.create(app);
		appLauncher.setRenderSystem(RenderSystem.JOGL);
		appLauncher.setUiSystem(UI.JOGL_SWING);
//		appLauncher.addCustomMenu(new RegistrationMenu());
//	    appLauncher.addCustomUi(button);

		
		
		
//		CgNode pointCloudNode = new CgNode(pointCloud, "point cloud");
//		getCgRootNode().addChild(pointCloudNode);
		
		
	}

}

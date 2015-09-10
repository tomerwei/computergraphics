package cgresearch.studentprojects.brickbuilder;

import javax.imageio.spi.IIORegistry;
import javax.swing.SwingUtilities;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshTransformation;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.jogl.ui.JoglFrame;
import cgresearch.rendering.jogl.ui.JoglSwingUserInterface;

public class BrickBuilderTest extends CgApplication {

	public static void main(String[] args) {
		IIORegistry registry = IIORegistry.getDefaultInstance();
		registry.registerServiceProvider(new cgresearch.studentprojects.brickbuilder.externalimageiotga.TGAImageReaderSpi());

		ResourcesLocator
				.getInstance()
				.addPath(
						"C:\\Users\\Teetasse\\Desktop\\Bachelorarbeit\\GIT-Projekte\\cg\\resources\\");
		ResourcesLocator
				.getInstance()
				.addPath(
						"C:\\Users\\Teetasse\\Desktop\\Bachelorarbeit\\GIT-Projekte\\cg_brickbuilder\\resources\\");
		ResourcesLocator.getInstance().addPath("C:\\models3\\");

		final BrickBuilderTest bbt = new BrickBuilderTest();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new JoglSwingUserInterface(bbt, new JoglFrame(bbt));
			}
		});
	}

	public BrickBuilderTest() {
		// modelScenario();
		// modelLoadScenario();

		// miniScenario();

		ITriangleMesh yoshi = new ObjFileReader().readFile(
				"meshes/yoshiPirate.obj").get(0);
		TriangleMeshTransformation.scale(yoshi, 0.01);
		CgNode child = new CgNode(yoshi, "Yoshi");
		// child.getContent().getMaterial().setLighting(false);
		rootNode.addChild(child);

	}

//	private void modelLoadScenario() {
//		IBrickCloud cloud = BrickCloudReader
//				.readFromFile("3___bunny003-50_0.brick");
//		ITriangleMesh mesh2 = BrickCloudMesher
//				.transformCloud2MeshDebugWeakBricks(cloud);
//		rootNode.addChild(new CgNode(mesh2, "bunny 02"));
//
//		// for (int i = 0; i < 8; i++) {
//		// ITriangleMesh mesh3 = BrickCloudMesher.transformCloud2Mesh(cloud, 0,
//		// i + 1);
//		// mesh3.getMaterial().setLighting(false);
//		// TriangleMeshTransformation.translate(mesh3,
//		// VectorMatrixFactory.newIVector3(1.0 + (1.0 * i), 0, 0));
//		// rootNode.addChild(new CgNode(mesh3, "bunny 03"));
//		// }
//	}

//	private void modelScenario() {
//		IVoxelCloud cloud = VoxelCloudReader.readFromFile("___cube-50_0.vox"); // voxelclouds/legoMan
//		// ITriangleMesh rootMesh = TriangleMeshFactory.createCube();
//
//		RootBrick root = new RootBrick(VectorMatrixFactory.newIVector3(0.08,
//				0.096, 0.08), null);
//		ComposedBrick brick2x1 = new ComposedBrick(root,
//				new VectorInt3(2, 1, 1));
//		ComposedBrick brick2x2 = new ComposedBrick(root,
//				new VectorInt3(2, 1, 2));
//		ComposedBrick brick3x1 = new ComposedBrick(root,
//				new VectorInt3(3, 1, 1));
//		ComposedBrick brick3x2 = new ComposedBrick(root,
//				new VectorInt3(3, 1, 2));
//		ComposedBrick brick4x1 = new ComposedBrick(root,
//				new VectorInt3(4, 1, 1));
//		ComposedBrick brick4x2 = new ComposedBrick(root,
//				new VectorInt3(4, 1, 2));
//		ComposedBrick brick6x1 = new ComposedBrick(root,
//				new VectorInt3(6, 1, 1));
//		ComposedBrick brick6x2 = new ComposedBrick(root,
//				new VectorInt3(6, 1, 2));
//		ComposedBrick brick8x1 = new ComposedBrick(root,
//				new VectorInt3(8, 1, 1));
//		ComposedBrick brick8x2 = new ComposedBrick(root,
//				new VectorInt3(8, 1, 2));
//
//		BrickSet set = new BrickSet(root);
//		set.addChildBrick(brick2x1, new IBrick[] { root });
//		set.addChildBrick(brick2x2, new IBrick[] { root, brick2x1 });
//		set.addChildBrick(brick3x1, new IBrick[] { root, brick2x1 });
//		set.addChildBrick(brick3x2, new IBrick[] { brick2x2, brick3x1 });
//		set.addChildBrick(brick4x1, new IBrick[] { brick2x1, brick3x1 });
//		set.addChildBrick(brick4x2,
//				new IBrick[] { brick4x1, brick3x2, brick2x2 });
//		set.addChildBrick(brick6x1, new IBrick[] { brick3x1, brick4x1 });
//		set.addChildBrick(brick6x2,
//				new IBrick[] { brick6x1, brick4x2, brick3x2 });
//		set.addChildBrick(brick8x1, new IBrick[] { brick4x1, brick6x1 });
//		set.addChildBrick(brick8x2,
//				new IBrick[] { brick8x1, brick6x2, brick4x2 });
//
//		// set.addChildBrick(brick2x1);
//		// set.addChildBrick(brick2x2);
//		// set.addChildBrick(brick3x1);
//		// set.addChildBrick(brick3x2);
//		// set.addChildBrick(brick4x1);
//		// set.addChildBrick(brick4x2);
//		// set.addChildBrick(brick6x1);
//		// set.addChildBrick(brick6x2);
//		// set.addChildBrick(brick8x1);
//		// set.addChildBrick(brick8x2);
//
//		// lego man
//		// set.addBrickColor(new ColorRGB(255, 0, 0));
//		// set.addBrickColor(new ColorRGB(255, 255, 0));
//		// set.addBrickColor(new ColorRGB(0, 0, 255));
//		// set.addBrickColor(new ColorRGB(0, 0, 0));
//
//		// all colors
//		List<IColorRGB> colors = new ArrayList<IColorRGB>();
//		for (int z = 0; z < cloud.getResolutions().getZ(); z++) {
//			for (int y = 0; y < cloud.getResolutions().getY(); y++) {
//				for (int x = 0; x < cloud.getResolutions().getX(); x++) {
//					VectorInt3 p = new VectorInt3(x, y, z);
//					if (cloud.getVoxelAt(p) == VoxelType.SURFACE) {
//						IColorRGB c = cloud.getVoxelColor(p);
//						if (c != null && !colors.contains(c)) {
//							colors.add(c);
//						}
//					}
//				}
//			}
//		}
//
//		for (IColorRGB c : colors)
//			set.addBrickColor(c);
//
//		// BrickBuilderAlgorithm algo = new BrickBuilderAlgorithm();
//		// long s = System.currentTimeMillis();
//		// System.out.println("start");
//		// IBrickCloud cloud2 = algo.transformVoxel2Bricks(cloud, set, true,
//		// true);
//
//		// System.out.println(cloud2);
//		// System.out.println(System.currentTimeMillis()-s+"ms");
//		// System.out.println("weakpoints: "+cloud2.getNumberOfWeakPoints());
//
//		// CgNode child = (new ObjFileReader()).readFile("meshes/legoMan.obj");
//		// child.getContent().getMaterial().setLighting(false);
//		// rootNode.addChild(child);
//
//		ITriangleMesh mesh1 = VoxelCloudMesher.transformCloud2Mesh(cloud, true);
//		// TriangleMeshTransformation.scale(mesh1, 1.3);
//		// TriangleMeshTransformation.translate(mesh1,
//		// VectorMatrixFactory.newIVector3(5, 0.1, 0));
//		rootNode.addChild(new CgNode(mesh1, "bunny 01"));
//
//		// ITriangleMesh mesh2 = BrickCloudMesher.transformCloud2Mesh(cloud2);
//		// mesh2.getMaterial().setLighting(false);
//		// // TriangleMeshTransformation.scale(mesh2, 0.3);
//		// // TriangleMeshTransformation.translate(mesh2,
//		// VectorMatrixFactory.newIVector3(-2.2, -0.35, -0.42));
//		// rootNode.addChild(new CgNode(mesh2, "bunny 02"));
//		//
//		// for (int i = 10; i < 13; i++) {
//		// ITriangleMesh mesh3 = BrickCloudMesher.transformCloud2Mesh(cloud2, 0,
//		// i + 1);
//		// mesh3.getMaterial().setLighting(false);
//		// TriangleMeshTransformation.translate(mesh3,
//		// VectorMatrixFactory.newIVector3(0, 0, (1.5 * (i - 9))));
//		// rootNode.addChild(new CgNode(mesh3, "bunny 03"));
//		// }
//
//		// ITriangleMesh mesh4 =
//		// BrickCloudMesher.transformCloud2MeshDebug(cloud2);
//		// mesh4.getMaterial().setLighting(false);
//		// TriangleMeshTransformation.translate(mesh4,
//		// VectorMatrixFactory.newIVector3(0, 0, -1.5));
//		// rootNode.addChild(new CgNode(mesh4, "bunny 03"));
//
//		// System.out.println("write");
//		// s = System.currentTimeMillis();
//		// BrickCloudWriter.writeToFile("C:\\models\\", "test01.brick", cloud2);
//		// System.out.println("done "+(System.currentTimeMillis()-s)+"ms");
//	}
}

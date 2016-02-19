package cgresearch.studentprojects.brickbuilder;

/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */

import java.util.List;

import javax.imageio.spi.IIORegistry;
import javax.swing.SwingUtilities;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.tree.OctreeFactory;
import cgresearch.graphics.datastructures.tree.OctreeFactoryStrategyTriangleMesh;
import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.fileio.PlyFileReader;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.studentprojects.brickbuilder.voxelcloud.IVoxelCloud;
import cgresearch.studentprojects.brickbuilder.voxelcloud.VoxelCloudMesher;
import cgresearch.studentprojects.brickbuilder.voxelcloud.VoxelCloudReader;

public class TestApp extends CgApplication {

	public TestApp() {
		// rootNode.addChild(new CoordinateSystem());

		// octreeTest();
		// bunnyTest();
		// voxelCloudTest();
		voxelizationTest();
		// lauranaTest();
		// houseTest();
	}

	void octreeTest() {
		ObjFileReader reader = new ObjFileReader();
		ITriangleMesh mesh = (ITriangleMesh) reader
				.readFile("meshes/bunny.obj").get(0);
		OctreeNode<Integer> root = new OctreeFactory<Integer>(
				new OctreeFactoryStrategyTriangleMesh(mesh)).create(7, 20);
		CgNode pointCloudNode = new CgNode(mesh, "bunny");
		getCgRootNode().addChild(pointCloudNode);
		CgNode octreeNode = new CgNode(root, "octree");
		getCgRootNode().addChild(octreeNode);
	}

	void voxelizationTest() {
		// Vector voxelScale = VectorMatrixFactory.newVector(80, 96, 80);
		// IBrick brick = new Brick(VectorMatrixFactory.newVector(0.08, 0.096,
		// 0.08)); // Lego 1x1 Brick (not Plate)
		// ITriangleMesh mesh = TriangleMeshFactory.createSphere(100);

		System.out.println("Reading bunny...");

		// ObjFileReader r = new ObjFileReader();
		// PlyFileReader rr = new PlyFileReader();
		// ITriangleMesh mesh = r.readFile("meshes/bunny002.obj").get(0); //
		// bunny002 yoshipirate hulk/Hulk legoMan
		// ITriangleMesh mesh = rr.readFile("meshes/cat.ply");
		// mesh.fitToUnitBox();

		// IVoxelCloud cloud = null;
		// ITriangleMesh mesh2 = null;
		// IVoxelizationAlgorithm algo = new VoxelizationParityCount();
		// int runs = 1;
		// long[] a = new long[runs];
		// long sum = 0;
		// for (int i = 0; i < runs; i++) {
		// long s = System.currentTimeMillis();
		// cloud = algo.transformMesh2Cloud(mesh, 50, brick);
		// a[i] = System.currentTimeMillis()-s;
		// sum += a[i];
		// System.out.println((i+1)+": "+a[i]+"ms");
		// }
		// System.out.println(Arrays.toString(a));
		// System.out.println("avg: "+(sum/runs)+"ms");
		// mesh2 = new VoxelCloudMesher().transformCloud2Mesh(cloud);

		// TriangleMeshTransformation.translate(mesh,
		// VectorMatrixFactory.newVector(0.5, 0.5, 0.5));
		// rootNode.addChild(new CgNode(mesh, "bunny"));
		//
		// System.out.println("Transform to voxel cloud...");
		// long s = System.currentTimeMillis();
		// IVoxelCloud cloud = new
		// VoxelizationParityCount().transformMesh2Cloud(mesh, 50, voxelScale);
		// System.out.println((System.currentTimeMillis()-s)+"ms");

		// VoxelCloudWriter.writeToFile("C:\\Users\\Teetasse\\Desktop\\models\\b200.vox",
		// cloud);
		// IVoxelCloud cloud =
		// VoxelCloudReader.readFromFile("voxelclouds/bunny50.vox");
		// IVoxelCloud cloud2 =
		// VoxelCloudReader.readFromFile("voxelclouds/bunny50c.vox");

		// System.out.println("Voxel cloud transformations...");
		// cloud = VoxelCloudTransformation.markSurfaceVoxels(cloud, 2);
		// VoxelCloudTransformation.addSurfaceColor(cloud, mesh);

		// VoxelCloudWriter.writeToFile("C:\\Users\\Teetasse\\Desktop\\models\\b200c.vox",
		// cloud);
		IVoxelCloud cloud = VoxelCloudReader
				.readFromFile("voxelclouds/bunny002-50.vox");

		System.out.println("Create meshes out of voxel cloud...");
		ITriangleMesh mesh1 = VoxelCloudMesher.transformCloud2Mesh(cloud, true);
		TriangleMeshTransformation.scale(mesh1, 1.3);
		TriangleMeshTransformation.translate(mesh1,
				VectorMatrixFactory.newVector(-1.2, 0.1, -0.1));
		rootNode.addChild(new CgNode(mesh1, "bunny 01"));

		CgNode child = list2node(
				new ObjFileReader().readFile("meshes/bunny002.obj"), "bunny");
		// child.getContent().getMaterial().setLighting(false);
		rootNode.addChild(child);

		// ITriangleMesh mesh2 = VoxelCloudMesher.transformCloud2Mesh(cloud2,
		// true);
		// mesh2.getMaterial().setLighting(false);
		// TriangleMeshTransformation.translate(mesh2,
		// VectorMatrixFactory.newVector(-1.0, 0, 0));
		// rootNode.addChild(new CgNode(mesh2, "bunny 02"));

		System.out.println("done");
	}

	private CgNode list2node(List<ITriangleMesh> meshes, String name) {
		CgNode node = new CgNode(null, name);
		for (ITriangleMesh mesh : meshes) {
			node.addChild(new CgNode(mesh, "mesh"));
		}
		return node;
	}

	// private ITriangleMesh createCube(Vector pos, double scale) {
	// ITriangleMesh cube = TriangleMeshFactory.createCube();
	// TriangleMeshTransformation.scale(cube, scale);
	// TriangleMeshTransformation.translate(cube, pos);
	// cube.getMaterial().setRenderMode(RenderMode.WIREFRAME);
	// return cube;
	// }

	void voxelCloudTest() {
		// IBrick brick = new Brick(VectorMatrixFactory.newVector(0.2, 0.2,
		// 0.2));
		// IVoxelCloud cloud = new VoxelCloud(brick,
		// VectorMatrixFactory.newVector(0.2, 0, 0.5), new VectorInt3(20, 20,
		// 20));
		// ITriangleMesh mesh = VoxelCloudMesher.transformCloud2Mesh(cloud);
		// mesh.getMaterial().setColor(VectorMatrixFactory.newVector(Math.random(),
		// Math.random(), Math.random()));
		// rootNode.addChild(new CgNode(mesh, "cloud"));
	}

	void houseTest() {
		ObjFileReader r = new ObjFileReader();
		rootNode.addChild(list2node(
				r.readFile("meshes/cornerhouse/cornerhouse.obj"), "cornerhouse"));
	}

	void bunnyTest() {
		ObjFileReader r = new ObjFileReader();
		ITriangleMesh mesh = (ITriangleMesh) r.readFile("meshes/bunny.obj")
				.get(0);
		TriangleMeshTransformation.scale(mesh, 4.0);
		TriangleMeshTransformation.translate(mesh,
				VectorMatrixFactory.newVector(0.4, -0.1, 0.2));
		rootNode.addChild(new CgNode(mesh, "bunny"));
	}

	void lauranaTest() {
		PlyFileReader r = new PlyFileReader();
		ITriangleMesh mesh = r.readFile("meshes/Armadillo.ply");
		// TriangleMeshTransformation.scale(mesh, 4.0);
		// TriangleMeshTransformation.translate(mesh,
		// VectorMatrixFactory.newVector(0.4, -0.1, 0.2));
		rootNode.addChild(new CgNode(mesh, "laurana"));
	}

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

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new TestApp();
			}
		});
	}

}

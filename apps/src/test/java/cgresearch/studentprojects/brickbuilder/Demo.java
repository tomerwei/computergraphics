package cgresearch.studentprojects.brickbuilder;

import java.util.List;

import javax.imageio.spi.IIORegistry;
import javax.swing.SwingUtilities;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.studentprojects.brickbuilder.brickcloud.BrickCloudMesher;
import cgresearch.studentprojects.brickbuilder.brickcloud.BrickCloudReader;
import cgresearch.studentprojects.brickbuilder.brickcloud.IBrickCloud;
import cgresearch.studentprojects.brickbuilder.voxelcloud.IVoxelCloud;
import cgresearch.studentprojects.brickbuilder.voxelcloud.VoxelCloudMesher;
import cgresearch.studentprojects.brickbuilder.voxelcloud.VoxelCloudReader;

public class Demo extends CgApplication {

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
				new Demo();
			}
		});
	}

	public Demo() {
		ObjFileReader read = new ObjFileReader();
		CgNode bunnies = new CgNode(null, "bunnies");
		CgNode legoMen = new CgNode(null, "lego men");

		List<ITriangleMesh> meshes = read.readFile("meshes/bunny003.obj");
		CgNode bunny = new CgNode(null, "bunny");
		for (ITriangleMesh mesh : meshes) {
			bunny.addChild(new CgNode(mesh, "mesh"));
		}

		bunnies.addChild(bunny);

		IVoxelCloud bunnyVoxelcloud = VoxelCloudReader
				.readFromFile("voxelclouds/bunny003-50.vox");
		ITriangleMesh bunnyVoxelMesh = VoxelCloudMesher.transformCloud2Mesh(
				bunnyVoxelcloud, true);
		TriangleMeshTransformation.scale(bunnyVoxelMesh, 1.3);
		TriangleMeshTransformation.translate(bunnyVoxelMesh,
				VectorMatrixFactory.newVector(1.5, 0.2, 0));
		bunnies.addChild(new CgNode(bunnyVoxelMesh, "bunny voxel"));

		IBrickCloud bunnyBrickcloud = BrickCloudReader
				.readFromFile("brickclouds/bunny003-50-colored.brick");
		ITriangleMesh bunnyBrickMesh = BrickCloudMesher
				.transformCloud2Mesh(bunnyBrickcloud);
		TriangleMeshTransformation.scale(bunnyBrickMesh, 0.3);
		TriangleMeshTransformation.translate(bunnyBrickMesh,
				VectorMatrixFactory.newVector(2.3, -0.35, -0.35));
		bunnies.addChild(new CgNode(bunnyBrickMesh, "bunny brick"));

		// CgNode lg = read.readFile("meshes/legoMan.obj");
		// legoMen.addChild(lg);

		IVoxelCloud lgc = VoxelCloudReader
				.readFromFile("voxelclouds/legoMan-50.vox");
		ITriangleMesh lgcm = VoxelCloudMesher.transformCloud2Mesh(lgc, true);
		TriangleMeshTransformation.scale(lgcm, 1.3);
		TriangleMeshTransformation.translate(lgcm,
				VectorMatrixFactory.newVector(1.5, 0.2, -3));
		legoMen.addChild(new CgNode(lgcm, "legoman voxel"));

		IBrickCloud lgb = BrickCloudReader
				.readFromFile("brickclouds/legoMan-50-colored.brick");
		ITriangleMesh lgbm = BrickCloudMesher.transformCloud2Mesh(lgb);
		TriangleMeshTransformation.scale(lgbm, 0.3);
		TriangleMeshTransformation.translate(lgbm,
				VectorMatrixFactory.newVector(2.3, -0.35, -3.35));
		legoMen.addChild(new CgNode(lgbm, "legoman brick"));

		ITriangleMesh lgbm2 = BrickCloudMesher.transformCloud2Mesh(lgb);
		TriangleMeshTransformation.scale(lgbm2, 0.3);
		TriangleMeshTransformation.translate(lgbm2,
				VectorMatrixFactory.newVector(3.6, -0.35, -3.35));
		legoMen.addChild(new CgNode(lgbm2, "legoman brick debug"));

		rootNode.addChild(bunnies);
		rootNode.addChild(legoMen);
	}
}

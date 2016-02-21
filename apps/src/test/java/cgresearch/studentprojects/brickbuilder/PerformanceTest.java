package cgresearch.studentprojects.brickbuilder;

//import java.util.Arrays;

import javax.imageio.spi.IIORegistry;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.studentprojects.brickbuilder.voxelcloud.IVoxelCloud;
import cgresearch.studentprojects.brickbuilder.voxelcloud.IVoxelizationAlgorithm;
import cgresearch.studentprojects.brickbuilder.voxelcloud.VoxelCloudTransformation;
import cgresearch.studentprojects.brickbuilder.voxelcloud.VoxelCloudWriter;
import cgresearch.studentprojects.brickbuilder.voxelcloud.VoxelizationParityCount;

public class PerformanceTest {
	private static String[] models = new String[] {
//			"meshes/bunny002.obj",
		"meshes/legoMan.obj",
//		"meshes/yoshipirate.obj",
//		"meshes/hulk/Hulk.obj",		
		"meshes/bunny003.obj"
//		"meshes/cube.obj"
	};
	private static int[] resolutions = new int[] {10, 25, 50/*, 100*/};
	private static int runsPerModelPerRes = 1;
	private static int shellSize = 8;
	private static String pathToSaveDir = "C:\\models4\\";
	
	public static void main(String[] args) {		
		IIORegistry registry = IIORegistry.getDefaultInstance();
		registry.registerServiceProvider(new cgresearch.studentprojects.brickbuilder.externalimageiotga.TGAImageReaderSpi());
		
		ResourcesLocator.getInstance().addPath("C:\\Users\\Teetasse\\Desktop\\Bachelorarbeit\\GIT-Projekte\\cg\\assets\\");
		ResourcesLocator.getInstance().addPath("C:\\Users\\Teetasse\\Desktop\\Bachelorarbeit\\GIT-Projekte\\cg_brickbuilder\\resources\\");
		
		ObjFileReader r = new ObjFileReader();
		IVoxelizationAlgorithm algo = new VoxelizationParityCount();
		Vector voxelScale = VectorFactory.createVector3(80, 96, 80);
		
		for (String m : models) {
			String[] mA = m.split("/");
			String mm = mA[mA.length-1].replace(".obj", "");
			System.out.println("Model: "+mm);
			ITriangleMesh mesh = r.readFile(m).get(0);
			mesh.fitToUnitBox();
		
			System.out.println(mesh.getNumberOfVertices());
			System.out.println(mesh.getNumberOfTriangles());
//			if (true) continue;
			
			for (int res : resolutions) {
				System.out.println("Res: "+res);
				long[] trans = new long[runsPerModelPerRes];
				long[] mark = new long[runsPerModelPerRes];
				long sumTrans = 0;
				long sumMark = 0;
				
				for (int i = 0; i < runsPerModelPerRes; i++) {
					// trans
					long s = System.currentTimeMillis();
					IVoxelCloud cloud = algo.transformMesh2Cloud(mesh, res, voxelScale);
					trans[i] = System.currentTimeMillis()-s;
					sumTrans += trans[i];
					System.out.println("trans "+(i+1)+": "+trans[i]+"ms");
					
					// mark
					s = System.currentTimeMillis();
					cloud = VoxelCloudTransformation.markSurfaceVoxels(cloud, shellSize);
					mark[i] = System.currentTimeMillis()-s;
					sumMark += mark[i];
					System.out.println("mark "+(i+1)+": "+mark[i]+"ms");
					
					// save to file once
					if (i == 0) {
						s = System.currentTimeMillis();
						VoxelCloudTransformation.removeUnnecessaryColors(cloud);
						VoxelCloudWriter.writeToFile(pathToSaveDir+"/___"+mm+"-"+res+".vox", cloud);
						System.out.println("save "+(i+1)+": "+(System.currentTimeMillis()-s)+"ms");
					}
				}
				
//				System.out.println(Arrays.toString(trans));
				System.out.println("avg trans: "+(sumTrans/runsPerModelPerRes)+"ms");
				
//				System.out.println(Arrays.toString(mark));
				System.out.println("avg mark: "+(sumMark/runsPerModelPerRes)+"ms");
			}
		}
	}
}

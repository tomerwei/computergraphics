package cgresearch.studentprojects.brickbuilder;

//import java.util.Arrays;

import javax.imageio.spi.IIORegistry;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.studentprojects.brickbuilder.brickcloud.BrickBuilderAlgorithm;
import cgresearch.studentprojects.brickbuilder.brickcloud.BrickSet;
import cgresearch.studentprojects.brickbuilder.brickcloud.ComposedBrick;
import cgresearch.studentprojects.brickbuilder.brickcloud.IBrickCloud;
import cgresearch.studentprojects.brickbuilder.brickcloud.RootBrick;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;
import cgresearch.studentprojects.brickbuilder.voxelcloud.IVoxelCloud;
import cgresearch.studentprojects.brickbuilder.voxelcloud.VoxelCloudReader;

public class PerformanceTest2 {
	private static String[] models = new String[] {
//			"bunny002",
//		"legoMan",
//		"yoshipirate",
//			"Hulk"
		
		"bunny003",
		"legoMan",
		"yoshipirate"
//		"cube"
	};
	private static int[] resolutions = new int[] {10, 25, 50/*, 100*/};
	private static int runsPerModelPerRes = 10;
	private static String pathToSaveDir = "C:\\models3\\";
	
	public static void main(String[] args) {		
		IIORegistry registry = IIORegistry.getDefaultInstance();
		registry.registerServiceProvider(new cgresearch.studentprojects.brickbuilder.externalimageiotga.TGAImageReaderSpi());
		
		ResourcesLocator.getInstance().addPath("C:\\Users\\Teetasse\\Desktop\\Bachelorarbeit\\GIT-Projekte\\cg\\assets\\");
		ResourcesLocator.getInstance().addPath("C:\\Users\\Teetasse\\Desktop\\Bachelorarbeit\\GIT-Projekte\\cg_brickbuilder\\resources\\");
		ResourcesLocator.getInstance().addPath(pathToSaveDir);
		
		BrickBuilderAlgorithm algo = new BrickBuilderAlgorithm();
		
		for (String m : models) {
			String[] mA = m.split("/");
			String mm = mA[mA.length-1].replace(".obj", "");
			System.out.println("Model: "+mm);
		
			for (int res : resolutions) {
				System.out.println("Res: "+res);
				IVoxelCloud cloud = VoxelCloudReader.readFromFile("___"+m+"-"+res+"_0.vox");
				long[] trans = new long[runsPerModelPerRes];
				long sumTrans = 0;
				
				RootBrick root = new RootBrick(VectorMatrixFactory.newVector(0.08, 0.096, 0.08), null);		
				ComposedBrick brick2x1 = new ComposedBrick(root, new VectorInt3(2, 1, 1));
				ComposedBrick brick2x2 = new ComposedBrick(root, new VectorInt3(2, 1, 2));		
				ComposedBrick brick3x1 = new ComposedBrick(root, new VectorInt3(3, 1, 1));
				ComposedBrick brick3x2 = new ComposedBrick(root, new VectorInt3(3, 1, 2));		
				ComposedBrick brick4x1 = new ComposedBrick(root, new VectorInt3(4, 1, 1));
				ComposedBrick brick4x2 = new ComposedBrick(root, new VectorInt3(4, 1, 2));
				ComposedBrick brick6x1 = new ComposedBrick(root, new VectorInt3(6, 1, 1));
				ComposedBrick brick6x2 = new ComposedBrick(root, new VectorInt3(6, 1, 2));
				ComposedBrick brick8x1 = new ComposedBrick(root, new VectorInt3(8, 1, 1));
				ComposedBrick brick8x2 = new ComposedBrick(root, new VectorInt3(8, 1, 2));
				
				BrickSet set = new BrickSet(root);
				set.addChildBrick(brick2x1);
				set.addChildBrick(brick2x2);
				set.addChildBrick(brick3x1);
				set.addChildBrick(brick3x2);
				set.addChildBrick(brick4x1);
				set.addChildBrick(brick4x2);
				set.addChildBrick(brick6x1);
				set.addChildBrick(brick6x2);
				set.addChildBrick(brick8x1);
				set.addChildBrick(brick8x2);
				
//				List<IColorRGB> colors = new ArrayList<IColorRGB>();
//				for (int z = 0; z < cloud.getResolutions().getZ(); z++) {
//					for (int y = 0; y < cloud.getResolutions().getY(); y++) {
//						for (int x = 0; x < cloud.getResolutions().getX(); x++) {
//							VectorInt3 p = new VectorInt3(x, y, z);
//							if (cloud.getVoxelAt(p) == VoxelType.SURFACE) {
//								IColorRGB c = cloud.getVoxelColor(p);
//								if (c != null && !colors.contains(c)) {
//									colors.add(c);
//								}
//							}
//						}
//					}
//				}			
//				for (IColorRGB c : colors) set.addBrickColor(c);
				
				int bricks = 0, cc = 0, wp = 0;
				
				for (int i = 0; i < runsPerModelPerRes; i++) {
					// trans
					long s = System.currentTimeMillis();
					IBrickCloud cloud2 = algo.transformVoxel2Bricks(cloud, set, false, false);
					trans[i] = System.currentTimeMillis()-s;
					sumTrans += trans[i];
					System.out.println("trans "+(i+1)+": "+trans[i]+"ms");
//					System.out.println("b: "+cloud2.getNumberOfBricks());
//					System.out.println("cc: "+cloud2.getNumberOfConnectedComponents());
//					System.out.println("wp: "+cloud2.getNumberOfWeakPoints());
					bricks += cloud2.getNumberOfBricks();
					cc += cloud2.getNumberOfConnectedComponents();
					wp += cloud2.getNumberOfWeakPoints();
					
//					System.out.println("???");
//					long t = System.currentTimeMillis();
//					System.out.println(algo.getWeakArticulationPoints(cloud2.getBrickGraph()).size());
//					System.out.println((System.currentTimeMillis()-t)+"ms");
					
					// save to file once
//					if (i == 0) {
//						s = System.currentTimeMillis();
//						BrickCloudWriter.writeToFile(pathToSaveDir+"/", "3___"+mm+"-"+res+"_"+i+".brick", cloud2);
//						System.out.println("save "+(i+1)+": "+(System.currentTimeMillis()-s)+"ms");
//					}
				}
				
//				System.out.println(Arrays.toString(trans));
				
				System.out.println("avg trans: "+(sumTrans/runsPerModelPerRes)+"ms");
				System.out.println("avg b: "+(1.0*bricks/runsPerModelPerRes));
				System.out.println("avg cc: "+(1.0*cc/runsPerModelPerRes));
				System.out.println("avg wp: "+(1.0*wp/runsPerModelPerRes));
			}
		}
	}
}

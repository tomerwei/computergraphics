/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;

import cgresearch.studentprojects.brickbuilder.math.IColorRGB;
import cgresearch.studentprojects.brickbuilder.math.IVectorInt3;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;
import cgresearch.studentprojects.brickbuilder.voxelcloud.IVoxelCloud;
import cgresearch.studentprojects.brickbuilder.voxelcloud.VoxelType;

/**
 * Implemenation of the brickbuilder algorithm interface.
 * Proposed in "Automatic Generation of Constructable Brick Sculptures"
 * by Romain Testuz, Yuliy Schwartzburg and Mark Pauly.
 * 
 * @author Chris Michael Marquardt
 */
public class BrickBuilderAlgorithm implements IBrickBuilderAlgorithm {

	@Override
	public IBrickCloud transformVoxel2Bricks(IVoxelCloud voxelCloud, IBrickSet brickSet,
			boolean colorSurface, boolean preHollow) {
		
		boolean coloring = colorSurface && voxelCloud.getNumberOfVoxelWithColor() > 0;				
		BrickCloud brickCloud = convertVoxelToBricks(voxelCloud, brickSet, coloring, preHollow);
		merging(brickCloud, coloring);			
		return solidityOptimization(brickCloud, voxelCloud, coloring);
	}
	
	/**
	 * Step 0: Convert a voxel cloud to a brickcloud.
	 * @param voxelCloud
	 * @return
	 */
	private BrickCloud convertVoxelToBricks(IVoxelCloud voxelCloud, IBrickSet brickSet, 
			boolean colorSurface, boolean preHollow) {
		
		BrickCloud brickCloud = new BrickCloud(voxelCloud.getLocationLowerLeft(),
				voxelCloud.getResolutions(), brickSet);
		
		for (int z = 0; z < voxelCloud.getResolutions().getZ(); z++) {
			for (int y = 0; y < voxelCloud.getResolutions().getY(); y++) {
				for (int x = 0; x < voxelCloud.getResolutions().getX(); x++) {
					IVectorInt3 vec = new VectorInt3(x, y, z);
					if (!preHollow || (preHollow &&
							(voxelCloud.getVoxelAt(vec) == VoxelType.SURFACE || 
							voxelCloud.getVoxelAt(vec) == VoxelType.SHELL))) {
						
						if (colorSurface && voxelCloud.getVoxelAt(vec) == VoxelType.SURFACE) {
							brickCloud.addBrick(brickSet.getRootBrick(), vec,
									BrickRotation.XDIR_POS, voxelCloud.getVoxelColor(vec));						
						}
						else {
							brickCloud.addBrick(brickSet.getRootBrick(), vec, BrickRotation.XDIR_POS);
						}
					}
				}
			}
		}
		
		return brickCloud;
	}
	
	/**
	 * Step 1: Merging
	 * @param brickCloud
	 */
	protected void merging(BrickCloud brickCloud, boolean checkColor) {
		IBrickBuilderMergeFunction func = new BrickBuilderMergeFunction();
		
		// from bottom to top
		for (int y = 0; y < brickCloud.getResolutions().getY(); y++) {
			mergingAtHeight(brickCloud, y, func, checkColor);
		}
	}


	private void mergingAtHeight(BrickCloud brickCloud, int y, IBrickBuilderMergeFunction func, boolean checkColor) {
		Random rand = new Random();
		// check if there are any bricks
		if (brickCloud.getNumberOfBricks(y) < 1) return;
		Set<BrickInstance> bricksUsed = new HashSet<BrickInstance>();		
		// while we find a mergeable brick
		while (bricksUsed.size() < brickCloud.getNumberOfBricks(y)) {			
			// choose a random brick
			BrickInstance brick = brickCloud.getRandomBrick(y);
//			// get current brick
			if (brick == null || bricksUsed.contains(brick)) continue;
			//IVectorInt3 pos = brick.getPos();
			// get next possible bricks
			List<IChildBrick> possibleBricks = brickCloud.getBrickSet().getNextBricks(brick.getBrick());	
			
			// repeat until random stone can not be merged anymore
			boolean repeat = true;
			do {
				// calc cost values for each brick for every rotation //possible position
				int maxCost = Integer.MIN_VALUE;
				Map<IBrick, BrickRotation> maxBricks = new HashMap<IBrick, BrickRotation>();
				Map<IBrick, IVectorInt3> maxPos = new HashMap<IBrick, IVectorInt3>();
				for (IChildBrick b : possibleBricks) {
					// only 2 rotations
					if (b instanceof ComposedBrick) {
						maxCost = checkBrickPosition(brickCloud, func, brick, maxCost, maxBricks, maxPos,
							b, BrickRotation.XDIR_POS, checkColor);
						maxCost = checkBrickPosition(brickCloud, func, brick, maxCost, maxBricks, maxPos,
							b, BrickRotation.ZDIR_POS, checkColor);
					}
					// all 4 rotations
					else {
						maxCost = checkBrickPosition(brickCloud, func, brick, maxCost, maxBricks, maxPos,
							b, BrickRotation.XDIR_POS, checkColor);
						maxCost = checkBrickPosition(brickCloud, func, brick, maxCost, maxBricks, maxPos,
							b, BrickRotation.XDIR_NEG, checkColor);
						maxCost = checkBrickPosition(brickCloud, func, brick, maxCost, maxBricks, maxPos,
							b, BrickRotation.ZDIR_POS, checkColor);
						maxCost = checkBrickPosition(brickCloud, func, brick, maxCost, maxBricks, maxPos,
							b, BrickRotation.ZDIR_NEG, checkColor);
					}
				}
				// replace bricks
				if (maxBricks.size() > 0) {
					// choose random
					IBrick[] arr = maxBricks.keySet().toArray(new IBrick[]{});
					IBrick chosen = arr[rand.nextInt(arr.length)];
					if (!checkColor)
						brickCloud.addBrick(chosen, maxPos.get(chosen), maxBricks.get(chosen));
					else {
						IColorRGB color = brick.getColor();
						if (color == null) {
							List<IVectorInt3> unitList = BrickInstance.getBrickUnitPositions(chosen, maxPos.get(chosen), maxBricks.get(chosen));
							for (IVectorInt3 v : unitList) {
								BrickInstance b = brickCloud.getBrickAt(v);
								if (b != null && b.getColor() != null) color = b.getColor();
							}
						}
					
						brickCloud.addBrick(chosen, maxPos.get(chosen), maxBricks.get(chosen), color);
					}
					
					repeat = true;
					bricksUsed.clear();		
				}
				else {
					repeat = false;
				}
			} while(repeat);
						
			bricksUsed.add(brickCloud.getBrickAt(brick.getPos()));
		}
	}
	
	
	/**
	 * Step 2: Solidity Optimization
	 * @param brickCloud
	 */
	protected BrickCloud solidityOptimization(BrickCloud brickCloud, IVoxelCloud voxelCloud, boolean checkColor) {
		IBrickBuilderMergeFunction func = new BrickBuilderMergeFunctionRandom();
		
		// are parts not conntected?
		int connectedComponents = brickCloud.getNumberOfConnectedComponents();		
		if (connectedComponents > 1) {
			int tries = 0, overallTries = 0;	
			
			while (connectedComponents > 1 && tries < 100 && overallTries < 1000) {
					
				ConnectivityInspector<String, DefaultEdge> inspec = brickCloud.getBrickGraphInspector();
				List<Set<String>> ccList = inspec.connectedSets();
//				System.err.println("try: "+tries+ " cc: "+connectedComponents);
				
				// copy cloud
				BrickCloud cloudBefore = new BrickCloud(brickCloud);
				
				// get biggest component
				int biggestIndex = 0, biggestSize = ccList.get(0).size();
				for (int i = 0; i < ccList.size(); i++) {
					if (ccList.get(i).size() > biggestSize) {
						biggestIndex = i;
						biggestSize = ccList.get(i).size();
					}
				}
				
				// remove it from the list
				ccList.remove(ccList.get(biggestIndex));
				
				// find every border brick, split it and the neighbors and merge
				Set<Integer> heights = new HashSet<Integer>();
				
				for (Set<String> cc : ccList) {
					for (String b : cc) {
						BrickInstance brick = BrickInstance.brickMap.get(Long.parseLong(b));
						brickCloud.removeBrickAndFill(brick.getPos());
							
						// split all the neighbors
						for (BrickInstance neighbor : brickCloud.getBrickNeighbors(brick)) {
							if (!(neighbor.getBrick() instanceof RootBrick)) {
								brickCloud.removeBrickAndFill(neighbor.getPos());
							}
						}
							
						// merging randomly
						heights.add(brick.getPos().getY());		
					}
				}		
				
				for (int h : heights)
					mergingAtHeight(brickCloud, h, func, checkColor);
				heights.clear();
				
				int ccs = brickCloud.getNumberOfConnectedComponents();	
				
				if (ccs == connectedComponents) {
					tries++;
				}
				else if (ccs < connectedComponents) {
					tries = 0;
					connectedComponents = ccs;					
				}
				else {
					brickCloud = cloudBefore;
					tries++;
				}
				
				overallTries++;
			}
		}
		
		// weakpoints
		int weakBrickCount = brickCloud.getWeakArticulationPoints().size();
		if (weakBrickCount > 0) {
			int tries = 0;			
			
			while (weakBrickCount > 0 && tries < 50) {
				
				List<BrickInstance> weakBricks = brickCloud.getWeakArticulationPoints();
				weakBrickCount = weakBricks.size();
//				System.err.println("try: "+tries+ " cc: "+connectedComponents+" wb "+weakBrickCount);
				
				for (BrickInstance brick : weakBricks) {
					// copy cloud
					BrickCloud cloudBefore = new BrickCloud(brickCloud);
				
					Set<Integer> heights = new HashSet<Integer>();
					brickCloud.removeBrickAndFill(brick.getPos());					
					for (BrickInstance neighbor : brickCloud.getBrickNeighbors(brick)) {
						brickCloud.removeBrickAndFill(neighbor.getPos());
					}	
					heights.add(brick.getPos().getY());			
					
					if (brick.getPos().getY() > 0) {
						IVectorInt3 pos = brick.getPos().add(new VectorInt3(0, -1, 0));
						BrickInstance bottom = brickCloud.getBrickAt(pos);
						
						if (bottom != null) {
							brickCloud.removeBrickAndFill(bottom.getPos());					
							for (BrickInstance neighbor : brickCloud.getBrickNeighbors(bottom)) {
								brickCloud.removeBrickAndFill(neighbor.getPos());
							}	
							heights.add(pos.getY());	
						}
					}
					
					if (brick.getPos().getY() < brickCloud.getResolutions().getY() - 1) {
						IVectorInt3 pos = brick.getPos().add(new VectorInt3(0, 1, 0));
						BrickInstance top = brickCloud.getBrickAt(pos);
						
						if (top != null) {
							brickCloud.removeBrickAndFill(top.getPos());					
							for (BrickInstance neighbor : brickCloud.getBrickNeighbors(top)) {
								brickCloud.removeBrickAndFill(neighbor.getPos());
							}	
							heights.add(pos.getY());
						}
					}
				
					for (int h : heights)
						mergingAtHeight(brickCloud, h, func, checkColor);
				
					int ccs = brickCloud.getNumberOfConnectedComponents();	
					int wb = brickCloud.getWeakArticulationPoints().size();
				
					// more components? reverse
					if (ccs > connectedComponents)
						brickCloud = cloudBefore;
					else if (ccs == connectedComponents) {		
						// more weak bricks? reverse
						if (wb >= weakBrickCount)
							brickCloud = cloudBefore;
					}
					else 
						connectedComponents = ccs;	
				}
				
				tries++;
			}
		}
		
//		System.out.println("cc after: "+brickCloud.getNumberOfConnectedComponents());	
//		System.out.println("wp after: "+brickCloud.getNumberOfWeakPoints());
//		System.out.println("bricks: "+brickCloud.getNumberOfBricks());
		
		return brickCloud;
	}
	
	private int checkBrickPosition(IBrickCloud brickCloud, IBrickBuilderMergeFunction func,
			BrickInstance brick, int maxCost, Map<IBrick, BrickRotation> maxBricks, Map<IBrick, IVectorInt3> maxPos, 
			IChildBrick b, BrickRotation rot, boolean checkColor) {
//		System.out.println(rot);
		for (IVectorInt3 v : BrickInstance.getBrickUnitPositions(b, brick.getPos(), rot)) {	
			if ((checkColor && brickCloud.canBrickReplaceBricksAt(b, v, rot, brick.getColor()) ||
					!checkColor && brickCloud.canBrickReplaceBricksAt(b, v, rot))) {
				int cost = func.valueFunction(brickCloud, b, v, rot);
				if (cost >= maxCost) {
					maxCost = cost; maxBricks.clear();
					maxBricks.put(b, rot);
					maxPos.put(b, v);
				}
			}
		}
		return maxCost;
	}
}

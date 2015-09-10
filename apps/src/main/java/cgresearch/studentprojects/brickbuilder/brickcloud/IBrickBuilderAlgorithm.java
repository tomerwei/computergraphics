/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

import cgresearch.studentprojects.brickbuilder.voxelcloud.IVoxelCloud;

/**
 * Interface for the brick builder algorithm to transform a given voxel cloud (IVoxelCloud) 
 * into a (stable) brick cloud.
 * 
 * @author Chris Michael Marquardt
 */
public interface IBrickBuilderAlgorithm {
	/**
	 * Transforms a voxel cloud into a brick cloud.
	 * 
	 * @param voxelCloud		voxel cloud
	 * @param brickSet			brick set to build the brick cloud
	 * @param colorSurface		should the surface be colored like the voxel cloud
	 * 							(only if the voxel cloud has colored voxels)
	 * @param preHollow			ignore voxels not marked as surface or shell
	 * @return
	 */
	IBrickCloud transformVoxel2Bricks(IVoxelCloud voxelCloud, IBrickSet brickSet,
			boolean colorSurface, boolean preHollow);
}

/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.voxelcloud;

import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;

/**
 * Interface for all voxelization algorithms to transform a given model
 * (ITriangleMesh) into a voxel reprensentation given a resolution and a voxel
 * definition (defined by an IBrick).
 * 
 * @author Chris Michael Marquardt
 */
public interface IVoxelizationAlgorithm {

	/**
	 * Transforms a given mesh into a voxel cloud.
	 * 
	 * @param mesh
	 *            a mesh to be transformed
	 * @param resolutionAxisX
	 *            resolution of the voxel cloud on the x-axis of the mesh
	 * @param voxelScale
	 *            a vector defining the scale of every voxel
	 * @return a voxel cloud
	 */
	public IVoxelCloud transformMesh2Cloud(ITriangleMesh mesh,
			int resolutionAxisX, Vector voxelScale);
}

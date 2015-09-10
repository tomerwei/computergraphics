/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.voxelcloud;

/**
 * Enumeratio declaring all type of voxels.
 * 
 * @author Chris Michael Marquardt
 */
public enum VoxelType {
	/**
	 * Exterior of the model
	 */
	EXTERIOR(),
	/**
	 * Interior of the model [> shellsize]
	 */
	INTERIOR(),
	/**
	 * Interior of the model [<= shellsize]
	 */
	SHELL(),
	/**
	 * Surface of the model (and shell)
	 */
	SURFACE();
}

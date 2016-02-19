/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.voxelcloud;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.studentprojects.brickbuilder.math.IColorRGB;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Interface for the voxel cloud which represents a voxel box in 3d.
 * 
 * @author Chris Michael Marquardt
 */
public interface IVoxelCloud {
	/**
	 * Get the bounding box of the cloud.
	 * @return
	 */
	public BoundingBox getBoundingBox();
	
	/**
	 * Get the dimensions of the voxel cloud.
	 * @return
	 */
	public Vector getDimensions();
	
	/**
	 * Get the dimensions of a voxel.
	 * @return
	 */
	public Vector getVoxelDimensions();
	
	/**
	 * Get the location of the lower left corner of the voxel cloud.
	 * @return
	 */
	public Vector getLocationLowerLeft();
	
	/**
	 * Get the resolution of every axis of the voxel cloud.
	 * @return
	 */
	public VectorInt3 getResolutions();	
	
	/**
	 * Returns the type of the voxel or null if it's out of bounds.
	 * @param vec	position
	 * @return
	 */
	public VoxelType getVoxelAt(VectorInt3 vec);
	
	/**
	 * Returns the location of the voxel or null if it's out of bounds.
	 * @param vec	position
	 * @return	
	 */
	public Vector getVoxelLocation(VectorInt3 vec);
	
	/**
	 * Returns the color of a voxel at the given position or null.
	 * @param vec	position
	 * @return
	 */
	public IColorRGB getVoxelColor(VectorInt3 vec);
	
	/**
	 * Sets a voxel at a given position. Returns false if the position is out of bounds.
	 * @param vec	position
	 * @param set	set value
	 * return 		true if set/unset, false if out of bounds
	 */
	public boolean setVoxelAt(VectorInt3 vec, VoxelType type);
	
	/**
	 * Sets the color of a voxel. Returns false if the voxel has no color or is out of bounds.
	 * @param vec	position
	 * @param value	color
	 * @return
	 */
	public boolean setVoxelColor(VectorInt3 vec, IColorRGB color);
	
	/**
	 * Sets the complete voxel cloud with the type.
	 * @param type
	 */
	public void setVoxelCloud(VoxelType type);
	
	/**
	 * Clears the color of the specified voxel.
	 * @param vec position
	 */
	public void clearVoxelColor(VectorInt3 vec);
	
	/**
	 * Clears all colors.
	 */
	public void clearVoxelColors();
	
	/**
	 * Returns the number of all voxels having a color.
	 * @return
	 */
	public int getNumberOfVoxelWithColor();
}

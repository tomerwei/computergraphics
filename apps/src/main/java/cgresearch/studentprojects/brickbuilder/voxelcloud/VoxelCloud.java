/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.voxelcloud;

import java.util.HashMap;
import java.util.Map;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.VectorFactory;
import cgresearch.studentprojects.brickbuilder.math.IColorRGB;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */

/**
 * Implementation of the IVoxelCloud interface as abstract class.
 * 
 * @author Chris Michael Marquardt
 */
public class VoxelCloud implements IVoxelCloud {
	/**
	 * Location of the cloud (lower left corner).
	 */
	private Vector locationLowerLeft;
	/**
	 * Dimensions of a voxel.
	 */
	private Vector voxelDimensions;
	/**
	 * Resolution of the cloud in every 3 axis.
	 */
	private VectorInt3 resolution;
	/**
	 * Voxel array.
	 */
	private VoxelType[] voxelArray;	
	/**
	 * Voxel color map.
	 */
	private Map<VectorInt3, IColorRGB> voxelColors;	
	/**
	 * Bounding box of the voxel cloud.
	 */
	private BoundingBox boundingBox;
	
	/**
	 * Constructor - creates a full voxel cloud.
	 * @param locationLowerLeft	location of the lower left corner
	 * @param voxelDimensions	voxel dimensions
	 * @param resolution		resolution
	 */
	public VoxelCloud(Vector locationLowerLeft, Vector voxelDimensions, VectorInt3 resolution) {
		this.locationLowerLeft = VectorFactory.createVector(locationLowerLeft);
		this.voxelDimensions = VectorFactory.createVector(voxelDimensions);
		this.resolution = resolution;
		this.voxelArray = new VoxelType[resolution.getZ() * resolution.getY() * resolution.getX()];
		this.voxelColors = new HashMap<VectorInt3, IColorRGB>();
		this.boundingBox = new BoundingBox(this.locationLowerLeft,
				this.locationLowerLeft.add(VectorFactory.createVector3(
						this.voxelDimensions.get(MathHelpers.INDEX_0) * this.resolution.getX(), 
						this.voxelDimensions.get(MathHelpers.INDEX_1) * this.resolution.getY(),
						this.voxelDimensions.get(MathHelpers.INDEX_2) * this.resolution.getZ()
						)));
		setVoxelCloud(VoxelType.EXTERIOR);
	}
	
	/**
	 * Copyconstructor.
	 * @param cloud
	 */
	public VoxelCloud(VoxelCloud cloud) {
		this.locationLowerLeft = VectorFactory.createVector(cloud.locationLowerLeft);
		this.voxelDimensions = VectorFactory.createVector(cloud.voxelDimensions);
		this.resolution = cloud.resolution;
		this.voxelArray = new VoxelType[resolution.getZ() * resolution.getY() * resolution.getX()];
		for (int i = 0; i < this.voxelArray.length; i++) this.voxelArray[i] = cloud.voxelArray[i];
		this.voxelColors = new HashMap<VectorInt3, IColorRGB>();
		for (VectorInt3 key : cloud.voxelColors.keySet()) this.voxelColors.put(key, cloud.voxelColors.get(key));
		this.boundingBox = new BoundingBox(this.locationLowerLeft,
				this.locationLowerLeft.add(VectorFactory.createVector3(
						this.voxelDimensions.get(MathHelpers.INDEX_0) * this.resolution.getX(), 
						this.voxelDimensions.get(MathHelpers.INDEX_1) * this.resolution.getY(),
						this.voxelDimensions.get(MathHelpers.INDEX_2) * this.resolution.getZ()
						)));
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	@Override
	public Vector getDimensions() {
		return VectorFactory.createVector3(
				voxelDimensions.get(MathHelpers.INDEX_0) * resolution.getX(),
				voxelDimensions.get(MathHelpers.INDEX_1) * resolution.getY(),
				voxelDimensions.get(MathHelpers.INDEX_2) * resolution.getZ());
	}
	
	@Override
	public Vector getVoxelDimensions() {
		return VectorFactory.createVector(voxelDimensions);
	}

	@Override
	public Vector getLocationLowerLeft() {
		return VectorFactory.createVector(locationLowerLeft);
	}

	@Override
	public VectorInt3 getResolutions() {
		return resolution;
	}

	@Override
	public VoxelType getVoxelAt(VectorInt3 vec) {
		if (!existVoxelAt(vec)) return null;
		return voxelArray[(resolution.getY() * resolution.getX() * vec.getZ()) + 
		                  (resolution.getX() * vec.getY()) + vec.getX()];
	}
	
	@Override
	public Vector getVoxelLocation(VectorInt3 vec) {
		if (!existVoxelAt(vec)) return null;		
		Vector location = VectorFactory.createVector(locationLowerLeft);
		location = location.add(voxelDimensions.multiply(0.5));
		location = location.add(VectorFactory.createVector3(
				voxelDimensions.get(MathHelpers.INDEX_0) * vec.getX(),
				voxelDimensions.get(MathHelpers.INDEX_1) * vec.getY(),
				voxelDimensions.get(MathHelpers.INDEX_2) * vec.getZ()));
		return location;
	}

	@Override
	public IColorRGB getVoxelColor(VectorInt3 vec) {
		return voxelColors.get(vec);
	}
	
	@Override
	public boolean setVoxelAt(VectorInt3 vec, VoxelType type) {
		if (!existVoxelAt(vec)) return false;		
		voxelArray[(resolution.getY() * resolution.getX() * vec.getZ()) + 
	                  (resolution.getX() * vec.getY()) + vec.getX()] = type;
		return true;
	}

	@Override
	public boolean setVoxelColor(VectorInt3 vec, IColorRGB color) {
		if (!existVoxelAt(vec)) return false;
		voxelColors.put(vec, color);
		return true;
	}

	@Override
	public void setVoxelCloud(VoxelType type) {
		for (int i = 0; i < this.voxelArray.length; i++) this.voxelArray[i] = type;
	}
	
	@Override
	public void clearVoxelColor(VectorInt3 vec) {
		voxelColors.remove(vec);
	}
	
	@Override
	public void clearVoxelColors() {
		voxelColors.clear();
	}

	@Override
	public int getNumberOfVoxelWithColor() {
		return voxelColors.size();
	}
	
	/**
	 * Checks if the vec is inbounds.
	 * @param vec
	 * @return
	 */
	private boolean existVoxelAt(VectorInt3 vec) {
		if (vec.getX() < 0 || vec.getX() >= resolution.getX()) return false;
		if (vec.getY() < 0 || vec.getY() >= resolution.getY()) return false;
		if (vec.getZ() < 0 || vec.getZ() >= resolution.getZ()) return false;
		return true;
	}
}

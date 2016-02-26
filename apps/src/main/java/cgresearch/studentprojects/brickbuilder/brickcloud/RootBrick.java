/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Rootbrick implemenation.
 * 
 * @author Chris Michael Marquardt
 */
public class RootBrick implements IBrick {
	/**
	 * Dimensions of the brick.
	 */
	private Vector dimensions;
	/**
	 * Model of the brick.
	 */
	private ITriangleMesh model;	
	/**
	 * Unit positions.
	 */
	private List<VectorInt3> unitPos;
	
	public RootBrick(Vector dimensions, ITriangleMesh model) {
		this.dimensions = VectorFactory.createVector(dimensions);
		this.model = model;
		this.unitPos = new ArrayList<VectorInt3>();
		this.unitPos.add(new VectorInt3(0, 0, 0));
	}
	
	@Override
	public Vector getDimensions() {
		return VectorFactory.createVector(dimensions);
	}

	@Override
	public ITriangleMesh getModel() {
		return model;
	}

	@Override
	public BrickType getBrickType() {
		return BrickType.ROOT;
	}

	@Override
	public VectorInt3 getResolution() {
		return new VectorInt3(1, 1, 1);
	}

	@Override
	public List<VectorInt3> getUnitPositions() {
		return unitPos;
	}
}

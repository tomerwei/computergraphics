/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.studentprojects.brickbuilder.math.IVectorInt3;
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
	private IVector3 dimensions;
	/**
	 * Model of the brick.
	 */
	private ITriangleMesh model;	
	/**
	 * Unit positions.
	 */
	private List<IVectorInt3> unitPos;
	
	public RootBrick(IVector3 dimensions, ITriangleMesh model) {
		this.dimensions = VectorMatrixFactory.newIVector3(dimensions);
		this.model = model;
		this.unitPos = new ArrayList<IVectorInt3>();
		this.unitPos.add(new VectorInt3(0, 0, 0));
	}
	
	@Override
	public IVector3 getDimensions() {
		return VectorMatrixFactory.newIVector3(dimensions);
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
	public IVectorInt3 getResolution() {
		return new VectorInt3(1, 1, 1);
	}

	@Override
	public List<IVectorInt3> getUnitPositions() {
		return unitPos;
	}
}

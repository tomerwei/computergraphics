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
 * A special brick.
 * 
 * @author Chris Michael Marquardt
 */
public class SpecialBrick implements IChildBrick {
	/**
	 * The root brick.
	 */
	private IBrick rootBrick;
	/**
	 * Resolution of the root brick.
	 */
	private IVectorInt3 resolution;
	/**
	 * Brick model.
	 */
	private ITriangleMesh model;
	/**
	 * Brick type.
	 */
	private BrickType brickType;
	/**
	 * Unit positions.
	 */
	private List<IVectorInt3> unitPos;
	
	/**
	 * Constructor.
	 * @param rootBrick
	 * @param resolution
	 * @param model
	 */
	public SpecialBrick(IBrick rootBrick, List<IVectorInt3> unitPos, ITriangleMesh model, BrickType brickType) {
		this.rootBrick = rootBrick;
		this.model = model;
		this.brickType = brickType;
		
		if (unitPos.size() < 1) throw new IllegalArgumentException("The unit position list can not by empty!");
		int[][] minMaxAxiz = {
				{Integer.MAX_VALUE, Integer.MIN_VALUE},
				{Integer.MAX_VALUE, Integer.MIN_VALUE},
				{Integer.MAX_VALUE, Integer.MIN_VALUE}
		};
		this.unitPos = new ArrayList<IVectorInt3>(unitPos);
		for (IVectorInt3 v : unitPos) {
			if (v.getX() < minMaxAxiz[0][0]) minMaxAxiz[0][0] = v.getX();
			if (v.getY() < minMaxAxiz[1][0]) minMaxAxiz[1][0] = v.getY();
			if (v.getZ() < minMaxAxiz[2][0]) minMaxAxiz[2][0] = v.getZ();
			if (v.getX() > minMaxAxiz[0][1]) minMaxAxiz[0][1] = v.getX();
			if (v.getY() > minMaxAxiz[1][1]) minMaxAxiz[1][1] = v.getY();
			if (v.getZ() > minMaxAxiz[2][1]) minMaxAxiz[2][1] = v.getZ();
		}
		this.resolution = new VectorInt3(minMaxAxiz[0][1] - minMaxAxiz[0][0],
				minMaxAxiz[1][1] - minMaxAxiz[1][0],
				minMaxAxiz[2][1] - minMaxAxiz[2][0]);
	}
	
	@Override
	public IVector3 getDimensions() {
		IVector3 dim = rootBrick.getDimensions();
		return VectorMatrixFactory.newIVector3(dim.get(0) * resolution.getX(),
				dim.get(1) * resolution.getY(),
				dim.get(2) * resolution.getZ());
	}

	@Override
	public ITriangleMesh getModel() {
		return model;
	}

	@Override
	public BrickType getBrickType() {
		return brickType;
	}

	@Override
	public IBrick getRootBrick() {
		return rootBrick;
	}
	
	@Override
	public IVectorInt3 getResolution() {
		return resolution;
	}

	@Override
	public List<IVectorInt3> getUnitPositions() {
		return unitPos;
	}
}

/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.algorithms.NodeMerger;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;


/**
 * A composed brick.
 * 
 * @author Chris Michael Marquardt
 */
public class ComposedBrick implements IChildBrick {
	/**
	 * The root brick.
	 */
	private IBrick rootBrick;
	/**
	 * Resolution of the root brick.
	 */
	private VectorInt3 resolution;
	/**
	 * Brick model.
	 */
	private ITriangleMesh model;
	/**
	 * Unit positions.
	 */
	private List<VectorInt3> unitPos;
	
	/**
	 * Constructor - generating model out of root brick.
	 * @param rootBrick
	 * @param resolution
	 */
	public ComposedBrick(IBrick rootBrick, VectorInt3 resolution) {
		this(rootBrick, resolution, null);
	}

	/**
	 * Constructor.
	 * @param rootBrick
	 * @param resolution
	 * @param model
	 */
	public ComposedBrick(IBrick rootBrick, VectorInt3 resolution, ITriangleMesh model) {
		this.rootBrick = rootBrick;
		this.resolution = resolution;
		this.unitPos = new ArrayList<VectorInt3>();
		for (int z = 0; z < resolution.getZ(); z++) {
			for (int y = 0; y < resolution.getY(); y++) {
				for (int x = 0; x < resolution.getX(); x++) {
					unitPos.add(new VectorInt3(x, y, z));
				}
			}
		}	
		if (model != null) this.model = model;
		else if (rootBrick.getModel() != null) createModel();
	}
	
	@Override
	public Vector getDimensions() {
		Vector dim = rootBrick.getDimensions();
		return VectorMatrixFactory.newVector(dim.get(0) * resolution.getX(),
				dim.get(1) * resolution.getY(),
				dim.get(2) * resolution.getZ());
	}

	@Override
	public ITriangleMesh getModel() {
		return model;
	}

	@Override
	public BrickType getBrickType() {
		return BrickType.COMPOSED;
	}

	@Override
	public IBrick getRootBrick() {
		return rootBrick;
	}
	
	@Override
	public VectorInt3 getResolution() {
		return resolution;
	}

	/**
	 * Creates a model out of the root brick model.
	 */
	private void createModel() {
		ITriangleMesh model = new TriangleMesh();
		BoundingBox box = model.getBoundingBox();
		Vector diagonal = box.getUpperRight().subtract(box.getLowerLeft());
		
		for (int z = 0; z < resolution.getZ(); z++) {
			for (int y = 0; y < resolution.getY(); y++) {
				for (int x = 0; x < resolution.getX(); x++) {
					ITriangleMesh add = new TriangleMesh(rootBrick.getModel());
					TriangleMeshTransformation.translate(add,
							VectorMatrixFactory.newVector(
									x * diagonal.get(0),
									y * diagonal.get(1),
									z * diagonal.get(2)));
					model.unite(add);
				}
			}
		}
		
		this.model = NodeMerger.merge(model, box.getDiameter() * 0.1);
	}

	@Override
	public List<VectorInt3> getUnitPositions() {
		return unitPos;
	}
}

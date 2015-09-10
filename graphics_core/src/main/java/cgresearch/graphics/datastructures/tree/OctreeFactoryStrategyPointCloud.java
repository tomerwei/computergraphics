package cgresearch.graphics.datastructures.tree;

import cgresearch.core.math.BoundingBox;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;

/**
 * Strategy class to create an octree of points.
 * 
 * @author Philipp Jenke
 *
 */
public class OctreeFactoryStrategyPointCloud implements
		IOctreeFactoryStrategy<Integer> {

	/**
	 * Point cloud reference.
	 */
	private final IPointCloud pointCloud;

	/**
	 * Constructor
	 */
	public OctreeFactoryStrategyPointCloud(IPointCloud pointCloud) {
		this.pointCloud = pointCloud;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return pointCloud.getBoundingBox();
	}

	@Override
	public int getNumberOfElements() {
		return pointCloud.getNumberOfPoints();
	}

	@Override
	public boolean elementFitsInNode(int elementIndex, OctreeNode<Integer> node) {
		Point p = pointCloud.getPoint(elementIndex);
		for (int i = 0; i < 3; i++) {
			if ((p.getPosition().get(i) < node.getLowerLeft().get(i)
					- NUMERICAL_ACCURACY)
					|| (p.getPosition().get(i) > node.getLowerLeft().get(i)
							+ node.getLength() + NUMERICAL_ACCURACY)) {
				return false;
			}
		}
		return true;
	}
}

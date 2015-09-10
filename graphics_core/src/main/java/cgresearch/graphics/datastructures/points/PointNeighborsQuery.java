package cgresearch.graphics.datastructures.points;

import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IVector3;
import cgresearch.graphics.datastructures.kd.KDTree;
import cgresearch.graphics.datastructures.kd.KeyDuplicateException;
import cgresearch.graphics.datastructures.kd.KeySizeException;

/**
 * Nearest neighbors query for a point cloud
 * 
 * @author Philipp Jenke
 *
 */
public class PointNeighborsQuery {

	/**
	 * KD tree for the query.
	 */
	private KDTree<Integer> tree = null;

	/**
	 * Point cloud to operate on.
	 */
	private IPointCloud pointCloud = null;

	/**
	 * List of neighbors from the last query.
	 */
	private List<Integer> neighborIndices = null;

	/**
	 * Constructor
	 */
	public PointNeighborsQuery(IPointCloud pointCloud) {
		this.pointCloud = pointCloud;
		// Build tree
		tree = new KDTree<Integer>(3);
		for (int pointIndex = 0; pointIndex < pointCloud.getNumberOfPoints(); pointIndex++) {
			try {
				tree.insert(pointCloud.getPoint(pointIndex).getPosition()
						.data(), pointIndex);
			} catch (KeySizeException e) {
				Logger.getInstance().exception("KD tree is full", e);
			} catch (KeyDuplicateException e) {
				Logger.getInstance().exception("KD tree insert ambibuity", e);
			}
		}
		Logger.getInstance().message(
				"Successfully built KD tree with "
						+ pointCloud.getNumberOfPoints() + " points.");
	}

	/**
	 * Run a query to find the knn nearst points from a given point index in the
	 * same point cloud.
	 */
	public void queryKnn(int pointIndex, int knn) {
		neighborIndices = null;
		try {
			neighborIndices = tree.nearest(pointCloud.getPoint(pointIndex)
					.getPosition().data(), knn);
		} catch (KeySizeException e) {
			Logger.getInstance().exception("KD tree size error", e);
		} catch (IllegalArgumentException e) {
			Logger.getInstance().exception("Error in knn query", e);
		}
	}

	/**
	 * Run a query to find the knn nearest points from a given point 'point'.
	 */
	public void queryKnn(IVector3 point, int knn) {
		neighborIndices = null;
		try {
			neighborIndices = tree.nearest(point.data(), knn);
		} catch (KeySizeException e) {
			Logger.getInstance().exception("KD tree size error", e);
		} catch (IllegalArgumentException e) {
			Logger.getInstance().exception("Error in knn query", e);
		}
	}

	/**
	 * Run a query to find the nearest points in epsilon-distance from a given
	 * point index in the same point cloud.
	 */
	public void queryEpsilon(int pointIndex, double epsilon) {
		neighborIndices = null;
		try {
			neighborIndices = tree.nearestEuclidean(
					pointCloud.getPoint(pointIndex).getPosition().data(),
					epsilon);
		} catch (KeySizeException e) {
			Logger.getInstance().exception("KD tree size error", e);
		} catch (IllegalArgumentException e) {
			Logger.getInstance().exception("Error in knn query", e);
		}
	}

	/**
	 * Run a query to find the nearest points in epsilon-distance from a given
	 * point.
	 */
	public void queryEpsilon(IVector3 point, double epsilon) {
		neighborIndices = null;
		try {
			neighborIndices = tree.nearestEuclidean(point.data(), epsilon);
		} catch (KeySizeException e) {
			Logger.getInstance().exception("KD tree size error", e);
		} catch (IllegalArgumentException e) {
			Logger.getInstance().exception("Error in knn query", e);
		}
	}

	/**
	 * Returns the number of neighbors from the last query.
	 * 
	 * @return
	 */
	public int getNumberOfNeighbors() {
		return (neighborIndices == null) ? 0 : neighborIndices.size();
	}

	/**
	 * Return the index of next point in the nearest neighbors query.
	 */
	public int getNeigbor(int index) {
		return neighborIndices.get(getNumberOfNeighbors() - index - 1);
	}

}

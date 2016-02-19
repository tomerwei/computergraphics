package cgresearch.graphics.algorithms;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.core.math.PrincipalComponentAnalysis;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.PointNeighborsQuery;

/**
 * Estimate the normals for a point cloud.
 * 
 * @author Philipp Jenke
 *
 */
public class NormalEstimator {

	/**
	 * Estimate the normals for each point based on the knn neighbors.
	 */
	public static void estimate(IPointCloud pointCloud, int knn) {
		PointNeighborsQuery query = new PointNeighborsQuery(pointCloud);
		PrincipalComponentAnalysis pca = new PrincipalComponentAnalysis();
		for (int pointIndex = 0; pointIndex < pointCloud.getNumberOfPoints(); pointIndex++) {
			query.queryKnn(pointIndex, knn);
			pca.clear();
			for (int i = 0; i < query.getNumberOfNeighbors(); i++) {
				pca.add(pointCloud.getPoint(query.getNeigbor(i)).getPosition());
			}
			pca.applyPCA();
			Vector normal = pca.getNormal();

			// Point into same direction as old normal
			if (normal.multiply(pointCloud.getPoint(pointIndex).getNormal()) < 0) {
				normal = normal.multiply(-1);
			}
			pointCloud.getPoint(pointIndex).setNormal(normal);
		}
		Logger.getInstance().message(
				"Estimated normals for point cloud with "
						+ pointCloud.getNumberOfPoints() + " points.");
	}
}

package cgresearch.graphics.datastructures;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloudFactory;
import cgresearch.graphics.datastructures.points.PointNeighborsQuery;

/**
 * Test the query for point neighborhoods.
 * 
 * @author Philipp Jenke
 *
 */
public class TestPointNeighborsQuery {

	@Test
	public void testKnn() {
		IPointCloud pointCloud = PointCloudFactory.createDummyPointCloud();
		PointNeighborsQuery query = new PointNeighborsQuery(pointCloud);
		int pointIndex = (int) (Math.random() * pointCloud.getNumberOfPoints());
		Point seedPoint = pointCloud.getPoint(pointIndex);

		int knn = 10;
		query.queryKnn(pointIndex, knn);

		assertEquals(query.getNumberOfNeighbors(), knn);
		for (int i = 0; i < query.getNumberOfNeighbors() - 1; i++) {
			int i0 = query.getNeigbor(i);
			int i1 = query.getNeigbor(i + 1);
			Point p0 = pointCloud.getPoint(i0);
			Point p1 = pointCloud.getPoint(i1);
			double d0 = p0.getPosition().subtract(seedPoint.getPosition())
					.getNorm();
			double d1 = p1.getPosition().subtract(seedPoint.getPosition())
					.getNorm();
			assertTrue(d0 <= d1);
		}
	}

	@Test
	public void testEpsilon() {
		IPointCloud pointCloud = PointCloudFactory.createDummyPointCloud();
		PointNeighborsQuery query = new PointNeighborsQuery(pointCloud);
		int pointIndex = (int) (Math.random() * pointCloud.getNumberOfPoints());
		Point seedPoint = pointCloud.getPoint(pointIndex);

		double epsilon = 0.1;
		query.queryEpsilon(pointIndex, epsilon);

		for (int i = 0; i < query.getNumberOfNeighbors() - 1; i++) {
			int i0 = query.getNeigbor(i);
			Point p0 = pointCloud.getPoint(i0);
			double d0 = p0.getPosition().subtract(seedPoint.getPosition())
					.getNorm();
			assertTrue(d0 <= epsilon);
		}
	}
}

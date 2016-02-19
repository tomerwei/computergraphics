/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.points;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.material.Material;

/**
 * Create dummy point clouds.
 * 
 * @author Philipp Jenke
 * 
 */
public class PointCloudFactory {

	/**
	 * Create a dummy point cloud.
	 */
	public static IPointCloud createDummyPointCloud() {
		int NUMBER_OF_POINTS = 5000;
		IPointCloud pointCloud = new PointCloud();
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			Vector position = VectorMatrixFactory.newVector(
					Math.random() - 0.5, Math.random() - 0.5,
					Math.random() - 0.5);
			Vector color = VectorMatrixFactory.newVector(Math.random(),
					Math.random(), Math.random());
			Vector normal = VectorMatrixFactory.newVector(Math.random(),
					Math.random(), Math.random());
			pointCloud.addPoint(new Point(position, color, normal));
		}
		return pointCloud;
	}

	/**
	 * Create the triangle mesh for a sphere with given origin an radius.
	 */
	public static IPointCloud createSphere(Vector center, double radius,
			int resolution) {
		IPointCloud pointCloud = new PointCloud();
		int resolutionX = resolution;
		int resolutionY = resolutionX / 2;
		// Compute vertex coordinates
		double deltaX = Math.PI * 2.0 / (double) resolutionX;
		double deltaY = Math.PI / (double) (resolutionY + 1);
		for (int x = 0; x < resolutionX; x++) {
			for (int y = 0; y < resolutionY; y++) {
				double phi = x * deltaX;
				double theta = (y + 1) * deltaY;
				Vector normal = VectorMatrixFactory.newVector(
						radius * Math.sin(theta) * Math.cos(phi)
								+ center.get(0), radius * Math.sin(theta)
								* Math.sin(phi) + center.get(1),
						radius * Math.cos(theta) + center.get(2));
				Vector position = normal.multiply(radius).add(center);
				Point point = new Point(position, Material.PALETTE2_COLOR2,
						normal);
				pointCloud.addPoint(point);
			}
		}

		return pointCloud;
	}
}

package cgresearch.studentprojects.registration;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.functions.Function;
import cgresearch.graphics.datastructures.points.IPointCloud;

/**
 * Distance function in the ICP registration.
 * 
 * @author Philipp Jenke
 *
 */
public class IcpDistanceFunction extends Function {

	/**
	 * First point cloud in the registration. This point cloud is not altered.
	 */
	private final IPointCloud pointCloudBase;

	/**
	 * Second point coud in the registration. This point cloud is tranformed.
	 */
	private final IPointCloud pointCloudRegister;

	/**
	 * Array of indices of corresponding points in the two point clouds. The
	 * first index is the index in the base point cloud, the second index is the
	 * index in the register point cloud (which is transformed).
	 */
	private int[][] correspondences = null;

	/*
	 * /** Constructor
	 */
	public IcpDistanceFunction(IPointCloud pointCloudBase,
			IPointCloud pointCloudRegister) {
		this.pointCloudBase = pointCloudBase;
		this.pointCloudRegister = pointCloudRegister;
	}

	/**
	 * Setter for the correspondences between points in the two point clouds.
	 */
	public void setCoorresponcences(int[][] correspondences) {
		this.correspondences = correspondences;
	}

	@Override
	public int getDimension() {
		return 6;
	}

	@Override
	public double eval(double[] x) {

		if (correspondences == null) {
			Logger.getInstance().error("Correspondences array missing!");
			return Double.POSITIVE_INFINITY;
		}

		double fx = 0;
		// Iterate of the corresponding point pairs.
		for (int i = 0; i < correspondences.length; i++) {
			int baseIndex = correspondences[i][0];
			int registerIndex = correspondences[i][1];

			fx += eval(baseIndex, registerIndex, x);
		}
		// Normalize over the number of correspondences
		return fx / (double) correspondences.length;
	}

	/**
	 * Evaluate the error function for a single correspondence.
	 */
	private double eval(int baseIndex, int registerIndex, double[] x) {
		double alpha = x[0];
		double beta = x[1];
		double gamma = x[2];
		double tx = x[3];
		double ty = x[4];
		double tz = x[5];
		IVector3 b = pointCloudBase.getPoint(baseIndex).getPosition();
		double bx = b.get(0);
		double by = b.get(1);
		double bz = b.get(2);
		IVector3 r = pointCloudRegister.getPoint(registerIndex).getPosition();
		double rx = r.get(0);
		double ry = r.get(1);
		double rz = r.get(2);

		// double fx = MathHelpers.sqr(ry
		// * cos(alpha)
		// * cos(beta)
		// * cos(gamma)
		// * sin(alpha)
		// * sin(gamma)
		// + rz
		// * cos(alpha)
		// * sin(beta)
		// + (cos(alpha) * cos(beta) * cos(gamma) - sin(alpha)
		// * sin(gamma)) * rx - bx + tx)
		// + MathHelpers.sqr(rx * cos(gamma) * sin(beta) - ry * sin(beta)
		// * sin(gamma) - rz * cos(beta) + bz - tz)
		// + MathHelpers.sqr(rz
		// * sin(alpha)
		// * sin(beta)
		// + (cos(beta) * cos(gamma) * sin(alpha) + cos(alpha)
		// * sin(gamma))
		// * rx
		// - (cos(beta) * sin(alpha) * sin(gamma) - cos(alpha)
		// * cos(gamma)) * ry - by + ty);
		double fx = MathHelpers.sqr(rz
				* cos(alpha)
				* cos(beta)
				- (cos(alpha) * cos(gamma) * sin(beta) - sin(alpha)
						* sin(gamma))
				* rx
				+ (cos(alpha) * sin(beta) * sin(gamma) + cos(gamma)
						* sin(alpha)) * ry - bz + tz)

				+ MathHelpers.sqr(rx * cos(beta) * cos(gamma) - ry * cos(beta)
						* sin(gamma) + rz * sin(beta) - bx + tx)

				+ MathHelpers.sqr(rz
						* cos(beta)
						* sin(alpha)
						- (cos(gamma) * sin(alpha) * sin(beta) + cos(alpha)
								* sin(gamma))
						* rx
						+ (sin(alpha) * sin(beta) * sin(gamma) - cos(alpha)
								* cos(gamma)) * ry + by - ty);

		return fx;
	}

	@Override
	public double[] getGradient(double[] x) {

		if (correspondences == null) {
			Logger.getInstance().error("Correspondences array missing!");
			return null;
		}

		// Check if gradient array was allocated
		if (gradient == null) {
			gradient = new double[getDimension()];
		}
		// Reset to 0
		for (int i = 0; i < getDimension(); i++) {
			gradient[i] = 0;
		}
		// Compute gradient for each corresponding pair of points
		for (int i = 0; i < correspondences.length; i++) {
			int baseIndex = correspondences[i][0];
			int registerIndex = correspondences[i][1];

			// Increment the gradient vector for each corresponding point pair.
			incGradient(baseIndex, registerIndex, x, gradient);
		}
		// Normalize gradient.
		for (int i = 0; i < getDimension(); i++) {
			gradient[i] /= (double) correspondences.length;
		}
		return gradient;
	}

	/**
	 * Compute the gradient for a corresponding point pair.
	 */
	private void incGradient(int baseIndex, int registerIndex, double[] x,
			double[] gradient) {
		double alpha = x[0];
		double beta = x[1];
		double gamma = x[2];
		double tx = x[3];
		double ty = x[4];
		double tz = x[5];
		IVector3 b = pointCloudBase.getPoint(baseIndex).getPosition();
		double bx = b.get(0);
		double by = b.get(1);
		double bz = b.get(2);
		IVector3 r = pointCloudRegister.getPoint(registerIndex).getPosition();
		double rx = r.get(0);
		double ry = r.get(1);
		double rz = r.get(2);

		gradient[0] += 2
				* (ry * MathHelpers.sqr(cos(alpha)) * cos(beta) * cos(gamma)
						* sin(gamma) - ry * cos(beta) * cos(gamma)
						* MathHelpers.sqr(sin(alpha)) * sin(gamma) - rz
						* sin(alpha) * sin(beta) - (cos(beta) * cos(gamma)
						* sin(alpha) + cos(alpha) * sin(gamma))
						* rx)
				* (ry
						* cos(alpha)
						* cos(beta)
						* cos(gamma)
						* sin(alpha)
						* sin(gamma)
						+ rz
						* cos(alpha)
						* sin(beta)
						+ (cos(alpha) * cos(beta) * cos(gamma) - sin(alpha)
								* sin(gamma)) * rx - bx + tx)
				+ 2
				* (rz
						* cos(alpha)
						* sin(beta)
						+ (cos(alpha) * cos(beta) * cos(gamma) - sin(alpha)
								* sin(gamma)) * rx - (cos(alpha) * cos(beta)
						* sin(gamma) + cos(gamma) * sin(alpha))
						* ry)
				* (rz
						* sin(alpha)
						* sin(beta)
						+ (cos(beta) * cos(gamma) * sin(alpha) + cos(alpha)
								* sin(gamma))
						* rx
						- (cos(beta) * sin(alpha) * sin(gamma) - cos(alpha)
								* cos(gamma)) * ry - by + ty);

		gradient[1] += -2
				* (ry
						* cos(alpha)
						* cos(beta)
						* cos(gamma)
						* sin(alpha)
						* sin(gamma)
						+ rz
						* cos(alpha)
						* sin(beta)
						+ (cos(alpha) * cos(beta) * cos(gamma) - sin(alpha)
								* sin(gamma)) * rx - bx + tx)
				* (ry * cos(alpha) * cos(gamma) * sin(alpha) * sin(beta)
						* sin(gamma) + rx * cos(alpha) * cos(gamma) * sin(beta) - rz
						* cos(alpha) * cos(beta))
				+ 2
				* (rx * cos(beta) * cos(gamma) - ry * cos(beta) * sin(gamma) + rz
						* sin(beta))
				* (rx * cos(gamma) * sin(beta) - ry * sin(beta) * sin(gamma)
						- rz * cos(beta) + bz - tz)
				- 2
				* (rx * cos(gamma) * sin(alpha) * sin(beta) - ry * sin(alpha)
						* sin(beta) * sin(gamma) - rz * cos(beta) * sin(alpha))
				* (rz
						* sin(alpha)
						* sin(beta)
						+ (cos(beta) * cos(gamma) * sin(alpha) + cos(alpha)
								* sin(gamma))
						* rx
						- (cos(beta) * sin(alpha) * sin(gamma) - cos(alpha)
								* cos(gamma)) * ry - by + ty);

		gradient[2] += 2
				* (ry * cos(alpha) * cos(beta) * MathHelpers.sqr(cos(gamma))
						* sin(alpha) - ry * cos(alpha) * cos(beta) * sin(alpha)
						* MathHelpers.sqr(sin(gamma)) - (cos(alpha) * cos(beta)
						* sin(gamma) + cos(gamma) * sin(alpha))
						* rx)
				* (ry
						* cos(alpha)
						* cos(beta)
						* cos(gamma)
						* sin(alpha)
						* sin(gamma)
						+ rz
						* cos(alpha)
						* sin(beta)
						+ (cos(alpha) * cos(beta) * cos(gamma) - sin(alpha)
								* sin(gamma)) * rx - bx + tx)
				- 2
				* (rx * cos(gamma) * sin(beta) - ry * sin(beta) * sin(gamma)
						- rz * cos(beta) + bz - tz)
				* (ry * cos(gamma) * sin(beta) + rx * sin(beta) * sin(gamma))
				- 2
				* (rz
						* sin(alpha)
						* sin(beta)
						+ (cos(beta) * cos(gamma) * sin(alpha) + cos(alpha)
								* sin(gamma))
						* rx
						- (cos(beta) * sin(alpha) * sin(gamma) - cos(alpha)
								* cos(gamma)) * ry - by + ty)
				* ((cos(beta) * sin(alpha) * sin(gamma) - cos(alpha)
						* cos(gamma))
						* rx + (cos(beta) * cos(gamma) * sin(alpha) + cos(alpha)
						* sin(gamma))
						* ry);

		gradient[3] += 2
				* ry
				* cos(alpha)
				* cos(beta)
				* cos(gamma)
				* sin(alpha)
				* sin(gamma)
				+ 2
				* rz
				* cos(alpha)
				* sin(beta)
				+ 2
				* (cos(alpha) * cos(beta) * cos(gamma) - sin(alpha)
						* sin(gamma)) * rx - 2 * bx + 2 * tx;

		gradient[4] += 2
				* rz
				* sin(alpha)
				* sin(beta)
				+ 2
				* (cos(beta) * cos(gamma) * sin(alpha) + cos(alpha)
						* sin(gamma))
				* rx
				- 2
				* (cos(beta) * sin(alpha) * sin(gamma) - cos(alpha)
						* cos(gamma)) * ry - 2 * by + 2 * ty;

		gradient[5] += -2 * rx * cos(gamma) * sin(beta) + 2 * ry * sin(beta)
				* sin(gamma) + 2 * rz * cos(beta) - 2 * bz + 2 * tz;

//		gradient[0] = 2
//				* (rz
//						* cos(alpha)
//						* cos(beta)
//						- (cos(alpha) * cos(gamma) * sin(beta) - sin(alpha)
//								* sin(gamma)) * rx + (cos(alpha) * sin(beta)
//						* sin(gamma) + cos(gamma) * sin(alpha))
//						* ry)
//				* (rz
//						* cos(beta)
//						* sin(alpha)
//						- (cos(gamma) * sin(alpha) * sin(beta) + cos(alpha)
//								* sin(gamma))
//						* rx
//						+ (sin(alpha) * sin(beta) * sin(gamma) - cos(alpha)
//								* cos(gamma)) * ry + by - ty)
//				- 2
//				* (rz
//						* cos(alpha)
//						* cos(beta)
//						- (cos(alpha) * cos(gamma) * sin(beta) - sin(alpha)
//								* sin(gamma))
//						* rx
//						+ (cos(alpha) * sin(beta) * sin(gamma) + cos(gamma)
//								* sin(alpha)) * ry - bz + tz)
//				* (rz
//						* cos(beta)
//						* sin(alpha)
//						- (cos(gamma) * sin(alpha) * sin(beta) + cos(alpha)
//								* sin(gamma)) * rx + (sin(alpha) * sin(beta)
//						* sin(gamma) - cos(alpha) * cos(gamma))
//						* ry);
//		gradient[1] = -2
//				* (rx * cos(alpha) * cos(beta) * cos(gamma) - ry * cos(alpha)
//						* cos(beta) * sin(gamma) + rz * cos(alpha) * sin(beta))
//				* (rz
//						* cos(alpha)
//						* cos(beta)
//						- (cos(alpha) * cos(gamma) * sin(beta) - sin(alpha)
//								* sin(gamma))
//						* rx
//						+ (cos(alpha) * sin(beta) * sin(gamma) + cos(gamma)
//								* sin(alpha)) * ry - bz + tz)
//				- 2
//				* (rx * cos(beta) * cos(gamma) * sin(alpha) - ry * cos(beta)
//						* sin(alpha) * sin(gamma) + rz * sin(alpha) * sin(beta))
//				* (rz
//						* cos(beta)
//						* sin(alpha)
//						- (cos(gamma) * sin(alpha) * sin(beta) + cos(alpha)
//								* sin(gamma))
//						* rx
//						+ (sin(alpha) * sin(beta) * sin(gamma) - cos(alpha)
//								* cos(gamma)) * ry + by - ty)
//				- 2
//				* (rx * cos(beta) * cos(gamma) - ry * cos(beta) * sin(gamma)
//						+ rz * sin(beta) - bx + tx)
//				* (rx * cos(gamma) * sin(beta) - ry * sin(beta) * sin(gamma) - rz
//						* cos(beta));
//		gradient[2] = -2
//				* (rx * cos(beta) * cos(gamma) - ry * cos(beta) * sin(gamma)
//						+ rz * sin(beta) - bx + tx)
//				* (ry * cos(beta) * cos(gamma) + rx * cos(beta) * sin(gamma))
//				+ 2
//				* (rz
//						* cos(alpha)
//						* cos(beta)
//						- (cos(alpha) * cos(gamma) * sin(beta) - sin(alpha)
//								* sin(gamma))
//						* rx
//						+ (cos(alpha) * sin(beta) * sin(gamma) + cos(gamma)
//								* sin(alpha)) * ry - bz + tz)
//				* ((cos(alpha) * sin(beta) * sin(gamma) + cos(gamma)
//						* sin(alpha))
//						* rx + (cos(alpha) * cos(gamma) * sin(beta) - sin(alpha)
//						* sin(gamma))
//						* ry)
//				+ 2
//				* (rz
//						* cos(beta)
//						* sin(alpha)
//						- (cos(gamma) * sin(alpha) * sin(beta) + cos(alpha)
//								* sin(gamma))
//						* rx
//						+ (sin(alpha) * sin(beta) * sin(gamma) - cos(alpha)
//								* cos(gamma)) * ry + by - ty)
//				* ((sin(alpha) * sin(beta) * sin(gamma) - cos(alpha)
//						* cos(gamma))
//						* rx + (cos(gamma) * sin(alpha) * sin(beta) + cos(alpha)
//						* sin(gamma))
//						* ry);
//		gradient[3] = 2 * rx * cos(beta) * cos(gamma) - 2 * ry * cos(beta)
//				* sin(gamma) + 2 * rz * sin(beta) - 2 * bx + 2 * tx;
//		gradient[4] = -2
//				* rz
//				* cos(beta)
//				* sin(alpha)
//				+ 2
//				* (cos(gamma) * sin(alpha) * sin(beta) + cos(alpha)
//						* sin(gamma))
//				* rx
//				- 2
//				* (sin(alpha) * sin(beta) * sin(gamma) - cos(alpha)
//						* cos(gamma)) * ry - 2 * by + 2 * ty;
//		gradient[5] = 2
//				* rz
//				* cos(alpha)
//				* cos(beta)
//				- 2
//				* (cos(alpha) * cos(gamma) * sin(beta) - sin(alpha)
//						* sin(gamma))
//				* rx
//				+ 2
//				* (cos(alpha) * sin(beta) * sin(gamma) + cos(gamma)
//						* sin(alpha)) * ry - 2 * bz + 2 * tz;

		gradient[3] = gradient[4] = gradient[5] = 0;
	}

	private double cos(double x) {
		return Math.cos(x);
	}

	private double sin(double x) {
		return Math.sin(x);
	}
}

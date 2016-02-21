package cgresearch.studentprojects.scanner.calibration;

import cgresearch.core.math.Matrix;
import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;

/**
 * Container for the scanner registration information.
 * 
 * @author Philipp Jenke
 *
 */
public class ScannerRegistrationInformation {

	/**
	 * Origin point of the sensor, distances in [m]
	 */
	public Vector dS = VectorFactory.createVector3(-9.322712285094652E-5,
			0, -0.2607322177580369);
	// public Vector dS = VectorMatrixFactory.newVector(0, 0, 0);

	/**
	 * Coordinate system of the sensor.
	 */
	private Matrix RS = MatrixFactory.createMatrix3(1, 0, 0, 0, 1, 0, 0,
			0, 1);

	/**
	 * Origin point of the sensor, distances in [m]
	 */
	private Vector dD = VectorFactory.createVector3(0, 0, 0);

	/**
	 * Coordinate system of the rotation platform.
	 */
	private Matrix RD = MatrixFactory.createMatrix3(1, 0, 0, 0, 1, 0, 0,
			0, 1);

	/**
	 * Helper matrix
	 */
	private Matrix invRD;

	/**
	 * Constructor.
	 */
	public ScannerRegistrationInformation() {
		// TODO: only true for unit matrix!
		invRD = RD;
	}

	/**
	 * Convert a measurement to the world coordinate system given the current
	 * registration information.
	 */
	public Vector convert2WorldCoordinates(ScannerMeasurement measurement) {

		Vector pMeasurement = VectorFactory.createVector3(0,
				measurement.height, measurement.distance);

		// return pMeasurement;

		Matrix rotationPlateRotation = MatrixFactory.createRotationMatrix(
				VectorFactory.createVector3(0, 1, 0), measurement.angle);
		Vector pEukledian = RS.multiply(pMeasurement).add(dS);
		Vector pWorld = rotationPlateRotation.multiply(invRD
				.multiply(pEukledian.subtract(dD)));
		return pWorld;
	}

	/**
	 * Special version of the function, which allows to used different
	 * parameters for the origin of the sensor (x and z coordinate).
	 */
	public Vector convert2WorldCoordinates(ScannerMeasurement measurement,
			double tdx, double tdz) {
		Matrix R = MatrixFactory.createRotationMatrix(
				VectorFactory.createVector3(0, 1, 0), measurement.angle);
		Vector p = R.multiply(VectorFactory.createVector3(0,
				measurement.height, measurement.distance).add(
				VectorFactory.createVector3(tdx, 0, tdz)));
		return p;
	}
}

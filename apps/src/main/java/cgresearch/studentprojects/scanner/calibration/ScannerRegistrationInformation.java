package cgresearch.studentprojects.scanner.calibration;

import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

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
	public IVector3 dS = VectorMatrixFactory.newIVector3(-9.322712285094652E-5,
			0, -0.2607322177580369);
	// public IVector3 dS = VectorMatrixFactory.newIVector3(0, 0, 0);

	/**
	 * Coordinate system of the sensor.
	 */
	private IMatrix3 RS = VectorMatrixFactory.newIMatrix3(1, 0, 0, 0, 1, 0, 0,
			0, 1);

	/**
	 * Origin point of the sensor, distances in [m]
	 */
	private IVector3 dD = VectorMatrixFactory.newIVector3(0, 0, 0);

	/**
	 * Coordinate system of the rotation platform.
	 */
	private IMatrix3 RD = VectorMatrixFactory.newIMatrix3(1, 0, 0, 0, 1, 0, 0,
			0, 1);

	/**
	 * Helper matrix
	 */
	private IMatrix3 invRD;

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
	public IVector3 convert2WorldCoordinates(ScannerMeasurement measurement) {

		IVector3 pMeasurement = VectorMatrixFactory.newIVector3(0,
				measurement.height, measurement.distance);

		// return pMeasurement;

		IMatrix3 rotationPlateRotation = VectorMatrixFactory.getRotationMatrix(
				VectorMatrixFactory.newIVector3(0, 1, 0), measurement.angle);
		IVector3 pEukledian = RS.multiply(pMeasurement).add(dS);
		IVector3 pWorld = rotationPlateRotation.multiply(invRD
				.multiply(pEukledian.subtract(dD)));
		return pWorld;
	}

	/**
	 * Special version of the function, which allows to used different
	 * parameters for the origin of the sensor (x and z coordinate).
	 */
	public IVector3 convert2WorldCoordinates(ScannerMeasurement measurement,
			double tdx, double tdz) {
		IMatrix3 R = VectorMatrixFactory.getRotationMatrix(
				VectorMatrixFactory.newIVector3(0, 1, 0), measurement.angle);
		IVector3 p = R.multiply(VectorMatrixFactory.newIVector3(0,
				measurement.height, measurement.distance).add(
				VectorMatrixFactory.newIVector3(tdx, 0, tdz)));
		return p;
	}
}

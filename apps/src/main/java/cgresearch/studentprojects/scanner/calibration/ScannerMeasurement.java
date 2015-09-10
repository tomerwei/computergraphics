package cgresearch.studentprojects.scanner.calibration;

/**
 * Representation of a single measurement.
 * 
 * @author Philipp Jenke
 *
 */
public class ScannerMeasurement {

	public ScannerMeasurement(double distance, double angle, double height) {
		this.distance = distance;
		this.angle = angle;
		this.height = height;
	}

	/**
	 * Measured distance value in [m]
	 */
	public double distance = 0;

	/**
	 * Current angle of the rotation plate in radiens [0;2PI]
	 */
	public double angle = 0;

	/**
	 * Current height of the sensor in [m]
	 */
	public double height;

	@Override
	public String toString() {
		return String.format("(%.5f, %.5f, %.5f", distance, angle, height);
	}

}

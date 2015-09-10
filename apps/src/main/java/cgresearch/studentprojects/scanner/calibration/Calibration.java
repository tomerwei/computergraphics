package cgresearch.studentprojects.scanner.calibration;

import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.NormalEstimator;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.datastructures.primitives.Cylinder;
import cgresearch.graphics.datastructures.primitives.IPrimitive;
import cgresearch.graphics.fileio.AsciiPointFormat;
import cgresearch.graphics.fileio.AsciiPointsWriter;
import cgresearch.graphics.fileio.BinaryPointsWriter;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * This class contains the functionality to calibrate the scanner.
 * 
 * @author Philipp Jenke
 *
 */
public class Calibration {

	/**
	 * Registration information.
	 */
	public ScannerRegistrationInformation registrationData = new ScannerRegistrationInformation();

	/**
	 * Step size for calibration optimization
	 */
	public double h = 1E-3;

	/**
	 * Calibration object
	 */
	public Cylinder calibrationObject = new Cylinder(
			VectorMatrixFactory.newIVector3(0, 0, 0),
			VectorMatrixFactory.newIVector3(0, 1, 0), 0.065 / (2.0));

	/**
	 * List of measurements.
	 */
	private List<ScannerMeasurement> measurements = null;

	/**
	 * Point cloud with the data given the current registration.
	 */
	private IPointCloud pointCloud = new PointCloud();

	/**
	 * Load a dataset with measurements.
	 */
	public void loadDataset(String filename) {
		ScannerDataReader reader = new ScannerDataReader();
		measurements = reader.readFromFile(filename, 0.4);
	}

	/**
	 * Create the registered point cloud from the measurements given the current
	 * registration information.
	 */
	public void createRegisteredPointCloud() {
		pointCloud.clear();

		for (int i = 0; i < measurements.size(); i++) {
			IVector3 color = Material.PALETTE0_COLOR0;
			ScannerMeasurement measurement = measurements.get(i);
			IVector3 pos = registrationData
					.convert2WorldCoordinates(measurement);
			IVector3 normal = VectorMatrixFactory.newIVector3(pos);
			normal.set(1, 0);
			normal.normalize();
			Point point = new Point(pos, color, normal);
			pointCloud.addPoint(point);
		}

		// Estimate normals
		NormalEstimator.estimate(pointCloud, 50);

		// Export ASCII
		AsciiPointsWriter exporter = new AsciiPointsWriter();
		exporter.writeToFile("reconstruction_export.ascii", pointCloud,
				new AsciiPointFormat().setPosition(0, 1, 2).setNormal(3, 4, 5)
						.setSeparator(" "));

		// Export binary
		BinaryPointsWriter exporterBinary = new BinaryPointsWriter();
		exporterBinary.write("reconstruction_export.bnpts", pointCloud);

		pointCloud.updateRenderStructures();
	}

	/**
	 * Getter.
	 */
	public ICgNodeContent getPointCloud() {
		return pointCloud;
	}

	/**
	 * Getter.
	 */
	public IPrimitive getCalibrationObject() {
		return calibrationObject;
	}

	/**
	 * Evaluate the registration energy function.
	 */
	public double evaluateEnergyFunction() {
		if (measurements == null) {
			return -1;
		}

		double f = 0;
		for (ScannerMeasurement measurement : measurements) {
			f += MathHelpers.sqr(calibrationObject.getDistance(registrationData
					.convert2WorldCoordinates(measurement)));
		}
		f /= measurements.size();
		return f;
	}

	public double evaluateEnergyFunction(double tdX, double tdZ, double cx,
			double cz) {
		if (measurements == null) {
			return -1;
		}

		double f = 0;
		for (ScannerMeasurement measurement : measurements) {
			f += MathHelpers.sqr(calibrationObject.getDistance(
					VectorMatrixFactory.newIVector3(cx, 0, cz),
					registrationData.convert2WorldCoordinates(measurement, tdX,
							tdZ)));
		}
		f /= measurements.size();
		return f;
	}

	public double[] computeGradient(double h) {
		double[] gradient = { 0, 0, 0, 0 };

		double x = registrationData.dS.get(0);
		double xh = registrationData.dS.get(0) + h;
		double y = registrationData.dS.get(2);
		double yh = registrationData.dS.get(2) + h;
		double z = calibrationObject.getPoint().get(0);
		double zh = calibrationObject.getPoint().get(0) + h;
		double w = calibrationObject.getPoint().get(2);
		double wh = calibrationObject.getPoint().get(2) + h;

		gradient[0] = (evaluateEnergyFunction(xh, y, z, w) - evaluateEnergyFunction(
				x, y, z, w)) / h;
		gradient[1] = (evaluateEnergyFunction(x, yh, z, w) - evaluateEnergyFunction(
				x, y, z, w)) / h;
		gradient[2] = (evaluateEnergyFunction(x, y, zh, w) - evaluateEnergyFunction(
				x, y, z, w)) / h;
		gradient[3] = (evaluateEnergyFunction(x, y, z, wh) - evaluateEnergyFunction(
				x, y, z, w)) / h;
		return gradient;
	}

	public void optimzationStep() {
		// -9.322712285094652E-5, -0.2607322177580369, -0.010599225513340117,
		// -0.004727994334856023
		// Fehler: 7.064131742787545E-7

		int numberOfSteps = 200;
		double x = 0, y = 0, z = 0, w = 0;
		for (int i = 0; i < numberOfSteps; i++) {
			double[] gradient = computeGradient(h);
			x = registrationData.dS.get(0) - gradient[0] * h;
			y = registrationData.dS.get(2) - gradient[1] * h;
			z = calibrationObject.getPoint().get(0) - gradient[2] * h;
			w = calibrationObject.getPoint().get(2) - gradient[3] * h;
			registrationData.dS.set(0, x);
			registrationData.dS.set(2, y);
			calibrationObject.getPoint().set(0, z);
			calibrationObject.getPoint().set(2, w);
		}
		Logger.getInstance().message("Energy: " + evaluateEnergyFunction());
		Logger.getInstance().message(x + ", " + y + ", " + z + ", " + w);
		createRegisteredPointCloud();
	}
}

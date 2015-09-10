package cgresearch.graphics.fileio;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;

/**
 * Read point data from an ASCII file.
 * 
 * @author Philipp Jenke
 *
 */
public class AsciiPointsReader {

	/**
	 * Temp container for double values.
	 */
	private double[] doubleValues = new double[20];

	/**
	 * Format of the point information.
	 */
	private AsciiPointFormat format = null;

	/**
	 * Constructor.
	 */
	public AsciiPointsReader() {
	}

	/**
	 * Read a point cloud from a file.
	 */
	public IPointCloud readFromFile(String filename, AsciiPointFormat format) {

		this.format = format;
		String absoluteFilename = ResourcesLocator.getInstance()
				.getPathToResource(filename);
		if (absoluteFilename == null || absoluteFilename.length() == 0) {
			Logger.getInstance().error("AsciiPointsReader: invalid filename");
			return null;
		}

		IPointCloud pointCloud = new PointCloud();

		String strLine;
		try {
			InputStream is = new FileInputStream(absoluteFilename);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				Point point = parseLine(strLine);
				if (point != null) {
					pointCloud.addPoint(point);
				}
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {
			Logger.getInstance().exception(
					"Failed to read mesh from ASCII data file", e);
			return null;
		}

		Logger.getInstance().message(
				"Read point cloud from ASCII with "
						+ pointCloud.getNumberOfPoints() + " points.");
		return pointCloud;
	}

	/**
	 * Parse a line from the ascii file.
	 */
	private Point parseLine(String strLine) {
		String[] tokens = strLine.trim().split(format.getSeparator());
		int numberOfValues = 0;
		for (String token : tokens) {
			try {
				double doubleValue = Double
						.parseDouble(token.replace(',', '.'));
				doubleValues[numberOfValues] = doubleValue;
				numberOfValues++;
			} catch (NumberFormatException e) {
				// Ignore this token
			}
		}

		Point point = new Point();
		if (!setPosition(point, numberOfValues)) {
			return null;
		}
		setNormal(point, numberOfValues);
		setColor(point, numberOfValues);
		return point;
	}

	/**
	 * Set the point information from the double values given the current format
	 */
	private void setColor(Point point, int numberOfValues) {
		for (int i = 0; i < 3; i++) {
			if (format.getColorIndex(i) < 0
					|| format.getColorIndex(i) >= numberOfValues) {
				return;
			}
		}
		for (int i = 0; i < 3; i++) {
			point.getColor().set(
					i,
					doubleValues[format.getColorIndex(i)]
							* format.getColorScale());
		}
	}

	/**
	 * Set the point information from the double values given the current format
	 */
	private void setNormal(Point point, int numberOfValues) {
		for (int i = 0; i < 3; i++) {
			if (format.getNormalIndex(i) < 0
					|| format.getNormalIndex(i) >= numberOfValues) {
				return;
			}
		}
		for (int i = 0; i < 3; i++) {
			point.getNormal().set(i, doubleValues[format.getNormalIndex(i)]);
		}
	}

	/**
	 * Set the point information from the double values given the current
	 * format. Return true if the position could be read, false otherwise.
	 */
	private boolean setPosition(Point point, int numberOfValues) {
		for (int i = 0; i < 3; i++) {
			if (format.getPositionIndex(i) < 0
					|| format.getPositionIndex(i) >= numberOfValues) {
				return false;
			}
		}
		for (int i = 0; i < 3; i++) {
			point.getPosition()
					.set(i, doubleValues[format.getPositionIndex(i)]);
		}
		return true;
	}
}

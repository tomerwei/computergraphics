package cgresearch.studentprojects.scanner.calibration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;

/**
 * Read point data from an ASCII file.
 * 
 * @author Philipp Jenke
 *
 */
public class ScannerDataReader {
	/**
	 * Constructor.
	 */
	public ScannerDataReader() {
	}

	/**
	 * Read a point cloud from a file.
	 */
	public List<ScannerMeasurement> readFromFile(String filename,
			double maxDistance) {

		String absoluteFilename = ResourcesLocator.getInstance()
				.getPathToResource(filename);
		if (absoluteFilename == null || absoluteFilename.length() == 0) {
			Logger.getInstance().error("AsciiPointsReader: invalid filename");
			return null;
		}

		List<ScannerMeasurement> measurements = new ArrayList<ScannerMeasurement>();

		String strLine;
		try {
			InputStream is = new FileInputStream(absoluteFilename);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				ScannerMeasurement measurement = parseMeasurement(strLine,
						maxDistance);
				if (measurement != null) {
					measurements.add(measurement);
				}

			}
			// Close the input stream
			in.close();
		} catch (Exception e) {
			Logger.getInstance().exception("Failed to read scaner raw data", e);
			return null;
		}

		Logger.getInstance().message(
				"Read scanner dataset with " + measurements.size()
						+ " measurements");

		return measurements;
	}

	private ScannerMeasurement parseMeasurement(String strLine,
			double maxDistance) {

		String line = strLine.trim();
		String[] tokens = line.split("\\s+");
		if (tokens.length != 3) {
			return null;
		}
		try {
			// TODO: Offset required?
			double distance = Double.parseDouble(tokens[0]) + 0.2554;

			if (distance > maxDistance) {
				return null;
			}

			double angle = Double.parseDouble(tokens[1]) / 360.0 * 2 * Math.PI;
			double height = Double.parseDouble(tokens[2]);
			ScannerMeasurement m = new ScannerMeasurement(distance, angle,
					height);
			return m;
		} catch (Exception e) {
			return null;
		}
	}
}

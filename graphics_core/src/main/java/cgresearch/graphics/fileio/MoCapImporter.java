package cgresearch.graphics.fileio;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.motioncapture.MotionCaptureFrame;
import cgresearch.graphics.datastructures.motioncapture.MotionCaptureMeasurement;
import cgresearch.graphics.datastructures.motioncapture.MotionCaptureTopology;
import cgresearch.graphics.scenegraph.Animation;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Imports a motion capture file.
 * 
 * @author Philipp Jenke
 *
 */
public class MoCapImporter {

	/**
	 * Constants in the mocap file.
	 */
	private static final String COMMAND_FR = "fr";
	private static final String COMMAND_TS = "ts";

	/**
	 * State of the parser: current frame number
	 */
	int currentFrameNumber = -1;

	/**
	 * State of the parser: current time stamp
	 */
	double currentTimeStamp = -1;

	/**
	 * Constructor.
	 */
	public MoCapImporter() {

	}

	/**
	 * Parse a motion capture dataset and save the content in a list of frame
	 * nodes, in a parent animation node.
	 */
	public CgNode readFromFile(String filename) {
		return readFromFile(filename, null);
	}

	/**
	 * Parse a motion capture dataset and save the content in a list of frame
	 * nodes, in a parent animation node. Provide topology.
	 */
	public CgNode readFromFile(String filename, MotionCaptureTopology topology) {

		String absoluteFilename = ResourcesLocator.getInstance()
				.getPathToResource(filename);
		if (absoluteFilename == null || absoluteFilename.length() == 0) {
			Logger.getInstance().error("MoCapImporter: invalid filename");
			return null;
		}

		CgNode motionCaptureNode = new CgNode(new Animation(),
				"motion caption data");

		String strLine;
		try {
			InputStream is = new FileInputStream(absoluteFilename);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				Object result = parseLine(strLine);
				if (result != null && result instanceof MotionCaptureFrame) {
					MotionCaptureFrame frame = (MotionCaptureFrame) result;
					if (topology != null) {
						frame.setTopoloyg(topology);
					}
					motionCaptureNode.addChild(new CgNode(frame, "mocap frame #"
							+ motionCaptureNode.getNumChildren()));
				}
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {
			Logger.getInstance().exception(
					"Failed to read mesh from MoCap data file", e);
		}

		return motionCaptureNode;
	}

	/**
	 * Parse a line in the input file.
	 */
	private Object parseLine(String strLine) {
		strLine = strLine.trim();
		if (strLine.startsWith(COMMAND_FR)) {
			try {
				currentFrameNumber = Integer.parseInt(strLine.substring(2)
						.trim());
			} catch (NumberFormatException e) {
				Logger.getInstance().exception("Failed to parse frame number.",
						e);
			}
			return null;
		} else if (strLine.startsWith(COMMAND_TS)) {
			try {
				currentTimeStamp = Double.parseDouble(strLine.substring(2)
						.trim());
			} catch (NumberFormatException e) {
				Logger.getInstance()
						.exception("Failed to parse time stamp.", e);
			}
			return null;
		} else if (strLine.contains("[")) {
			MotionCaptureFrame frame = parseFrame(strLine);
			return frame;
		}

		return null;
	}

	/**
	 * Parse a line in the input file which represents a frame information.
	 */
	private MotionCaptureFrame parseFrame(String strLine) {
		strLine = strLine.trim();
		String termPattern = "\\[[\\d\\.\\s-]*\\]";
		Pattern pattern = Pattern.compile(termPattern + termPattern
				+ termPattern);
		Matcher matcher = pattern.matcher(strLine);
		MotionCaptureFrame frame = new MotionCaptureFrame();
		while (matcher.find()) {
			MotionCaptureMeasurement measurement = parseMarker(matcher.group());
			if (measurement != null) {
				frame.addMeasurement(measurement);
			}
		}
		return frame;
	}

	/**
	 * Parse a marker
	 * 
	 * @return A single measurement for the marker.
	 */
	private MotionCaptureMeasurement parseMarker(String markerMatch) {
		String patternTerm = "\\[([\\d\\.\\s-]*)\\]";
		Pattern pattern = Pattern.compile(patternTerm + patternTerm
				+ patternTerm);
		Matcher matcher = pattern.matcher(markerMatch);
		matcher.find();
		String idToken = matcher.group(1).trim();
		String translationToken = matcher.group(2).trim();
		String rotationToken = matcher.group(3).trim();
		String[] idTokens = idToken.split("\\s+");
		String[] translationTokens = translationToken.split("\\s+");
		String[] rotationTokens = rotationToken.split("\\s+");
		String id = idTokens[0];
		IVector3 position = VectorMatrixFactory.newIVector3(
				Double.parseDouble(translationTokens[0]),
				Double.parseDouble(translationTokens[1]),
				Double.parseDouble(translationTokens[2]));
		IMatrix3 oriantation = VectorMatrixFactory.newIMatrix3(
				Double.parseDouble(rotationTokens[0]),
				Double.parseDouble(rotationTokens[1]),
				Double.parseDouble(rotationTokens[2]),
				Double.parseDouble(rotationTokens[3]),
				Double.parseDouble(rotationTokens[4]),
				Double.parseDouble(rotationTokens[5]),
				Double.parseDouble(rotationTokens[6]),
				Double.parseDouble(rotationTokens[7]),
				Double.parseDouble(rotationTokens[8]));
		return new MotionCaptureMeasurement(id, position, oriantation);
	}
}

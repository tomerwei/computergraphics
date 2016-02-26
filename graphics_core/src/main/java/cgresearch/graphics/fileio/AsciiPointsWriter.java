package cgresearch.graphics.fileio;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;

/**
 * Writer for point clouds to ASCII (text) files.
 * 
 * @author Philipp Jenke
 *
 */
public class AsciiPointsWriter {
	/**
	 * 
	 * @param filename
	 *            Filename to write to.
	 * @param pointCloud
	 *            Point cloud information to be written.
	 * @param format
	 *            Descriptor for the output format.
	 */
	public void writeToFile(String filename, IPointCloud pointCloud,
			AsciiPointFormat format) {

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filename));

			writeHeader(writer, format);

			for (int pointIndex = 0; pointIndex < pointCloud
					.getNumberOfPoints(); pointIndex++) {
				writePoint(writer, format, pointCloud.getPoint(pointIndex));
			}

			writer.close();

		} catch (Exception e) {
			Logger.getInstance().exception("Failed to write OBJ file", e);
		}
		Logger.getInstance().message(
				"Successfully wrote point cloud with "
						+ pointCloud.getNumberOfPoints()
						+ " points to ASCII file " + filename + ".");
	}

	/**
	 * Create a header for the ASCII file.
	 * 
	 * @param writer
	 * @param format
	 * @throws IOException
	 */
	private void writeHeader(BufferedWriter writer, AsciiPointFormat format)
			throws IOException {
		writer.write("# Point cloud data.\n");
		writer.write("# File created by cg software.\n");
		writer.write("# (C) Hochschule für Angewandte Wissenschaften, Hamburg.\n");
		writer.write("#\n");
		writer.write("# Format: ");
		int numberOfTokens = format.getNumberOfTokens();
		for (int index = 0; index < numberOfTokens; index++) {
			writer.write(format.getTokenName(index));
			if (index != numberOfTokens - 1) {
				writer.write(format.getSeparator());
			}
		}
		writer.write("\n");
		writer.write("#\n");
	}

	/**
	 * Write a point to a file in the writer.
	 * 
	 * @param point
	 *            Point to be written
	 * @throws Exception
	 */
	private void writePoint(BufferedWriter writer, AsciiPointFormat format,
			Point point) throws Exception {
		int numberOfTokens = format.getNumberOfTokens();
		for (int i = 0; i < numberOfTokens; i++) {
			String token = getToken(i, format, point);
			writer.write(token);
			if (i != numberOfTokens - 1) {
				writer.write(format.getSeparator());
			}
		}
		writer.write("\n");

	}

	/**
	 * Return the token (value) specified by the given index in the given
	 * format.
	 * 
	 * The current implementation is very nasty with a huge if-cascade - better
	 * ideas?
	 * 
	 * @throws Exception
	 */
	private String getToken(int index, AsciiPointFormat format, Point point)
			throws Exception {
		double value = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < 3; i++) {
			if (format.getPositionIndex(i) == index) {
				value = point.getPosition().get(i);
			} else if (format.getColorIndex(i) == index) {
				value = point.getColor().get(i);
			} else if (format.getNormalIndex(i) == index) {
				value = point.getNormal().get(i);
			}
		}
		if (value != Double.NEGATIVE_INFINITY) {
			return String.format("%f", value);
		} else {
			throw new Exception("Token for index " + index + " not found.");
		}
	}
}

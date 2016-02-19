package cgresearch.graphics.fileio;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;

public class BinaryPointsWriter {

	/**
	 * Write point cloud to a file in a binary fashion. For each point the
	 * following information is written: position (x,y,z) and normal (x,y,z).
	 * Each component is saved as 32bit floating point.
	 * 
	 * @param pointCloud
	 * @param filename
	 */
	public void write(String filename, IPointCloud pointCloud) {

		if (pointCloud == null) {
			Logger.getInstance().error("Invalid input point cloud.");
			return;
		}

		DataOutputStream os;
		try {
			os = new DataOutputStream(new FileOutputStream(filename));
			for (int pointIndex = 0; pointIndex < pointCloud
					.getNumberOfPoints(); pointIndex++) {
				Point point = pointCloud.getPoint(pointIndex);
				Vector pos = point.getPosition();
				Vector normal = point.getNormal();
				float value = (float) pos.get(0);
				os.writeFloat(value);
				value = (float) pos.get(1);
				os.writeFloat(value);
				value = (float) pos.get(2);
				os.writeFloat(value);
				value = (float) normal.get(0);
				os.writeFloat(value);
				value = (float) normal.get(1);
				os.writeFloat(value);
				value = (float) normal.get(2);
				os.writeFloat(value);
			}
			os.close();
		} catch (FileNotFoundException e) {
			Logger.getInstance().exception(
					"Failed to write point cloud to binary file.", e);
			return;
		} catch (IOException e) {
			Logger.getInstance().exception(
					"Failed to write point cloud to binary file.", e);
			return;
		}

		Logger.getInstance().message(
				"Successfully wrote point cloud with "
						+ pointCloud.getNumberOfPoints() + " to binary file "
						+ filename);
	}
}

package cgresearch.studentprojects.urbanreconstruction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;

public class PlyConverter {

	private IPointCloud pointCloud;

	public PlyConverter(File ply) {
		convert(ply);

	}

	public IPointCloud getPointCloud() {
		return pointCloud;
	}

	public void convert(File ply) {
		String line = "";
		List<String> points = new LinkedList<String>();
		// File ply = new File("pmvs_options.txt.ply");

		try {
			BufferedReader br = new BufferedReader(new FileReader(ply));

			do {
				line = br.readLine();
			} while (!line.equals("end_header"));

			while (line != null) {
				line = br.readLine();
				if (line != null)
					points.add(line);
			}

			br.close();

		} catch (FileNotFoundException e) {
			Logger.getInstance().exception("Failed to read PLY mesh", e);
		} catch (IOException e) {
			Logger.getInstance().exception("Failed to read PLY mesh", e);
		}

		double[] point = new double[9];

		int NUMBER_OF_POINTS = points.size();
		IPointCloud pointCloud = new PointCloud();
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			divideString(point, points.get(i));
			Vector position = VectorFactory.createVector3(point[0],
					point[1], point[2]);
			Vector color = VectorFactory.createVector3(point[6] / 255,
					point[7] / 255, point[8] / 255);

			Vector normal = VectorFactory.createVector3(point[3],
					point[4], point[5]);
			pointCloud.addPoint(new Point(position, color, normal));
		}

		this.pointCloud = pointCloud;
	}

	private void divideString(double[] point, String s) {

		String[] sub = s.split(" ");
		for (int i = 0; i < sub.length; i++) {
			point[i] = Double.parseDouble(sub[i]);
		}

	}

}

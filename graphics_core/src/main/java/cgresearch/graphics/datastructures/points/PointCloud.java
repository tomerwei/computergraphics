/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.points;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.BoundingBox;

/**
 * Representation of point data in 3-space.
 * 
 * @author Philipp Jenke
 * 
 */
public class PointCloud extends IPointCloud {

	/**
	 * List of represented points.
	 */
	private List<Point> points = new ArrayList<Point>();

	/**
	 * Constructor.
	 */
	public PointCloud() {

	}

	/**
	 * Getter.
	 */
	public int getNumberOfPoints() {
		return points.size();
	}

	/**
	 * Getter.
	 */
	public Point getPoint(int index) {
		return points.get(index);
	}

	/**
	 * Add a point to the data structure.
	 * 
	 * @param point
	 *            New point.
	 */
	public void addPoint(Point point) {
		points.add(point);
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.datastructures.IBoundingBoxed#getBoundingBox()
	 */
	@Override
	public BoundingBox getBoundingBox() {
		BoundingBox bbox = new BoundingBox();
		for (Point point : points) {
			bbox.add(point.getPosition());
		}
		return bbox;
	}

	@Override
	public void clear() {
		points.clear();
	}

	@Override
	public IPointCloud clone() {
		IPointCloud clone = new PointCloud();
		for (Point p : points) {
			clone.addPoint(p.clone());
		}
		return clone;
	}

}

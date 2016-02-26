/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.points;

import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * Interface for all point clouds.
 * 
 * @author Philipp Jenke
 * 
 */
public abstract class IPointCloud extends ICgNodeContent {

	/**
	 * Getter.
	 */
	public abstract int getNumberOfPoints();

	/**
	 * Getter.
	 */
	public abstract Point getPoint(int index);

	/**
	 * Add a point to the data structure.
	 * 
	 * @param point
	 *            New point.
	 */
	public abstract void addPoint(Point point);

	/**
	 * Remove all points.
	 */
	public abstract void clear();

	/**
	 * Create a cloned version.
	 */
	public abstract IPointCloud clone();
}

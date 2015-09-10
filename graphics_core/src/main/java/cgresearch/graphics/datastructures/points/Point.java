/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.points;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Representation of a point in a @see PointCloud.
 * 
 * @author Philipp Jenke
 * 
 */
public class Point {

	/**
	 * Position of the point in 3-space.
	 */
	private IVector3 position = VectorMatrixFactory.newIVector3();

	/**
	 * Color of the point.
	 */
	private IVector3 color = VectorMatrixFactory.newIVector3();

	/**
	 * Point normal
	 */
	private IVector3 normal = VectorMatrixFactory.newIVector3();

	/**
	 * Texture coordinate
	 */
	private IVector3 texCoord = VectorMatrixFactory.newIVector3();

	/**
	 * Constructor.
	 * 
	 * @param position
	 *            Initial value for the position.
	 */
	public Point(IVector3 position, IVector3 color, IVector3 normal) {
		this.position.copy(position);
		this.color.copy(color);
		this.normal.copy(normal);
	}

	/**
	 * Constructor.
	 */
	public Point(IVector3 position) {
		this(position, VectorMatrixFactory.newIVector3(), VectorMatrixFactory
				.newIVector3());
	}

	/**
	 * Constructor.
	 */
	public Point(IVector3 position, IVector3 normal) {
		this(position, normal, VectorMatrixFactory.newIVector3());
	}

	/**
	 * Constructor.
	 */
	public Point() {
		this(VectorMatrixFactory.newIVector3(), VectorMatrixFactory
				.newIVector3(), VectorMatrixFactory.newIVector3());
	}

	/**
	 * Getter.
	 */
	public IVector3 getPosition() {
		return position;
	}

	/**
	 * Getter.
	 */
	public IVector3 getColor() {
		return color;
	}

	/**
	 * Getter.
	 */
	public IVector3 getNormal() {
		return normal;
	}

	/**
	 * Setter.
	 */
	public void setNormal(IVector3 normal) {
		this.normal.copy(normal);
	}

	/**
	 * Setter.
	 */
	public void setColor(IVector3 color) {
		this.color.copy(color);
	}

	public Point clone() {
		Point clone = new Point(position, color, normal);
		return clone;
	}

	/**
	 * Getter.
	 */
	public IVector3 getTexCoord() {
		return texCoord;
	}

}

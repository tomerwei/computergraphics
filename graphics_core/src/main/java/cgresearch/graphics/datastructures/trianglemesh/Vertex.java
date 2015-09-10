/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.trianglemesh;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Representation of a vertex.
 * 
 * @author Philipp Jenke
 * 
 */
public class Vertex {

	/**
	 * 3D position of the vertex.
	 */
	private final IVector3 position = VectorMatrixFactory.newIVector3();

	/**
	 * (Normalized) normal direction of the vertex.
	 */
	private IVector3 normal = VectorMatrixFactory.newIVector3(1, 0, 0);

	/**
	 * Color
	 */
	//private IVector3 color = VectorMatrixFactory.newIVector3(0, 0, 0);

	/**
	 * Constructor.
	 */
	public Vertex() {
	}

	/**
	 * Constructor.
	 * 
	 * @param position
	 *            Initial value for position.
	 */
	public Vertex(IVector3 position) {
		this.position.copy(position);
		this.normal = VectorMatrixFactory.newIVector3(1, 0, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param position
	 *            Initial value for position.
	 * @param normal
	 *            Initial value for normal.
	 */
	public Vertex(IVector3 position, IVector3 normal) {
		this.position.copy(position);
		this.normal = normal;
	}

	/**
	 * Copy constructor
	 */
	public Vertex(Vertex vertex) {
		this(VectorMatrixFactory.newIVector3(vertex.getPosition()),
				VectorMatrixFactory.newIVector3(vertex.normal));
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
	public IVector3 getNormal() {
		return normal;
	}

	/**
	 * Getter.
	 */
//	public IVector3 getColor() {
//		return color;
//	}

	/**
	 * @param normal
	 */
	public void setNormal(IVector3 normal) {
		this.normal = normal;
	}

	/**
	 * @param color
	 */
	// public void setColor(IVector3 color) {
	// this.color.copy(color);
	// }
}
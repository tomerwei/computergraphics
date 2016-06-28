package cgresearch.studentprojects.posegen.datastructure;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.trianglemesh.IVertex;

public class VertexMutable implements IVertex {

	// Link to neighbor vertices. To prevent overlapping by not moving this one
	// over the neighbors
	private VertexMutable neighborTop = null;
	private VertexMutable neighborRight = null;
	private VertexMutable neighborBottom = null;
	private VertexMutable neighborLeft = null;

	/**
	 * Constructor.
	 * 
	 * @param position
	 *            Initial value for position.
	 */
	public VertexMutable(Vector position) {
		this.position.copy(position);
		this.normal = VectorFactory.createVector3(1, 0, 0);
	}

	/**
	 * Set the neighboring vertices on the canvas. Null = vertex ignored
	 * 
	 * @param top
	 * @param right
	 * @param bottom
	 * @param left
	 */
	private void setNeighbors(VertexMutable top, VertexMutable right, VertexMutable bottom, VertexMutable left) {
		// if (null != neighborTop || null != neighborRight || null !=
		// neighborBottom || null != neighborLeft) {
		// Logger.getInstance().error("Resetting neighbour vertices not
		// supported. Allready initialised");
		// }
		neighborTop = top;
		neighborRight = right;
		neighborBottom = bottom;
		neighborLeft = left;
	}

	public void setNeighborTop(VertexMutable top) {
		this.neighborTop = top;
	}

	public void setNeighborRight(VertexMutable right) {
		this.neighborRight = right;
	}

	public void setNeighborBottom(VertexMutable bottom) {
		this.neighborBottom = bottom;
	}

	public void setNeighborLeft(VertexMutable left) {
		this.neighborLeft = left;
	}

	/**
	 * Constructor.
	 * 
	 * @param position
	 *            Initial value for position.
	 * @param normal
	 *            Initial value for normal.
	 */
	public VertexMutable(Vector position, Vector normal) {
		this.position.copy(position);
		this.normal = normal;
	}

	/**
	 * 3D position of the vertex.
	 */
	public Vector position = VectorFactory.createVector(3);

	/**
	 * (Normalized) normal direction of the vertex.
	 */
	private Vector normal = VectorFactory.createVector3(1, 0, 0);

	@Override
	public Vector getPosition() {
		return this.position;
	}

	@Override
	public Vector getNormal() {
		return normal;
	}

	@Override
	public void setNormal(Vector newNormal) {
		normal = newNormal;
	}

	/**
	 * Try to set the position. If a neighbor vertex is in the way. A different
	 * position is set. (Without overlapping the neighbor)
	 * 
	 * @param position
	 *            the actual new position
	 * @return
	 */
	public Vector trySetPosition(Vector position) {
		// Test if new position is allowed or overlapping with neighbour. If
		// overlapping return new position
		double x = position.get(0);
		double y = position.get(1);
		if (null != neighborTop && y > neighborTop.getPosition().get(1)) {
			y = neighborTop.getPosition().get(1); // Tried to move over top
		} else if (null != neighborBottom && y < neighborBottom.getPosition().get(1)) {
			y = neighborBottom.getPosition().get(1); // Tried to move over
														// bottom
		}

		if (null != neighborRight && x > neighborRight.getPosition().get(0)) {
			x = neighborRight.getPosition().get(0);
		} else if (null != neighborLeft && x < neighborLeft.getPosition().get(0)) {
			x = neighborLeft.getPosition().get(0);
		}
		this.position.set(0, x);
		this.position.set(1, y);

		return new Vector(this.position);
	}

}

package cgresearch.studentprojects.posegen.datastructure;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.trianglemesh.IVertex;

public class VertexMutable implements IVertex {

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
	   * Constructor.
	   * 
	   * @param position
	   *          Initial value for position.
	   * @param normal
	   *          Initial value for normal.
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
	
	public void setPosition(Vector position){
		this.position.copy(position);
	}
	

}

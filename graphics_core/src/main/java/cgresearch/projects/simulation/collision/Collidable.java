package cgresearch.projects.simulation.collision;

import cgresearch.core.math.Vector;

public interface Collidable {

	/**
	 * Check if the position x collides with the collidable
	 * 
	 * @return True on case of collision, false otherwise.
	 */
	public boolean collides(Vector x);

	/**
	 * Compute the closest surface point for the given position.
	 */
	public Vector projectToSurface(Vector x);

}

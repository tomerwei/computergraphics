package cgresearch.projects.simulation.collision;

import cgresearch.core.math.IVector3;

public interface Collidable {

	/**
	 * Check if the position x collides with the collidable
	 * 
	 * @return True on case of collision, false otherwise.
	 */
	public boolean collides(IVector3 x);

	/**
	 * Compute the closest surface point for the given position.
	 */
	public IVector3 projectToSurface(IVector3 x);

}

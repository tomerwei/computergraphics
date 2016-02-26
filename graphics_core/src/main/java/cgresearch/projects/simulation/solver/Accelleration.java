package cgresearch.projects.simulation.solver;

import cgresearch.core.math.Vector;

/**
 * Parent interface for all systems, which allow to evaluate an accelleration.
 * 
 * @author Philipp Jenke
 *
 */
public interface Accelleration {

	/**
	 * Evaluate the acceleration at the node with the specified index. The
	 * position of the node with the specified index is used.
	 * 
	 * @param index
	 *            Index of the node to be evaluated.
	 * @return Acceleration at the node.
	 */
	public Vector eval(int index);

	/**
	 * Evaluate the acceleration at the node with the specified index.
	 * 
	 * @param index
	 *            Index of the node to be evaluated.
	 * @param Position
	 *            , where the force is evaluated.
	 * @return Acceleration at the node.
	 */
	public Vector eval(int index, Vector pos);

}

package cgresearch.projects.simulation;

import java.util.ArrayList;
import java.util.List;

import cgresearch.projects.simulation.solver.Accelleration;

/**
 * Representation of a cloth
 * 
 * @author Philipp Jenke
 *
 */
public abstract class Cloth implements Accelleration {

	/**
	 * List of mass points.
	 */
	List<MassPoint> massPoints = new ArrayList<MassPoint>();

	/**
	 * List of springs
	 */
	List<Spring> springs = new ArrayList<Spring>();

	public int getNumberOfMassPoints() {
		return massPoints.size();
	}

	public MassPoint getMassPoint(int index) {
		return massPoints.get(index);
	}

	public int addMassPoint(MassPoint massPoint) {
		massPoints.add(massPoint);
		return massPoints.size() - 1;
	}

	public int getNumberOfSprings() {
		return springs.size();
	}

	public Spring getSpring(int index) {
		return springs.get(index);
	}

	public int addSpring(Spring spring) {
		springs.add(spring);
		massPoints.get(spring.getFirstMassPoints()).addSpring(spring);
		massPoints.get(spring.getSecondMassPoints()).addSpring(spring);
		return springs.size() - 1;
	}

	/**
	 * Reset to the starting state.
	 */
	public abstract void reset();

	/**
	 * Clear content.
	 */
	protected void clear() {
		springs.clear();
		massPoints.clear();
	}

}

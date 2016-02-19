package cgresearch.projects.simulation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Representation of a mass point in a cloth simulation.
 * 
 * @author Philipp Jenke
 *
 */
public class MassPoint {

	/**
	 * Mass of the point in [kg].
	 */
	private final double mass;

	/**
	 * List of incident springs
	 */
	private Set<Spring> springs = new HashSet<Spring>();

	/**
	 * Current velocity of the mass point
	 */
	private Vector v = VectorMatrixFactory.newVector(0, 0, 0);

	/**
	 * Position of the point in 3-spaces
	 */
	private Vector x = VectorMatrixFactory.newVector(0, 0, 0);

	/**
	 * Constructor.
	 */
	public MassPoint(Vector x, double mass) {
		this.x.copy(x);
		this.mass = mass;
	}

	public Vector getX() {
		return x;
	}

	public void setX(Vector x) {
		this.x = x;
	}

	public double getMass() {
		return mass;
	}

	public Vector getVelocity() {
		return v;
	}

	public void setVelocity(Vector velocity) {
		this.v.copy(velocity);
	}

	public int getNumberOfSprings() {
		return springs.size();
	}

	public void addSpring(Spring spring) {
		springs.add(spring);
	}

	public Iterator<Spring> getSpringsIterator() {
		return springs.iterator();
	}
}

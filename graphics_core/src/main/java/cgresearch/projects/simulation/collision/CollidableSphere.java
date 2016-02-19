package cgresearch.projects.simulation.collision;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;

public class CollidableSphere implements Collidable {

	/**
	 * Sphere center
	 */
	private final Vector center = VectorMatrixFactory.newVector(0, 0, 0);

	/**
	 * Radius of the sphere.
	 */
	private final double radius;

	/**
	 * Constructor.
	 */
	public CollidableSphere(Vector center, double radius) {
		this.center.copy(center);
		this.radius = radius;
	}

	public Vector getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public boolean collides(Vector x) {
		return x.subtract(center).getSqrNorm() < radius * radius;
	}

	@Override
	public Vector projectToSurface(Vector x) {
		Vector direction = x.subtract(center);
		direction.normalize();
		direction = direction.multiply(radius);
		return center.add(direction);
	}

}

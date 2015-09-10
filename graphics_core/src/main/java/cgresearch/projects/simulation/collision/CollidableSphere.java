package cgresearch.projects.simulation.collision;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

public class CollidableSphere implements Collidable {

	/**
	 * Sphere center
	 */
	private final IVector3 center = VectorMatrixFactory.newIVector3(0, 0, 0);

	/**
	 * Radius of the sphere.
	 */
	private final double radius;

	/**
	 * Constructor.
	 */
	public CollidableSphere(IVector3 center, double radius) {
		this.center.copy(center);
		this.radius = radius;
	}

	public IVector3 getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public boolean collides(IVector3 x) {
		return x.subtract(center).getSqrNorm() < radius * radius;
	}

	@Override
	public IVector3 projectToSurface(IVector3 x) {
		IVector3 direction = x.subtract(center);
		direction.normalize();
		direction = direction.multiply(radius);
		return center.add(direction);
	}

}

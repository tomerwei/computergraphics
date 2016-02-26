package cgresearch.projects.simulation.collision;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.primitives.Plane;

public class CollidablePlane implements Collidable {

	/**
	 * Representation of the plane properties
	 */
	private Plane plane = new Plane();

	/**
	 * Constructor.
	 */
	public CollidablePlane(Vector point, Vector normal) {
		plane.setPoint(point);
		Vector normalizedNormal = VectorFactory.createVector(3);
		normalizedNormal.copy(normal);
		normalizedNormal.normalize();
		plane.setNormal(normalizedNormal);
	}

	@Override
	public boolean collides(Vector x) {
		return plane.computeSignedDistance(x) < 0;
	}

	@Override
	public Vector projectToSurface(Vector x) {
		double distance = plane.computeDistance(x);
		return x.add(plane.getNormal().multiply(distance));
	}

	public Vector getPoint() {
		return plane.getPoint();
	}

}

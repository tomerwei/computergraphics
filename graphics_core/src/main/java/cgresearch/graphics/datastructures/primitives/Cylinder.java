package cgresearch.graphics.datastructures.primitives;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;

/**
 * Representation of a cylinder. The base of the cylinders is at the point
 * start, the top of the cylinder at the point end.
 * 
 * @author Philipp Jenke
 *
 */
public class Cylinder extends IPrimitive {

	private Vector point = VectorFactory.createVector3(0, 0, 0);

	/**
	 * Always normalized.
	 */
	private Vector direction = VectorFactory.createVector3(0, 1, 0);

	/**
	 * Radius of the cylinder in [mm].
	 */
	double radius = 1;

	/**
	 * Constructor.
	 */
	public Cylinder(Vector point, Vector direction, double radius) {
		this.point.copy(point);
		this.direction.copy(direction);
		this.direction.normalize();
		this.radius = radius;
	}

	/**
	 * Getter.
	 */
	public double getRadius() {
		return radius;
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.datastructures.IBoundingBoxed#getBoundingBox()
	 */
	@Override
	public BoundingBox getBoundingBox() {
		BoundingBox bbox = new BoundingBox();
		// bbox.add(point);
		// bbox.add(point.add(direction));
		return bbox;
	}

	/**
	 * Getter.
	 */
	public Vector getDirection() {
		return direction;
	}

	/**
	 * Getter.
	 */
	public Vector getPoint() {
		return point;
	}

	/**
	 * Compute the squared distance of the distance of the point from the
	 * cylinder's surface
	 */
	public double getDistance(Vector p) {
		Vector start2P = p.subtract(point);
		Vector projectedP = point.add(direction.multiply(start2P
				.multiply(direction)));
		double distance = p.subtract(projectedP).getNorm();
		return distance - radius;
	}

	/**
	 * Special version of the getDistance function which a new point vector.
	 */
	public double getDistance(Vector alternativePoint, Vector p) {
		Vector start2P = p.subtract(alternativePoint);
		Vector projectedP = alternativePoint.add(direction.multiply(start2P
				.multiply(direction)));
		double distance = p.subtract(projectedP).getNorm();
		return distance - radius;
	}
}

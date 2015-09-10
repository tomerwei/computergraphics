package cgresearch.graphics.datastructures.primitives;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Representation of a cylinder. The base of the cylinders is at the point
 * start, the top of the cylinder at the point end.
 * 
 * @author Philipp Jenke
 *
 */
public class Cylinder extends IPrimitive {

	private IVector3 point = VectorMatrixFactory.newIVector3(0, 0, 0);

	/**
	 * Always normalized.
	 */
	private IVector3 direction = VectorMatrixFactory.newIVector3(0, 1, 0);

	/**
	 * Radius of the cylinder in [mm].
	 */
	double radius = 1;

	/**
	 * Constructor.
	 */
	public Cylinder(IVector3 point, IVector3 direction, double radius) {
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
	public IVector3 getDirection() {
		return direction;
	}

	/**
	 * Getter.
	 */
	public IVector3 getPoint() {
		return point;
	}

	/**
	 * Compute the squared distance of the distance of the point from the
	 * cylinder's surface
	 */
	public double getDistance(IVector3 p) {
		IVector3 start2P = p.subtract(point);
		IVector3 projectedP = point.add(direction.multiply(start2P
				.multiply(direction)));
		double distance = p.subtract(projectedP).getNorm();
		return distance - radius;
	}

	/**
	 * Special version of the getDistance function which a new point vector.
	 */
	public double getDistance(IVector3 alternativePoint, IVector3 p) {
		IVector3 start2P = p.subtract(alternativePoint);
		IVector3 projectedP = alternativePoint.add(direction.multiply(start2P
				.multiply(direction)));
		double distance = p.subtract(projectedP).getNorm();
		return distance - radius;
	}
}

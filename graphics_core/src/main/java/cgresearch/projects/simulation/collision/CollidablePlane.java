package cgresearch.projects.simulation.collision;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.primitives.Plane;

public class CollidablePlane implements Collidable {

	/**
	 * Representation of the plane properties
	 */
	private Plane plane = new Plane();

	/**
	 * Constructor.
	 */
	public CollidablePlane(IVector3 point, IVector3 normal) {
		plane.setPoint(point);
		IVector3 normalizedNormal = VectorMatrixFactory.newIVector3();
		normalizedNormal.copy(normal);
		normalizedNormal.normalize();
		plane.setNormal(normalizedNormal);
	}

	@Override
	public boolean collides(IVector3 x) {
		return plane.computeSignedDistance(x) < 0;
	}

	@Override
	public IVector3 projectToSurface(IVector3 x) {
		double distance = plane.computeDistance(x);
		return x.add(plane.getNormal().multiply(distance));
	}

	public IVector3 getPoint() {
		return plane.getPoint();
	}

}

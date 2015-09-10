package cgresearch.core.math;


/**
 * A 2D ray represented by a start point and a direction vector. The y
 * coordinate is ignored.
 * 
 * @author Philipp Jenke
 *
 */
public class Ray2D {

	/**
	 * Constants
	 */
	private static final int X = 0;
	private static final int Z = 2;

	/**
	 * Starting point of the ray.
	 */
	private IVector3 p;

	/**
	 * Direction vector of the ray.
	 */
	private IVector3 direction;

	/**
	 * Constructor.
	 */
	public Ray2D(IVector3 p, IVector3 direction) {
		this.p = p;
		this.direction = direction;
	}

	/**
	 * Getter.
	 */
	public IVector3 getDirection() {
		return direction;
	}

	/**
	 * Evaluate the ray, compute the position
	 */
	public IVector3 eval(double lambda) {
		return p.add(direction.multiply(lambda));
	}

	/**
	 * Inner class to represent the intersection result.
	 */
	public class IntersectionResult {
		public double lambda1;
		public double lambda2;

		public IntersectionResult(double lambda1, double lambda2) {
			this.lambda1 = lambda1;
			this.lambda2 = lambda2;
		}
	}

	/**
	 * Compute the intersection point of two rays.
	 * 
	 * @param other
	 *            Other ray used in the computation (together with this-object).
	 * 
	 * @return Parameter values lambda for the this-ray (lambda1) and the other
	 *         ray (lamda2) at the intersection point. If the rays do not
	 *         intersect, the method return null.
	 */
	public IntersectionResult intersect(Ray2D other) {
		double denom = (direction.get(Z) * other.direction.get(X) - direction
				.get(X) * other.direction.get(Z));
		if (Math.abs(denom) < 1e-5) {
			return null;
		}
		double lambda2 = (direction.get(X) * other.p.get(Z) - direction.get(X)
				* p.get(Z) - direction.get(Z) * other.p.get(X) + direction
				.get(Z) * p.get(X))
				/ denom;
		double lambda1 = (other.p.get(X) + lambda2 * other.direction.get(X) - p
				.get(X)) / direction.get(X);
		return new IntersectionResult(lambda1, lambda2);
	}
}

package cgresearch.projects.portalculling;

import cgresearch.core.math.IVector3;

/**
 * Representation of a viewing volume in 2D. The volume is bounded by an origin
 * point plus a left and right boundary vector. The vector data types are 3d,
 * therefore the y-coordinate is ignored (set to 0).
 * 
 * @author Philipp Jenke
 *
 */
public class ViewVolume2D {

	/**
	 * Origin of the viewing volume.
	 */
	private IVector3 origin;

	/**
	 * Left boundary vector of the volume.
	 */
	private IVector3 leftBoundary;

	/**
	 * Right boundary vector of the volume.
	 */
	private IVector3 rightBoundary;

	/**
	 * Constructor.
	 */
	public ViewVolume2D(IVector3 origin, IVector3 leftBoundary,
			IVector3 rightBoundary) {
		this.origin = origin;
		this.leftBoundary = leftBoundary;
		this.rightBoundary = rightBoundary;
	}

	/**
	 * Getter.
	 */
	public IVector3 getOrigin() {
		return origin;
	}

	/**
	 * Getter.
	 */
	public IVector3 getLeftBoundary() {
		return leftBoundary;
	}

	/**
	 * Getter.
	 */
	public IVector3 getRightBoundary() {
		return rightBoundary;
	}

	/**
	 * Return the angle between the two boundaries.
	 */
	public int getAngle() {
		double normalization = leftBoundary.getNorm() * rightBoundary.getNorm();
		double angleRadiens = Math.acos(leftBoundary.multiply(rightBoundary)
				/ normalization);
		int angle = (int) radiensToDegree(angleRadiens);
		return angle;
	}

	/**
	 * Helper method: convert radiens to degree.
	 */
	private double radiensToDegree(double radiensAngle) {
		return radiensAngle * 180.0 / Math.PI;
	}
}

/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

/**
 * Representation of a plane in 3-space.
 * 
 * @author Philipp Jenke
 * 
 */
public class Plane {

    /**
     * Normal of the plane, points into the 'positive halfspace'.
     */
    private IVector3 normal;

    /**
     * A point on the plane.
     */
    private IVector3 point;

    private static final double PARALLEL_THRESHOLD = 0.99;

    /**
     * Constructor.
     * 
     * @param point
     * @param normal
     */
    public Plane(IVector3 point, IVector3 normal) {
        this.point = point;
        this.normal = normal.getNormalized();
    }

    /**
     * Return true if the point is in the positive halfspace of the plane.
     */
    public boolean isInPositiveHalfSpace(IVector3 p) {
        double distance = getDistance(p);
        return distance > 0;
    }

    /**
     * Compute the distance of the point from the plane.
     */
    public double getDistance(IVector3 p) {
        IVector3 v = p.subtract(point);
        return v.multiply(normal);
    }

    /**
     * Project the point p onto the plane.
     */
    public IVector3 project(IVector3 p) {
        return p.subtract(normal.multiply(getDistance(p)));
    }

    /**
     * Return a tangent vector which is perperdicular to normal and
     * getTangentU().
     */
    public IVector3 getTangentU() {
        IVector3 helper = VectorMatrixFactory.newIVector3(1, 0, 0);
        if (Math.abs(helper.multiply(normal)) > PARALLEL_THRESHOLD) {
            helper = VectorMatrixFactory.newIVector3(0, 1, 0);
        }
        return normal.cross(helper).getNormalized();
    }

    /**
     * Return a tangent vector which is perpendicular to normal and
     * getTangentU().
     */
    public IVector3 getTangentV() {
        return normal.cross(getTangentU());
    }

    public IVector3 getPoint() {
        return point;
    }
}

package cgresearch.core.math;

/**
 * Representation of a ray in 3-space.
 * 
 * @author Philipp Jenke
 *
 */
public class Ray3D {

  /**
   * Starting point.
   */
  private Vector p = VectorMatrixFactory.newVector(3);

  /**
   * Direction
   */
  private Vector r = VectorMatrixFactory.newVector(3);

  /**
   * Constructor.
   */
  public Ray3D(Vector p, Vector r) {
    this.p.copy(p);
    this.r.copy(r);
  }

  @Override
  public String toString() {
    return p + " + lambda * " + r;
  }

  /**
   * Getter.
   */
  public Vector getDirection() {
    return r;
  }

  /**
   * Getter.
   */
  public Vector getPoint() {
    return p;
  }

}

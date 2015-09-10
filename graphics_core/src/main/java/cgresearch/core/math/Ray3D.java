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
  private IVector3 p = VectorMatrixFactory.newIVector3();

  /**
   * Direction
   */
  private IVector3 r = VectorMatrixFactory.newIVector3();

  /**
   * Constructor.
   */
  public Ray3D(IVector3 p, IVector3 r) {
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
  public IVector3 getDirection() {
    return r;
  }

  /**
   * Getter.
   */
  public IVector3 getPoint() {
    return p;
  }

}

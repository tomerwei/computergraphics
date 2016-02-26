package cgresearch.projects.raytracing;

import cgresearch.core.math.Ray3D;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * Interface for all intersection computations between a 3D ray and an object.
 * 
 * @author Philipp Jenke
 *
 * @param <T>
 *          Type of the object
 */
public interface Intersect<T extends ICgNodeContent> {

  /**
   * Numerical accuracy.
   */
  public static final double EPISLON = 1e-5;

  /**
   * Compute the intersection result of the ray and the object.
   * 
   * @return Intersection representation, null, if there is no intersection.
   */
  public IntersectionResult intersect(Ray3D ray, ICgNodeContent object);

  /**
   * Returns the type of the objects that can be handled.
   */
  public Class<?> getHandledType();

  /**
   * Return true if the intersector is able to handle the object.
   */
  public boolean canHandle(ICgNodeContent object);

}

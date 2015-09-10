package cgresearch.projects.raytracing;

import cgresearch.core.math.IVector3;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * Representation of the intersection result.
 */
public class IntersectionResult {

  /**
   * The intersection happens at this point.
   */
  public IVector3 point;

  /**
   * Normal at the given point.
   */
  public IVector3 normal;

  /**
   * Intersected object
   */
  public ICgNodeContent object;
}

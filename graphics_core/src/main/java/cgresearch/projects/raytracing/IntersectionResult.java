package cgresearch.projects.raytracing;

import cgresearch.core.math.Vector;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * Representation of the intersection result.
 */
public class IntersectionResult {

  /**
   * The intersection happens at this point.
   */
  public Vector point;

  /**
   * Normal at the given point.
   */
  public Vector normal;

  /**
   * Intersected object
   */
  public ICgNodeContent object;
}

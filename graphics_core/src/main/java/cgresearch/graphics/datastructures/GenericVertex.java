package cgresearch.graphics.datastructures;

import cgresearch.core.math.IVector3;

/**
 * Superclass required to use containers for different vertex types.
 * 
 * @author Philip Jenke
 */
public interface GenericVertex {

  /**
   * Returns the position of the vertex
   */
  public IVector3 getPosition();

}

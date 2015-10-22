package cgresearch.graphics.datastructures.trianglemesh;

import cgresearch.core.math.IVector3;

/**
 * Parent interface for all vertices.
 * 
 * @author Philipp Jenke
 *
 */
public interface IVertex {

  public IVector3 getPosition();

  public IVector3 getNormal();

  public void setNormal(IVector3 newNormal);

}

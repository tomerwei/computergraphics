package cgresearch.graphics.datastructures.trianglemesh;

import cgresearch.core.math.IVector3;
import cgresearch.graphics.datastructures.GenericVertex;

/**
 * Parent interface for all vertices.
 * 
 * @author Philipp Jenke
 *
 */
public interface IVertex extends GenericVertex {

  public IVector3 getPosition();

  public IVector3 getNormal();

  public void setNormal(IVector3 newNormal);

}

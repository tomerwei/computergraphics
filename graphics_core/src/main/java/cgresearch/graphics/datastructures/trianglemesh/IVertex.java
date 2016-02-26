package cgresearch.graphics.datastructures.trianglemesh;

import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.GenericVertex;

/**
 * Parent interface for all vertices.
 * 
 * @author Philipp Jenke
 *
 */
public interface IVertex extends GenericVertex {

  public Vector getPosition();

  public Vector getNormal();

  public void setNormal(Vector newNormal);

}

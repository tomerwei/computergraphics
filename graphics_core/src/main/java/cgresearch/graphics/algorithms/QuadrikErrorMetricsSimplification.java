package cgresearch.graphics.algorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import cgresearch.core.math.IMatrix;
import cgresearch.core.math.IVector3;
import cgresearch.graphics.datastructures.GenericEdge;
import cgresearch.graphics.datastructures.GenericVertex;

/**
 * Parent class for surface simplification methods in 2D and 3D
 * 
 * @author Philipp Jenke
 */
public abstract class QuadrikErrorMetricsSimplification {

  /**
   * Internal datastructure to represent an edge collapse operation.
   * 
   * @author Philipp Jenke
   */
  class EdgeCollapse {
    public EdgeCollapse(double error, IMatrix Q, IVector3 newPos) {
      this.error = error;
      this.Q = Q;
      this.newPos = newPos;
    }

    /**
     * Error (estimate) of the collapse
     */
    public double error;

    /**
     * Quadric error metric
     */
    public IMatrix Q;

    /**
     * Optimal position after the collapse
     */
    public IVector3 newPos;
  }

  /**
   * List of QEMs for the points.
   */
  protected Map<GenericVertex, IMatrix> pointQems = new HashMap<GenericVertex, IMatrix>();

  /**
   * Collapse information for the edge
   */
  protected Map<GenericEdge, EdgeCollapse> edgesQems = new HashMap<GenericEdge, EdgeCollapse>();

  /**
   * Contains the edges ordered bei increasing collapse error.
   */
  protected PriorityQueue<GenericEdge> queue = null;
}

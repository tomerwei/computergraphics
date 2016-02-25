package cgresearch.graphics.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
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
    public EdgeCollapse(double error, Matrix Q, Vector newPos) {
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
    public Matrix Q;

    /**
     * Optimal position after the collapse
     */
    public Vector newPos;
  }

  /**
   * List of QEMs for the points.
   */
  protected Map<GenericVertex, Matrix> pointQems = new HashMap<GenericVertex, Matrix>();

  /**
   * Collapse information for the edge
   */
  protected Map<GenericEdge, EdgeCollapse> edgesQems = new HashMap<GenericEdge, EdgeCollapse>();

  /**
   * Contains the edges ordered bei increasing collapse error.
   */
  protected PriorityQueue<GenericEdge> queue = null;

  public QuadrikErrorMetricsSimplification() {
    queue = new PriorityQueue<GenericEdge>((e1, e2) -> (getError(e1) < getError(e2)) ? -1 : 1);
  }

  /**
   * Reset state.
   */
  public void reset() {
    pointQems.clear();
    edgesQems.clear();
    queue.clear();
    // Compute QEM for each vertex.
    for (int i = 0; i < getNumberOfVertices(); i++) {
      GenericVertex vertex = getVertex(i);
      pointQems.put(vertex, computePointQem(vertex));
    }
    for (int edgeIndex = 0; edgeIndex < getNumberOfEdges(); edgeIndex++) {
      GenericEdge edge = getEdge(edgeIndex);
      edgesQems.put(edge, computeEdgeCollapseResult(edge));
      queue.add(edge);
    }
  }

  /**
   * Compute the QEM for the vertex at the specified index (initialization).
   */
  protected abstract Matrix computePointQem(GenericVertex vertex);

  /**
   * Compute the result of a possible edge collapse of the specified edge
   * (error, QEM, optimal vertex position).
   */
  protected EdgeCollapse computeEdgeCollapseResult(GenericEdge edge) {
    GenericVertex p0 = edge.getStartVertex();
    GenericVertex p1 = edge.getEndVertex();
    Matrix qem = pointQems.get(p0).add(pointQems.get(p1));
    Matrix derivQem = MatrixFactory.createMatrix(qem);
    derivQem.set(3, 0, 0);
    derivQem.set(3, 1, 0);
    derivQem.set(3, 2, 0);
    derivQem.set(3, 3, 1);
    double det = Math.abs(derivQem.getDeterminant());
    if (det > 1e-5) {
      // Matrix is invertible
      Matrix invQ = derivQem.getInverse();
      Vector pos = invQ.multiply(VectorFactory.createVector4(0, 0, 0, 1));
      double error = pos.multiply(qem.multiply(pos));
      return new EdgeCollapse(error, qem, VectorFactory.createVector3(pos.get(0), pos.get(1), pos.get(2)));
    } else {
      // Matrix cannot be inverted, use corner points or midpoint
      List<Vector> candidates = new ArrayList<Vector>();
      candidates.add(p0.getPosition());
      candidates.add(p1.getPosition());
      candidates.add((p0.getPosition().add(p1.getPosition())).multiply(0.5));
      double minError = Double.POSITIVE_INFINITY;
      Vector minPos = null;
      for (Vector p : candidates) {
        Vector pos = VectorFactory.createHomogeniousFor3spaceVector(p);
        pos.set(3, 1);
        double error = pos.multiply(qem.multiply(pos));
        if (error < minError) {
          minError = error;
          minPos = VectorFactory.createVector3(pos.get(0), pos.get(1), pos.get(2));
        }
      }
      return new EdgeCollapse(minError, qem, minPos);
    }
  }

  /**
   * Return the edge with the specified index.
   */
  protected abstract GenericEdge getEdge(int edgeIndex);

  /**
   * Return the vertex with the specified index.
   */
  protected abstract GenericVertex getVertex(int vertexIndex);

  /**
   * Return the number of vertices in the datastructure.
   */
  protected abstract int getNumberOfVertices();

  /**
   * Return the number of edges in the datastructure.
   */
  protected abstract int getNumberOfEdges();

  /**
   * Apply one simplification step.
   */
  public void simplify() {
    if (getNumberOfEdges() == 0) {
      Logger.getInstance().error("Cannot collapse with less than 2 points.");
      return;
    }

    GenericEdge queueEdge = queue.poll();
    GenericVertex p = collapse(queueEdge, edgesQems.get(queueEdge).newPos);
    pointQems.put(p, edgesQems.get(queueEdge).Q);

    // Update incident edges
    List<GenericEdge> incidentEdges = getIncidentEdges(p);
    for (GenericEdge edge : incidentEdges) {
      edgesQems.put(edge, computeEdgeCollapseResult(edge));
    }

    // Old version without queue
    // double errorMin = Double.POSITIVE_INFINITY;
    // int optimalEdgeIndex = -1;
    // Vector optimalPos = VectorMatrixFactory.newVector();
    // Matrix optimalQ = VectorMatrixFactory.newMatrix();
    // for (int edgeIndex = 0; edgeIndex < polygon.getNumEdges(); edgeIndex++) {
    // ErrorComputationResult result = computeError(polygon.getEdge(edgeIndex));
    // if (result.error < errorMin) {
    // errorMin = result.error;
    // optimalEdgeIndex = edgeIndex;
    // optimalPos.copy(result.newPos);
    // optimalQ.copy(result.Q);
    // }
    // }
    // if (optimalEdgeIndex >= 0) {
    // optimalPos.set(2, 0);
    // PolygonVertex p = polygon.collapse(optimalEdgeIndex, optimalPos);
    // pointQems.put(p, optimalQ);
    // } else {
    // Logger.getInstance().error("Invalid edge index - should not happen.");
    // }
  }

  /**
   * Return list of incident edges of a vertex.
   * 
   * @param vertex
   *          Vertex
   * @return List of incident edges.
   */
  protected abstract List<GenericEdge> getIncidentEdges(GenericVertex vertex);

  /**
   * Collapse the specified edge, remove collapsed edges from queue, set new
   * vertex position for remaining vertex.
   * 
   * @Return Remaining vertex
   */
  protected abstract GenericVertex collapse(GenericEdge edge, Vector newPos);

  /**
   * Color-code edge by error.
   */
  public void computeEdgeErrorColor() {
    double errorMin = Double.POSITIVE_INFINITY;
    double errorMax = Double.NEGATIVE_INFINITY;
    for (int edgeIndex = 0; edgeIndex < getNumberOfEdges(); edgeIndex++) {
      EdgeCollapse result = computeEdgeCollapseResult(getEdge(edgeIndex));
      if (result.error < errorMin) {
        errorMin = result.error;
      }
      if (result.error > errorMax) {
        errorMax = result.error;
      }
    }
    for (int edgeIndex = 0; edgeIndex < getNumberOfEdges(); edgeIndex++) {
      EdgeCollapse result = computeEdgeCollapseResult(getEdge(edgeIndex));
      Vector color =
          VectorFactory.createVector3(transferFunction((result.error - errorMin) / (errorMax - errorMin)), 0, 0);
      getEdge(edgeIndex).setColor(color);
    }
  }

  private double transferFunction(double x) {
    return Math.pow(1 - x, 8);
  }

  /**
   * Returns the error for the specified edge.
   */
  protected double getError(GenericEdge edge) {
    return edgesQems.get(edge).error;
  }
}

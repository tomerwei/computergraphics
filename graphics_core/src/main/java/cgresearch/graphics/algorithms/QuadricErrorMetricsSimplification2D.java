package cgresearch.graphics.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IMatrix;
import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.GenericEdge;
import cgresearch.graphics.datastructures.polygon.Polygon;
import cgresearch.graphics.datastructures.polygon.PolygonEdge;
import cgresearch.graphics.datastructures.polygon.PolygonVertex;

public class QuadricErrorMetricsSimplification2D extends QuadrikErrorMetricsSimplification {

  /**
   * Processed polygon.
   */
  private Polygon polygon;

  public QuadricErrorMetricsSimplification2D(Polygon polygon) {
    this.polygon = polygon;
    this.queue =
        new PriorityQueue<GenericEdge>((e1, e2) -> (edgesQems.get(e1).error < edgesQems.get(e2).error) ? -1 : 1);
    reset();
  }

  /**
   * Reset state.
   */
  public void reset() {
    pointQems.clear();
    edgesQems.clear();
    queue.clear();
    // Compute QEM for each vertex.
    for (int i = 0; i < polygon.getNumPoints(); i++) {
      PolygonVertex vertex = polygon.getPoint(i);
      pointQems.put(vertex, computePointQem(vertex));
    }
    for (int edgeIndex = 0; edgeIndex < polygon.getNumEdges(); edgeIndex++) {
      PolygonEdge edge = polygon.getEdge(edgeIndex);
      edgesQems.put(edge, computeEdgeCollapse(edge));
      queue.add(edge);
    }
  }

  /**
   * Compute QEM for the point with the given index.
   */
  private IMatrix3 computePointQem(PolygonVertex vertex) {
    PolygonEdge incomingEdge = vertex.getIncomingEdge();
    PolygonEdge outgoingEdge = vertex.getOutgoingEdge();
    PolygonVertex pred = incomingEdge.getStartVertex();
    PolygonVertex succ = outgoingEdge.getEndVertex();
    IVector3 pMinus = pred.getPosition();
    IVector3 p = vertex.getPosition();
    IVector3 pPlus = succ.getPosition();
    // QEMs for the incident planes (edges)
    IMatrix3 QMinus = computeEdgeQem(p, pMinus);
    IMatrix3 QPlus = computeEdgeQem(pPlus, p);
    IMatrix3 pointQem = QMinus.add(QPlus);
    return pointQem;
  }

  /**
   * Compute QEM for the edge from p to q.
   */
  public IMatrix3 computeEdgeQem(IVector3 p, IVector3 q) {
    IVector3 normal = getNormal(p.subtract(q));
    normal.normalize();
    double distance = normal.multiply(p);
    IVector3 v = VectorMatrixFactory.newIVector3(normal);
    v.set(2, -distance);
    IMatrix3 edgeQem = v.innerProduct(v);
    return edgeQem;
  }

  /**
   * Return a perpendicular vector.
   */
  private IVector3 getNormal(IVector3 v) {
    return VectorMatrixFactory.newIVector3(v.get(1), -v.get(0), 0);
  }

  /**
   * Apply one simplification step.
   */
  public void simplify() {
    if (polygon.getNumPoints() < 2) {
      Logger.getInstance().error("Cannot collapse polygon with less than 2 points.");
      return;
    }

    PolygonEdge queueEdge = (PolygonEdge) queue.poll();
    PolygonVertex p = polygon.collapse(queueEdge, edgesQems.get(queueEdge).newPos);
    pointQems.put(p, edgesQems.get(queueEdge).Q);
    // Update edge QEMs
    edgesQems.put(p.getIncomingEdge(), computeEdgeCollapse(p.getIncomingEdge()));
    edgesQems.put(p.getOutgoingEdge(), computeEdgeCollapse(p.getOutgoingEdge()));

    // Old version without queue
    // double errorMin = Double.POSITIVE_INFINITY;
    // int optimalEdgeIndex = -1;
    // IVector3 optimalPos = VectorMatrixFactory.newIVector3();
    // IMatrix3 optimalQ = VectorMatrixFactory.newIMatrix3();
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
   * Computes the error by collapsing the edge with the given index.
   */
  private EdgeCollapse computeEdgeCollapse(PolygonEdge edge) {
    PolygonVertex p0 = edge.getStartVertex();
    PolygonVertex p1 = edge.getEndVertex();
    IMatrix qem = pointQems.get(p0).add(pointQems.get(p1));
    IMatrix derivQem = VectorMatrixFactory.newIMatrix3(qem);
    derivQem.set(2, 0, 0);
    derivQem.set(2, 1, 0);
    derivQem.set(2, 2, 1);
    double det = Math.abs(derivQem.getDeterminant());
    if (det > 1e-5) {
      // Matrix is invertible
      IMatrix3 invQ = derivQem.getInverse();
      IVector3 pos = invQ.multiply(VectorMatrixFactory.newIVector3(0, 0, 1));
      pos.set(2, 0);
      double error = pos.multiply(qem.multiply(pos));
      return new EdgeCollapse(error, qem, pos);
    } else {
      // Matrix cannot be inverted, use corner points or midpoint
      List<IVector3> candidates = new ArrayList<IVector3>();
      candidates.add(p0.getPosition());
      candidates.add(p1.getPosition());
      candidates.add((p0.getPosition().add(p1.getPosition())).multiply(0.5));
      double minError = Double.POSITIVE_INFINITY;
      IVector3 minPos = null;
      for (IVector3 p : candidates) {
        IVector3 pos = VectorMatrixFactory.newIVector3(p);
        pos.set(2, 1);
        double error = pos.multiply(qem.multiply(pos));
        if (error < minError) {
          minError = error;
          minPos = pos;
        }
      }
      return new EdgeCollapse(minError, qem, minPos);
    }
  }

  /**
   * Color-code edge by error.
   */
  public void computeEdgeErrorColor() {
    double errorMin = Double.POSITIVE_INFINITY;
    double errorMax = Double.NEGATIVE_INFINITY;
    for (int edgeIndex = 0; edgeIndex < polygon.getNumEdges(); edgeIndex++) {
      EdgeCollapse result = computeEdgeCollapse(polygon.getEdge(edgeIndex));
      if (result.error < errorMin) {
        errorMin = result.error;
      }
      if (result.error > errorMax) {
        errorMax = result.error;
      }
    }
    for (int edgeIndex = 0; edgeIndex < polygon.getNumEdges(); edgeIndex++) {
      EdgeCollapse result = computeEdgeCollapse(polygon.getEdge(edgeIndex));
      IVector3 color =
          VectorMatrixFactory.newIVector3(transferFunction((result.error - errorMin) / (errorMax - errorMin)), 0, 0);
      polygon.setEdgeColor(edgeIndex, color);
    }
  }

  private double transferFunction(double x) {
    return Math.pow(1 - x, 8);
  }
}

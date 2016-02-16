package cgresearch.graphics.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.polygon.Polygon;
import cgresearch.graphics.datastructures.polygon.PolygonEdge;
import cgresearch.graphics.datastructures.polygon.PolygonVertex;

public class QuadricErrorMetricsSimplification2D {

  /**
   * Processed polygon.
   */
  private Polygon polygon;

  /**
   * List of QEMs for the points.
   */
  private Map<PolygonVertex, IMatrix3> pointQems = new HashMap<PolygonVertex, IMatrix3>();

  public QuadricErrorMetricsSimplification2D(Polygon polygon) {
    this.polygon = polygon;
  }

  /**
   * Reset state.
   */
  public void reset() {
    pointQems.clear();
    // Compute QEM for each vertex.
    for (int i = 0; i < polygon.getNumPoints(); i++) {
      PolygonVertex vertex = polygon.getPoint(i);
      pointQems.put(vertex, computePointQem(vertex));
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
    IVector3 normal = findPerpendicular2D(p.subtract(q));
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
  private IVector3 findPerpendicular2D(IVector3 v) {
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
    double errorMin = Double.POSITIVE_INFINITY;
    int optimalEdgeIndex = -1;
    IVector3 optimalPos = VectorMatrixFactory.newIVector3();
    IMatrix3 optimalQ = VectorMatrixFactory.newIMatrix3();
    for (int pointIndex = 0; pointIndex < polygon.getNumPoints(); pointIndex++) {
      ErrorComputationResult result = computeError(pointIndex);
      if (result.error < errorMin) {
        errorMin = result.error;
        optimalEdgeIndex = pointIndex;
        optimalPos.copy(result.newPos);
        optimalQ.copy(result.Q);
      }
    }

    if (optimalEdgeIndex >= 0) {
      optimalPos.set(2, 0);
      PolygonVertex p = polygon.collapse(optimalEdgeIndex, optimalPos);
      pointQems.put(p, optimalQ);
    } else {
      Logger.getInstance().error("Invalid edge index - should not happen.");
    }
  }

  class ErrorComputationResult {
    public ErrorComputationResult(double error, IMatrix3 Q, int edgeIndex, IVector3 newPos) {
      this.error = error;
      this.Q = Q;
      this.edgeIndex = edgeIndex;
      this.newPos = newPos;
    }

    public double error;
    public IMatrix3 Q;
    public int edgeIndex;
    public IVector3 newPos;
  }

  /**
   * Computes the error by collapsing the edge with the given index
   * 
   * @param pointIndex
   * @return
   */
  private ErrorComputationResult computeError(int edgeIndex) {

    PolygonEdge edge = polygon.getEdge(edgeIndex);
    PolygonVertex p0 = edge.getStartVertex();
    PolygonVertex p1 = edge.getEndVertex();

    IMatrix3 qem = pointQems.get(p0).add(pointQems.get(p1));

    IMatrix3 derivQem = VectorMatrixFactory.newIMatrix3(qem);
    derivQem.set(2, 0, 0);
    derivQem.set(2, 1, 0);
    derivQem.set(2, 2, 1);
    double det = Math.abs(derivQem.getDeteterminant());
    if (det > 1e-5) {
      // Matrix is invertible
      IMatrix3 invQ = derivQem.getInverse();
      IVector3 pos = invQ.multiply(VectorMatrixFactory.newIVector3(0, 0, 1));
      pos.set(2, 0);
      double error = pos.multiply(qem.multiply(pos));
      return new ErrorComputationResult(error, qem, edgeIndex, pos);
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
      return new ErrorComputationResult(minError, qem, edgeIndex, minPos);
    }
  }

  /**
   * Color-code edge by error.
   */
  public void computeEdgeErrorColor() {
    double errorMin = Double.POSITIVE_INFINITY;
    double errorMax = Double.NEGATIVE_INFINITY;
    for (int edgeIndex = 0; edgeIndex < polygon.getNumEdges(); edgeIndex++) {
      ErrorComputationResult result = computeError(edgeIndex);
      if (result.error < errorMin) {
        errorMin = result.error;
      }
      if (result.error > errorMax) {
        errorMax = result.error;
      }
    }
    for (int edgeIndex = 0; edgeIndex < polygon.getNumEdges(); edgeIndex++) {
      ErrorComputationResult result = computeError(edgeIndex);
      IVector3 color =
          VectorMatrixFactory.newIVector3(transferFunction((result.error - errorMin) / (errorMax - errorMin)), 0, 0);
      polygon.setEdgeColor(edgeIndex, color);
    }
  }

  private double transferFunction(double x) {
    return Math.pow(1 - x, 8);
  }
}

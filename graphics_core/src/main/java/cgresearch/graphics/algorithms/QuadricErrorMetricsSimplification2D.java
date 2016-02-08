package cgresearch.graphics.algorithms;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.Polygon;

public class QuadricErrorMetricsSimplification2D {

  /**
   * Processed polygon.
   */
  private Polygon polygon;

  /**
   * List of QEMs for the points.
   */
  private List<IMatrix3> pointQems = new ArrayList<IMatrix3>();

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
      pointQems.add(computePointQem(i));
    }
  }

  private IMatrix3 computePointQem(int index) {
    IVector3 pMinus = polygon.getPoint((index + polygon.getNumPoints() - 1) % polygon.getNumPoints());
    IVector3 p = polygon.getPoint(index);
    IVector3 pPlus = polygon.getPoint((index + 1) % polygon.getNumPoints());
    IVector3 nMinus = findPerpendicular2D(p.subtract(pMinus));
    nMinus.normalize();
    double dMinus = nMinus.multiply(p);
    nMinus.set(2, -dMinus);
    IVector3 nPlus = findPerpendicular2D(pPlus.subtract(p));
    nPlus.normalize();
    double dPlus = nPlus.multiply(p);
    nPlus.set(2, -dPlus);
    IMatrix3 QMinus = nMinus.innerProduct(nMinus);
    IMatrix3 QPlus = nPlus.innerProduct(nPlus);
    IMatrix3 Q = QMinus.add(QPlus);

    // double x = p.multiply(Q.multiply(p));
    // IVector3 test = VectorMatrixFactory.newIVector3(p.get(0), p.get(1), 1);
    // System.out.println(test.multiply(Q.multiply(test)));

    return Q;
  }

  private IVector3 findPerpendicular2D(IVector3 v) {
    return VectorMatrixFactory.newIVector3(v.get(1), -v.get(0), 0);
  }

  /**
   * Apply one simplification step.
   */
  public void simplify() {
    double errorMin = Double.POSITIVE_INFINITY;
    int optimalEdgeIndex = -1;
    IVector3 optimalPos = null;
    IMatrix3 optimalQ = null;
    for (int pointIndex = 0; pointIndex < polygon.getNumPoints(); pointIndex++) {
      IMatrix3 Q = pointQems.get(pointIndex).add(pointQems.get((pointIndex + 1) % polygon.getNumPoints()));

      if (Math.abs(Q.getDeteterminant()) > 1e-5) {
        // Matrix is invertible
        IMatrix3 invQ = Q.getInverse();
        IVector3 pos = invQ.multiply(VectorMatrixFactory.newIVector3(0, 0, 1));
        double error = pos.multiply(Q.multiply(pos));
        if (error < errorMin) {
          errorMin = error;
          optimalEdgeIndex = pointIndex;
          optimalPos = pos;
          optimalQ = Q;
        }
      } else {
        // Matrix cannot be inverted, us corner points or midpoint
        List<IVector3> candidates = new ArrayList<IVector3>();
        IVector3 p0 = polygon.getPoint(pointIndex);
        IVector3 p1 = polygon.getPoint((pointIndex + 1) % polygon.getNumPoints());
        candidates.add(p0);
        candidates.add(p1);
        candidates.add((p0.add(p1)).multiply(0.5));
        for (IVector3 p : candidates) {
          IVector3 pos = VectorMatrixFactory.newIVector3(p);
          pos.set(2, 1);
          double error = pos.multiply(Q.multiply(pos));
          if (error < errorMin) {
            errorMin = error;
            optimalEdgeIndex = pointIndex;
            optimalPos = pos;
            optimalQ = Q;
          }
        }
      }
    }

    optimalPos.set(2, 0);
    polygon.collapse(optimalEdgeIndex, optimalPos);
    pointQems.remove(optimalEdgeIndex);
    pointQems.set(optimalEdgeIndex%polygon.getNumPoints(), optimalQ);
  }
}

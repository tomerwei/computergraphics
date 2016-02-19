package cgresearch.graphics.algorithms;

import java.util.Arrays;
import java.util.List;

import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.GenericEdge;
import cgresearch.graphics.datastructures.GenericVertex;
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
    reset();
  }

  @Override
  protected GenericEdge getEdge(int edgeIndex) {
    return polygon.getEdge(edgeIndex);
  }

  @Override
  protected GenericVertex getVertex(int vertexIndex) {
    return polygon.getPoint(vertexIndex);
  }

  @Override
  protected int getNumberOfVertices() {
    return polygon.getNumPoints();
  }

  @Override
  protected int getNumberOfEdges() {
    return polygon.getNumEdges();
  }

  @Override
  protected Matrix computePointQem(GenericVertex v) {
    if (!(v instanceof PolygonVertex)) {
      throw new IllegalArgumentException();
    }
    PolygonVertex vertex = (PolygonVertex) v;
    Matrix Q = initComputeEdgeQem(vertex.getIncomingEdge()).add(initComputeEdgeQem(vertex.getOutgoingEdge()));
    return Q;
  }

  /**
   * Computes the initial QEM for an edge.
   */
  private Matrix initComputeEdgeQem(PolygonEdge edge) {
    Vector p = edge.getStartVertex().getPosition();
    Vector q = edge.getEndVertex().getPosition();
    Vector perp = p.subtract(q);
    Vector normal = VectorMatrixFactory.newVector(perp.get(1), -perp.get(0), 0);
    normal.normalize();
    double distance = normal.multiply(p);
    Vector v = VectorMatrixFactory.newVector(normal.get(0), normal.get(1), normal.get(2), 1);
    v.set(3, -distance);
    Matrix Q = v.innerProduct(v);
    return Q;
  }

  @Override
  protected GenericVertex collapse(GenericEdge edge, Vector newPos) {
    if (!(edge instanceof PolygonEdge)) {
      throw new IllegalArgumentException();
    }
    return polygon.collapse((PolygonEdge) edge, newPos);
  }

  @Override
  protected List<GenericEdge> getIncidentEdges(GenericVertex v) {
    if (!(v instanceof PolygonVertex)) {
      throw new IllegalArgumentException();
    }
    PolygonVertex vertex = (PolygonVertex) v;
    return Arrays.asList(vertex.getIncomingEdge(), vertex.getOutgoingEdge());
  }
}

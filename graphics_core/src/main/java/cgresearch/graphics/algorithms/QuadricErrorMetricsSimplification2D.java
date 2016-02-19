package cgresearch.graphics.algorithms;

import java.util.Arrays;
import java.util.List;

import cgresearch.core.math.IMatrix;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.IVector4;
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
  protected IMatrix computePointQem(GenericVertex v) {
    if (!(v instanceof PolygonVertex)) {
      throw new IllegalArgumentException();
    }
    PolygonVertex vertex = (PolygonVertex) v;
    IMatrix Q = initComputeEdgeQem(vertex.getIncomingEdge()).add(initComputeEdgeQem(vertex.getOutgoingEdge()));
    return Q;
  }

  /**
   * Computes the initial QEM for an edge.
   */
  private IMatrix initComputeEdgeQem(PolygonEdge edge) {
    IVector3 p = edge.getStartVertex().getPosition();
    IVector3 q = edge.getEndVertex().getPosition();
    IVector3 perp = p.subtract(q);
    IVector3 normal = VectorMatrixFactory.newIVector3(perp.get(1), -perp.get(0), 0);
    normal.normalize();
    double distance = normal.multiply(p);
    IVector4 v = VectorMatrixFactory.newIVector3(normal).makeHomogenious();
    v.set(3, -distance);
    IMatrix Q = v.innerProduct(v);
    return Q;
  }

  @Override
  protected GenericVertex collapse(GenericEdge edge, IVector3 newPos) {
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

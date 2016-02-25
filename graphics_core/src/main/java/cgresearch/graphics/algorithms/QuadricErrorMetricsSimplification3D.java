package cgresearch.graphics.algorithms;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.GenericEdge;
import cgresearch.graphics.datastructures.GenericVertex;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdge;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangle;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangleMeshTools;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangleMeshTools.HalfEdgeCollapse;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeVertex;

/**
 * Implementation of the Garland/Heckbert QEM method in 3D.
 * 
 * @author Philipp Jenke
 *
 */
public class QuadricErrorMetricsSimplification3D extends QuadrikErrorMetricsSimplification {
  private final HalfEdgeTriangleMesh mesh;
  private HalfEdgeTriangleMeshTools heTools = new HalfEdgeTriangleMeshTools();

  public QuadricErrorMetricsSimplification3D(HalfEdgeTriangleMesh mesh) {
    this.mesh = mesh;
  }

  @Override
  protected Matrix computePointQem(GenericVertex vertex) {
    if (!(vertex instanceof HalfEdgeVertex)) {
      throw new IllegalArgumentException();
    }
    HalfEdgeVertex v = (HalfEdgeVertex) vertex;
    List<HalfEdgeTriangle> incidentFacets = HalfEdgeTriangleMeshTools.getIncidentFacets(v);
    Matrix Q = new Matrix(4, 4);
    for (HalfEdgeTriangle triangle : incidentFacets) {
      Q = Q.add(initComputeQemTriangle(triangle));
    }
    return Q;
  }

  /**
   * Compute QEM for a triangle (initialization).
   */
  private Matrix initComputeQemTriangle(HalfEdgeTriangle triangle) {
    Vector normal = triangle.getNormal();
    double distance = normal.multiply(triangle.getHalfEdge().getStartVertex().getPosition());
    Vector v = VectorFactory.createVector4(normal.get(0), normal.get(1), normal.get(2), 1);
    v.set(3, -distance);
    Matrix Q = v.innerProduct(v);
    return Q;
  }

  @Override
  protected GenericEdge getEdge(int edgeIndex) {
    return mesh.getHalfEdge(edgeIndex);
  }

  @Override
  protected HalfEdgeVertex getVertex(int vertexIndex) {
    return mesh.getVertex(vertexIndex);
  }

  @Override
  protected int getNumberOfVertices() {
    return mesh.getNumberOfVertices();
  }

  @Override
  protected int getNumberOfEdges() {
    return mesh.getNumberOfHalfEdges();
  }

  @Override
  protected List<GenericEdge> getIncidentEdges(GenericVertex p) {
    if (!(p instanceof HalfEdgeVertex)) {
      throw new IllegalArgumentException();
    }
    HalfEdgeVertex vertex = (HalfEdgeVertex) p;
    List<HalfEdge> incidentEdges = HalfEdgeTriangleMeshTools.getIncidentHalfEdges(vertex);
    List<GenericEdge> result = new ArrayList<GenericEdge>(incidentEdges);
    return result;
  }

  @Override
  protected GenericVertex collapse(GenericEdge edge, Vector newPos) {
    if (!(edge instanceof HalfEdge)) {
      throw new IllegalArgumentException();
    }
    HalfEdge halfEdge = (HalfEdge) edge;
    HalfEdgeCollapse heCollapse = heTools.collapse(mesh, halfEdge);
    for (HalfEdge he : heCollapse.removedHalfEdges) {
      queue.remove(he);
    }
    heCollapse.remainingVertex.getPosition().copy(newPos);
    return heCollapse.remainingVertex;
  }
}

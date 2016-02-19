package cgresearch.graphics.algorithms;

import java.util.List;

import cgresearch.core.math.IMatrix;
import cgresearch.core.math.IVector3;
import cgresearch.graphics.datastructures.GenericEdge;
import cgresearch.graphics.datastructures.GenericVertex;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeVertex;

/**
 * Implementation of the Garland/Heckbert QEM method in 3D.
 * 
 * @author Philipp Jenke
 *
 */
public class QuadricErrorMetricsSimplification3D extends QuadrikErrorMetricsSimplification {
  private final HalfEdgeTriangleMesh mesh;

  public QuadricErrorMetricsSimplification3D(HalfEdgeTriangleMesh mesh) {
    this.mesh = mesh;
  }

  @Override
  protected IMatrix computePointQem(GenericVertex vertex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected GenericEdge getEdge(int edgeIndex) {
    // TODO
    return null;
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
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  protected List<GenericEdge> getIncidentEdges(GenericVertex p) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected GenericVertex collapse(GenericEdge edge, IVector3 newPos) {
    // TODO
    return null;
  }
}

package cgresearch.graphics.datastructures.trianglemesh;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IVector3;

/**
 * A triangle mesh a a list of facets, a list of half edges and a list of
 * vertices
 * 
 * @author Philipp Jenke
 *
 */
public class HalfEdgeTriangleMesh extends ITriangleMesh {

  private List<HalfEdgeTriangle> facets = new ArrayList<HalfEdgeTriangle>();

  private List<HalfEdgeVertex> vertices = new ArrayList<HalfEdgeVertex>();

  private List<HalfEdge> halfEdges = new ArrayList<HalfEdge>();

  public void addHalfEdge(HalfEdge halfEdge) {
    halfEdges.add(halfEdge);
  }

  /**
   * Create 3 half edges and one facet for the triangle. The referenced Vertices
   * must already be in the data structure.
   */
  @Override
  public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
    HalfEdge halfEdge1 = new HalfEdge();
    HalfEdge halfEdge2 = new HalfEdge();
    HalfEdge halfEdge3 = new HalfEdge();
    HalfEdgeTriangle facet = new HalfEdgeTriangle();
    halfEdge1.setNext(halfEdge2);
    halfEdge2.setNext(halfEdge3);
    halfEdge3.setNext(halfEdge1);
    halfEdge1.setStartVertex(vertices.get(vertexIndex1));
    halfEdge2.setStartVertex(vertices.get(vertexIndex2));
    halfEdge3.setStartVertex(vertices.get(vertexIndex3));
    halfEdge1.setFacet(facet);
    halfEdge2.setFacet(facet);
    halfEdge3.setFacet(facet);
    facet.setHalfEdge(halfEdge1);
    halfEdges.add(halfEdge1);
    halfEdges.add(halfEdge2);
    halfEdges.add(halfEdge3);
    facets.add(facet);
  }

  @Override
  public void addTriangle(ITriangle t) {
    if (!(t instanceof HalfEdgeTriangle)) {
      throw new IllegalArgumentException("Only works with HalfEdgeTriangles.");
    }
    facets.add((HalfEdgeTriangle) t);
  }

  @Override
  public int addVertex(IVertex v) {
    if (!(v instanceof HalfEdgeVertex)) {
      Logger.getInstance().error("Half edge triangle mesh only support HalfEdgeVertices);");
      return -1;
    }
    vertices.add((HalfEdgeVertex) v);
    return vertices.size() - 1;
  }

  @Override
  public int getNumberOfTriangles() {
    return facets.size();
  }

  @Override
  public int getNumberOfVertices() {
    return vertices.size();
  }

  public int getNumberOfHalfEdges() {
    return halfEdges.size();
  }

  @Override
  public HalfEdgeVertex getVertex(int index) {
    return vertices.get(index);
  }

  @Override
  public void clear() {
    vertices.clear();
    facets.clear();
    halfEdges.clear();
  }

  @Override
  public void computeTriangleNormals() {
    for (int triangleIndex = 0; triangleIndex < getNumberOfTriangles(); triangleIndex++) {
      HalfEdgeTriangle facet = facets.get(triangleIndex);
      HalfEdge he = facet.getHalfEdge();
      IVertex v1 = he.getStartVertex();
      IVertex v2 = he.getNext().getStartVertex();
      IVertex v3 = he.getNext().getNext().getStartVertex();
      IVector3 u = v2.getPosition().subtract(v1.getPosition());
      IVector3 v = v3.getPosition().subtract(v1.getPosition());
      IVector3 normal = u.cross(v).getNormalized();
      facet.setNormal(normal);
    }

    System.out.println("Successfully computed face normals.");
  }

  /**
   * Compute the vertex normals from the average of the adjacent facet normals.
   */
  public void computeVertexNormals() {
    for (int vertexIndex = 0; vertexIndex < getNumberOfVertices(); vertexIndex++) {
      HalfEdgeVertex vertex = getVertex(vertexIndex);
      HalfEdge startHE = vertex.getHalfEdge();
      IVector3 normal = startHE.getFacet().getNormal();
      HalfEdge currentHE = startHE.getOpposite().getNext();
      // int numberOfAdjacentFacets = 1;
      while (currentHE != startHE) {
        normal = normal.add(currentHE.getFacet().getNormal());
        currentHE = currentHE.getOpposite().getNext();
        // ÏnumberOfAdjacentFacets++;
      }
      vertex.setNormal(normal.getNormalized());
      // System.out.println("Number of adjacent facets: " +
      // numberOfAdjacentFacets);
    }

    System.out.println("Successfully computed vertex normals.");
  }

  @Override
  public HalfEdgeTriangle getTriangle(int facetIndex) {
    return facets.get(facetIndex);
  }

  /**
   * Finally, opposite half edges must be connected and each vertex must be
   * assigned a outgoing half edge.
   * 
   * Attention: O(n^2)!
   */
  public void connectHalfEdges() {
    // Set half edge for each vertex
    for (int halfEdgeIndex = 0; halfEdgeIndex < halfEdges.size(); halfEdgeIndex++) {
      HalfEdge halfEdge = halfEdges.get(halfEdgeIndex);
      halfEdge.getStartVertex().setHalfEgde(halfEdge);
    }

    // Connect opposite halfEdges
    for (int i = 0; i < getNumberOfHalfEdges(); i++) {
      HalfEdge halfEdge1 = getHalfEdge(i);
      for (int j = i + 1; j < getNumberOfHalfEdges(); j++) {
        HalfEdge halfEdge2 = getHalfEdge(j);
        if (halfEdge1.getStartVertex() == getEndVertex(halfEdge2)
            && halfEdge2.getStartVertex() == getEndVertex(halfEdge1)) {
          halfEdge1.setOpposite(halfEdge2);
          halfEdge2.setOpposite(halfEdge1);
        }
      }
    }

    System.out.println("Successfully connected half edges.");
  }

  /**
   * Return the end vertex of a half edge (not directly saved.
   * 
   * @param halfEdge
   *          The end vertex for this half edge is sought.
   * @return End index of the half edge, null on error.
   */
  private HalfEdgeVertex getEndVertex(HalfEdge halfEdge) {
    if (halfEdge == null || halfEdge.getNext() == null) {
      throw new IllegalArgumentException();
    }
    return halfEdge.getNext().getStartVertex();
  }

  public HalfEdge getHalfEdge(int halfEdgeIndex) {
    return halfEdges.get(halfEdgeIndex);
  }

  @Override
  public int addTextureCoordinate(IVector3 texCoord3f) {
    throw new UnsupportedOperationException("Method not implemented.", null);
  }

  @Override
  public int getNumberOfTextureCoordinates() {
    throw new UnsupportedOperationException("Method not implemented.", null);
  }

  @Override
  public IVector3 getTextureCoordinate(int index) {
    throw new UnsupportedOperationException("Method not implemented.", null);
  }

  @Override
  public void fitToUnitBox() {
    throw new UnsupportedOperationException("Method not implemented.", null);
  }

  @Override
  public void invertFaceNormals() {
    throw new UnsupportedOperationException("Method not implemented.", null);
  }

  @Override
  public void unite(ITriangleMesh otherMesh) {
    throw new UnsupportedOperationException("Method not implemented.", null);
  }
}

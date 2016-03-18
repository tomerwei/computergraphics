package cgresearch.graphics.datastructures.trianglemesh;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.algorithms.NodeMerger;
import cgresearch.graphics.material.Material;

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
      Vector u = v2.getPosition().subtract(v1.getPosition());
      Vector v = v3.getPosition().subtract(v1.getPosition());
      Vector normal = u.cross(v).getNormalized();
      facet.setNormal(normal);
    }

    Logger.getInstance().message("Successfully computed face normals.");
  }

  /**
   * Compute the vertex normals from the average of the adjacent facet normals.
   */
  public void computeVertexNormals() {
    for (int vertexIndex = 0; vertexIndex < getNumberOfVertices(); vertexIndex++) {
      HalfEdgeVertex vertex = getVertex(vertexIndex);
      HalfEdge startHE = vertex.getHalfEdge();
      Vector normal = startHE.getFacet().getNormal();
      HalfEdge currentHE = startHE.getOpposite().getNext();
      // int numberOfAdjacentFacets = 1;
      while (currentHE != startHE) {
        normal = normal.add(currentHE.getFacet().getNormal());
        currentHE = currentHE.getOpposite().getNext();
        // ÏnumberOfAdjacentFacets++;
      }
      vertex.setNormal(normal.getNormalized());
    }

    Logger.getInstance().message("Successfully computed vertex normals.");
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
      halfEdge.getStartVertex().setHalfEdge(halfEdge);
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
  public Vector getTextureCoordinate(int index) {
    return VectorFactory.createVector3(-1, -1, 0);
  }

  @Override
  public int addTextureCoordinate(Vector texCoord3f) {
    throw new UnsupportedOperationException("Method not implemented.", null);
  }

  @Override
  public int getNumberOfTextureCoordinates() {
    return 0;
  }

  @Override
  public void fitToUnitBox() {
    BoundingBox bb = getBoundingBox();
    Vector center = bb.getCenter();
    Vector diagonal = bb.getUpperRight().subtract(bb.getLowerLeft());
    double scale = Math.max(Math.max(diagonal.get(MathHelpers.INDEX_0), diagonal.get(MathHelpers.INDEX_1)),
        diagonal.get(MathHelpers.INDEX_2));
    for (int i = 0; i < vertices.size(); i++) {
      vertices.get(i).getPosition().copy(vertices.get(i).getPosition().subtract(center).multiply(1.0 / scale));
    }
  }

  @Override
  public BoundingBox getBoundingBox() {
    BoundingBox bbox = new BoundingBox();
    for (IVertex vertex : vertices) {
      bbox.add(vertex.getPosition());
    }
    return bbox;
  }

  @Override
  public void invertFaceNormals() {
    for (int vertexIndex = 0; vertexIndex < getNumberOfVertices(); vertexIndex++) {
      getVertex(vertexIndex).setNormal(getVertex(vertexIndex).getNormal().multiply(-1));
    }
    for (int triangleIndex = 0; triangleIndex < getNumberOfTriangles(); triangleIndex++) {
      getTriangle(triangleIndex).setNormal(getTriangle(triangleIndex).getNormal().multiply(-1));
    }
  }

  public void removeFacet(HalfEdgeTriangle facet) {
    facet.setHalfEdge(null);
    facets.remove(facet);
  }

  public void removeVertex(HalfEdgeVertex vertex) {
    vertex.setHalfEdge(null);
    vertices.remove(vertex);
  }

  public void removeHalfEdge(HalfEdge halfEdge) {
    halfEdge.setNext(null);
    halfEdge.setOpposite(null);
    halfEdge.setStartVertex(null);
    halfEdge.setFacet(null);
    halfEdges.remove(halfEdge);
  }

  public boolean checkConsistencyVertices() {
    boolean isConsistent = true;
    for (int i = 0; i < getNumberOfVertices(); i++) {
      HalfEdgeVertex vertex = getVertex(i);
      if (vertex.getHalfEdge() == null) {
        isConsistent = false;
      }
      if (!halfEdges.contains(vertex.getHalfEdge())) {
        isConsistent = false;
      }
    }
    return isConsistent;
  }

  public boolean checkConsistencyFacets() {
    boolean isConsistent = true;
    for (int i = 0; i < getNumberOfTriangles(); i++) {
      HalfEdgeTriangle facet = getTriangle(i);
      isConsistent = isConsistent && facet.getHalfEdge() != null;
      isConsistent = isConsistent && facet.getHalfEdge().getFacet() == getTriangle(i);
      isConsistent = isConsistent && facet.getHalfEdge() != null;
      isConsistent = isConsistent && halfEdges.contains(facet.getHalfEdge());
    }
    return isConsistent;
  }

  public boolean checkConsistencyHalfEdges() {
    boolean isConsistent = true;
    for (int i = 0; i < getNumberOfHalfEdges(); i++) {

      HalfEdge halfEdge = getHalfEdge(i);

      if (halfEdge.getOpposite() == null || halfEdge.getOpposite() == halfEdge
          || halfEdge != halfEdge.getOpposite().getOpposite()) {
        isConsistent = false;
        throw new IllegalArgumentException();
      }

      // Test facet link
      if (halfEdge.getFacet() != getHalfEdge(i).getNext().getFacet()) {
        isConsistent = false;
        throw new IllegalArgumentException();
      }

      // Test vertex
      if (halfEdge.getStartVertex() == null || !vertices.contains(halfEdge.getStartVertex())) {
        isConsistent = false;
        throw new IllegalArgumentException();
      }
    }

    return isConsistent;
  }

  public boolean checkConsistency() {
    boolean isConsistent = true;
    if (!checkConsistencyFacets()) {
      isConsistent = false;
      Logger.getInstance().error("Consistency check failed - facets inconsistent");
    }
    if (!checkConsistencyHalfEdges()) {
      isConsistent = false;
      Logger.getInstance().error("Consistency check failed - half edges inconsistent");
    }
    if (!checkConsistencyVertices()) {
      isConsistent = false;
      Logger.getInstance().error("Consistency check failed - vertices inconsistent");
    }
    return isConsistent;
  }

  @Override
  public void unite(ITriangleMesh otherMesh) {
    Material oldMaterial = new Material();
    oldMaterial.copyFrom(otherMesh.getMaterial());

    otherMesh = NodeMerger.merge(otherMesh, 1e-5);

    for (int i = 0; i < otherMesh.getNumberOfVertices(); i++) {
      addVertex(new HalfEdgeVertex(otherMesh.getVertex(i).getPosition()));
    }
    for (int i = 0; i < otherMesh.getNumberOfTriangles(); i++) {
      ITriangle t = otherMesh.getTriangle(i);
      addTriangle(t.getA(), t.getB(), t.getC());
    }
    connectHalfEdges();
    computeTriangleNormals();
    // computeVertexNormals();

    getMaterial().copyFrom(oldMaterial);
  }

  @Override
  public void copyFrom(ITriangleMesh mesh) {
    Material oldMaterial = new Material();
    oldMaterial.copyFrom(getMaterial());
    clear();
    unite(mesh);
    getMaterial().copyFrom(oldMaterial);
  }

  @Override
  public void removeTriangle(int triangleIndex) {
    throw new UnsupportedOperationException("Method removeTriangle() not implemented yet for HalfEdgeTriangleMesh.");
  }

  @Override
  public void split() {
    Material oldMaterial = new Material();
    oldMaterial.copyFrom(getMaterial());
    TriangleMesh mesh = new TriangleMesh();
    mesh.copyFrom(this);
    mesh.split();
    copyFrom(mesh);
    getMaterial().copyFrom(oldMaterial);
  }

  @Override
  public void setTriangleVisible(int triangleIndex, boolean visible) {
    throw new UnsupportedOperationException("Method setTriangleVisible() not implemente yet for HalfEdgeTriangleMesh.");
  }
}

package cgresearch.graphics.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;

/**
 * Compute subdivision on a 3D mesh.
 * 
 * @author Philipp Jenke
 */
public class Subdivision3D {
  /**
   * This mesh is subdivided.
   */
  private final ITriangleMesh mesh;

  public Subdivision3D(ITriangleMesh mesh) {
    this.mesh = mesh;
  }

  /**
   * Apply one step of loop subdivision
   */
  public void subdivide() {
    // Create edge list
    List<Edge> edges = createEdges();

    // Map vertex -> edges
    Map<Integer, List<Edge>> vertex2edgesMapping = createVertexEdgesMapping(edges);

    // Map edge -> triangles
    Map<Integer, List<Integer>> edge2Triangles = createEdgeTrianglesMapping(edges);
    if (edge2Triangles == null) {
      Logger.getInstance().error("[Subdivide] Failed to do subdivision step.");
      return;
    }

    // Create vector of new positions
    List<IVector3> newPositions = computeUpdatedVertexPositions(edges, vertex2edgesMapping, edge2Triangles);

    // Set updated positions
    setNewVertexPositions(newPositions);

    // Add new triangle
    createNewTriangles(edges);

    // Remove old triangles
    removeOldTriangles();

    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();
  }

  /**
   * Remove old (replaced) triangles.
   */
  private void removeOldTriangles() {
    int oldNumberOfTriangles = mesh.getNumberOfTriangles() / 5;
    for (int i = 0; i < oldNumberOfTriangles; i++) {
      mesh.removeTriangle(0);
    }
    Logger.getInstance()
        .debug("[Subdivision] Removed old triangles, #triangles = " + mesh.getNumberOfTriangles() + ".");
  }

  /**
   * Created new triangles for each old triangle.
   */
  private void createNewTriangles(List<Edge> edges) {
    int oldNumberOfTriangles = mesh.getNumberOfTriangles();
    int oldNumberOfVertices = mesh.getNumberOfVertices() - edges.size();
    for (int triangleIndex = 0; triangleIndex < oldNumberOfTriangles; triangleIndex++) {
      ITriangle t = mesh.getTriangle(triangleIndex);
      int a = t.getA();
      int b = t.getB();
      int c = t.getC();
      int ab = getEdge(a, b, edges) + oldNumberOfVertices;
      int bc = getEdge(b, c, edges) + oldNumberOfVertices;
      int ca = getEdge(c, a, edges) + oldNumberOfVertices;
      mesh.addTriangle(new Triangle(a, ab, ca));
      mesh.addTriangle(new Triangle(b, bc, ab));
      mesh.addTriangle(new Triangle(c, ca, bc));
      mesh.addTriangle(new Triangle(ab, bc, ca));
    }
    Logger.getInstance()
        .debug("[Subdivision] Created new triangles, #triangles = " + mesh.getNumberOfTriangles() + ".");
  }

  /**
   * Add new vertices, updated positions (old + new).
   */
  private void setNewVertexPositions(List<IVector3> newPositions) {
    int numberOfOldPoints = mesh.getNumberOfVertices();
    for (int i = 0; i < numberOfOldPoints; i++) {
      mesh.getVertex(i).getPosition().copy(newPositions.get(i));
    }
    for (int i = numberOfOldPoints; i < newPositions.size(); i++) {
      mesh.addVertex(new Vertex(newPositions.get(i)));
    }
    Logger.getInstance().debug("[Subdivision] Added new vertices, updated positions (old + new).");
  }

  /**
   * Compute the updated vertex positions (old + new vertices).
   */
  private List<IVector3> computeUpdatedVertexPositions(List<Edge> edges, Map<Integer, List<Edge>> vertex2edgesMapping,
      Map<Integer, List<Integer>> edge2Triangles) {
    List<IVector3> newPositions = new ArrayList<IVector3>();
    int oldNumberOfVertices = mesh.getNumberOfVertices();
    for (int i = 0; i < oldNumberOfVertices + edges.size(); i++) {
      newPositions.add(VectorMatrixFactory.newIVector3(0, 0, 0));
    }

    // Compute positions for old points
    computeUpdatedVertexPositionsOldPoints(vertex2edgesMapping, newPositions, oldNumberOfVertices);

    // Compute positions for new points
    computeUpdatedVertexPositionsNewPoints(edges, newPositions, oldNumberOfVertices, edge2Triangles);

    Logger.getInstance().debug("[Subdivision] Computed the updated vertex positions (old + new vertices).");
    return newPositions;
  }

  /**
   * Compute the updated vertex positions for the new points.
   */
  private void computeUpdatedVertexPositionsNewPoints(List<Edge> edges, List<IVector3> newPositions,
      int oldNumberOfVertices, Map<Integer, List<Integer>> edge2Triangles) {

    for (int edgeIndex = 0; edgeIndex < edges.size(); edgeIndex++) {
      if (edges.get(edgeIndex).isBoundary()) {
        // TODO: Compute boundary new position
        Edge edge = edges.get(edgeIndex);
        int vertexIndex = oldNumberOfVertices + edgeIndex;
        IVector3 a = mesh.getVertex(edge.getV0()).getPosition();
        IVector3 b = mesh.getVertex(edge.getV1()).getPosition();
        newPositions.get(vertexIndex).copy((a.add(b)).multiply(0.5));
      } else if (edge2Triangles.get(edgeIndex).size() == 2) {
        Edge edge = edges.get(edgeIndex);
        IVector3 a = mesh.getVertex(edge.getV0()).getPosition();
        IVector3 b = mesh.getVertex(edge.getV1()).getPosition();
        ITriangle t0 = mesh.getTriangle(edge2Triangles.get(edgeIndex).get(0));
        ITriangle t1 = mesh.getTriangle(edge2Triangles.get(edgeIndex).get(1));
        IVector3 c = mesh.getVertex(t0.getOther(edge.getV0(), edge.getV1())).getPosition();
        IVector3 d = mesh.getVertex(t1.getOther(edge.getV0(), edge.getV1())).getPosition();
        IVector3 newPosition =
            (a.multiply(3.0 / 8.0)).add(b.multiply(3.0 / 8.0)).add(c.multiply(1.0 / 8.0)).add(d.multiply(1.0 / 8.0));
        int vertexIndex = oldNumberOfVertices + edgeIndex;
        newPositions.get(vertexIndex).copy(newPosition);
      }
    }
  }

  /**
   * Compute the updated vertex positions for the old points.
   */
  private void computeUpdatedVertexPositionsOldPoints(Map<Integer, List<Edge>> vertex2edgesMapping,
      List<IVector3> newPositions, int oldNumberOfVertices) {
    for (int vertexIndex = 0; vertexIndex < oldNumberOfVertices; vertexIndex++) {
      List<Edge> vertexEdges = vertex2edgesMapping.get(vertexIndex);
      boolean isBoundary = false;
      for (Edge edge : vertexEdges) {
        if (edge.isBoundary()) {
          isBoundary = true;
        }
      }
      if (isBoundary) {
        // Boundary: neighbor vertices along boundary edges: 1/8, self: 3/4
        IVector3 newPosition = mesh.getVertex(vertexIndex).getPosition().multiply(3.0 / 4.0);
        int numberOfNeighbors = 0;
        for (int nIndex = 0; nIndex < vertexEdges.size(); nIndex++) {
          Edge edge = vertexEdges.get(nIndex);
          if (edge.isBoundary()) {
            int neighborVertexIndex = edge.getOther(vertexIndex);
            newPosition.addSelf(mesh.getVertex(neighborVertexIndex).getPosition().multiply(1.0 / 8.0));
            numberOfNeighbors++;
          }
        }
        if (numberOfNeighbors != 2) {
          throw new IllegalArgumentException("Counld not find boundary neighbor vertices.");
        }
        newPositions.get(vertexIndex).copy(newPosition);
      } else {
        double beta = (vertexEdges.size() > 3) ? 3.0 / (8.0 * vertexEdges.size()) : 3.0 / 16.0;
        IVector3 newPosition = mesh.getVertex(vertexIndex).getPosition().multiply(1 - vertexEdges.size() * beta);
        for (int nIndex = 0; nIndex < vertexEdges.size(); nIndex++) {
          newPosition
              .addSelf(mesh.getVertex(vertexEdges.get(nIndex).getOther(vertexIndex)).getPosition().multiply(beta));
        }
        newPositions.get(vertexIndex).copy(newPosition);
      }
    }
  }

  /**
   * Create a mapping from each vertex to its incident edges.
   */
  private Map<Integer, List<Edge>> createVertexEdgesMapping(List<Edge> edges) {
    Map<Integer, List<Edge>> vertex2Edges = new HashMap<Integer, List<Edge>>();
    for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
      vertex2Edges.put(i, new ArrayList<Edge>());
    }
    for (int i = 0; i < edges.size(); i++) {
      Edge edge = edges.get(i);
      vertex2Edges.get(edge.getV0()).add(edge);
      vertex2Edges.get(edge.getV1()).add(edge);
    }
    Logger.getInstance().debug("[Subdivision] Created vertex -> edges mapping.");
    return vertex2Edges;
  }

  /**
   * Create a mapping from each edge to its incident triangles.
   */
  private Map<Integer, List<Integer>> createEdgeTrianglesMapping(List<Edge> edges) {
    Map<Integer, List<Integer>> edge2Triangles = new HashMap<Integer, List<Integer>>();
    for (int edgeIndex = 0; edgeIndex < edges.size(); edgeIndex++) {
      edge2Triangles.put(edgeIndex, new ArrayList<Integer>());
    }
    for (int triangleIndex = 0; triangleIndex < mesh.getNumberOfTriangles(); triangleIndex++) {
      for (int edgeIndex = 0; edgeIndex < edges.size(); edgeIndex++) {
        Edge edge = edges.get(edgeIndex);
        for (int i = 0; i < 3; i++) {
          if (edge.equals(mesh.getTriangle(triangleIndex).get(i), mesh.getTriangle(triangleIndex).get((i + 1) % 3))) {
            if (!edge2Triangles.get(edgeIndex).contains(triangleIndex)) {
              edge2Triangles.get(edgeIndex).add(triangleIndex);
            }
          }
        }
      }
    }
    Logger.getInstance().debug("[Subdivision] Created triangle -> edges mapping.");

    for (int edgeIndex = 0; edgeIndex < edges.size(); edgeIndex++) {
      List<Integer> triangles = edge2Triangles.get(edgeIndex);
      if (triangles.size() == 1) {
        edges.get(edgeIndex).setIsBoundary(true);
      } else {
        edges.get(edgeIndex).setIsBoundary(false);
      }
    }

    return edge2Triangles;
  }

  /**
   * Create the list of edges in the mesh.
   */
  private List<Edge> createEdges() {
    List<Edge> edges = new ArrayList<Edge>();
    for (int triangleIndex = 0; triangleIndex < mesh.getNumberOfTriangles(); triangleIndex++) {
      ITriangle t = mesh.getTriangle(triangleIndex);
      for (int i = 0; i < 3; i++) {
        int v0 = t.get(i);
        int v1 = t.get((i + 1) % 3);
        Edge edge = new Edge(Math.min(v0, v1), Math.max(v0, v1));
        if (!edges.contains(edge)) {
          edges.add(edge);
        }
      }
    }
    Logger.getInstance().debug("[Subdivision] Created edge list with " + edges.size() + " edges.");
    return edges;
  }

  /**
   * Returns the edge index which connects the two given indices.
   */
  private int getEdge(int a, int b, List<Edge> edges) {
    for (int i = 0; i < edges.size(); i++) {
      if (edges.get(i).equals(a, b)) {
        return i;
      }
    }
    return -1;
  }
}

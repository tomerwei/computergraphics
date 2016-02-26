package cgresearch.graphics.datastructures.trianglemesh;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;

/**
 * Collection of tools for the half edge triangle mesh datastructure.
 * 
 * @author Philipp Jenke
 *
 */
public class HalfEdgeTriangleMeshTools {

  /**
   * Create a Half edge triangle mesh from a regular mesh.
   * 
   * @param mesh
   *          Mesh to be converted from.
   * @return Half edge triangle mesh representation.
   */
  public static HalfEdgeTriangleMesh fromMesh(ITriangleMesh mesh) {
    HalfEdgeTriangleMesh heMesh = new HalfEdgeTriangleMesh();
    fromMesh(heMesh, mesh);
    return heMesh;
  }

  /**
   * Create a Half edge triangle mesh from a regular mesh.
   * 
   * @param mesh
   *          Mesh to be converted from.
   * @return Half edge triangle mesh representation.
   */
  public static void fromMesh(HalfEdgeTriangleMesh heMesh, ITriangleMesh mesh) {
    heMesh.clear();
    for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
      heMesh.addVertex(new HalfEdgeVertex(mesh.getVertex(i).getPosition()));
    }
    for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
      ITriangle t = mesh.getTriangle(i);
      heMesh.addTriangle(t.getA(), t.getB(), t.getC());
    }
    heMesh.connectHalfEdges();
    heMesh.computeTriangleNormals();
    heMesh.computeVertexNormals();
  }

  public static List<HalfEdgeVertex> getAdjacentVertices(HalfEdgeVertex vertex) {
    List<HalfEdgeVertex> adjacentVertices = new ArrayList<HalfEdgeVertex>();

    // Start with the edge at the vertex.
    HalfEdge currentEdge = vertex.getHalfEdge();
    while (currentEdge != null) {
      HalfEdge opposite = currentEdge.getOpposite();
      if (opposite == null) {
        break;
      }
      // Vertex of opposite is adjacent
      HalfEdgeVertex adjacentVertex = opposite.getStartVertex();
      if (adjacentVertex == null || adjacentVertices.contains(adjacentVertex)) {
        break;
      } else {
        adjacentVertices.add(adjacentVertex);
      }
      currentEdge = opposite.getNext();
    }
    return adjacentVertices;
  }

  public static List<HalfEdgeTriangle> getIncidentFacets(HalfEdgeVertex vertex) {
    List<HalfEdgeTriangle> incidetFacets = new ArrayList<HalfEdgeTriangle>();

    // Start with the half edge at the vertex
    HalfEdge currentEdge = vertex.getHalfEdge();
    while (currentEdge != null) {
      HalfEdge opposite = currentEdge.getOpposite();
      if (opposite == null) {
        break;
      }

      // We use the facets of the opposite half edge here
      HalfEdgeTriangle incidentFacet = opposite.getFacet();
      if (incidentFacet == null || incidetFacets.contains(incidentFacet)) {
        break;
      } else {
        incidetFacets.add(incidentFacet);
      }
      currentEdge = opposite.getNext();
    }
    return incidetFacets;
  }

  public static List<HalfEdge> getIncidentHalfEdges(HalfEdgeVertex vertex) {
    List<HalfEdge> incidetEdges = new ArrayList<HalfEdge>();
    // Start with a the half edge connected to the vertex
    HalfEdge currentEdge = vertex.getHalfEdge();
    while (currentEdge != null) {
      if (incidetEdges.contains(currentEdge)) {
        break;
      }

      // Add the half edge in direction from the vertex
      incidetEdges.add(currentEdge);
      HalfEdge opposite = currentEdge.getOpposite();
      if (opposite == null) {
        break;
      }
      // Add the oppisite half edge
      incidetEdges.add(opposite);
      currentEdge = opposite.getNext();
    }
    return incidetEdges;
  }

  public static List<HalfEdgeVertex> getIncidentVertices(HalfEdgeTriangle facet) {
    List<HalfEdgeVertex> incidentVertices = new ArrayList<HalfEdgeVertex>();

    // Start with the half edge the facet
    HalfEdge halfEdge = facet.getHalfEdge();
    while (halfEdge != null) {
      HalfEdgeVertex vertex = halfEdge.getStartVertex();
      if (incidentVertices.contains(vertex)) {
        break;
      }
      // Add the next vertex
      incidentVertices.add(vertex);

      // Follow the half edge circle around the facet
      halfEdge = halfEdge.getNext();
    }
    return incidentVertices;
  }

  public static List<HalfEdgeTriangle> getIncidentFacets(HalfEdgeTriangle facet) {
    List<HalfEdgeTriangle> incidentFacets = new ArrayList<HalfEdgeTriangle>();

    // Start with the half edge the facet
    HalfEdge halfEdge = facet.getHalfEdge();
    while (halfEdge != null) {

      HalfEdge opposite = halfEdge.getOpposite();
      if (opposite == null) {
        break;
      }

      // Use the facet of the opposite half edge
      HalfEdgeTriangle incidentFacet = opposite.getFacet();
      if (incidentFacets.contains(incidentFacet)) {
        break;
      }
      incidentFacets.add(incidentFacet);

      // Follow the half edge circle around the facet
      halfEdge = halfEdge.getNext();
    }
    return incidentFacets;
  }

  public static List<HalfEdge> getIncidentHalfEdges(HalfEdgeTriangle facet) {
    List<HalfEdge> incidentHalfEdges = new ArrayList<HalfEdge>();

    // Start with half edge reference
    HalfEdge halfEdge = facet.getHalfEdge();
    while (halfEdge != null) {
      if (incidentHalfEdges.contains(halfEdge)) {
        break;
      }
      incidentHalfEdges.add(halfEdge);

      // Move to next half edge in order
      halfEdge = halfEdge.getNext();
    }
    return incidentHalfEdges;
  }

  public class HalfEdgeCollapse {
    public List<HalfEdge> removedHalfEdges = new ArrayList<HalfEdge>();
    public HalfEdgeVertex remainingVertex = null;
  }

  public HalfEdgeCollapse collapse(HalfEdgeTriangleMesh halfEdgeDataStructure, HalfEdge halfEdge) {

    if (halfEdgeDataStructure.getNumberOfVertices() <= 4) {
      Logger.getInstance().error("Cannot collapse any further - tetrahedron reached.");
      return null;
    }

    // Assemble information
    HalfEdge e2 = halfEdge.getNext();
    HalfEdge e0 = e2.getNext();
    if (e0.getNext() != halfEdge) {
      Logger.getInstance().error("Only valid half edge triangle meshes are supported - aborting collapse()");
      return null;
    }
    HalfEdge e3 = halfEdge.getOpposite();
    HalfEdge e4 = e3.getNext();
    HalfEdge e5 = e4.getNext();
    if (e5.getNext() != e3) {
      Logger.getInstance().error("Only valid half edge triangle meshes are supported - aborting collapse()");
      return null;
    }
    HalfEdgeVertex v0 = halfEdge.getStartVertex();
    HalfEdgeVertex v1 = e3.getStartVertex();
    HalfEdgeVertex v2 = e0.getStartVertex();
    HalfEdgeVertex v3 = e5.getStartVertex();
    List<HalfEdge> v1HalfEdges = getIncidentHalfEdges(v1);
    HalfEdge e0Opposite = e0.getOpposite();
    HalfEdge e2Opposite = e2.getOpposite();
    HalfEdge e4Opposite = e4.getOpposite();
    HalfEdge e5Opposite = e5.getOpposite();

    // Set new structure
    e0Opposite.setOpposite(e2Opposite);
    e2Opposite.setOpposite(e0Opposite);
    e4Opposite.setOpposite(e5Opposite);
    e5Opposite.setOpposite(e4Opposite);
    for (HalfEdge v1HalfEdge : v1HalfEdges) {
      if (v1HalfEdge.getStartVertex() == v1) {
        v1HalfEdge.setStartVertex(v0);
      }
    }

    // Update vertex information
    v0.setHalfEdge(e0Opposite);
    v0.getPosition().copy(v0.getPosition().add(v1.getPosition()).multiply(0.5));

    v2.setHalfEdge(e2Opposite);
    v3.setHalfEdge(e4Opposite);

    // Not used any more: halfEdge.getFacet(),
    // halfEdge.getOpposite().getFacet, v1, e0, halfEdge, e2, e3, e4, e5
    halfEdgeDataStructure.removeFacet(halfEdge.getFacet());
    halfEdgeDataStructure.removeFacet(halfEdge.getOpposite().getFacet());
    halfEdgeDataStructure.removeVertex(v1);
    halfEdgeDataStructure.removeHalfEdge(e0);
    halfEdgeDataStructure.removeHalfEdge(halfEdge);
    halfEdgeDataStructure.removeHalfEdge(e2);
    halfEdgeDataStructure.removeHalfEdge(e3);
    halfEdgeDataStructure.removeHalfEdge(e4);
    halfEdgeDataStructure.removeHalfEdge(e5);

    if (!halfEdgeDataStructure.checkConsistency()) {
      Logger.getInstance().error("Collapse caused an error!");
      throw new IllegalArgumentException();
    }

    // Success!
    HalfEdgeCollapse collapseInfo = new HalfEdgeCollapse();
    collapseInfo.removedHalfEdges.add(e0);
    collapseInfo.removedHalfEdges.add(halfEdge);
    collapseInfo.removedHalfEdges.add(e2);
    collapseInfo.removedHalfEdges.add(e3);
    collapseInfo.removedHalfEdges.add(e4);
    collapseInfo.removedHalfEdges.add(e5);
    collapseInfo.remainingVertex = v0;
    return collapseInfo;
  }
}

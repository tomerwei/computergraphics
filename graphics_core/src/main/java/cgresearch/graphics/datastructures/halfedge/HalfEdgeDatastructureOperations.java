/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.halfedge;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;

/**
 * Container class for operations based on the half edge data structure.
 * Implements to @see IHalfEdgeDatastructureOperations interface.
 * 
 * @author Philipp Jenke
 * 
 */
public class HalfEdgeDatastructureOperations implements
        IHalfEdgeDatastructureOperations {

    @Override
    public List<HalfEdgeVertex> getAdjacentVertices(HalfEdgeVertex vertex) {
        List<HalfEdgeVertex> adjacentVertices = new ArrayList<HalfEdgeVertex>();

        // Start with the edge at the vertex.
        HalfEdge currentEdge = vertex.getHalfEdge();
        while (currentEdge != null) {
            HalfEdge opposite = currentEdge.getOpposite();
            if (opposite == null) {
                break;
            }
            // Vertex of opposite is adjacent
            HalfEdgeVertex adjacentVertex = opposite.getVertex();
            if (adjacentVertex == null
                    || adjacentVertices.contains(adjacentVertex)) {
                break;
            } else {
                adjacentVertices.add(adjacentVertex);
            }
            currentEdge = opposite.getNext();
        }
        return adjacentVertices;
    }

    @Override
    public List<IHalfEdgeFacet> getIncidentFacets(HalfEdgeVertex vertex) {
        List<IHalfEdgeFacet> incidetFacets = new ArrayList<IHalfEdgeFacet>();

        // Start with the half edge at the vertex
        HalfEdge currentEdge = vertex.getHalfEdge();
        while (currentEdge != null) {
            HalfEdge opposite = currentEdge.getOpposite();
            if (opposite == null) {
                break;
            }

            // We use the facets of the opposite half edge here
            IHalfEdgeFacet incidentFacet = opposite.getFacet();
            if (incidentFacet == null || incidetFacets.contains(incidentFacet)) {
                break;
            } else {
                incidetFacets.add(incidentFacet);
            }
            currentEdge = opposite.getNext();
        }
        return incidetFacets;
    }

    @Override
    public List<HalfEdge> getIncidetEdges(HalfEdgeVertex vertex) {
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

    @Override
    public List<HalfEdgeVertex> getIncidentVertices(IHalfEdgeFacet facet) {
        List<HalfEdgeVertex> incidentVertices = new ArrayList<HalfEdgeVertex>();

        // Start with the half edge the facet
        HalfEdge halfEdge = facet.getHalfEdge();
        while (halfEdge != null) {
            HalfEdgeVertex vertex = halfEdge.getVertex();
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

    @Override
    public List<IHalfEdgeFacet> getIncidentFacets(IHalfEdgeFacet facet) {
        List<IHalfEdgeFacet> incidentFacets = new ArrayList<IHalfEdgeFacet>();

        // Start with the half edge the facet
        HalfEdge halfEdge = facet.getHalfEdge();
        while (halfEdge != null) {

            HalfEdge opposite = halfEdge.getOpposite();
            if (opposite == null) {
                break;
            }

            // Use the facet of the opposite half edge
            IHalfEdgeFacet incidentFacet = opposite.getFacet();
            if (incidentFacets.contains(incidentFacet)) {
                break;
            }
            incidentFacets.add(incidentFacet);

            // Follow the half edge circle around the facet
            halfEdge = halfEdge.getNext();
        }
        return incidentFacets;
    }

    @Override
    public List<HalfEdge> getIncidentHalfEdges(IHalfEdgeFacet facet) {
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

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.haw.cg.datastructures.halfedge.IHalfEdgeDatastructureOperations#collapse
     * (edu.haw.cg.datastructures.halfedge.HalfEdge)
     */
    @Override
    public boolean collapse(IHalfEdgeDatastructure halfEdgeDataStructure,
            HalfEdge halfEdge) {

        if (halfEdgeDataStructure.getNumberOfVertices() <= 4) {
            Logger.getInstance().error(
                    "Cannot collapse any further - tetrahedron reached.");
            return false;
        }

        // Assemble information
        HalfEdge e2 = halfEdge.getNext();
        HalfEdge e0 = e2.getNext();
        if (e0.getNext() != halfEdge) {
            Logger.getInstance()
                    .error("Only valid half edge triangle meshes are supported - aborting collapse()");
            return false;
        }
        HalfEdge e3 = halfEdge.getOpposite();
        HalfEdge e4 = e3.getNext();
        HalfEdge e5 = e4.getNext();
        if (e5.getNext() != e3) {
            Logger.getInstance()
                    .error("Only valid half edge triangle meshes are supported - aborting collapse()");
            return false;
        }
        HalfEdgeVertex v0 = halfEdge.getVertex();
        HalfEdgeVertex v1 = e3.getVertex();
        HalfEdgeVertex v2 = e0.getVertex();
        HalfEdgeVertex v3 = e5.getVertex();
        List<HalfEdge> v1HalfEdges = getIncidetEdges(v1);
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
            if (v1HalfEdge.getVertex() == v1) {
                v1HalfEdge.setVertex(v0);
            }
        }

        // Update vertex information
        v0.setHalfEdge(e0Opposite);
        v0.getPosition().copy(
                v0.getPosition().add(v1.getPosition()).multiply(0.5));

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
        }

        // Success!
        return true;
    }
}

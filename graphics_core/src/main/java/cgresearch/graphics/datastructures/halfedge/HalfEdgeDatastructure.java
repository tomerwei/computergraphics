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
 * Container for a half edge data structure.
 * 
 * @author Philipp Jenke
 * 
 */
public class HalfEdgeDatastructure implements IHalfEdgeDatastructure {

    /**
     * List of vertices.
     */
    private List<HalfEdgeVertex> vertices = new ArrayList<HalfEdgeVertex>();

    /**
     * List of facets.
     */
    private List<IHalfEdgeFacet> facets = new ArrayList<IHalfEdgeFacet>();

    /**
     * List of half edges.
     */
    private List<HalfEdge> halfEdges = new ArrayList<HalfEdge>();

    /**
     * Constructor.
     */
    public HalfEdgeDatastructure() {

    }

    /*
     * (nicht-Javadoc)
     * 
     * @see edu.cg1.exercises.neighbordatastructure.IHalfEdgeDataStructure#
     * getNumberOfVertices()
     */
    @Override
    public int getNumberOfVertices() {
        return vertices.size();
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see edu.cg1.exercises.neighbordatastructure.IHalfEdgeDataStructure#
     * getNumberOfFacets()
     */
    @Override
    public int getNumberOfFacets() {
        return facets.size();
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see edu.cg1.exercises.neighbordatastructure.IHalfEdgeDataStructure#
     * getNumberOfHalfEdges()
     */
    @Override
    public int getNumberOfHalfEdges() {
        return halfEdges.size();
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.cg1.exercises.neighbordatastructure.IHalfEdgeDataStructure#getVertex
     * ()
     */
    @Override
    public HalfEdgeVertex getVertex(int index) {
        return vertices.get(index);
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.cg1.exercises.neighbordatastructure.IHalfEdgeDataStructure#getFacet
     * (int)
     */
    @Override
    public IHalfEdgeFacet getFacet(int index) {
        return facets.get(index);
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.cg1.exercises.neighbordatastructure.IHalfEdgeDataStructure#getHalfEdge
     * (int)
     */
    @Override
    public HalfEdge getHalfEdge(int index) {
        return halfEdges.get(index);
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.cg1.exercises.neighbordatastructure.IHalfEdgeDatastructure#addVertex
     * (edu.cg1.exercises.neighbordatastructure.Vertex)
     */
    @Override
    public int addVertex(HalfEdgeVertex vertex) {
        vertices.add(vertex);
        vertex.setName("" + (vertices.size() - 1));
        return vertices.size() - 1;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.cg1.exercises.neighbordatastructure.IHalfEdgeDatastructure#addFacet
     * (edu.cg1.exercises.neighbordatastructure.Facet)
     */
    @Override
    public int addFacet(IHalfEdgeFacet facet) {
        facets.add(facet);
        return facets.size() - 1;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.cg1.exercises.neighbordatastructure.IHalfEdgeDatastructure#addHalfEdge
     * (edu.cg1.exercises.neighbordatastructure.HalfEdge)
     */
    @Override
    public int addHalfEdge(HalfEdge halfEdge) {
        halfEdges.add(halfEdge);
        halfEdge.setName("" + (halfEdges.size() - 1));
        return halfEdges.size() - 1;
    }

    @Override
    public void removeFacet(IHalfEdgeFacet facet) {
        facet.setHalfEdge(null);
        facets.remove(facet);
    }

    @Override
    public void removeVertex(HalfEdgeVertex vertex) {
        vertex.setHalfEdge(null);
        vertex.setName("invalid");
        vertices.remove(vertex);
    }

    @Override
    public void removeHalfEdge(HalfEdge halfEdge) {
        halfEdge.setNext(null);
        halfEdge.setPrev(null);
        halfEdge.setOpposite(null);
        halfEdge.setVertex(null);
        halfEdge.setFacet(null);
        // halfEdge.setName("invalid");
        halfEdges.remove(halfEdge);
    }

    @Override
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

    /*
     * (nicht-Javadoc)
     * 
     * @see edu.haw.cg.datastructures.halfedge.IHalfEdgeDatastructure#
     * checkConsistencyFacets()
     */
    @Override
    public boolean checkConsistencyFacets() {
        boolean isConsistent = true;
        for (int i = 0; i < getNumberOfFacets(); i++) {
            IHalfEdgeFacet facet = getFacet(i);
            isConsistent = isConsistent && facet.getHalfEdge() != null;
            isConsistent = isConsistent
                    && facet.getHalfEdge().getFacet() == getFacet(i);
            isConsistent = isConsistent && facet.getHalfEdge() != null;
            isConsistent = isConsistent
                    && halfEdges.contains(facet.getHalfEdge());
        }
        return isConsistent;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see edu.haw.cg.datastructures.halfedge.IHalfEdgeDatastructure#
     * checkConsistencyHalfEdges()
     */
    @Override
    public boolean checkConsistencyHalfEdges() {
        boolean isConsistent = true;
        for (int i = 0; i < getNumberOfHalfEdges(); i++) {

            HalfEdge halfEdge = getHalfEdge(i);

            if (halfEdge.getOpposite() == null
                    || halfEdge.getOpposite() == halfEdge
                    || halfEdge != halfEdge.getOpposite().getOpposite()) {
                isConsistent = false;
            }

            // Test next() -> prev()
            if (halfEdge.getNext() == null
                    || halfEdge.getNext().getPrev() != getHalfEdge(i)) {
                isConsistent = false;
            }

            // Test prev() -> next()
            if (halfEdge.getPrev() == null
                    || halfEdge.getPrev().getNext() != getHalfEdge(i)) {
                isConsistent = false;
            }
            // Test facet link
            if (halfEdge.getFacet() != getHalfEdge(i).getNext().getFacet()) {
                isConsistent = false;
            }

            // Test vertex
            if (halfEdge.getVertex() == null
                    || !vertices.contains(halfEdge.getVertex())) {
                isConsistent = false;
            }
        }
        return isConsistent;
    }

    @Override
    public boolean checkConsistency() {
        boolean isConsistent = true;
        if (!checkConsistencyFacets()) {
            isConsistent = false;
            Logger.getInstance().error(
                    "Consistency check failed - facets inconsistent");
        }
        if (!checkConsistencyHalfEdges()) {
            isConsistent = false;
            Logger.getInstance().error(
                    "Consistency check failed - half edges inconsistent");
        }
        if (!checkConsistencyVertices()) {
            isConsistent = false;
            Logger.getInstance().error(
                    "Consistency check failed - vertices inconsistent");
        }
        return isConsistent;
    }
}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.halfedge;

/**
 * @author Philipp Jenke
 * 
 */
public interface IHalfEdgeDatastructure {
    /**
     * Getter for the number of vertices.
     */
    public int getNumberOfVertices();

    /**
     * Getter for the number of facets.
     */
    public int getNumberOfFacets();

    /**
     * Getter for the number of half edges.
     */
    public int getNumberOfHalfEdges();

    /**
     * Getter for a specified vertex.
     * 
     * @param index
     *            Index of the vertex.
     * @return Vertex at the specified position.
     */
    public HalfEdgeVertex getVertex(int index);

    /**
     * Getter for a specified facet.
     * 
     * @param index
     *            Index of the facet.
     * @return Facet at the specified index.
     */
    public IHalfEdgeFacet getFacet(int index);

    /**
     * Getter for a specified half edge.
     * 
     * @param index
     *            Position of the half edge.
     * @return Half edge at the specified index.
     */
    public HalfEdge getHalfEdge(int index);

    /**
     * Add a vertex to the data structure.
     * 
     * @param vertex
     *            New vertex.
     * @return Index of the vertex in the data structure.
     */
    public int addVertex(HalfEdgeVertex vertex);

    /**
     * Add a facet to the data structure.
     * 
     * @param facet
     *            New facet
     * @return Index of the facet in the data structure.
     */
    public int addFacet(IHalfEdgeFacet facet);

    /**
     * Add a half edge to the data structure.
     * 
     * @param halfEdge
     *            New half edge.
     * @return Index of the half edge in the data structure.
     */
    public int addHalfEdge(HalfEdge halfEdge);

    /**
     * Remove the facet from data structure.
     * 
     * @param facet
     *            Facet to be removed.
     */
    public void removeFacet(IHalfEdgeFacet facet);

    /**
     * Remove a vertex from the data structure.
     * 
     * @param vertex
     *            Vertex to be removed.
     */
    public void removeVertex(HalfEdgeVertex vertex);

    /**
     * Remove a half edge from the data structure.
     * 
     * @param halfEdge
     *            Half edge to be removed.
     */
    public void removeHalfEdge(HalfEdge halfEdge);

    /**
     * Check if the data structure is consistent - all vertices must be linked
     * to an edge.
     * 
     * @return True if data structure is consistent, false otherwise.
     */
    public boolean checkConsistencyVertices();

    /**
     * Check if the data structure is consistent - check facet properties.
     * (currently only borderless meshes supported).
     * 
     * @return True if the data structure is consistent, false otherwise.
     */
    public boolean checkConsistencyFacets();

    /**
     * Check if the data structure is consistent - check half edge properties.
     * 
     * @return True if the data structure is consistent, false otherwise.
     */
    public boolean checkConsistencyHalfEdges();

    /**
     * Check if the data structure is consistent.
     * 
     * @return True if the data structure is consistent, false otherwise.
     */
    public boolean checkConsistency();

}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.halfedge;

import cgresearch.core.math.IVector3;

/**
 * Representation of a vertex in the half edge date structure.
 * 
 * @author Philipp Jenke
 * 
 */
public class HalfEdgeVertex {

    /**
     * Name, used for debugging purposes.
     */
    private String name;

    /**
     * Position in 3-space
     */
    private final IVector3 position;

    /**
     * Reference to an adjacent half edge
     */
    private HalfEdge halfEdge = null;

    /**
     * Constructor
     * 
     * @param position
     *            Position of the vertex.
     */
    public HalfEdgeVertex(IVector3 position) {
        this.position = position;
    }

    /**
     * Getter
     */
    public HalfEdge getHalfEdge() {
        return halfEdge;
    }

    /**
     * Setter.
     */
    public void setHalfEdge(HalfEdge halfEdge) {
        this.halfEdge = halfEdge;
    }

    /**
     * Getter.
     */
    public IVector3 getPosition() {
        return position;
    }

    /**
     * Setter
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter
     */
    public String getName() {
        return name;
    }
}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.graphics.datastructures.halfedge.HalfEdgeDatastructureFactory;
import cgresearch.graphics.datastructures.halfedge.IHalfEdgeDatastructure;

/**
 * Testing functionality for the half edge data structure
 * 
 * @author Philipp Jenke
 * 
 */
public class TestHalfEdgeDatastructure {

    /**
     * Test the link information in all vertices
     */
    @Test
    public void testVertices() {
        HalfEdgeDatastructureFactory factory = new HalfEdgeDatastructureFactory();
        IHalfEdgeDatastructure ds = factory.createHalfEdgeDatastructure();
        assertTrue("Invalid vertex link.", ds.checkConsistencyVertices());
    }

    /**
     * Test the link information in all facets
     */
    @Test
    public void testFacets() {
        HalfEdgeDatastructureFactory factory = new HalfEdgeDatastructureFactory();
        IHalfEdgeDatastructure ds = factory.createHalfEdgeDatastructure();
        assertTrue(ds.checkConsistencyFacets());
    }

    /**
     * Test the link information in all half edges
     */
    @Test
    public void testHalfEdges() {
        HalfEdgeDatastructureFactory factory = new HalfEdgeDatastructureFactory();
        IHalfEdgeDatastructure ds = factory.createHalfEdgeDatastructure();
        assertTrue(ds.checkConsistencyHalfEdges());
    }
}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import cgresearch.graphics.datastructures.trianglemesh.HalfEdge;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangle;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangleMeshFactory;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangleMeshTools;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeVertex;

/**
 * Test class for the half edge data structure operations.
 * 
 * @author Philipp Jenke
 * 
 */
public class TestHalfEdgeTriangleMeshTools {

  /**
   * Test query vertex -> vertex
   */
  @Test
  public void testQueryVertexVertex() {
    HalfEdgeTriangleMeshFactory factory = new HalfEdgeTriangleMeshFactory();
    HalfEdgeTriangleMesh ds = factory.createHalfEdgeDatastructure();
    List<HalfEdgeVertex> queryResult = HalfEdgeTriangleMeshTools.getAdjacentVertices(ds.getVertex(2));
    HalfEdgeVertex[] expected = { ds.getVertex(0), ds.getVertex(1), ds.getVertex(6), ds.getVertex(3) };
    assertEquals("Query vertex -> vertex failed.", expected.length, queryResult.size());
    for (HalfEdgeVertex vertex : expected) {
      assertTrue("Query vertex -> vertex failed.", queryResult.contains(vertex));
    }
  }

  /**
   * Test query vertex -> facet
   */
  @Test
  public void testQueryVertexFacet() {
    HalfEdgeTriangleMeshFactory factory = new HalfEdgeTriangleMeshFactory();
    HalfEdgeTriangleMesh ds = factory.createHalfEdgeDatastructure();
    List<HalfEdgeTriangle> queryResult = HalfEdgeTriangleMeshTools.getIncidentFacets(ds.getVertex(2));
    HalfEdgeTriangle[] expected = { ds.getTriangle(0), ds.getTriangle(1), ds.getTriangle(2), ds.getTriangle(4) };
    assertEquals("Query vertex -> facet failed.", expected.length, queryResult.size());
    for (HalfEdgeTriangle facet : expected) {
      assertTrue("Query vertex -> facet failed.", queryResult.contains(facet));
    }
  }

  /**
   * Test query vertex -> half edge
   */
  @Test
  public void testQueryVertexHalfEdge() {
    HalfEdgeTriangleMeshFactory factory = new HalfEdgeTriangleMeshFactory();
    HalfEdgeTriangleMesh ds = factory.createHalfEdgeDatastructure();
    List<HalfEdge> queryResult = HalfEdgeTriangleMeshTools.getIncidentHalfEdges(ds.getVertex(2));
    HalfEdge[] expected = { ds.getHalfEdge(0), ds.getHalfEdge(5), ds.getHalfEdge(4), ds.getHalfEdge(6),
        ds.getHalfEdge(8), ds.getHalfEdge(12), ds.getHalfEdge(1), ds.getHalfEdge(14) };
    assertEquals("Query vertex -> half edge failed.", expected.length, queryResult.size());
    for (HalfEdge halfEdge : expected) {
      assertTrue("Query vertex -> half edge failed.", queryResult.contains(halfEdge));
    }
  }

  /**
   * Test query facet -> vertex
   */
  @Test
  public void testQueryFacetVertex() {
    HalfEdgeTriangleMeshFactory factory = new HalfEdgeTriangleMeshFactory();
    HalfEdgeTriangleMesh ds = factory.createHalfEdgeDatastructure();
    List<HalfEdgeVertex> queryResult = HalfEdgeTriangleMeshTools.getIncidentVertices(ds.getTriangle(4));
    HalfEdgeVertex[] expected = { ds.getVertex(2), ds.getVertex(3), ds.getVertex(6) };
    assertEquals("Query facet -> vertex failed.", expected.length, queryResult.size());
    for (HalfEdgeVertex vertex : expected) {
      assertTrue("Query facet -> vertex failed.", queryResult.contains(vertex));
    }
  }

  /**
   * Test query facet -> facet
   */
  @Test
  public void testQueryFacetFacet() {
    HalfEdgeTriangleMeshFactory factory = new HalfEdgeTriangleMeshFactory();
    HalfEdgeTriangleMesh ds = factory.createHalfEdgeDatastructure();
    List<HalfEdgeTriangle> queryResult = HalfEdgeTriangleMeshTools.getIncidentFacets(ds.getTriangle(4));
    HalfEdgeTriangle[] expected = { ds.getTriangle(0), ds.getTriangle(2), ds.getTriangle(5) };
    assertEquals("Query facet -> facet failed.", expected.length, queryResult.size());
    for (HalfEdgeTriangle facet : expected) {
      assertTrue("Query facet -> facet failed.", queryResult.contains(facet));
    }
  }

  /**
   * Test query facet -> half edge
   */
  @Test
  public void testQueryFacetHalfEdge() {
    HalfEdgeTriangleMeshFactory factory = new HalfEdgeTriangleMeshFactory();
    HalfEdgeTriangleMesh ds = factory.createHalfEdgeDatastructure();
    List<HalfEdge> queryResult = HalfEdgeTriangleMeshTools.getIncidentHalfEdges(ds.getTriangle(4));
    HalfEdge[] expected = { ds.getHalfEdge(12), ds.getHalfEdge(13), ds.getHalfEdge(14) };
    assertEquals("Query facet -> half edge failed.", expected.length, queryResult.size());
    for (HalfEdge halfEdge : expected) {
      assertTrue("Query facet -> half edge failed.", queryResult.contains(halfEdge));
    }
  }

  @Test
  public void testEdgeCollapse() {
    HalfEdgeTriangleMeshFactory factory = new HalfEdgeTriangleMeshFactory();
    HalfEdgeTriangleMesh ds = factory.createHalfEdgeDatastructure();
    HalfEdgeTriangleMeshTools tools = new HalfEdgeTriangleMeshTools();
    HalfEdge halfEdge = ds.getHalfEdge(3);
    int numberOfFacets = ds.getNumberOfTriangles();
    int numberOfHalfEdges = ds.getNumberOfHalfEdges();
    int numberOfVertices = ds.getNumberOfVertices();
    tools.collapse(ds, halfEdge);
    assertEquals(numberOfFacets, ds.getNumberOfTriangles() + 2);
    assertEquals(numberOfHalfEdges, ds.getNumberOfHalfEdges() + 6);
    assertEquals(numberOfVertices, ds.getNumberOfVertices() + 1);
  }
}

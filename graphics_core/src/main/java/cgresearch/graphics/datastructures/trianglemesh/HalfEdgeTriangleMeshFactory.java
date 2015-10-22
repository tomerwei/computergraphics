/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.trianglemesh;

import cgresearch.core.math.VectorMatrixFactory;

/**
 * Factory class for half edge data structures.
 * 
 * @author Philipp Jenke
 * 
 */
public class HalfEdgeTriangleMeshFactory {
  /**
   * Helper method for all query tests - create a half edge data structure for a
   * cube.
   * 
   * @return Half edge data structure.
   */
  public HalfEdgeTriangleMesh createHalfEdgeDatastructure() {
    HalfEdgeTriangleMesh ds = new HalfEdgeTriangleMesh();

    // Create and insert vertices
    ds.addVertex(new HalfEdgeVertex(VectorMatrixFactory.newIVector3(0, 0, 0)));
    ds.addVertex(new HalfEdgeVertex(VectorMatrixFactory.newIVector3(1, 0, 0)));
    ds.addVertex(new HalfEdgeVertex(VectorMatrixFactory.newIVector3(1, 1, 0)));
    ds.addVertex(new HalfEdgeVertex(VectorMatrixFactory.newIVector3(0, 1, 0)));
    ds.addVertex(new HalfEdgeVertex(VectorMatrixFactory.newIVector3(0, 0, 1)));
    ds.addVertex(new HalfEdgeVertex(VectorMatrixFactory.newIVector3(1, 0, 1)));
    ds.addVertex(new HalfEdgeVertex(VectorMatrixFactory.newIVector3(1, 1, 1)));
    ds.addVertex(new HalfEdgeVertex(VectorMatrixFactory.newIVector3(0, 1, 1)));

    // Create and insert facets
    for (int i = 0; i < 12; i++) {
      ds.addTriangle(new HalfEdgeTriangle());
    }

    // Create and insert half edges
    for (int i = 0; i < 36; i++) {
      ds.addHalfEdge(new HalfEdge());
    }

    // Link vertices
    ds.getVertex(0).setHalfEdge(ds.getHalfEdge(0));
    ds.getVertex(1).setHalfEdge(ds.getHalfEdge(9));
    ds.getVertex(2).setHalfEdge(ds.getHalfEdge(6));
    ds.getVertex(3).setHalfEdge(ds.getHalfEdge(2));
    ds.getVertex(4).setHalfEdge(ds.getHalfEdge(31));
    ds.getVertex(5).setHalfEdge(ds.getHalfEdge(10));
    ds.getVertex(6).setHalfEdge(ds.getHalfEdge(16));
    ds.getVertex(7).setHalfEdge(ds.getHalfEdge(17));

    // Link facets
    ds.getTriangle(0).setHalfEdge(ds.getHalfEdge(0));
    ds.getTriangle(1).setHalfEdge(ds.getHalfEdge(3));
    ds.getTriangle(2).setHalfEdge(ds.getHalfEdge(6));
    ds.getTriangle(3).setHalfEdge(ds.getHalfEdge(9));
    ds.getTriangle(4).setHalfEdge(ds.getHalfEdge(12));
    ds.getTriangle(5).setHalfEdge(ds.getHalfEdge(15));
    ds.getTriangle(6).setHalfEdge(ds.getHalfEdge(18));
    ds.getTriangle(7).setHalfEdge(ds.getHalfEdge(21));
    ds.getTriangle(8).setHalfEdge(ds.getHalfEdge(24));
    ds.getTriangle(9).setHalfEdge(ds.getHalfEdge(27));
    ds.getTriangle(10).setHalfEdge(ds.getHalfEdge(33));
    ds.getTriangle(11).setHalfEdge(ds.getHalfEdge(30));

    // Link half edges
    ds.getHalfEdge(0).setNext(ds.getHalfEdge(1));
    ds.getHalfEdge(0).setOpposite(ds.getHalfEdge(5));
    ds.getHalfEdge(0).setStartVertex(ds.getVertex(0));
    ds.getHalfEdge(0).setFacet(ds.getTriangle(0));
    ds.getHalfEdge(1).setNext(ds.getHalfEdge(2));
    ds.getHalfEdge(1).setOpposite(ds.getHalfEdge(14));
    ds.getHalfEdge(1).setStartVertex(ds.getVertex(2));
    ds.getHalfEdge(1).setFacet(ds.getTriangle(0));
    ds.getHalfEdge(2).setNext(ds.getHalfEdge(0));
    ds.getHalfEdge(2).setOpposite(ds.getHalfEdge(27));
    ds.getHalfEdge(2).setStartVertex(ds.getVertex(3));
    ds.getHalfEdge(2).setFacet(ds.getTriangle(0));
    ds.getHalfEdge(3).setNext(ds.getHalfEdge(4));
    ds.getHalfEdge(3).setOpposite(ds.getHalfEdge(32));
    ds.getHalfEdge(3).setStartVertex(ds.getVertex(0));
    ds.getHalfEdge(3).setFacet(ds.getTriangle(1));
    ds.getHalfEdge(4).setNext(ds.getHalfEdge(5));
    ds.getHalfEdge(4).setOpposite(ds.getHalfEdge(6));
    ds.getHalfEdge(4).setStartVertex(ds.getVertex(1));
    ds.getHalfEdge(4).setFacet(ds.getTriangle(1));
    ds.getHalfEdge(5).setNext(ds.getHalfEdge(3));
    ds.getHalfEdge(5).setOpposite(ds.getHalfEdge(0));
    ds.getHalfEdge(5).setStartVertex(ds.getVertex(2));
    ds.getHalfEdge(5).setFacet(ds.getTriangle(1));
    ds.getHalfEdge(6).setNext(ds.getHalfEdge(7));
    ds.getHalfEdge(6).setOpposite(ds.getHalfEdge(4));
    ds.getHalfEdge(6).setStartVertex(ds.getVertex(2));
    ds.getHalfEdge(6).setFacet(ds.getTriangle(2));
    ds.getHalfEdge(7).setNext(ds.getHalfEdge(8));
    ds.getHalfEdge(7).setOpposite(ds.getHalfEdge(11));
    ds.getHalfEdge(7).setStartVertex(ds.getVertex(1));
    ds.getHalfEdge(7).setFacet(ds.getTriangle(2));
    ds.getHalfEdge(8).setNext(ds.getHalfEdge(6));
    ds.getHalfEdge(8).setOpposite(ds.getHalfEdge(12));
    ds.getHalfEdge(8).setStartVertex(ds.getVertex(6));
    ds.getHalfEdge(8).setFacet(ds.getTriangle(2));
    ds.getHalfEdge(9).setNext(ds.getHalfEdge(10));
    ds.getHalfEdge(9).setOpposite(ds.getHalfEdge(35));
    ds.getHalfEdge(9).setStartVertex(ds.getVertex(1));
    ds.getHalfEdge(9).setFacet(ds.getTriangle(3));
    ds.getHalfEdge(10).setNext(ds.getHalfEdge(11));
    ds.getHalfEdge(10).setOpposite(ds.getHalfEdge(20));
    ds.getHalfEdge(10).setStartVertex(ds.getVertex(5));
    ds.getHalfEdge(10).setFacet(ds.getTriangle(3));
    ds.getHalfEdge(11).setNext(ds.getHalfEdge(9));
    ds.getHalfEdge(11).setOpposite(ds.getHalfEdge(7));
    ds.getHalfEdge(11).setStartVertex(ds.getVertex(6));
    ds.getHalfEdge(11).setFacet(ds.getTriangle(3));
    ds.getHalfEdge(12).setNext(ds.getHalfEdge(13));
    ds.getHalfEdge(12).setOpposite(ds.getHalfEdge(8));
    ds.getHalfEdge(12).setStartVertex(ds.getVertex(2));
    ds.getHalfEdge(12).setFacet(ds.getTriangle(4));
    ds.getHalfEdge(13).setNext(ds.getHalfEdge(14));
    ds.getHalfEdge(13).setOpposite(ds.getHalfEdge(15));
    ds.getHalfEdge(13).setStartVertex(ds.getVertex(6));
    ds.getHalfEdge(13).setFacet(ds.getTriangle(4));
    ds.getHalfEdge(14).setNext(ds.getHalfEdge(12));
    ds.getHalfEdge(14).setOpposite(ds.getHalfEdge(1));
    ds.getHalfEdge(14).setStartVertex(ds.getVertex(3));
    ds.getHalfEdge(14).setFacet(ds.getTriangle(4));
    ds.getHalfEdge(15).setNext(ds.getHalfEdge(16));
    ds.getHalfEdge(15).setOpposite(ds.getHalfEdge(13));
    ds.getHalfEdge(15).setStartVertex(ds.getVertex(3));
    ds.getHalfEdge(15).setFacet(ds.getTriangle(5));
    ds.getHalfEdge(16).setNext(ds.getHalfEdge(17));
    ds.getHalfEdge(16).setOpposite(ds.getHalfEdge(19));
    ds.getHalfEdge(16).setStartVertex(ds.getVertex(6));
    ds.getHalfEdge(16).setFacet(ds.getTriangle(5));
    ds.getHalfEdge(17).setNext(ds.getHalfEdge(15));
    ds.getHalfEdge(17).setOpposite(ds.getHalfEdge(24));
    ds.getHalfEdge(17).setStartVertex(ds.getVertex(7));
    ds.getHalfEdge(17).setFacet(ds.getTriangle(5));
    ds.getHalfEdge(18).setNext(ds.getHalfEdge(19));
    ds.getHalfEdge(18).setOpposite(ds.getHalfEdge(21));
    ds.getHalfEdge(18).setStartVertex(ds.getVertex(5));
    ds.getHalfEdge(18).setFacet(ds.getTriangle(6));
    ds.getHalfEdge(19).setNext(ds.getHalfEdge(20));
    ds.getHalfEdge(19).setOpposite(ds.getHalfEdge(16));
    ds.getHalfEdge(19).setStartVertex(ds.getVertex(7));
    ds.getHalfEdge(19).setFacet(ds.getTriangle(6));
    ds.getHalfEdge(20).setNext(ds.getHalfEdge(18));
    ds.getHalfEdge(20).setOpposite(ds.getHalfEdge(10));
    ds.getHalfEdge(20).setStartVertex(ds.getVertex(6));
    ds.getHalfEdge(20).setFacet(ds.getTriangle(6));
    ds.getHalfEdge(21).setNext(ds.getHalfEdge(22));
    ds.getHalfEdge(21).setOpposite(ds.getHalfEdge(18));
    ds.getHalfEdge(21).setStartVertex(ds.getVertex(7));
    ds.getHalfEdge(21).setFacet(ds.getTriangle(7));
    ds.getHalfEdge(22).setNext(ds.getHalfEdge(23));
    ds.getHalfEdge(22).setOpposite(ds.getHalfEdge(34));
    ds.getHalfEdge(22).setStartVertex(ds.getVertex(5));
    ds.getHalfEdge(22).setFacet(ds.getTriangle(7));
    ds.getHalfEdge(23).setNext(ds.getHalfEdge(21));
    ds.getHalfEdge(23).setOpposite(ds.getHalfEdge(25));
    ds.getHalfEdge(23).setStartVertex(ds.getVertex(4));
    ds.getHalfEdge(23).setFacet(ds.getTriangle(7));
    ds.getHalfEdge(24).setNext(ds.getHalfEdge(25));
    ds.getHalfEdge(24).setOpposite(ds.getHalfEdge(17));
    ds.getHalfEdge(24).setStartVertex(ds.getVertex(3));
    ds.getHalfEdge(24).setFacet(ds.getTriangle(8));
    ds.getHalfEdge(25).setNext(ds.getHalfEdge(26));
    ds.getHalfEdge(25).setOpposite(ds.getHalfEdge(23));
    ds.getHalfEdge(25).setStartVertex(ds.getVertex(7));
    ds.getHalfEdge(25).setFacet(ds.getTriangle(8));
    ds.getHalfEdge(26).setNext(ds.getHalfEdge(24));
    ds.getHalfEdge(26).setOpposite(ds.getHalfEdge(28));
    ds.getHalfEdge(26).setStartVertex(ds.getVertex(4));
    ds.getHalfEdge(26).setFacet(ds.getTriangle(8));
    ds.getHalfEdge(27).setNext(ds.getHalfEdge(28));
    ds.getHalfEdge(27).setOpposite(ds.getHalfEdge(2));
    ds.getHalfEdge(27).setStartVertex(ds.getVertex(0));
    ds.getHalfEdge(27).setFacet(ds.getTriangle(9));
    ds.getHalfEdge(28).setNext(ds.getHalfEdge(29));
    ds.getHalfEdge(28).setOpposite(ds.getHalfEdge(26));
    ds.getHalfEdge(28).setStartVertex(ds.getVertex(3));
    ds.getHalfEdge(28).setFacet(ds.getTriangle(9));
    ds.getHalfEdge(29).setNext(ds.getHalfEdge(27));
    ds.getHalfEdge(29).setOpposite(ds.getHalfEdge(30));
    ds.getHalfEdge(29).setStartVertex(ds.getVertex(4));
    ds.getHalfEdge(29).setFacet(ds.getTriangle(9));
    ds.getHalfEdge(30).setNext(ds.getHalfEdge(31));
    ds.getHalfEdge(30).setOpposite(ds.getHalfEdge(29));
    ds.getHalfEdge(30).setStartVertex(ds.getVertex(0));
    ds.getHalfEdge(30).setFacet(ds.getTriangle(11));
    ds.getHalfEdge(31).setNext(ds.getHalfEdge(32));
    ds.getHalfEdge(31).setOpposite(ds.getHalfEdge(33));
    ds.getHalfEdge(31).setStartVertex(ds.getVertex(4));
    ds.getHalfEdge(31).setFacet(ds.getTriangle(11));
    ds.getHalfEdge(32).setNext(ds.getHalfEdge(30));
    ds.getHalfEdge(32).setOpposite(ds.getHalfEdge(3));
    ds.getHalfEdge(32).setStartVertex(ds.getVertex(1));
    ds.getHalfEdge(32).setFacet(ds.getTriangle(11));
    ds.getHalfEdge(33).setNext(ds.getHalfEdge(34));
    ds.getHalfEdge(33).setOpposite(ds.getHalfEdge(31));
    ds.getHalfEdge(33).setStartVertex(ds.getVertex(1));
    ds.getHalfEdge(33).setFacet(ds.getTriangle(10));
    ds.getHalfEdge(34).setNext(ds.getHalfEdge(35));
    ds.getHalfEdge(34).setOpposite(ds.getHalfEdge(22));
    ds.getHalfEdge(34).setStartVertex(ds.getVertex(4));
    ds.getHalfEdge(34).setFacet(ds.getTriangle(10));
    ds.getHalfEdge(35).setNext(ds.getHalfEdge(33));
    ds.getHalfEdge(35).setOpposite(ds.getHalfEdge(9));
    ds.getHalfEdge(35).setStartVertex(ds.getVertex(5));
    ds.getHalfEdge(35).setFacet(ds.getTriangle(10));

    return ds;
  }
}

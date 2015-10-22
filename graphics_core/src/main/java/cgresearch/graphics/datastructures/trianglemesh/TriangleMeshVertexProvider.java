package cgresearch.graphics.datastructures.trianglemesh;

/**
 * Returns the corner vertices for a triangle.
 * 
 * @author Philipp Jenke
 *
 */
public class TriangleMeshVertexProvider {

  /**
   * Returns the corner vertex of the given triangle at the given index.
   * 
   * @param index
   *          Valid values: 0,1,2.
   * @param mesh
   *          Half edge triangle mesh (not used here).
   * @param triangle
   *          Triangle to be considered.
   * @return Vertex at the specified index.
   */
  public static IVertex getVertex(int index, HalfEdgeTriangleMesh mesh, HalfEdgeTriangle triangle) {
    if (index == 0) {
      return triangle.getHalfEdge().getStartVertex();
    } else if (index == 1) {
      return triangle.getHalfEdge().getNext().getStartVertex();
    } else if (index == 2) {
      return triangle.getHalfEdge().getNext().getNext().getStartVertex();
    } else {
      throw new IllegalArgumentException("Invalid index: " + index);
    }
  }

  /**
   * Returns the corner vertex of the given triangle at the given index.
   * 
   * @param index
   *          Valid values: 0,1,2.
   * @param mesh
   *          Triangle mesh.
   * @param triangle
   *          Triangle to be considered.
   * @return Vertex at the specified index.
   */
  public static IVertex getVertex(int index, TriangleMesh mesh, Triangle triangle) {
    return mesh.getVertex(triangle.get(index));
  }

  /**
   * Returns the corner vertex of the given triangle at the given index.
   * 
   * @param index
   *          Valid values: 0,1,2.
   * @param mesh
   *          Triangle mesh.
   * @param triangle
   *          Triangle to be considered.
   * @return Vertex at the specified index.
   */
  public static IVertex getVertex(int index, ITriangleMesh mesh, ITriangle triangle) {
    if (mesh instanceof HalfEdgeTriangleMesh && triangle instanceof HalfEdgeTriangle) {
      return getVertex(index, (HalfEdgeTriangleMesh) mesh, (HalfEdgeTriangle) triangle);
    } else if (mesh instanceof TriangleMesh && triangle instanceof Triangle) {
      return getVertex(index, (TriangleMesh) mesh, (Triangle) triangle);
    } else {
      throw new IllegalArgumentException("Invalid triangle mesh, triangle combination.");
    }
  }

}

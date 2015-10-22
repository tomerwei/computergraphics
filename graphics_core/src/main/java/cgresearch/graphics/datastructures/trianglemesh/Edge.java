package cgresearch.graphics.datastructures.trianglemesh;

/**
 * Representation of an edge Helper class for better performance with shadow
 * volumes
 */
public class Edge {
  /**
   * Indices of the vertices
   */
  private int[] vertexIndices = { -1, -1 };

  /**
   * Triangles using edge
   */
  private ITriangle[] triangles = { null, null };

  /**
   * Constructor
   */
  public Edge(int source, int target) {
    vertexIndices[0] = source;
    vertexIndices[1] = target;
  }

  /**
   * Getter
   * 
   * @return First vertex index
   */
  public int getA() {
    return vertexIndices[0];
  }

  /**
   * Getter
   * 
   * @return Second vertex index
   */
  public int getB() {
    return vertexIndices[1];
  }

  /**
   * Adds the given triangle to the edge
   *
   * @return true if triangle was added, false if this edge already has two
   *         triangles
   */
  public boolean addTriangle(ITriangle triangle) {
    for (int i = 0; i < triangles.length; i++) {
      if (triangles[i] == null) {
        triangles[i] = triangle;
        return true;
      }
    }
    return false;
  }

  /**
   * Removes the given triangle from the edge
   */
  public void removeTriangle(ITriangle triangle) {
    for (int i = 0; i < triangles.length; i++) {
      if (triangles[i] == triangle) {
        triangles[i] = null;
      }
    }
  }

  /**
   * Getter
   */
  public ITriangle getTriangle(int index) {
    if (index > 1 || index < 0) {
      return null;
    }
    return triangles[index];
  }

  /**
   * Getter
   * 
   * @return Returns the other triangle using this edge
   */
  public ITriangle getNeighbor(Triangle triangle) {
    for (int i = 0; i < triangles.length; i++) {
      if (triangles[i] != triangle) {
        return triangles[i];
      }
    }
    return null;
  }

  /**
   * Checks if the given triangle uses this edge
   */
  public boolean isTriangleUsingEdge(Triangle t) {
    for (int i = 0; i < triangles.length; i++) {
      if (t == triangles[i]) {
        return true;
      }
    }
    return false;
  }

  public int getIndexOfTriangle(Triangle t) {
    for (int i = 0; i < triangles.length; i++) {
      if (triangles[i] == t) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Edge) {
      if (obj == this) {
        return true;
      }
      Edge edge = (Edge) obj;
      return (edge.getA() == this.getA() && edge.getB() == this.getB())
          || (edge.getA() == this.getB() && edge.getB() == this.getA());
    }
    return false;
  }
}

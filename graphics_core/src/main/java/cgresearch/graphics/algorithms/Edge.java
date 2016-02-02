package cgresearch.graphics.algorithms;

public class Edge {

  /**
   * Start vertex index (smaller value).
   */
  private final int v0;

  /**
   * End vertex index (larger value).
   */
  private final int v1;
  
  /**
   * This flag indicates that the edge is a boundary edge.
   */
  private Boolean isBoundary = null;

  public Edge(int v0, int v1) {
    this.v0 = Math.min(v0, v1);
    this.v1 = Math.max(v0, v1);
  }

  public int getV0() {
    return v0;
  }

  public int getV1() {
    return v1;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof Edge)) {
      return false;
    }
    Edge otherEdge = (Edge) other;
    return v0 == otherEdge.v0 && v1 == otherEdge.v1;
  }

  /**
   * Checks if the edge connects the given indices.
   */
  public boolean equals(int a, int b) {
    return v0 == Math.min(a, b) && v1 == Math.max(a, b);
  }

  /**
   * Provides the other index of the edge.
   */
  public int getOther(int vertexIndex) {
    if (v0 == vertexIndex) {
      return v1;
    } else if (v1 == vertexIndex) {
      return v0;
    } else {
      throw new IllegalArgumentException("Invalid vertex index.");
    }
  }

  /**
   * Checks if the given index is in the edge.
   */
  public boolean contains(int index) {
    return v0 == index || v1 == index;
  }

  @Override
  public String toString() {
    return v0 + " <-> " + v1;
  }

  public void setIsBoundary(boolean value) {
    isBoundary = value;
  }

  public boolean isBoundary() {
    if (isBoundary == null) {
      throw new IllegalArgumentException("isBoundary not initialized yet!");
    }
    return isBoundary;
  }
}

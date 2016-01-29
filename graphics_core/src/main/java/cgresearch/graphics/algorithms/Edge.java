package cgresearch.graphics.algorithms;

public class Edge {

  private final int v0;
  private final int v1;

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
}

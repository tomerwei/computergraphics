package cgresearch.graphics.datastructures.trianglemesh;

import cgresearch.core.math.Vector;

/**
 * Parent interface for all triangle implementations
 * 
 * @author Philipp Jenke
 *
 */
public interface ITriangle {

  /**
   * Return index of first vertex.
   * 
   * @return Index of first vertex.
   */
  public int getA();

  /**
   * Return index of second vertex.
   * 
   * @return Index of second vertex.
   */
  public int getB();

  /**
   * Return index of third vertex.
   * 
   * @return Index of third vertex.
   */
  public int getC();

  /**
   * Return the texture coordinate for the vertex with the given index.
   * 
   * @param index
   *          Index of the vertex, Valid values: 0-2
   * @return Texture coordinate at the given vertex.
   */
  public int getTextureCoordinate(int index);

  public Vector getNormal();

  public void setNormal(Vector newNormal);

  /**
   * Returns the index of the i'th vertex.
   * 
   * @param i
   *          Index of the vertex (in 0-2).
   * @return Index of the vertex in the global vertex list.
   */
  public int get(int i);

  /**
   * Returns the index of the third (not provided) incident vertex
   */
  public int getOther(int a, int b);

  /**
   * Return true if the triangle is incident to the specified vertex index.
   */
  public boolean contains(int vertexIndex);

  /**
   * Return true if the triangle is visible, false otherwise.
   */
  public boolean isVisible();

  /**
   * Set visibility.
   */
  public void setVisible(boolean visible);

}

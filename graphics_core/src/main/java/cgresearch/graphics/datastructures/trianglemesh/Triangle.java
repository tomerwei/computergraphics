/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.trianglemesh;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Representation of a triangle consisting of three indices. The indices
 * reference vertices in the vertex list in a triangle mesh.
 * 
 * @author Philipp Jenke
 * 
 */
public class Triangle implements ITriangle {

  /**
   * Indices of the vertices.
   */
  private int[] vertexIndices = { -1, -1, -1 };

  /**
   * Indices of the vertices.
   */
  private int[] texCoordIndices = { -1, -1, -1 };

  /**
   * Normal of the triangle, initialized with a default direction.
   */
  private Vector normal = VectorMatrixFactory.newVector(1, 0, 0);

  /**
   * Default constructor
   * 
   * @param a
   *          Initial value.
   * @param b
   *          Initial value.
   * @param c
   *          Initial value.
   */
  public Triangle(int a, int b, int c) {
    vertexIndices[0] = a;
    vertexIndices[1] = b;
    vertexIndices[2] = c;
  }

  /**
   * Default constructor
   * 
   * @param a
   *          Initial value.
   * @param b
   *          Initial value.
   * @param c
   *          Initial value.
   */
  public Triangle(int a, int b, int c, int tA, int tB, int tC) {

    if (a < 0 || b < 0 || c < 0) {
      Logger.getInstance().error("Invalid vertex index in triangle.");
    }

    vertexIndices[0] = a;
    vertexIndices[1] = b;
    vertexIndices[2] = c;
    texCoordIndices[0] = tA;
    texCoordIndices[1] = tB;
    texCoordIndices[2] = tC;
  }

  /**
   * Constructor which immediately sets the normal
   * 
   * @param a
   *          Initial value.
   * @param b
   *          Initial value.
   * @param c
   *          Initial value.
   * @param normal
   *          Initial value.
   */
  public Triangle(int a, int b, int c, int tA, int tB, int tC, Vector normal) {
    vertexIndices[0] = a;
    vertexIndices[1] = b;
    vertexIndices[2] = c;
    this.normal = normal;
  }

  /**
   * @param triangle
   */
  public Triangle(ITriangle otherTriangle) {
    vertexIndices[0] = otherTriangle.getA();
    vertexIndices[1] = otherTriangle.getB();
    vertexIndices[2] = otherTriangle.getC();
    texCoordIndices[0] = otherTriangle.getTextureCoordinate(0);
    texCoordIndices[1] = otherTriangle.getTextureCoordinate(1);
    texCoordIndices[2] = otherTriangle.getTextureCoordinate(2);
    normal.copy(otherTriangle.getNormal());
  }

  /**
   * Getter.
   */
  public Vector getNormal() {
    return normal;
  }

  /**
   * Getter.
   * 
   * @return First vertex index.
   */
  public int getA() {
    return vertexIndices[0];
  }

  /**
   * Getter.
   * 
   * @return Second vertex index.
   */
  public int getB() {
    return vertexIndices[1];
  }

  /**
   * Getter.
   * 
   * @return Third vertex index.
   */
  public int getC() {
    return vertexIndices[2];
  }

  /**
   * Getter. Valid value for n are 0, 1, 2.
   * 
   * @return n'th vertex index.
   */
  public int get(int n) {
    return vertexIndices[n];
  }

  /**
   * Setter.
   */
  public void setNormal(Vector normal) {
    this.normal = normal;
  }

  /**
   * Getter
   * 
   * @param vIndex
   *          Index of the texture coordinate in the triangle. Valid values are
   *          0,1,2.
   * @return Index of the texture coordinate in the mesh data structure.
   */
  public int getTextureCoordinate(int vIndex) {
    return texCoordIndices[vIndex];
  }

  /**
   * Add an offset to all vertex indices.
   */
  public void addVertexIndexOffset(int verticesOffset) {
    for (int i = 0; i < 3; i++) {
      vertexIndices[i] += verticesOffset;
    }
  }

  /**
   * Add an offset to all texture coordinates.
   */
  public void addTexCoordOffset(int texCoordOffset) {
    for (int i = 0; i < 3; i++) {
      texCoordIndices[i] += texCoordOffset;
    }
  }

  @Override
  public int getOther(int a, int b) {
    if (!contains(a) || !contains(b)) {
      throw new IllegalArgumentException("Invalid indices.");
    }

    for (int i = 0; i < 3; i++) {
      if (vertexIndices[i] != a && vertexIndices[i] != b) {
        return vertexIndices[i];
      }
    }

    throw new IllegalArgumentException("Invalid indices.");
  }

  @Override
  public boolean contains(int index) {
    for (int i = 0; i < 3; i++) {
      if (vertexIndices[i] == index) {
        return true;
      }
    }
    return false;
  }
}

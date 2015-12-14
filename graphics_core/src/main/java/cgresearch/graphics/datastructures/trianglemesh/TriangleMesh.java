/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.trianglemesh;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Datastructure to represent a triangle mesh.
 * 
 * @author Philipp Jenke
 * 
 */
public class TriangleMesh extends ITriangleMesh {

  /**
   * Container for the triangles.
   */
  private List<ITriangle> triangles = new ArrayList<ITriangle>();

  /**
   * Container for the vertices.
   */
  private List<IVertex> vertices = new ArrayList<IVertex>();

  /**
   * Container for the texture coordinates..
   */
  private List<IVector3> textureCoordinates = new ArrayList<IVector3>();

  /**
   * Bounding box
   */
  private BoundingBox boundingBox = new BoundingBox();

  /**
   * Default constructor.
   */
  public TriangleMesh() {
  }

  /**
   * Copy constructor.
   */
  public TriangleMesh(ITriangleMesh other) {
    if (other instanceof TriangleMesh) {
      copyFrom((TriangleMesh) other);
    } else {
      Logger.getInstance().error("Cannot use triangle mesh copy constructor with ITriangleMesh instances.");
    }
  }

  /**
   * Copy all content (deep copy).
   */
  public void copyFrom(TriangleMesh other) {
    TriangleMesh otherTriangleMesh = (TriangleMesh) other;
    getMaterial().copyFrom(other.getMaterial());

    triangles = new ArrayList<ITriangle>();
    for (int i = 0; i < otherTriangleMesh.getNumberOfTriangles(); i++) {
      ITriangle triangle = new Triangle(other.getTriangle(i));
      triangles.add(triangle);
    }

    vertices = new ArrayList<IVertex>();
    for (int i = 0; i < otherTriangleMesh.getNumberOfVertices(); i++) {
      IVertex vertex = new Vertex(other.getVertex(i));
      vertices.add(vertex);
    }

    textureCoordinates = new ArrayList<IVector3>();
    for (int i = 0; i < otherTriangleMesh.getNumberOfTextureCoordinates(); i++) {
      IVector3 texCoord = VectorMatrixFactory.newIVector3(other.getTextureCoordinate(i));
      textureCoordinates.add(texCoord);
    }
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see
   * edu.cg1.exercises.mesh.ITriangleMesh#addTriangle(edu.cg1.exercises.mesh
   * .Triangle)
   */
  @Override
  public void addTriangle(ITriangle t) {
    triangles.add(t);
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.cg1.exercises.mesh.ITriangleMesh#addVertex(double, double, double)
   */
  @Override
  public int addVertex(IVertex v) {
    vertices.add(v);
    updateBoundingBox = true;
    return vertices.size() - 1;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.cg1.exercises.mesh.ITriangleMesh#getNumberOfTriangle()
   */
  @Override
  public int getNumberOfTriangles() {
    return triangles.size();
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.cg1.exercises.mesh.ITriangleMesh#getNumberOfVertices()
   */
  @Override
  public int getNumberOfVertices() {
    return vertices.size();
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.cg1.exercises.mesh.ITriangleMesh#getTriangle(int)
   */
  @Override
  public ITriangle getTriangle(int index) {
    return triangles.get(index);
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.cg1.exercises.mesh.ITriangleMesh#getVertex(int)
   */
  @Override
  public IVertex getVertex(int index) {
    if (index >= 0 && index < vertices.size()) {
      return vertices.get(index);
    } else {
      Logger.getInstance().error("Invalid vertex index.");
      return null;
    }
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.cg1.exercises.mesh.ITriangleMesh#clear()
   */
  @Override
  public void clear() {
    vertices.clear();
    triangles.clear();
    updateBoundingBox = true;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.cg1.exercises.mesh.ITriangleMesh#clear()
   */
  @Override
  public void computeTriangleNormals() {

    for (int i = 0; i < getNumberOfTriangles(); i++) {
      ITriangle t = getTriangle(i);
      IVertex pA = getVertex(t.getA());
      IVertex pB = getVertex(t.getB());
      IVertex pC = getVertex(t.getC());
      if (pA != null && pB != null && pC != null) {
        IVector3 v1 = pB.getPosition().subtract(pA.getPosition());
        IVector3 v2 = pC.getPosition().subtract(pA.getPosition());
        IVector3 helperNormal = v1.cross(v2);
        helperNormal.normalize();
        t.setNormal(helperNormal);
      } else {
        t.setNormal(VectorMatrixFactory.newIVector3(1, 0, 0));
      }
    }
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see
   * edu.cg1.exercises.shader.IAdvancedTriangleMesh#addTextureCoordinate(javax
   * .vecmath.TexCoord3f)
   */
  @Override
  public int addTextureCoordinate(IVector3 texCoord3f) {
    textureCoordinates.add(texCoord3f);
    return textureCoordinates.size() - 1;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.cg1.exercises.shader.IAdvancedTriangleMesh#
   * getNumberOfTextureCoordinates ()
   */
  @Override
  public int getNumberOfTextureCoordinates() {
    return textureCoordinates.size();
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see
   * edu.cg1.exercises.shader.IAdvancedTriangleMesh#getTextureCoordinate(int)
   */
  @Override
  public IVector3 getTextureCoordinate(int index) {
    if (textureCoordinates.size() == 0) {
      return VectorMatrixFactory.newIVector3(0, 0, 0);
    }
    if (index >= textureCoordinates.size()) {
      Logger.getInstance().error("Invalid texture coordinate index.");
      return VectorMatrixFactory.newIVector3(0, 0, 0);
    }
    return textureCoordinates.get(index);
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see
   * edu.cg1.exercises.textures.IAdvancedTriangleMesh#computeVertexNormals()
   */
  @Override
  public void computeVertexNormals() {
    for (int vertexIndex = 0; vertexIndex < getNumberOfVertices(); vertexIndex++) {
      getVertex(vertexIndex).getNormal().copy(VectorMatrixFactory.newIVector3(0, 0, 0));
    }

    for (int triangleIndex = 0; triangleIndex < getNumberOfTriangles(); triangleIndex++) {
      ITriangle triangle = getTriangle(triangleIndex);
      for (int i = 0; i < 3; i++) {
        int vertexIndex = triangle.get(i);
        IVertex vertex = getVertex(vertexIndex);
        if (vertex != null) {
          IVector3 oldNormal = vertex.getNormal();
          IVector3 newNormal = oldNormal.add(triangle.getNormal());
          vertex.setNormal(newNormal);
        } else {
          Logger.getInstance().error("Invalid vertex index!");
        }

      }
    }

    for (int vertexIndex = 0; vertexIndex < getNumberOfVertices(); vertexIndex++) {
      IVertex vertex = getVertex(vertexIndex);
      vertex.getNormal().normalize();
    }
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.haw.cg.datastructures.ITriangleMesh#fitToUnitBox()
   */
  @Override
  public void fitToUnitBox() {
    BoundingBox bb = getBoundingBox();
    IVector3 center = bb.getCenter();
    IVector3 diagonal = bb.getUpperRight().subtract(bb.getLowerLeft());
    double scale = Math.max(Math.max(diagonal.get(MathHelpers.INDEX_0), diagonal.get(MathHelpers.INDEX_1)),
        diagonal.get(MathHelpers.INDEX_2));
    for (int i = 0; i < vertices.size(); i++) {
      vertices.get(i).getPosition().copy(vertices.get(i).getPosition().subtract(center).multiply(1.0 / scale));
    }
    setUpdateBoundingBox();
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.haw.cg.datastructures.IBoundingBoxed#getBoundingBox()
   */
  @Override
  public BoundingBox getBoundingBox() {
    if (updateBoundingBox) {
      // Necessary if clear forced the update
      boundingBox.setInitialized(false);
      for (IVertex vertex : vertices) {
        boundingBox.add(vertex.getPosition());
      }
      updateBoundingBox = false;
    }
    return boundingBox;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see
   * edu.haw.cg.datastructures.trianglemesh.ITriangleMesh#invertFaceNormals()
   */
  @Override
  public void invertFaceNormals() {
    for (int vertexIndex = 0; vertexIndex < getNumberOfVertices(); vertexIndex++) {
      getVertex(vertexIndex).setNormal(getVertex(vertexIndex).getNormal().multiply(-1));
    }
    for (int triangleIndex = 0; triangleIndex < getNumberOfTriangles(); triangleIndex++) {
      getTriangle(triangleIndex).setNormal(getTriangle(triangleIndex).getNormal().multiply(-1));
    }
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.haw.cg.datastructures.trianglemesh.ITriangleMesh#unite(edu.haw.cg
   * .datastructures.trianglemesh.ITriangleMesh)
   */
  @Override
  public void unite(ITriangleMesh otherMesh) {
    // Copy vertices
    int verticesOffset = getNumberOfVertices();
    for (int vertexIndex = 0; vertexIndex < otherMesh.getNumberOfVertices(); vertexIndex++) {
      addVertex(new Vertex(otherMesh.getVertex(vertexIndex)));
    }
    // Copy triangles
    for (int triangleIndex = 0; triangleIndex < otherMesh.getNumberOfTriangles(); triangleIndex++) {
      Triangle copyTriangle = new Triangle(otherMesh.getTriangle(triangleIndex));
      copyTriangle.addVertexIndexOffset(verticesOffset);
      addTriangle(copyTriangle);
    }
    textureCoordinates.clear();

  }

  @Override
  public void addTriangle(int indexA, int indexB, int indexC) {
    addTriangle(new Triangle(indexA, indexB, indexC));
  }

  @Override
  public void removeTriangle(int triangleIndex) {
    triangles.remove(triangleIndex);
  }
}

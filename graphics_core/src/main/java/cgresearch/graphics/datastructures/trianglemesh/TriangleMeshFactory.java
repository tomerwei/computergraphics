/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.trianglemesh;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Matrix;
import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.datastructures.primitives.Arrow;
import cgresearch.graphics.datastructures.primitives.Cylinder;
import cgresearch.graphics.datastructures.primitives.Line3D;
import cgresearch.graphics.material.Material;

/**
 * Factory class for triangle meshes.
 * 
 * @author Philipp Jenke
 * 
 */
public class TriangleMeshFactory {

  /**
   * Create the triangle mesh for the half (in y-direction) of a unit sphere at
   * the origin with radius 1.
   */
  public static ITriangleMesh createHalfSphere(int resolution) {
    ITriangleMesh mesh = createSphere(VectorFactory.createVector3(0, 0, 0), 1, resolution);
    List<Integer> removeVertices = new ArrayList<Integer>();
    // Find vertices to remove
    for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
      if (mesh.getVertex(i).getPosition().get(1) < 0) {
        removeVertices.add(i);
      }
    }

    // Remove triangle which contain the invalid vertices
    for (int vertexIndex : removeVertices) {
      for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
        if (mesh.getTriangle(i).contains(vertexIndex)) {
          mesh.removeTriangle(i);
          i--;
        }
      }
    }

    return mesh;
  }

  public static ITriangleMesh createSquare() {
    ITriangleMesh triangleMesh = new TriangleMesh();
    int a = triangleMesh.addVertex(new Vertex(VectorFactory.createVector3(-0.5, 0, -0.5)));
    int b = triangleMesh.addVertex(new Vertex(VectorFactory.createVector3(0.5, 0, -0.5)));
    int c = triangleMesh.addVertex(new Vertex(VectorFactory.createVector3(-0.5, 0, 0.5)));
    int d = triangleMesh.addVertex(new Vertex(VectorFactory.createVector3(0.5, 0, 0.5)));
    triangleMesh.addTriangle(new Triangle(a, b, c));
    triangleMesh.addTriangle(new Triangle(b, c, d));
    triangleMesh.computeTriangleNormals();
    triangleMesh.computeVertexNormals();
    return triangleMesh;
  }

  /**
   * Create a simple triangle mesh consisting of a single triangle.
   * 
   * @param mesh
   */
  public static ITriangleMesh createTriangle() {
    ITriangleMesh mesh = new TriangleMesh();

    // Corner points
    Vector pA = VectorFactory.createVector3(0.5f, 0, 0);
    Vector pB = VectorFactory.createVector3(0.5f, 1, 0);
    Vector pC = VectorFactory.createVector3(0, 0.5f, 1);

    // Add vertices to mesh
    mesh.addVertex(new Vertex(pA));
    mesh.addVertex(new Vertex(pB));
    mesh.addVertex(new Vertex(pC));

    // Create triangle, compute normal and add to mesh.
    Triangle t = new Triangle(0, 1, 2);

    mesh.addTriangle(t);
    mesh.computeTriangleNormals();

    return mesh;
  }

  /**
   * @param resolution
   * @param x
   * @param y
   * @return
   */
  private static int computeCircleIndex(int resolutionX, int resolutionY, int x, int y) {
    return resolutionY * (x % resolutionX) + (y % resolutionY);
  }

  /**
   * Create a simple textured quad.
   * 
   * @return Triangle mesh object.
   */
  public static ITriangleMesh createQuadWithTextureCoordinates() {

    ITriangleMesh mesh = new TriangleMesh();
    mesh.clear();

    // Vertices
    float size = 1.0f;
    Vertex p00 = new Vertex(VectorFactory.createVector3(-size / 2.0f, 0, -size / 2.0f),
        VectorFactory.createVector3(0, 1, 0));
    Vertex p01 = new Vertex(VectorFactory.createVector3(-size / 2.0f, 0, size / 2.0f),
        VectorFactory.createVector3(0, 1, 0));
    Vertex p10 = new Vertex(VectorFactory.createVector3(size / 2.0f, 0, -size / 2.0f),
        VectorFactory.createVector3(0, 1, 0));
    Vertex p11 = new Vertex(VectorFactory.createVector3(size / 2.0f, 0, size / 2.0f),
        VectorFactory.createVector3(0, 1, 0));
    int i00 = mesh.addVertex(p00);
    int i01 = mesh.addVertex(p01);
    int i10 = mesh.addVertex(p10);
    int i11 = mesh.addVertex(p11);

    // Texture coordinates
    int texCoord00 = mesh.addTextureCoordinate(VectorFactory.createVector3(0, 0, 0));
    int texCoord01 = mesh.addTextureCoordinate(VectorFactory.createVector3(0, 1, 0));
    int texCoord10 = mesh.addTextureCoordinate(VectorFactory.createVector3(1, 0, 0));
    int texCoord11 = mesh.addTextureCoordinate(VectorFactory.createVector3(1, 1, 0));

    // Triangles
    Triangle t1 = new Triangle(i00, i10, i01, texCoord00, texCoord10, texCoord01);
    Triangle t2 = new Triangle(i01, i10, i11, texCoord01, texCoord10, texCoord11);
    mesh.addTriangle(t1);
    mesh.addTriangle(t2);
    mesh.computeTriangleNormals();

    // Create and return shape object
    return mesh;
  }

  /**
   * Create a triangle mesh of a cube with side length 1.
   * 
   * @return
   */
  public static ITriangleMesh createCube() {
    ITriangleMesh mesh = new TriangleMesh();
    mesh.clear();

    double d = 0.5;
    int v000 = mesh.addVertex(new Vertex(VectorFactory.createVector3(-d, -d, -d)));
    int v010 = mesh.addVertex(new Vertex(VectorFactory.createVector3(-d, d, -d)));
    int v110 = mesh.addVertex(new Vertex(VectorFactory.createVector3(d, d, -d)));
    int v100 = mesh.addVertex(new Vertex(VectorFactory.createVector3(d, -d, -d)));
    int v001 = mesh.addVertex(new Vertex(VectorFactory.createVector3(-d, -d, d)));
    int v011 = mesh.addVertex(new Vertex(VectorFactory.createVector3(-d, d, d)));
    int v111 = mesh.addVertex(new Vertex(VectorFactory.createVector3(d, d, d)));
    int v101 = mesh.addVertex(new Vertex(VectorFactory.createVector3(d, -d, d)));

    int t00 = mesh.addTextureCoordinate(VectorFactory.createVector3(0, 0, 0));
    int t01 = mesh.addTextureCoordinate(VectorFactory.createVector3(0, 1, 0));
    int t10 = mesh.addTextureCoordinate(VectorFactory.createVector3(1, 0, 0));
    int t11 = mesh.addTextureCoordinate(VectorFactory.createVector3(1, 1, 0));

    // front
    mesh.addTriangle(new Triangle(v000, v100, v110, t00, t10, t11));
    mesh.addTriangle(new Triangle(v000, v110, v010, t00, t11, t01));
    // right
    mesh.addTriangle(new Triangle(v100, v101, v111, t00, t01, t11));
    mesh.addTriangle(new Triangle(v100, v111, v110, t00, t11, t10));
    // back
    mesh.addTriangle(new Triangle(v101, v001, v011, t10, t00, t01));
    mesh.addTriangle(new Triangle(v101, v011, v111, t10, t01, t11));
    // left
    mesh.addTriangle(new Triangle(v001, v000, v010, t01, t00, t10));
    mesh.addTriangle(new Triangle(v001, v010, v011, t01, t10, t11));
    // top
    mesh.addTriangle(new Triangle(v010, v110, v111, t00, t10, t11));
    mesh.addTriangle(new Triangle(v010, v111, v011, t00, t11, t01));
    // bottom
    mesh.addTriangle(new Triangle(000, v100, v101, t00, t10, t11));
    mesh.addTriangle(new Triangle(v000, v101, v001, t00, t11, t01));

    mesh.computeTriangleNormals();
    mesh.getMaterial().setRenderMode(Material.Normals.PER_FACET);

    return mesh;
  }

  /**
   * Create a triangle mesh representing a triangle from (0,0,0) to (1,0,0)
   */
  public static ITriangleMesh createLine3D(int resolution) {
    return createLine3D(resolution, 0.015f);
  }

  /**
   * Create a triangle mesh representing a triangle from (0,0,0) to (1,0,0)
   */
  public static ITriangleMesh createLine3D(int resolution, double radiusSmall) {
    ITriangleMesh mesh = new TriangleMesh();

    // Bottom
    for (int i = 0; i < resolution; i++) {
      double alpha = (double) i / (double) resolution * 2.0 * Math.PI;
      mesh.addVertex(
          new Vertex(VectorFactory.createVector3(0, Math.sin(alpha) * radiusSmall, Math.cos(alpha) * radiusSmall)));
    }
    // Top
    for (int i = 0; i < resolution; i++) {
      double alpha = (double) i / (double) resolution * 2.0 * Math.PI;
      mesh.addVertex(new Vertex(
          VectorFactory.createVector3(1.0f, Math.sin(alpha) * radiusSmall, Math.cos(alpha) * radiusSmall)));
    }

    // Triangles
    for (int i = 0; i < resolution; i++) {
      mesh.addTriangle(new Triangle(i, (i + 1) % resolution, resolution + (i + 1) % resolution));
      mesh.addTriangle(new Triangle(i, resolution + (i + 1) % resolution, resolution + i));
    }

    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();
    return mesh;
  }

  /**
   * Create a triangle mesh representing a triangle from (0,0,0) to (1,0,0)
   */
  public static ITriangleMesh createArrow() {
    ITriangleMesh mesh = new TriangleMesh();

    int RESOLUTION = 10;
    float radiusSmall = 0.035f;
    float radiusLarge = 0.08f;
    float segmentLength = 0.8f;
    // Bottom vertices
    for (int i = 0; i < RESOLUTION; i++) {
      double alpha = (double) i / (double) RESOLUTION * 2.0 * Math.PI;
      mesh.addVertex(
          new Vertex(VectorFactory.createVector3(0, Math.sin(alpha) * radiusSmall, Math.cos(alpha) * radiusSmall)));
    }
    // Shaft inner vertices
    for (int i = 0; i < RESOLUTION; i++) {
      double alpha = (double) i / (double) RESOLUTION * 2.0 * Math.PI;
      mesh.addVertex(new Vertex(VectorFactory.createVector3(segmentLength, Math.sin(alpha) * radiusSmall,
          Math.cos(alpha) * radiusSmall)));
    }
    // Shaft outer vertices
    for (int i = 0; i < RESOLUTION; i++) {
      double alpha = (double) i / (double) RESOLUTION * 2.0 * Math.PI;
      mesh.addVertex(new Vertex(VectorFactory.createVector3(segmentLength, Math.sin(alpha) * radiusLarge,
          Math.cos(alpha) * radiusLarge)));
    }
    int bottomIndex = mesh.addVertex(new Vertex(VectorFactory.createVector3(0, 0, 0)));
    int topIndex = mesh.addVertex(new Vertex(VectorFactory.createVector3(1, 0, 0)));

    // Triangles at the bottom
    for (int i = 0; i < RESOLUTION; i++) {
      mesh.addTriangle(new Triangle(i, (i + 1) % RESOLUTION, bottomIndex));
    }
    // Triangles bottom -> shaft
    for (int i = 0; i < RESOLUTION; i++) {
      int iPlus = (i + 1) % RESOLUTION;
      mesh.addTriangle(new Triangle(i, iPlus, RESOLUTION + iPlus));
      mesh.addTriangle(new Triangle(i, RESOLUTION + iPlus, RESOLUTION + i));
    }
    // Triangles shaft inner -> shaft outer
    for (int i = 0; i < RESOLUTION; i++) {
      int iPlus = (i + 1) % RESOLUTION;
      mesh.addTriangle(new Triangle(RESOLUTION + i, RESOLUTION + iPlus, 2 * RESOLUTION + iPlus));
      mesh.addTriangle(new Triangle(RESOLUTION + i, 2 * RESOLUTION + iPlus, 2 * RESOLUTION + i));
    }
    // Triangles at top
    for (int i = 0; i < RESOLUTION; i++) {
      int iPlus = (i + 1) % RESOLUTION;
      mesh.addTriangle(new Triangle(2 * RESOLUTION + i, 2 * RESOLUTION + iPlus, topIndex));
    }

    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();
    return mesh;
  }

  /**
   * @return
   */
  public static ITriangleMesh createTetrahedron() {
    ITriangleMesh mesh = new TriangleMesh();
    mesh.clear();

    // Vertices
    // (±1, 0, −1/√2)
    // (0, ±1, 1/√2)
    int v0 = mesh.addVertex(new Vertex(VectorFactory.createVector3(0.5, 0.0, -0.5 / Math.sqrt(2))));
    int v1 = mesh.addVertex(new Vertex(VectorFactory.createVector3(-0.5, 0.0, -0.5 / Math.sqrt(2))));
    int v2 = mesh.addVertex(new Vertex(VectorFactory.createVector3(0.0, 0.5, 0.5 / Math.sqrt(2))));
    int v3 = mesh.addVertex(new Vertex(VectorFactory.createVector3(0.0, -0.5, 0.5 / Math.sqrt(2))));

    // Faces
    mesh.addTriangle(new Triangle(v0, v2, v1));
    mesh.addTriangle(new Triangle(v1, v3, v2));
    mesh.addTriangle(new Triangle(v0, v3, v2));
    mesh.addTriangle(new Triangle(v0, v3, v1));

    mesh.computeTriangleNormals();
    mesh.getMaterial().setRenderMode(Material.Normals.PER_FACET);

    return mesh;
  }

  /**
   * Create a mesh for a specific line.
   */
  public static ITriangleMesh createLine3D(Line3D line, int resolution, double radius) {
    ITriangleMesh mesh = TriangleMeshFactory.createLine3D(resolution, radius);
    mesh.getMaterial().setRenderMode(Material.Normals.PER_VERTEX);

    // Scale to the required length
    double length = line.getEnd().subtract(line.getStart()).getNorm();
    TriangleMeshTransformation.scale(mesh, length);

    // Rotate
    Vector direction = line.getEnd().subtract(line.getStart());
    direction.normalize();
    Matrix T = MatrixFactory.createCoordinateFrameX(direction);
    TriangleMeshTransformation.transform(mesh, T);

    // Translate start
    TriangleMeshTransformation.translate(mesh, line.getStart());
    return mesh;
  }

  /**
   * Create a mesh for a specific arrow.
   */
  public static ITriangleMesh createArrow(Arrow arrow) {
    ITriangleMesh mesh = TriangleMeshFactory.createArrow();
    mesh.getMaterial().setRenderMode(Material.Normals.PER_FACET);

    // Scale to the required length
    double length = arrow.getEnd().subtract(arrow.getStart()).getNorm();
    TriangleMeshTransformation.scale(mesh, length);

    // Rotate
    Vector direction = arrow.getEnd().subtract(arrow.getStart());
    direction.normalize();
    Matrix T = MatrixFactory.createCoordinateFrameX(direction);
    TriangleMeshTransformation.transform(mesh, T);

    // Translate start
    TriangleMeshTransformation.translate(mesh, arrow.getStart());

    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();

    return mesh;
  }

  /**
   * Create the triangle mesh for a unit sphere at the origin with radius 1.
   */
  public static ITriangleMesh createSphere(int resolution) {
    return createSphere(VectorFactory.createVector3(0, 0, 0), 1, resolution);
  }

  /**
   * Create the triangle mesh for a sphere with given origin an radius.
   */
  public static ITriangleMesh createSphere(Vector center, double radius, int resolution) {

    ITriangleMesh mesh = new TriangleMesh();

    int resolutionX = resolution;
    int resolutionY = resolutionX / 2;

    // Compute vertex coordinates
    double deltaX = Math.PI * 2.0 / (double) resolutionX;
    double deltaY = Math.PI / (double) (resolutionY + 1);
    for (int x = 0; x < resolutionX; x++) {
      for (int y = 0; y < resolutionY; y++) {
        double phi = x * deltaX;
        double theta = (y + 1) * deltaY;
        double u = phi / Math.PI;
        double v = theta / Math.PI;
        Vector p00 = VectorFactory.createVector3(radius * Math.sin(theta) * Math.cos(phi) + center.get(0),
            radius * Math.sin(theta) * Math.sin(phi) + center.get(1), radius * Math.cos(theta) + center.get(2));
        mesh.addVertex(new Vertex(p00));
        mesh.addTextureCoordinate(VectorFactory.createVector3(u, v, 0));
      }
    }
    // Add top and bottom index
    int bottomIndex = mesh
        .addVertex(new Vertex(VectorFactory.createVector3(center.get(0), center.get(1), center.get(2) - radius)));
    int topIndex = mesh
        .addVertex(new Vertex(VectorFactory.createVector3(center.get(0), center.get(1), center.get(2) + radius)));

    // Compute triangles
    for (int x = 0; x < resolutionX; x++) {
      for (int y = 0; y < resolutionY - 1; y++) {
        int a = computeCircleIndex(resolutionX, resolutionY, x, y);
        int b = computeCircleIndex(resolutionX, resolutionY, x + 1, y);
        int c = computeCircleIndex(resolutionX, resolutionY, x + 1, y + 1);
        int d = computeCircleIndex(resolutionX, resolutionY, x, y + 1);
        mesh.addTriangle(new Triangle(a, c, b, a, c, b));
        mesh.addTriangle(new Triangle(a, d, c, a, d, c));
      }
    }

    // Compute triangles for top and bottom
    for (int x = 0; x < resolutionX; x++) {
      int a = computeCircleIndex(resolutionX, resolutionY, x, 0);
      int b = computeCircleIndex(resolutionX, resolutionY, x + 1, 0);
      int c = topIndex;
      int cTexIndex = mesh.addTextureCoordinate(VectorFactory.createVector3((float) x / (float) resolutionX, 0, 0));
      mesh.addTriangle(new Triangle(a, b, c, a, b, cTexIndex));

      a = bottomIndex;
      b = computeCircleIndex(resolutionX, resolutionY, x + 1, resolutionY - 1);
      c = computeCircleIndex(resolutionX, resolutionY, x, resolutionY - 1);
      Vector texCoord = VectorFactory.createVector3((x + 0.5) / (float) resolutionX, 0.95, 0);
      int aTexIndex = mesh.addTextureCoordinate(texCoord);
      mesh.addTriangle(new Triangle(a, b, c, aTexIndex, b, c));
    }

    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();

    /*
     * Logger.getInstance().message( "Created sphere with " +
     * mesh.getNumberOfTriangles() + " triangles and " +
     * mesh.getNumberOfVertices() + " vertices.");
     */

    return mesh;
  }

  public static ITriangleMesh createCylinder(Cylinder cylinder, int resolution) {

    Vector dir = cylinder.getDirection();
    Matrix T = MatrixFactory.createCoordinateFrameY(dir);
    Vector d = VectorFactory.createVector3(T.get(0, 0), T.get(0, 1), T.get(0, 2));
    Vector x = d.multiply(cylinder.getRadius());

    ITriangleMesh mesh = new TriangleMesh();

    double deltaAngle = Math.PI * 2 / resolution;
    Vector top = cylinder.getPoint().add(cylinder.getDirection().multiply(cylinder.getRadius() * 2));

    // Points in the bottom circle
    for (int i = 0; i < resolution; i++) {
      Matrix R = MatrixFactory.createRotationMatrix(dir, i * deltaAngle);
      Vector p = cylinder.getPoint().add(R.multiply(x));
      mesh.addVertex(new Vertex(p));
    }
    // Points in the top circle
    for (int i = 0; i < resolution; i++) {
      Matrix R = MatrixFactory.createRotationMatrix(dir, i * deltaAngle);
      Vector p = top.add(R.multiply(x));
      mesh.addVertex(new Vertex(p));
    }
    // Top and bottom point
    int bottomIndex = mesh.addVertex(new Vertex(cylinder.getPoint()));
    int topIndex = mesh.addVertex(new Vertex(top));

    // Triangles
    for (int i = 0; i < resolution; i++) {
      mesh.addTriangle(new Triangle(i, (i + 1) % resolution, bottomIndex));
      mesh.addTriangle(new Triangle(i + resolution, (i + 1) % resolution + resolution, topIndex));

      mesh.addTriangle(new Triangle(i, (i + 1) % resolution, i + resolution));
      mesh.addTriangle(new Triangle(i + 1, i + resolution, (i + 1) % resolution + resolution));
    }

    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();

    return mesh;

  }

  /**
   * Create a mesh that represents a plane.
   */
  public static ITriangleMesh createPlane(Vector point, Vector normal, double extend) {
    Vector tangentU = VectorFactory.createVector3(1, 0, 0);
    if (Math.abs(tangentU.multiply(normal)) > 0.95) {
      tangentU = VectorFactory.createVector3(0, 1, 0);
    }
    Vector tangentV = normal.cross(tangentU).getNormalized();
    tangentU = tangentV.cross(normal).getNormalized();
    tangentU.multiplySelf(extend);
    tangentV.multiplySelf(extend);

    ITriangleMesh mesh = new TriangleMesh();

    mesh.addVertex(new Vertex(point.subtract(tangentU).subtract(tangentV)));
    mesh.addVertex(new Vertex(point.subtract(tangentU).add(tangentV)));
    mesh.addVertex(new Vertex(point.add(tangentU).add(tangentV)));
    mesh.addVertex(new Vertex(point.add(tangentU).subtract(tangentV)));
    mesh.addTriangle(new Triangle(0, 2, 1));
    mesh.addTriangle(new Triangle(0, 3, 2));

    mesh.getMaterial().setShaderId(Material.SHADER_WIREFRAME);

    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();
    return mesh;

  }
}

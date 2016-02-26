package cgresearch.graphics.algorithms;

import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;

/**
 * A collection of mesh tools.
 * 
 * @author Philipp Jenke
 *
 */
public class TriangleMeshTools {

  /**
   * Create the mesh Laplacian.
   * 
   * @param mesh
   *          Considered mesh
   * @return Laplacian.
   */
  public static Matrix createLaplacian(ITriangleMesh mesh) {
    // Create Laplacian
    Matrix L = new Matrix(mesh.getNumberOfVertices(), mesh.getNumberOfVertices());
    for (int triangleIndex = 0; triangleIndex < mesh.getNumberOfTriangles(); triangleIndex++) {
      ITriangle t = mesh.getTriangle(triangleIndex);
      L.set(t.getA(), t.getB(), -1);
      L.set(t.getB(), t.getA(), -1);
      L.set(t.getA(), t.getC(), -1);
      L.set(t.getC(), t.getA(), -1);
      L.set(t.getB(), t.getC(), -1);
      L.set(t.getC(), t.getB(), -1);
    }
    for (int rowIndex = 0; rowIndex < L.getNumberOfRows(); rowIndex++) {
      double valence = 0;
      for (int columnIndex = 0; columnIndex < L.getNumberOfColumns(); columnIndex++) {
        valence += L.get(rowIndex, columnIndex);
      }
      L.set(rowIndex, rowIndex, -valence);
    }
    return L;
  }

  /**
   * Add noise to the mesh vertices in normal direction.
   * 
   * @param mesh
   *          Mesh to be modified.
   * @param noiseSigma
   *          Maximum noise (distance from original position).
   */
  public static void addNoise(ITriangleMesh mesh, double noiseSigma) {
    for (int vertexIndex = 0; vertexIndex < mesh.getNumberOfVertices(); vertexIndex++) {
      Vector pos = mesh.getVertex(vertexIndex).getPosition();
      Vector normal = mesh.getVertex(vertexIndex).getNormal();
      mesh.getVertex(vertexIndex).getPosition()
          .copy(pos.add(normal.multiply((2.0 * Math.random() - 1.0) * noiseSigma)));
    }
    mesh.updateRenderStructures();
  }

  /**
   * Cleanup data structure: 1) remove all deprectated triangles.
   * 
   * @param mesh
   *          Mesh to be cleaned.
   */
  public static void cleanup(ITriangleMesh mesh) {
    for (int triangleIndex = 0; triangleIndex < mesh.getNumberOfTriangles(); triangleIndex++) {
      Vector a = mesh.getVertex(mesh.getTriangle(triangleIndex).getA()).getPosition();
      Vector b = mesh.getVertex(mesh.getTriangle(triangleIndex).getB()).getPosition();
      Vector c = mesh.getVertex(mesh.getTriangle(triangleIndex).getC()).getPosition();
      if (a.equals(b) || b.equals(c) || c.equals(a)) {
        mesh.removeTriangle(triangleIndex);
        triangleIndex--;
      }
    }

  }
}

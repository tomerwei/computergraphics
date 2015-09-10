package cgresearch.graphics.datastructures.trianglemesh;

import cgresearch.core.math.IMatrix;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.Matrix;

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
  public static IMatrix createLaplacian(ITriangleMesh mesh) {
    // Create Laplacian
    IMatrix L = new Matrix(mesh.getNumberOfVertices(), mesh.getNumberOfVertices());
    for (int triangleIndex = 0; triangleIndex < mesh.getNumberOfTriangles(); triangleIndex++) {
      Triangle t = mesh.getTriangle(triangleIndex);
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
      IVector3 pos = mesh.getVertex(vertexIndex).getPosition();
      IVector3 normal = mesh.getVertex(vertexIndex).getNormal();
      mesh.getVertex(vertexIndex).getPosition()
          .copy(pos.add(normal.multiply((2.0 * Math.random() - 1.0) * noiseSigma)));
    }
    mesh.updateRenderStructures();
  }
}

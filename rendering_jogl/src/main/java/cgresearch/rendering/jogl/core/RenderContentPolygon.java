/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.core.math.IVector3;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.graphics.scenegraph.Transformation;
import com.jogamp.opengl.GL2;

import cgresearch.graphics.datastructures.Polygon;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshFactory;
import cgresearch.graphics.material.Material;

/**
 * A render node for the line segments data structures.
 * 
 * @author Philipp Jenke
 * 
 */
public class RenderContentPolygon implements IRenderContent {

  /**
   * Reference to the triangle mesh.
   */
  private final Polygon polygon;

  private final JoglRenderNode spheresNode;

  private ITriangleMesh spheresMesh = new TriangleMesh();

  /**
   * Constructor
   */
  public RenderContentPolygon(Polygon polygon, CgNode cgNode) {
    this.polygon = polygon;
    buildSpheresMesh();
    spheresMesh.getMaterial().setRenderMode(Material.Normals.PER_VERTEX);
    spheresNode = new JoglRenderNode(cgNode, new RenderContentTriangleMesh(spheresMesh));
  }

  /**
   * Build a combined mesh for all spheres.
   */
  public void buildSpheresMesh() {
    spheresMesh.clear();
    for (int i = 0; i < polygon.getNumPoints(); i++) {
      IVector3 p = polygon.getPoint(i);
      spheresMesh.unite(TriangleMeshFactory.createSphere(p, 0.05, 20));
    }
    spheresMesh.updateRenderStructures();
  }

  @Override
  public void draw3D(GL2 gl) {
    if (polygon == null) {
      return;
    }

    if (polygon.needsUpdateRenderStructures()) {
      buildSpheresMesh();
    }

    gl.glBegin(GL2.GL_LINES);
    for (int lineIndex = 0; lineIndex < polygon.getNumPoints(); lineIndex++) {
      gl.glVertex3fv(polygon.getPoint(lineIndex).floatData(), 0);
      gl.glVertex3fv(polygon.getPoint((lineIndex + 1) % polygon.getNumPoints()).floatData(), 0);
    }
    gl.glEnd();
    spheresNode.draw3D(gl);
  }

  @Override
  public void draw3D(GL2 gl, LightSource lightSource, Transformation transformation, IVector3[] nearPlaneCorners,
      boolean cameraPositionChanged) {

  }

  @Override
  public void updateRenderStructures() {
    buildSpheresMesh();
  }

  @Override
  public void afterDraw(GL2 gl) {
  }
}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.core.math.Vector;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.graphics.scenegraph.Transformation;
import com.jogamp.opengl.GL2;

import cgresearch.graphics.datastructures.polygon.Polygon;
import cgresearch.graphics.datastructures.polygon.PolygonEdge;
import cgresearch.graphics.datastructures.polygon.PolygonVertex;
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

    if (!polygon.getMaterial().getShowPointSpheres()) {
      return;
    }

    for (int i = 0; i < polygon.getNumPoints(); i++) {
      Vector p = polygon.getPoint(i).getPosition();
      spheresMesh.unite(TriangleMeshFactory.createSphere(p, polygon.getMaterial().getPointSphereSize(), 20));
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

    spheresNode.draw3D(gl);

    gl.glBegin(GL2.GL_LINES);
    for (int edgeIndex = 0; edgeIndex < polygon.getNumEdges(); edgeIndex++) {
      PolygonEdge edge = polygon.getEdge(edgeIndex);
      PolygonVertex p0 = edge.getStartVertex();
      PolygonVertex p1 = edge.getEndVertex();
      gl.glColor3fv(polygon.getEdgeColor(edgeIndex).floatData(), 0);
      gl.glVertex3fv(p0.getPosition().floatData(), 0);
      gl.glVertex3fv(p1.getPosition().floatData(), 0);
    }
    gl.glEnd();
  }

  @Override
  public void draw3D(GL2 gl, LightSource lightSource, Transformation transformation, Vector[] nearPlaneCorners,
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

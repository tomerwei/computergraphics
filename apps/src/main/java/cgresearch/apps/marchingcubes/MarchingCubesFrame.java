/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.marchingcubes;

import com.jogamp.opengl.GL2;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.algorithms.MarchingCubes;
import cgresearch.graphics.algorithms.NodeMerger;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.implicitfunction.IImplicitFunction3D;
import cgresearch.graphics.datastructures.implicitfunction.ImplicitFunctionGourSat;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.misc.ImplicitFunctionVisualization;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;
import cgresearch.rendering.jogl.core.JoglRenderable;

/**
 * Central frame for the mesh exercise.
 * 
 * @author Philipp Jenke
 * 
 */
public class MarchingCubesFrame extends CgApplication implements JoglRenderable {

  /**
   * Visualizer for the implicit function.
   */
  private ImplicitFunctionVisualization vis = null;

  /**
   * Algorithm container for the marching cubes.
   */
  private MarchingCubes marchingCubes = null;

  /**
   * Constructor.
   */
  public MarchingCubesFrame() {
    marchingCubes = new MarchingCubes(50, VectorMatrixFactory.newIVector3(-3, -3, -3), 6);
    vis = new ImplicitFunctionVisualization(256);

    createMesh(new ImplicitFunctionGourSat());

    // Create visualization
    ITriangleMesh mesh = vis.getTriangleMesh();
    CgNode node = new CgNode(mesh, "implicit function plane");
    // getCgRootNode().addChild(node);
    node.setVisible(true);

    CoordinateSystem coordSys = new CoordinateSystem();
    getCgRootNode().addChild(coordSys);
  }

  public void createMesh(IImplicitFunction3D implicitFunction) {
    // Create marching cubes mesh
    ITriangleMesh marchingCubesMesh = marchingCubes.createMesh(implicitFunction);
    marchingCubesMesh = NodeMerger.merge(marchingCubesMesh, 1e-5);
    marchingCubesMesh.getMaterial().setRenderMode(Material.Normals.PER_VERTEX);
    marchingCubesMesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    marchingCubesMesh.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
    marchingCubesMesh.getMaterial().setReflectionDiffuse(Material.PALETTE2_COLOR4);
    CgNode nodeSuperquadric = new CgNode(marchingCubesMesh, "marching cubes");
    nodeSuperquadric.setVisible(true);
    getCgRootNode().removeAllChildren();
    getCgRootNode().addChild(nodeSuperquadric);
    getCgRootNode().addChild(new CoordinateSystem());
  }

  @Override
  public void draw3D(GL2 gl) {
    // Debugging
    // MarchingCubes marchingCubes = new MarchingCubes(50);
    // IVector3[] vertices =
    // { VectorMatrixFactory.newIVector3(-1, -1, -1),
    // VectorMatrixFactory.newIVector3(1, -1, -1),
    // VectorMatrixFactory.newIVector3(1, 1, -1),
    // VectorMatrixFactory.newIVector3(-1, 1, -1),
    // VectorMatrixFactory.newIVector3(-1, -1, 1),
    // VectorMatrixFactory.newIVector3(1, -1, 1),
    // VectorMatrixFactory.newIVector3(1, 1, 1),
    // VectorMatrixFactory.newIVector3(-1, 1, 1) };
    // double[] values = { 1.0, 1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0 };
    // marchingCubes.createTriangles(vertices[0], vertices[1], vertices[2],
    // vertices[3], vertices[4], vertices[5], vertices[6], vertices[7],
    // values[0], values[1], values[2], values[3], values[4], values[5],
    // values[6], values[7]);
    // ITriangleMesh mesh = marchingCubes.getMesh();

    // gl.glDisable(GL2.GL_LIGHTING);
    // gl.glPointSize(5);
    // gl.glBegin(GL2.GL_POINTS);
    // for (int i = 0; i < 8; i++) {
    // if (values[i] > 0) {
    // gl.glColor3f(1, 0, 0);
    // } else {
    // gl.glColor3f(0, 0, 1);
    // }
    // gl.glVertex3fv(vertices[i].floatData(), 0);
    // }
    // gl.glEnd();
    //
    // gl.glDisable(GL2.GL_LIGHTING);
    // gl.glBegin(GL2.GL_TRIANGLES);
    // gl.glColor3f(0, 1, 0);
    // for (int triangleIndex = 0; triangleIndex < mesh.getNumberOfTriangles();
    // triangleIndex++) {
    // Triangle triangle = mesh.getTriangle(triangleIndex);
    // gl.glVertex3fv(mesh.getVertex(triangle.getA()).getPosition().floatData(),
    // 0);
    // gl.glVertex3fv(mesh.getVertex(triangle.getB()).getPosition().floatData(),
    // 0);
    // gl.glVertex3fv(mesh.getVertex(triangle.getC()).getPosition().floatData(),
    // 0);
    // }
    // gl.glEnd();
    //
    // gl.glEnable(GL2.GL_LIGHTING);

  }

  @Override
  public BoundingBox getBoundingBox() {
    return null;
  }

  /**
   * Getter.
   */
  // private ImplicitFunctionVisualization getVis() {
  // return vis;
  // }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    MarchingCubesFrame app = new MarchingCubesFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    appLauncher.addCustomUi(new MarchingCubesGui(app));
  }
}

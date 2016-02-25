/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.simplication;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.polygon.Polygon;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangleMesh;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;

/**
 * Central widget to test the half edge simplification functionality.
 * 
 * @author Philipp Jenke
 * 
 */
public class SimplifcationFrame extends CgApplication {

  /**
   * Half edge data structure
   */
  private HalfEdgeTriangleMesh heMesh = null;

  private Polygon polygon;

  /**
   * Constructor.
   */
  public SimplifcationFrame() {
    heMesh = new HalfEdgeTriangleMesh();
    CgNode meshNode = new CgNode(heMesh, "Mesh");
    meshNode.setVisible(true);
    getCgRootNode().addChild(meshNode);

    polygon = new Polygon();
    CgNode polyNode = new CgNode(polygon, "Polygon");
    polyNode.setVisible(false);
    getCgRootNode().addChild(polyNode);

    // Coordinate system.
    getCgRootNode().addChild(new CoordinateSystem());
  }

  /**
   * Program entry point
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    SimplifcationFrame frame = new SimplifcationFrame();
    appLauncher.create(frame);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    appLauncher.addCustomUi(new SimplificationToolbar(frame.getMesh(), frame.getPolygon()));
  }

  private Polygon getPolygon() {
    return polygon;
  }

  private HalfEdgeTriangleMesh getMesh() {
    return heMesh;
  }

}

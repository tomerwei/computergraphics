/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.subdivision;

import cgresearch.JoglAppLauncher;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.polygon.Polygon;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;
import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.graphics.scenegraph.LightSource.Type;

/**
 * Subdivision in 2D
 * 
 * @author Philipp Jenke
 * 
 */
public class SubdivisionFrame extends CgApplication {

  private Polygon polygon = new Polygon();
  private ITriangleMesh mesh = new TriangleMesh();

  /**
   * Constructor.
   */
  public SubdivisionFrame() {
    getCgRootNode().addChild(new CgNode(polygon, "Polygon"));
    getCgRootNode().addChild(new CgNode(mesh, "Mesh"));
    getCgRootNode().addChild(new CoordinateSystem(0.01));

    LightSource light = new LightSource(Type.POINT);
    light.setPosition(VectorMatrixFactory.newIVector3(-5, -5, -5));
    getCgRootNode().addLight(light);
  }

  public Polygon getPolygon() {
    return polygon;
  }

  public ITriangleMesh getMesh() {
    return mesh;
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    SubdivisionFrame app = new SubdivisionFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    // Logger.getInstance().setVerboseMode(VerboseMode.DEBUG);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    appLauncher.addCustomUi(new SubdivisionGui(app.getPolygon(), app.getMesh()));
  }

}

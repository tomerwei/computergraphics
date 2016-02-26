/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.polygon;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.polygon.Polygon;
import cgresearch.graphics.fileio.PolygonIO;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;

/**
 * Play around with polygons.
 * 
 * @author Philipp Jenke
 * 
 */
public class PolygonFrame extends CgApplication {

  /**
   * Constructor.
   */
  public PolygonFrame() {

    PolygonIO reader = new PolygonIO();
    String inFilename = "polygons/krake.polygon";
    Polygon polygon = reader.readPolygon(inFilename);

    // Export
    // String outFilename =
    // "/Users/abo781/abo781/code/computergraphics/assets/polygons/music.polygon";

    // SVG import
    // String svgFilename = "polygons/music.svg";
    // Polygon polygon = reader.importFromSvgPath(svgFilename);

    if (polygon != null) {
      // SVG Import
      // polygon.fitToUnitBox();

      CgNode node = new CgNode(polygon, "polygon");
      polygon.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
      polygon.getMaterial().setReflectionDiffuse(Material.PALETTE0_COLOR2);
      polygon.getMaterial().setPointSphereSize(0.02);
      getCgRootNode().addChild(node);

      // Export
      // reader.writePolygon(polygon, outFilename);
    }
    getCgRootNode().addChild(new CoordinateSystem());
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    CgApplication app = new PolygonFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }
}

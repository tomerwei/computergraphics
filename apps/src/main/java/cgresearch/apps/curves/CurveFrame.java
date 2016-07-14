/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.curves;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.curves.CurveModel;
import cgresearch.graphics.datastructures.curves.CurveModel.CurveType;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;
import cgresearch.graphics.scenegraph.CoordinateSystem.Dimension;
import cgresearch.ui.IApplicationControllerGui;

/**
 * Central widget for the curve exercise.
 * 
 * @author Philipp Jenke
 * 
 */
public class CurveFrame extends CgApplication {

  private final CurveFrameGui gui;

  private CurveModel model = new CurveModel();

  /**
   * Constructor.
   */
  public CurveFrame() {
    gui = new CurveFrameGui(model);

    // Generate curve
    model.generateCurve(CurveType.HERMITE);

    // Move control points
    new CurveControlPointInteraction(model.getCurve());

    // Scene graph node
    model.getCurve().getMaterial().setShowCurrentPoint(true);
    model.getCurve().getMaterial().setShowControlPolyon(true);
    CgNode curveNode = new CgNode(model.getCurve(), "Curve");
    curveNode.setVisible(true);
    getCgRootNode().addChild(curveNode);

    getCgRootNode().addChild(new CoordinateSystem(Dimension.DIMENSION_2D, 0.5));

    new BasisFunctionWidget(model);
  }

  /**
   * Getter.
   */
  private IApplicationControllerGui getUi() {
    return gui;
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    CurveFrame app = new CurveFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    appLauncher.addCustomUi(app.getUi());
  }
}

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
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.curves.BezierCurve;
import cgresearch.graphics.datastructures.curves.HermiteCurve;
import cgresearch.graphics.datastructures.curves.ICurve;
import cgresearch.graphics.datastructures.curves.LagrangeCurve;
import cgresearch.graphics.datastructures.curves.MonomialCurve;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;
import cgresearch.ui.IApplicationControllerGui;

/**
 * Central widget for the curve exercise.
 * 
 * @author Philipp Jenke
 * 
 */
public class CurveFrame extends CgApplication {

  public static enum CurveType {
    MONOM, HERMITE, LAGRANGE, BEZIER
  };

  private final CurveFrameGui gui;

  /**
   * Constructor.
   */
  public CurveFrame() {
    gui = new CurveFrameGui();

    // Generate curve
    ICurve curve = generateCurve(CurveType.BEZIER);

    // UI
    gui.registerCurve(curve, "Curve");

    // Move control points
    new CurveControlPointInteraction(curve);

    // Scene graph node
    curve.getMaterial().setShowCurrentPoint(true);
    curve.getMaterial().setShowControlPolyon(true);
    CgNode curveNode = new CgNode(curve, "Curve");
    curveNode.setVisible(true);
    getCgRootNode().addChild(curveNode);

    getCgRootNode().addChild(new CoordinateSystem());
  }

  /**
     * 
     */
  private ICurve generateCurve(CurveType type) {
    switch (type) {
      case BEZIER:
        BezierCurve bezier = new BezierCurve(3);
        bezier.setControlPoint(0, VectorFactory.createVector3(-0.5, -0.5, 0.5));
        bezier.setControlPoint(1,
            VectorFactory.createVector3(-0.25, 0.5, -0.5));
        bezier.setControlPoint(2, VectorFactory.createVector3(0.25, -0.5, 0.5));
        bezier.setControlPoint(3, VectorFactory.createVector3(0.5, 0.5, 0.5));
        return bezier;
      case LAGRANGE:
        LagrangeCurve lagrange = new LagrangeCurve(3);
        lagrange.setControlPoint(0,
            VectorFactory.createVector3(-0.5, -0.5, 0.5));
        lagrange.setControlPoint(1,
            VectorFactory.createVector3(-0.25, 0.5, -0.5));
        lagrange.setControlPoint(2,
            VectorFactory.createVector3(0.25, -0.5, 0.5));
        lagrange.setControlPoint(3, VectorFactory.createVector3(0.5, 0.5, 0.5));
        return lagrange;
      case HERMITE:
        ICurve curveHermite =
            new HermiteCurve(VectorFactory.createVector3(-0.5, -0.5, 0),
                VectorFactory.createVector3(-0.5, 0.5, 0),
                VectorFactory.createVector3(0.5, -0.5, 0),
                VectorFactory.createVector3(0.5, 0.5, 0));

        return curveHermite;
      case MONOM:
        MonomialCurve monomial = new MonomialCurve(2);
        monomial.setControlPoint(0,
            VectorFactory.createVector3(-0.5, -0.5, 0.5));
        monomial.setControlPoint(1, VectorFactory.createVector3(0, 0.5, 0));
        monomial.setControlPoint(2,
            VectorFactory.createVector3(0.5, -0.5, 0.5));
        return monomial;
      default:
        return null;
    }
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

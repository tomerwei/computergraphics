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
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.curves.BezierCurve;
import cgresearch.graphics.datastructures.curves.HermiteCurve;
import cgresearch.graphics.datastructures.curves.ICurve;
import cgresearch.graphics.datastructures.curves.LagrangeCurve;
import cgresearch.graphics.datastructures.curves.MonomialCurve;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.ui.IApplicationControllerGui;

/**
 * Central widget for the curve exercise.
 * 
 * @author Philipp Jenke
 * 
 */
public class CurveFrame extends CgApplication {

  private final CurveFrameGui gui;

  /**
   * Constructor.
   */
  public CurveFrame() {
    gui = new CurveFrameGui();
    gui.registerCurve(addPolynomialCurve(), "Monom-Curve");
    gui.registerCurve(addHermiteCurve(), "Hermite-Curve");
    gui.registerCurve(addLagrangeCurve(), "Lagrange-Curve");
    gui.registerCurve(addBezierCurve(), "Bezier-Curve");
  }

  /**
     * 
     */
  private ICurve addBezierCurve() {
    BezierCurve curve = new BezierCurve(3);
    curve.setControlPoint(0, VectorMatrixFactory.newIVector3(-0.5, -0.5, 0.5));
    curve.setControlPoint(1, VectorMatrixFactory.newIVector3(-0.25, 0.5, -0.5));
    curve.setControlPoint(2, VectorMatrixFactory.newIVector3(0.25, -0.5, 0.5));
    curve.setControlPoint(3, VectorMatrixFactory.newIVector3(0.5, 0.5, 0.5));
    CgNode curveNode = new CgNode(curve, "Bezier Curve");
    curveNode.setVisible(true);
    getCgRootNode().addChild(curveNode);
    return curve;
  }

  /**
   * Add Lagrange curve to the application
   */
  private ICurve addLagrangeCurve() {
    LagrangeCurve curve = new LagrangeCurve(3);
    curve.setControlPoint(0, VectorMatrixFactory.newIVector3(-0.5, -0.5, 0.5));
    curve.setControlPoint(1, VectorMatrixFactory.newIVector3(-0.25, 0.5, -0.5));
    curve.setControlPoint(2, VectorMatrixFactory.newIVector3(0.25, -0.5, 0.5));
    curve.setControlPoint(3, VectorMatrixFactory.newIVector3(0.5, 0.5, 0.5));
    CgNode curveNode = new CgNode(curve, "Lagrange Curve");
    curveNode.setVisible(false);
    getCgRootNode().addChild(curveNode);
    return curve;
  }

  /**
   * Add hermite curve to the application
   */
  private ICurve addHermiteCurve() {
    ICurve curveHermite =
        new HermiteCurve(VectorMatrixFactory.newIVector3(-0.5, -0.5, 0), VectorMatrixFactory.newIVector3(-0.5, 0.5, 0),
            VectorMatrixFactory.newIVector3(0.5, -0.5, 0), VectorMatrixFactory.newIVector3(0.5, 0.5, 0));
    CgNode curveNode = new CgNode(curveHermite, "Hermite Curve");
    curveNode.setVisible(false);
    getCgRootNode().addChild(curveNode);
    return curveHermite;
  }

  /**
   * Add polynomial curve to the application.
   */
  private ICurve addPolynomialCurve() {
    MonomialCurve curvePolynomial = new MonomialCurve(2);
    curvePolynomial.setControlPoint(0, VectorMatrixFactory.newIVector3(-0.5, -0.5, 0.5));
    curvePolynomial.setControlPoint(1, VectorMatrixFactory.newIVector3(0, 0.5, 0));
    curvePolynomial.setControlPoint(2, VectorMatrixFactory.newIVector3(0.5, -0.5, 0.5));
    CgNode curveNode = new CgNode(curvePolynomial, "Polynomial (Monom) Curve");
    curveNode.setVisible(false);
    getCgRootNode().addChild(curveNode);
    return curvePolynomial;
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

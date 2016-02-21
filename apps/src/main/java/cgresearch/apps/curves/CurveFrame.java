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
    getCgRootNode().addChild(new CoordinateSystem());
  }

  /**
     * 
     */
  private ICurve addBezierCurve() {
    BezierCurve curve = new BezierCurve(3);
    curve.setControlPoint(0, VectorFactory.createVector3(-0.5, -0.5, 0.5));
    curve.setControlPoint(1, VectorFactory.createVector3(-0.25, 0.5, -0.5));
    curve.setControlPoint(2, VectorFactory.createVector3(0.25, -0.5, 0.5));
    curve.setControlPoint(3, VectorFactory.createVector3(0.5, 0.5, 0.5));
    curve.getMaterial().setShowCurrentPoint(true);
    curve.getMaterial().setShowControlPolyon(true);
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
    curve.setControlPoint(0, VectorFactory.createVector3(-0.5, -0.5, 0.5));
    curve.setControlPoint(1, VectorFactory.createVector3(-0.25, 0.5, -0.5));
    curve.setControlPoint(2, VectorFactory.createVector3(0.25, -0.5, 0.5));
    curve.setControlPoint(3, VectorFactory.createVector3(0.5, 0.5, 0.5));
    curve.getMaterial().setShowCurrentPoint(true);
    curve.getMaterial().setShowControlPolyon(true);
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
        new HermiteCurve(VectorFactory.createVector3(-0.5, -0.5, 0), VectorFactory.createVector3(-0.5, 0.5, 0),
            VectorFactory.createVector3(0.5, -0.5, 0), VectorFactory.createVector3(0.5, 0.5, 0));
    curveHermite.getMaterial().setShowCurrentPoint(true);
    curveHermite.getMaterial().setShowControlPolyon(true);
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
    curvePolynomial.setControlPoint(0, VectorFactory.createVector3(-0.5, -0.5, 0.5));
    curvePolynomial.setControlPoint(1, VectorFactory.createVector3(0, 0.5, 0));
    curvePolynomial.setControlPoint(2, VectorFactory.createVector3(0.5, -0.5, 0.5));
    curvePolynomial.getMaterial().setShowCurrentPoint(true);
    curvePolynomial.getMaterial().setShowControlPolyon(true);
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

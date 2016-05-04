/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.curves;

import java.util.ArrayList;
import java.util.List;

import cgresearch.AppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.curves.BasisFunctionMonomial;
import cgresearch.graphics.datastructures.curves.Curve;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshFactory;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;
import cgresearch.ui.IApplicationControllerGui;

/**
 * Interpolation of 3 points with an monom curve of degree 2.
 * 
 */
public class MonomInterpolation extends CgApplication {

  private final CurveFrameGui gui;

  /**
   * Constructor.
   */
  public MonomInterpolation() {
    List<Vector> interpolationPoints = new ArrayList<Vector>();
    interpolationPoints.add(VectorFactory.createVector3(1, 1, -0.5));
    interpolationPoints.add(VectorFactory.createVector3(0.5, 1, 1));
    interpolationPoints.add(VectorFactory.createVector3(-0.5, -0.5, 0.5));
    CurveModel curveModel =
        new CurveModel(computeInterpolatedCurve(interpolationPoints));
    getCgRootNode().addChild(new CgNode(curveModel.getCurve(), "Interpolated monom curve"));

    gui = new CurveFrameGui(curveModel);

    for (int i = 0; i < interpolationPoints.size(); i++) {
      ITriangleMesh sphere = TriangleMeshFactory
          .createSphere(interpolationPoints.get(i), 0.05, 20);
      sphere.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
      sphere.getMaterial().setReflectionDiffuse(Material.PALETTE0_COLOR3);
      CgNode node = new CgNode(sphere, "Interpolation point " + i);
      getCgRootNode().addChild(node);
    }

    getCgRootNode().addChild(new CoordinateSystem());
  }

  private Curve computeInterpolatedCurve(List<Vector> interpolationPoints) {
    Curve monomialCurve = new Curve(new BasisFunctionMonomial(),
        VectorFactory.createVector3(0, 0, 0),
        VectorFactory.createVector3(0, 0, 0));
    Vector c0 = interpolationPoints.get(0);
    Vector c1 = interpolationPoints.get(0).multiply(-3)
        .add(interpolationPoints.get(1).multiply(4))
        .subtract(interpolationPoints.get(2));
    Vector c2 = interpolationPoints.get(2).subtract(interpolationPoints.get(0))
        .subtract(c1);
    monomialCurve.setControlPoint(0, c0);
    monomialCurve.setControlPoint(1, c1);
    monomialCurve.setControlPoint(2, c2);
    return monomialCurve;
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
    MonomInterpolation app = new MonomInterpolation();
    AppLauncher.getInstance().create(app);
    AppLauncher.getInstance().setRenderSystem(RenderSystem.JOGL);
    AppLauncher.getInstance().setUiSystem(UI.JOGL_SWING);
    AppLauncher.getInstance().addCustomUi(app.getUi());
  }
}

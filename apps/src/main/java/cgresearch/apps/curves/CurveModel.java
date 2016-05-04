package cgresearch.apps.curves;

import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.curves.BasisFunctionBezier;
import cgresearch.graphics.datastructures.curves.BasisFunctionHermite;
import cgresearch.graphics.datastructures.curves.Curve;
import cgresearch.graphics.datastructures.curves.BasisFunctionLagrange;
import cgresearch.graphics.datastructures.curves.BasisFunctionMonomial;

public class CurveModel {

  public static enum CurveType {
    MONOM, HERMITE, LAGRANGE, BEZIER
  };

  private final Curve curve;

  public CurveModel() {
    this(new Curve(new BasisFunctionBezier(),
        VectorFactory.createVector3(-0.5, -0.5, 0.5),
        VectorFactory.createVector3(-0.25, 0.5, -0.5),
        VectorFactory.createVector3(0.25, -0.5, 0.5),
        VectorFactory.createVector3(0.5, 0.5, 0.5)));
    generateCurve(CurveType.BEZIER);
  }

  public CurveModel(Curve curve) {
    this.curve = curve;
  }

  /**
   * 
   */
  public void generateCurve(CurveType type) {
    switch (type) {
      case BEZIER:
        curve.setBasisFunctions(new BasisFunctionBezier());
        break;
      case LAGRANGE:
        curve.setBasisFunctions(new BasisFunctionLagrange());
        break;
      case HERMITE:
        curve.setBasisFunctions(new BasisFunctionHermite());
        break;
      case MONOM:
        curve.setBasisFunctions(new BasisFunctionMonomial());
        break;
    }
  }

  public Curve getCurve() {
    return curve;
  }
}

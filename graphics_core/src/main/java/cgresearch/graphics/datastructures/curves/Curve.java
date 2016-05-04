/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.curves;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.material.CurveMaterial;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * Generic interface for all curves.
 * 
 * @author Philipp Jenke
 * 
 */
public class Curve extends ICgNodeContent {

  /**
   * Array of control points
   */
  protected Vector[] controlPoints = null;

  /**
   * Min value of the parameter.
   */
  protected double paramMin = 0;

  /**
   * Max value of the parameter.
   */
  protected double paramMax = 1;

  /**
   * Parameter of the curve, must be between paramMin and paramMax.
   */
  private double parameter = 0;

  private IBasisFunction basisFunctions;

  public Curve(IBasisFunction basisFunctions, Vector... controlPoints) {
    material = new CurveMaterial();
    this.basisFunctions = basisFunctions;
    this.controlPoints = controlPoints;
  }

  public Curve(BasisFunctionBezier basisFunctions, int degree) {
    material = new CurveMaterial();
    this.basisFunctions = basisFunctions;
    controlPoints = new Vector[degree];
    for (int i = 0; i <= degree; i++) {
      controlPoints[i] = VectorFactory.createVector3(0, 0, 0);
    }
  }

  /**
   * Evaluate the curve at the parameter t.
   * 
   * @param t
   *          Evaluation parameter
   * 
   * @return Point on the curve at the specified parameter.
   */
  public Vector eval(double t) {
    Vector p = VectorFactory.createVector3(0, 0, 0);
    for (int i = 0; i <= getDegree(); i++) {
      p = p.add(
          controlPoints[i].multiply(basisFunctions.eval(i, t, getDegree())));
    }
    return p;
  }

  /**
   * Evaluate the derivative of the curve at the parameter t.
   * 
   * @param t
   *          Evaluation parameter
   * 
   * @return Derivative (tangent) on the curve at the specified parameter.
   */
  public Vector derivative(double t) {
    double h = 0.001;
    return eval(t + h / 2.0).subtract(eval(t - h / 2.0)).multiply(1.0 / h);
  }

  /**
   * Getter
   * 
   * @return Degree of the curve.
   */
  public int getDegree() {
    return controlPoints.length - 1;
  }

  /**
   * Getter.
   * 
   * @param i
   *          Index of the accessed control point.
   * @return Control point at index i of the curve.
   */
  public Vector getControlPoint(int index) {
    return controlPoints[index];
  }

  /**
   * Setter for a control point..
   * 
   * @param index
   *          Index of the control point to be set.
   * @param p
   *          Control point.
   */
  public void setControlPoint(int index, Vector p) {
    controlPoints[index].copy(p);
  }

  @Override
  public BoundingBox getBoundingBox() {
    BoundingBox bbox = new BoundingBox();
    int RESOLUTION = 20;
    for (int i = 0; i <= RESOLUTION; i++) {
      double t =
          (double) i / (double) RESOLUTION * (paramMax - paramMin) + paramMin;
      bbox.add(eval(t));
    }
    return bbox;
  }

  /**
   * Getter.
   */
  public double getParamMin() {
    return paramMin;
  }

  /**
   * Getter.
   * 
   * @return
   */
  public double getParamMax() {
    return paramMax;
  }

  /**
   * Setter
   */
  public void setParameter(double parameter) {
    this.parameter = parameter;

    setChanged();
    notifyObservers();
  }

  /**
   * Getter
   */
  public double getParameter() {
    return parameter;
  }

  @Override
  public CurveMaterial getMaterial() {
    return (CurveMaterial) (super.getMaterial());
  }

  @Override
  public void updateRenderStructures() {
    super.updateRenderStructures();
    setChanged();
    notifyObservers();
  }

  public void setBasisFunctions(IBasisFunction basisFunctions) {
    this.basisFunctions = basisFunctions;
    updateRenderStructures();
  }
}

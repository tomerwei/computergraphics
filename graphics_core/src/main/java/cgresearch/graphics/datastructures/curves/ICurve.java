/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.curves;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.graphics.material.CurveMaterial;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * Generic interface for all curves.
 * 
 * @author Philipp Jenke
 * 
 */
public abstract class ICurve extends ICgNodeContent {

  /**
   * Array of control points
   */
  protected IVector3[] controlPoints = null;

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

  public ICurve() {
    material = new CurveMaterial();
  }

  /**
   * Evaluate the curve at the parameter t.
   * 
   * @param t
   *          Evaluation parameter
   * 
   * @return Point on the curve at the specified parameter.
   */
  public abstract IVector3 eval(double t);

  /**
   * Evaluate the derivative of the curve at the parameter t.
   * 
   * @param t
   *          Evaluation parameter
   * 
   * @return Derivative (tangent) on the curve at the specified parameter.
   */
  public IVector3 derivative(double t) {
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
  public IVector3 getControlPoint(int index) {
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
  public void setControlPoint(int index, IVector3 p) {
    controlPoints[index] = p;
  }

  @Override
  public BoundingBox getBoundingBox() {
    BoundingBox bbox = new BoundingBox();
    int RESOLUTION = 20;
    for (int i = 0; i <= RESOLUTION; i++) {
      double t = (double) i / (double) RESOLUTION * (paramMax - paramMin) + paramMin;
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
}

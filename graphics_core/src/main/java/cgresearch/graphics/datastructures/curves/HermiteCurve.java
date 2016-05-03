/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.curves;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;

/**
 * Implementation of a Hermite curve.
 * 
 * @author Philipp Jenke
 * 
 */
public class HermiteCurve extends ICurve {
  /**
   * 
   * @param p0
   *          Interpolation position at 0.
   * @param m0
   *          Tangent at parameter 0.
   * @param m1
   *          Tangent at 1.
   * @param p1
   *          Interpolation position at 1
   */
  public HermiteCurve(Vector p0, Vector m0, Vector m1, Vector p1) {
    controlPoints = new Vector[4];
    for (int i = 0; i < controlPoints.length; i++) {
      controlPoints[i] = VectorFactory.createVector3(0, 0, 0);
    }
    controlPoints[0] = p0;
    controlPoints[1] = m0;
    controlPoints[2] = m1;
    controlPoints[3] = p1;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.cg1.exercises.curves.ICurve#eval(double)
   */
  @Override
  public Vector eval(double t) {
    Vector result = VectorFactory.createVector3(0, 0, 0);
    for (int i = 0; i < 4; i++) {
      result = result.add(controlPoints[i].multiply(evalBasisFunction(i, t)));
    }
    return result;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.cg1.exercises.curves.ICurve#derivative(double)
   */
  @Override
  public Vector derivative(double t) {
    Vector result = VectorFactory.createVector3(0, 0, 0);
    for (int i = 0; i < 4; i++) {
      result = result.add(controlPoints[i].multiply(evalDerivative(i, t)));
    }
    return result;
  }

  /**
   * Evaluate the basis function with index 'index' at position 't'.
   * 
   * @param index
   *          Index of the basis function.
   * @param t
   *          Parameter value.
   * @return Value of the corresponding basis function at the given parameter.
   */
  private double evalBasisFunction(int index, double t) {
    switch (index) {
      case 0:
        return (1 - t) * (1 - t) * (1 + 2 * t);
      case 1:
        return t * (1 - t) * (1 - t);
      case 2:
        return -t * t * (1 - t);
      case 3:
        return (3 - 2 * t) * t * t;
      default:
        Logger.getInstance().error("Invalid call to evalBasisFunction()");
        break;
    }
    return 0;
  }

  /**
   * Evaluate the basis function with index 'index' at position 't'.
   * 
   * @param index
   *          Index of the basis function.
   * @param t
   *          Parameter value.
   * @return Value of the corresponding basis function at the given parameter.
   */
  private double evalDerivative(int index, double t) {
    switch (index) {
      case 0:
        return -6 * t + 6 * t * t;
      case 1:
        return 1 - 4 * t + 3 * t * t;
      case 2:
        return -2 * t + 3 * t * t;
      case 3:
        return 6 * t - 6 * t * t;
      default:
        Logger.getInstance().error("Invalid call to evalBasisFunction()");
        break;
    }
    return 0;
  }
}

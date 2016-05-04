/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.curves;

import cgresearch.core.logging.Logger;

/**
 * Implementation of a Hermite curve.
 * 
 * @author Philipp Jenke
 * 
 */
public class BasisFunctionHermite implements IBasisFunction {

  /**
   * Evaluate the basis function with index 'index' at position 't'.
   * 
   * @param index
   *          Index of the basis function.
   * @param t
   *          Parameter value.
   * @return Value of the corresponding basis function at the given parameter.
   */
  public double eval(int index, double t, int degree) {
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

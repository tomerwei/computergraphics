/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.curves;

import cgresearch.core.math.MathHelpers;

/**
 * Implementation of a Bezier curve.
 * 
 * @author Philipp Jenke
 * 
 */
public class BasisFunctionBezier implements IBasisFunction {
  @Override
  public double eval(int index, double t, int degree) {
    return MathHelpers.over(degree, index) * Math.pow(t, index)
        * Math.pow(1 - t, degree - index);
  }
}

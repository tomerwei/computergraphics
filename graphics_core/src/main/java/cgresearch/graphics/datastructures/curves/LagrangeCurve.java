/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.curves;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;

/**
 * Implementation of a lagrane curve.
 * 
 * @author Philipp Jenke
 * 
 */
public class LagrangeCurve extends ICurve {
  /**
   * @param i
   */
  public LagrangeCurve(int degree) {
    controlPoints = new Vector[degree + 1];
    for (int i = 0; i < controlPoints.length; i++) {
      controlPoints[i] = VectorFactory.createVector3(0, 0, 0);
    }
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.haw.cg.datastructures.curves.ICurve#eval(double)
   */
  @Override
  public Vector eval(double t) {
    Vector p = VectorFactory.createVector3(0, 0, 0);
    for (int i = 0; i <= getDegree(); i++) {
      p = p.add(controlPoints[i].multiply(evalBasisFunction(i, t)));
    }
    return p;
  }

  /**
   * Evaluate the i'th basis function at position t.
   * 
   * @param i
   *          Index of the basis function.
   * @param t
   *          Parameter value
   * @return Value of the basis function.
   */
  private double evalBasisFunction(int index, double t) {
    double numerator = 1;
    double denominator = 1;
    double delta = 1.0 / getDegree();
    for (int i = 0; i <= getDegree(); i++) {
      if (i != index) {
        numerator *= t - i * delta;
        denominator *= index * delta - i * delta;
      }
    }
    return numerator / denominator;
  }

}

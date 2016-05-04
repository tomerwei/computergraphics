/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.curves;

/**
 * Implementation of a lagrane curve.
 * 
 * @author Philipp Jenke
 * 
 */
public class BasisFunctionLagrange implements IBasisFunction {

  public double eval(int index, double t, int degree) {
    double numerator = 1;
    double denominator = 1;
    double delta = 1.0 / degree;
    for (int i = 0; i <= degree; i++) {
      if (i != index) {
        numerator *= t - i * delta;
        denominator *= index * delta - i * delta;
      }
    }
    return numerator / denominator;
  }

}

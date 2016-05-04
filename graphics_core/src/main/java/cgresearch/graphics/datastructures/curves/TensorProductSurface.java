package cgresearch.graphics.datastructures.curves;

import java.util.Observable;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;

/**
 * Tensor product surface Based of Bezier curves.
 * 
 * @author Philipp Jenke
 *
 */
public class TensorProductSurface extends Observable {

  /**
   * 2-dimensional array with the control points.
   */
  private final Vector[][] controlPoints;

  /**
   * Basis function in u-direction.
   */
  private final IBasisFunction basisFunctionU;

  /**
   * Basis function in v-direction.
   */
  private final IBasisFunction basisFunctionV;

  /**
   * Constructor.
   */
  public TensorProductSurface(int degreeU, int degreeV) {
    controlPoints = new Vector[degreeU + 1][degreeV + 1];
    // for (int i = 0; i <= getDegreeU(); i++) {
    // controlPoints[i] = new Vector[degreeV + 1];
    // }
    for (int i = 0; i <= getDegreeU(); i++) {
      for (int j = 0; j <= getDegreeV(); j++) {
        controlPoints[i][j] =
            VectorFactory.createVector3((double) i / (double) getDegreeU(), 0,
                (double) j / (double) getDegreeV());
      }
    }
    basisFunctionU = new BasisFunctionBezier();
    basisFunctionV = new BasisFunctionBezier();
  }

  /**
   * Evaluate the surface at (u,v). Both parameters are defined in [0,1].
   */
  public Vector eval(double u, double v) {
    Vector p = VectorFactory.createVector3(0, 0, 0);
    for (int i = 0; i <= getDegreeU(); i++) {
      for (int j = 0; j <= getDegreeV(); j++) {
        p.addSelf(getControlPoint(i, j)
            .multiply(basisFunctionU.eval(i, u, getDegreeU())
                * basisFunctionV.eval(j, v, getDegreeV())));
      }
    }
    return p;
  }

  /**
   * Getter.
   */
  public Vector getControlPoint(int i, int j) {
    return controlPoints[i][j];
  }

  /**
   * Return the degree in v-direction.
   */
  public int getDegreeV() {
    return controlPoints[0].length - 1;
  }

  /**
   * Return the degree in u-direction.
   */
  public int getDegreeU() {
    return controlPoints.length - 1;
  }

  /**
   * Setter.
   */
  public void setControlPoint(int i, int j, Vector p) {
    controlPoints[i][j].copy(p);

    setChanged();
    notifyObservers();
  }

}

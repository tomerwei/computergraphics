package cgresearch.graphics.datastructures.implicitfunction;

import cgresearch.core.math.Vector;

public class ImplicitFunctionGourSat implements IImplicitFunction3D {

  /**
   * Function parameters.
   */
  private double a, b, c;

  /**
   * Constructor.
   */
  public ImplicitFunctionGourSat() {
    a = 0.0;
    b = -5.0;
    c = 11.8;
  }

  @Override
  public double f(Vector p) {
    double x = p.get(0);
    double y = p.get(1);
    double z = p.get(2);
    return Math.pow(x, 4) + Math.pow(y, 4) + Math.pow(z, 4) + a
        * Math.pow((Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)), 2) + b
        * (Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) + c;
  }
}

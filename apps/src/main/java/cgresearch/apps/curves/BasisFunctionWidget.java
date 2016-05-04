package cgresearch.apps.curves;

import javax.swing.JFrame;

import cgresearch.graphics.datastructures.curves.CurveModel;

/**
 * This frame displays basis functions of a curve
 * 
 * @author Philipp Jenke
 *
 */
public class BasisFunctionWidget extends JFrame {

  /**
   * 
   */
  private static final long serialVersionUID = -2133624293071437015L;

  public BasisFunctionWidget(CurveModel curve) {
    DrawBasisFunctionCanvas canvas = new DrawBasisFunctionCanvas(curve);

    add(canvas);

    setTitle("Basis Functions");
    setSize(200, 200);
    setVisible(true);
    setLocation(600, 400);
  }

}

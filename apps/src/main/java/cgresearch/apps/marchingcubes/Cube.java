package cgresearch.apps.marchingcubes;

import java.util.Observable;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;

public class Cube extends Observable {

  /**
   * Lower left corner point.
   */
  private Vector lowerLeft = VectorFactory.createVector3(-1, -1, -1);

  /**
   * Upper right corner point.
   */
  private Vector upperRight = VectorFactory.createVector3(1, 1, 1);

  /**
   * Corner point function values.
   */
  private double[] values = { -1, 2, 2, 2, 2, 2, 2, 2 };

  /**
   * ISO-value of the visualization
   */
  private double isoValue = 0;

  /**
   * Get the corner point with the given index
   * 
   * @param index
   *          Valid indices are 0...7
   * @return
   */
  public Vector getCornerPoint(int index) {
    double dx = upperRight.get(0) - lowerLeft.get(0);
    double dy = upperRight.get(1) - lowerLeft.get(1);
    double dz = upperRight.get(2) - lowerLeft.get(2);
    switch (index) {
      case 0:
        return lowerLeft.add(VectorFactory.createVector3(0, 0, 0));
      case 1:
        return lowerLeft.add(VectorFactory.createVector3(dx, 0, 0));
      case 2:
        return lowerLeft.add(VectorFactory.createVector3(dx, dy, 0));
      case 3:
        return lowerLeft.add(VectorFactory.createVector3(0, dy, 0));
      case 4:
        return lowerLeft.add(VectorFactory.createVector3(0, 0, dz));
      case 5:
        return lowerLeft.add(VectorFactory.createVector3(dx, 0, dz));
      case 6:
        return lowerLeft.add(VectorFactory.createVector3(dx, dy, dz));
      case 7:
        return lowerLeft.add(VectorFactory.createVector3(0, dy, dz));
      default:
        return null;
    }
  }

  /**
   * Return the function value at the given corner point.
   */
  public double getValue(int index) {
    return values[index];
  }

  /**
   * Set the current iso value;
   * 
   * @param isoValue
   *          New value.
   */
  public void setIsoValue(double isoValue) {
    this.isoValue = isoValue;
    setChanged();
    notifyObservers();
  }

  /**
   * Set function value at corner point
   * 
   * @param index
   *          Index of the corner point in 0 ... 7
   * @param value
   */
  public void setValue(int index, double value) {
    values[index] = value;
    setChanged();
    notifyObservers();
  }

  public double getIsoValue() {
    return isoValue;
  }
}

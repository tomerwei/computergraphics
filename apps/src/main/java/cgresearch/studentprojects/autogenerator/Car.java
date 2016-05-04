package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Vector;
import cgresearch.core.math.Vector;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.curves.BasisFunctionBezier;
import cgresearch.graphics.datastructures.curves.Curve;

public class Car {

  private final int carVektor = 28;

  private List<Curve> curves = new ArrayList<Curve>();
  private List<Vector> points = new ArrayList<Vector>();
  private Vector x = new Vector(carVektor);
  private Vector y = new Vector(carVektor);
  private Vector z = new Vector(carVektor);

  public Car() {

  }

  public Car(Vector x, Vector y, Vector z) {
    setX(x);
    setY(y);
    setZ(z);
    refillPoints();
    refillCurves();
  }

  private void refillCurves() {
    curves = null;
    if (!points.isEmpty() || points != null) {
      curves = new ArrayList<Curve>();
      Curve frontLeft = new Curve(new BasisFunctionBezier(), 2);
      Curve frontTop = new Curve(new BasisFunctionBezier(), 2);

      Curve gastLeft = new Curve(new BasisFunctionBezier(), 2);
      Curve gastTop = new Curve(new BasisFunctionBezier(), 2);
      Curve gastRight = new Curve(new BasisFunctionBezier(), 2);

      Curve hecktTop = new Curve(new BasisFunctionBezier(), 2);
      Curve heckRight = new Curve(new BasisFunctionBezier(), 2);

      Curve chassisRight = new Curve(new BasisFunctionBezier(), 2);
      Curve chassisBottom = new Curve(new BasisFunctionBezier(), 2);
      Curve chassisLeft = new Curve(new BasisFunctionBezier(), 2);

      if (points.size() == carVektor) {
        frontLeft.setControlPoint(0, points.get(0));
        frontLeft.setControlPoint(1, points.get(1));
        frontLeft.setControlPoint(2, points.get(2));

        frontTop.setControlPoint(0, points.get(2));
        frontTop.setControlPoint(1, points.get(4));
        frontTop.setControlPoint(2, points.get(5));
        // frontTop.setControlPoint(0, points.get(2));
        // frontTop.setControlPoint(1, points.get(3));
        // frontTop.setControlPoint(2, points.get(4));

        gastLeft.setControlPoint(0, points.get(5));
        gastLeft.setControlPoint(1, points.get(7));
        gastLeft.setControlPoint(2, points.get(8));
        // gastLeft.setControlPoint(0, points.get(4));
        // gastLeft.setControlPoint(1, points.get(5));
        // gastLeft.setControlPoint(2, points.get(6));

        gastTop.setControlPoint(0, points.get(8));
        gastTop.setControlPoint(1, points.get(10));
        // gastTop.setControlPoint(0, points.get(6));
        // gastTop.setControlPoint(1, points.get(7));

        gastRight.setControlPoint(0, points.get(10));
        gastRight.setControlPoint(1, points.get(12));
        gastRight.setControlPoint(2, points.get(13));
        // gastRight.setControlPoint(0, points.get(7));
        // gastRight.setControlPoint(1, points.get(8));
        // gastRight.setControlPoint(2, points.get(9));

        hecktTop.setControlPoint(0, points.get(13));
        hecktTop.setControlPoint(1, points.get(15));
        hecktTop.setControlPoint(2, points.get(16));
        // hecktTop.setControlPoint(0, points.get(9));
        // hecktTop.setControlPoint(1, points.get(10));
        // hecktTop.setControlPoint(2, points.get(11));

        heckRight.setControlPoint(0, points.get(16));
        heckRight.setControlPoint(1, points.get(18));
        heckRight.setControlPoint(2, points.get(19));
        // heckRight.setControlPoint(0, points.get(11));
        // heckRight.setControlPoint(1, points.get(12));
        // heckRight.setControlPoint(2, points.get(13));

        chassisRight.setControlPoint(0, points.get(19));
        chassisRight.setControlPoint(1, points.get(21));
        chassisRight.setControlPoint(2, points.get(22));
        // chassisRight.setControlPoint(0, points.get(13));
        // chassisRight.setControlPoint(1, points.get(14));
        // chassisRight.setControlPoint(2, points.get(15));

        chassisBottom.setControlPoint(0, points.get(22));
        chassisBottom.setControlPoint(1, points.get(24));
        // chassisBottom.setControlPoint(0, points.get(15));
        // chassisBottom.setControlPoint(1, points.get(16));

        chassisLeft.setControlPoint(0, points.get(24));
        chassisLeft.setControlPoint(1, points.get(26));
        chassisLeft.setControlPoint(2, points.get(0));
        // chassisLeft.setControlPoint(0, points.get(16));
        // chassisLeft.setControlPoint(1, points.get(17));
        // chassisLeft.setControlPoint(2, points.get(0));
      } else {
        frontLeft.setControlPoint(0, points.get(0));
        frontLeft.setControlPoint(1, points.get(1));
        frontLeft.setControlPoint(2, points.get(2));

        frontTop.setControlPoint(0, points.get(2));
        frontTop.setControlPoint(1, points.get(3));
        frontTop.setControlPoint(2, points.get(4));

        gastLeft.setControlPoint(0, points.get(4));
        gastLeft.setControlPoint(1, points.get(5));
        gastLeft.setControlPoint(2, points.get(6));

        gastTop.setControlPoint(0, points.get(6));
        gastTop.setControlPoint(1, points.get(7));

        gastRight.setControlPoint(0, points.get(7));
        gastRight.setControlPoint(1, points.get(8));
        gastRight.setControlPoint(2, points.get(9));

        hecktTop.setControlPoint(0, points.get(9));
        hecktTop.setControlPoint(1, points.get(10));
        hecktTop.setControlPoint(2, points.get(11));

        heckRight.setControlPoint(0, points.get(11));
        heckRight.setControlPoint(1, points.get(12));
        heckRight.setControlPoint(2, points.get(13));

        chassisRight.setControlPoint(0, points.get(13));
        chassisRight.setControlPoint(1, points.get(14));
        chassisRight.setControlPoint(2, points.get(15));

        chassisBottom.setControlPoint(0, points.get(15));
        chassisBottom.setControlPoint(1, points.get(16));

        chassisLeft.setControlPoint(0, points.get(16));
        chassisLeft.setControlPoint(1, points.get(17));
        chassisLeft.setControlPoint(2, points.get(0));
      }
      curves.add(frontLeft);
      curves.add(frontTop);
      curves.add(gastLeft);
      curves.add(gastTop);
      curves.add(gastRight);
      curves.add(hecktTop);
      curves.add(heckRight);
      curves.add(chassisRight);
      curves.add(chassisBottom);
      curves.add(chassisLeft);

    } else {
      System.err.println("POINTS empty!");
    }
  }

  private void refillPoints() {
    points = null;
    if (x != null && y != null && z != null) {
      points = new ArrayList<Vector>();
      for (int i = 0; i < x.getDimension(); i++) {
        Vector v = VectorFactory.createVector3(x.get(i), y.get(i), z.get(i));
        points.add(v);
      }

    } else {
      System.err.println("VECTORS empty!");
    }
  }

  public void fillPoints() {
    if (!curves.isEmpty()) {
      for (Curve curve : curves) {
        int degree = curve.getDegree();
        for (int i = 0; i <= degree; i++) {
          points.add(curve.getControlPoint(i));
        }
      }
    } else {
      System.err.println("List CURVES is empty!");
    }
  }

  public void fillArrays() {
    if (!points.isEmpty()) {
      int i = 0;
      for (Vector v : points) {
        x.set(i, v.get(0));
        y.set(i, v.get(1));
        z.set(i, v.get(2));
        i++;
      }
    } else {
      System.err.println("List POINTS is empty!");
    }
  }

  public List<Curve> getCurves() {
    return curves;
  }

  public void setCurves(List<Curve> curves) {
    this.curves = curves;
  }

  public List<Vector> getPoints() {
    return points;
  }

  public void setPoints(List<Vector> points) {
    this.points = points;
  }

  public Vector getX() {
    return x;
  }

  public void setX(Vector x) {
    this.x = null;
    this.x = x;
  }

  public Vector getY() {
    return y;
  }

  public void setY(Vector y) {
    this.y = null;
    this.y = y;
  }

  public Vector getZ() {
    return z;
  }

  public void setZ(Vector z) {
    this.z = null;
    this.z = z;
  }

}

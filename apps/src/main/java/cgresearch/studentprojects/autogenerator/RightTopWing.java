package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.curves.BasisFunctionBezier;
import cgresearch.graphics.datastructures.curves.Curve;
import cgresearch.graphics.datastructures.primitives.Line3D;

public class RightTopWing {
  private Vector center;

  private Vector A;
  private Vector B;
  private Vector C;
  private Vector D;

  private Line3D AB;
  private Line3D BC;
  private Line3D CD;
  private Line3D DA;

  private Curve top;
  private Curve right;
  private Curve bottom;

  private double hoehe;
  private double laenge;

  private Line3D[] lines = new Line3D[4];
  private List<Curve> curves = new ArrayList<Curve>();

  public RightTopWing(Vector center, double hoehe, double laenge, Vector tp,
      double wingMitteBreite, double twth, double twtv, double twlrh,
      double twlrv, double twtph, double twtpv, double twbh, double twbv) {

    setCenter(center);
    setHoehe(hoehe);
    setLaenge(laenge);
    setA(center, hoehe, laenge);
    setB(center, hoehe, laenge);
    setC(center, hoehe, laenge);
    setD(center, hoehe, laenge);
    setAB();
    setBC();
    setCD();
    setDA();
    setLines();
    setTop(wingMitteBreite, twth, twtv);
    setRight(twlrh, twlrv, twtph, twtpv);
    setBottom(tp, twtph, twtpv, twbh, twbv);
    setCurves(top, right, bottom);
  }

  public Vector getCenter() {
    return center;
  }

  public void setCenter(Vector center) {
    this.center = center;
  }

  public Vector getA() {
    return A;
  }

  public void setA(Vector center, double hoehe, double laenge) {
    double x = center.get(0) - laenge / 2;
    double y = center.get(1) - hoehe / 2;
    A = VectorFactory.createVector3(x, y, 0);
  }

  public Vector getB() {
    return B;
  }

  public void setB(Vector center, double hoehe, double laenge) {
    double x = center.get(0) - laenge / 2;
    double y = center.get(1) + hoehe / 2;
    B = VectorFactory.createVector3(x, y, 0);
  }

  public Vector getC() {
    return C;
  }

  public void setC(Vector center, double hoehe, double laenge) {
    double x = center.get(0) + laenge / 2;
    double y = center.get(1) + hoehe / 2;
    C = VectorFactory.createVector3(x, y, 0);
  }

  public Vector getD() {
    return D;
  }

  public void setD(Vector center, double hoehe, double laenge) {
    double x = center.get(0) + laenge / 2;
    double y = center.get(1) - hoehe / 2;
    D = VectorFactory.createVector3(x, y, 0);
  }

  public Line3D getAB() {
    return AB;
  }

  public void setAB() {
    AB = new Line3D(this.A, this.B);
  }

  public Line3D getBC() {
    return BC;
  }

  public void setBC() {
    BC = new Line3D(this.B, this.C);
  }

  public Line3D getCD() {
    return CD;
  }

  public void setCD() {
    CD = new Line3D(this.C, this.D);
  }

  public Line3D getDA() {
    return DA;
  }

  public void setDA() {
    DA = new Line3D(this.D, this.A);
  }

  public double getHoehe() {
    return hoehe;
  }

  public void setHoehe(double hoehe) {
    this.hoehe = hoehe;
  }

  public double getLaenge() {
    return laenge;
  }

  public void setLaenge(double laenge) {
    this.laenge = laenge;
  }

  public Line3D[] getLines() {
    return lines;
  }

  public void setLines() {
    this.lines[0] = AB;
    this.lines[1] = BC;
    this.lines[2] = CD;
    this.lines[3] = DA;
  }

  public Curve getTop() {
    return top;
  }

  public void setTop(double wingMitteBreite, double twth, double twtv) {

    double breite = wingMitteBreite / 2;

    Curve curve = new Curve(new BasisFunctionBezier(), 2);
    ;

    curve.setControlPoint(0,
        VectorFactory.createVector3(A.get(0), A.get(1) + breite, 0));

    curve.setControlPoint(1,
        VectorFactory.createVector3(B.get(0) + twth, B.get(1) + twtv, 0));

    curve.setControlPoint(2,
        VectorFactory.createVector3(C.get(0), C.get(1), 0));

    this.top = curve;
  }

  public Curve getRight() {
    return right;
  }

  public void setRight(double twlrh, double twlrv, double twtph, double twtpv) {
    Curve curve = new Curve(new BasisFunctionBezier(), 2);

    curve.setControlPoint(0,
        VectorFactory.createVector3(C.get(0), C.get(1), 0));

    curve.setControlPoint(1, VectorFactory.createVector3(C.get(0) + twlrh,
        C.get(1) - hoehe / 4 + twlrv, 0));

    curve.setControlPoint(2, VectorFactory.createVector3(
        D.get(0) - laenge / 4 + twtph, D.get(1) + hoehe / 2 + twtpv, 0));

    this.right = curve;
  }

  public Curve getBottom() {
    return bottom;
  }

  public void setBottom(Vector tp, double twtph, double twtpv, double twbh,
      double twbv) {
    Curve curve = new Curve(new BasisFunctionBezier(), 2);

    curve.setControlPoint(0, VectorFactory.createVector3(
        D.get(0) - laenge / 4 + twtph, D.get(1) + hoehe / 2 + twtpv, 0));

    curve.setControlPoint(1, VectorFactory.createVector3(D.get(0) + twbh,
        D.get(1) + hoehe / 4 + twbv, 0));

    curve.setControlPoint(2,
        VectorFactory.createVector3(tp.get(0), tp.get(1), 0));

    this.bottom = curve;
  }

  public List<Curve> getCurves() {
    return curves;
  }

  public void setCurves(Curve... curves) {
    for (Curve c : curves) {
      this.curves.add(c);
    }
  }
}

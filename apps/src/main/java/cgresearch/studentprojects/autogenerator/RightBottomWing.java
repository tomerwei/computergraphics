package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.curves.BasisFunctionBezier;
import cgresearch.graphics.datastructures.curves.Curve;
import cgresearch.graphics.datastructures.primitives.Line3D;

public class RightBottomWing {

  final double offset = 0.03;

  private Vector center;

  private Vector A;
  private Vector B;
  private Vector C;
  private Vector D;

  private Line3D AB;
  private Line3D BC;
  private Line3D CD;
  private Line3D DA;

  private Curve right;
  private Curve bottom;

  private double hoehe;
  private double laenge;

  private Line3D[] lines = new Line3D[4];
  private List<Curve> curves = new ArrayList<Curve>();

  public RightBottomWing(Vector center, double hoehe, double laenge,
      double wingMitteBreite, double bwlrh, double bwlrv, double bwbh,
      double bwbv, double bwtph, double bwtpv, double bwb2h, double bwb2v,
      double bwlr2h, double bwlr2v) {

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
    setRight(bwlrh, bwlrv, bwtph, bwtpv, bwlr2h, bwlr2v);
    setBottom(wingMitteBreite, bwbh, bwbv, bwtph, bwtpv, bwb2h, bwb2v);
    setCurves(bottom, right);
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

  public Curve getRight() {
    return right;
  }

  public void setRight(double bwlrh, double bwlrv, double bwtph, double bwtpv,
      double bwlr2h, double bwlr2v) {
    Curve curve = new Curve(new BasisFunctionBezier(), 3);

    curve.setControlPoint(3,
        VectorFactory.createVector3(D.get(0) + bwtph, D.get(1) + bwtpv, 0));

    curve.setControlPoint(2, VectorFactory.createVector3(D.get(0) + bwlr2h,
        D.get(1) + offset + bwlr2v, 0));

    curve.setControlPoint(1, VectorFactory.createVector3(D.get(0) + bwlrh,
        D.get(1) + hoehe / 2 + bwlrv, 0));

    curve.setControlPoint(0,
        VectorFactory.createVector3(C.get(0), C.get(1), 0));

    this.right = curve;
  }

  public Curve getBottom() {
    return bottom;
  }

  public void setBottom(double wingMitteBreite, double bwbh, double bwbv,
      double bwtph, double bwtpv, double bwb2h, double bwb2v) {

    double breite = wingMitteBreite / 2;

    Curve curve = new Curve(new BasisFunctionBezier(), 3);

    curve.setControlPoint(3,
        VectorFactory.createVector3(B.get(0), B.get(1) - breite, 0));

    curve.setControlPoint(2,
        VectorFactory.createVector3(A.get(0) + bwbh, A.get(1) + bwbv, 0));

    curve.setControlPoint(1, VectorFactory
        .createVector3(D.get(0) + offset + bwb2h, D.get(1) + bwb2v, 0));

    curve.setControlPoint(0,
        VectorFactory.createVector3(D.get(0) + bwtph, D.get(1) + bwtpv, 0));

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

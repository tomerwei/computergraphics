package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Vector;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.curves.BezierCurve;
import cgresearch.graphics.datastructures.primitives.Line3D;

public class Body {

	private Vector center;

	private Vector A;
	private Vector B;
	private Vector C;
	private Vector D;

	private Line3D AB;
	private Line3D BC;
	private Line3D CD;
	private Line3D DA;

	private BezierCurve topLeft;
	private BezierCurve topRight;
	private BezierCurve bottomLeft;
	private BezierCurve bottomRight;

	private double hoehe;
	private double laenge;

	private Line3D[] lines = new Line3D[4];
	private List<BezierCurve> curves = new ArrayList<BezierCurve>();

	public Body(Vector center, double hoehe, double laenge, Vector tl, Vector tr, double wingMitteBreite) {
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
		setTopLeft(tl, wingMitteBreite);
		setBottomLeft(tl, wingMitteBreite);
		setTopRight(tr, wingMitteBreite);
		setBottomRight(tr, wingMitteBreite);
		setCurves(topLeft, topRight, bottomLeft, bottomRight);
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

	public BezierCurve getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(Vector tl, double wingMitteBreite) {

		double breite = wingMitteBreite / 2;

		BezierCurve curve = new BezierCurve(2);

		curve.setControlPoint(0, VectorFactory.createVector3(tl.get(0), tl.get(1) + breite, 0));

		curve.setControlPoint(1, VectorFactory.createVector3(B.get(0), B.get(1), 0));

		curve.setControlPoint(2, VectorFactory.createVector3(B.get(0) + laenge / 2, B.get(1), 0));

		this.topLeft = curve;
	}

	public BezierCurve getTopRight() {
		return topRight;
	}

	public void setTopRight(Vector tr, double wingMitteBreite) {

		double breite = wingMitteBreite / 2;

		BezierCurve curve = new BezierCurve(2);

		curve.setControlPoint(0, VectorFactory.createVector3(B.get(0) + laenge / 2, B.get(1), 0));

		curve.setControlPoint(1, VectorFactory.createVector3(C.get(0), C.get(1), 0));

		curve.setControlPoint(2, VectorFactory.createVector3(tr.get(0), tr.get(1) + breite, 0));

		this.topRight = curve;
	}

	public BezierCurve getBottomLeft() {
		return bottomLeft;
	}

	public void setBottomLeft(Vector bl, double wingMitteBreite) {

		double breite = wingMitteBreite / 2;

		BezierCurve curve = new BezierCurve(2);

		curve.setControlPoint(0, VectorFactory.createVector3(A.get(0) + laenge / 2, A.get(1), 0));

		curve.setControlPoint(1, VectorFactory.createVector3(A.get(0), A.get(1), 0));

		curve.setControlPoint(2, VectorFactory.createVector3(bl.get(0), bl.get(1) - breite, 0));

		this.bottomLeft = curve;
	}

	public BezierCurve getBottomRight() {
		return bottomRight;
	}

	public void setBottomRight(Vector br, double wingMitteBreite) {

		double breite = wingMitteBreite / 2;

		BezierCurve curve = new BezierCurve(2);

		curve.setControlPoint(0, VectorFactory.createVector3(br.get(0), br.get(1) - breite, 0));

		curve.setControlPoint(1, VectorFactory.createVector3(D.get(0), D.get(1), 0));

		curve.setControlPoint(2, VectorFactory.createVector3(A.get(0) + laenge / 2, A.get(1), 0));

		this.bottomRight = curve;
	}

	public List<BezierCurve> getCurves() {
		return curves;
	}

	public void setCurves(BezierCurve... curves) {
		for (BezierCurve c : curves) {
			this.curves.add(c);
		}
	}

}

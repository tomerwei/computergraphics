package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.curves.BezierCurve;
import cgresearch.graphics.datastructures.primitives.Line3D;

public class LeftTopWing {
	private IVector3 center;

	private IVector3 A;
	private IVector3 B;
	private IVector3 C;
	private IVector3 D;

	private Line3D AB;
	private Line3D BC;
	private Line3D CD;
	private Line3D DA;

	private BezierCurve top;
	private BezierCurve left;
	private BezierCurve bottom;

	private double hoehe;
	private double laenge;

	private Line3D[] lines = new Line3D[4];
	private List<BezierCurve> curves = new ArrayList<BezierCurve>();

	public LeftTopWing(IVector3 center, double hoehe, double laenge, IVector3 tp, double wingMitteBreite) {

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
		setTop(wingMitteBreite);
		setLeft();
		setBottom(tp);
		setCurves(top, left, bottom);
	}

	public IVector3 getCenter() {
		return center;
	}

	public void setCenter(IVector3 center) {
		this.center = center;
	}

	public IVector3 getA() {
		return A;
	}

	public void setA(IVector3 center, double hoehe, double laenge) {
		double x = center.get(0) - laenge / 2;
		double y = center.get(1) - hoehe / 2;
		A = VectorMatrixFactory.newIVector3(x, y, 0);
	}

	public IVector3 getB() {
		return B;
	}

	public void setB(IVector3 center, double hoehe, double laenge) {
		double x = center.get(0) - laenge / 2;
		double y = center.get(1) + hoehe / 2;
		B = VectorMatrixFactory.newIVector3(x, y, 0);
	}

	public IVector3 getC() {
		return C;
	}

	public void setC(IVector3 center, double hoehe, double laenge) {
		double x = center.get(0) + laenge / 2;
		double y = center.get(1) + hoehe / 2;
		C = VectorMatrixFactory.newIVector3(x, y, 0);
	}

	public IVector3 getD() {
		return D;
	}

	public void setD(IVector3 center, double hoehe, double laenge) {
		double x = center.get(0) + laenge / 2;
		double y = center.get(1) - hoehe / 2;
		D = VectorMatrixFactory.newIVector3(x, y, 0);
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

	public BezierCurve getTop() {
		return top;
	}

	public void setTop(double wingMitteBreite) {

		double breite = wingMitteBreite / 2;

		BezierCurve curve = new BezierCurve(2);

		curve.setControlPoint(0, VectorMatrixFactory.newIVector3(D.get(0), D.get(1) + breite, 0));

		curve.setControlPoint(1, VectorMatrixFactory.newIVector3(C.get(0), C.get(1), 0));

		curve.setControlPoint(2, VectorMatrixFactory.newIVector3(B.get(0), B.get(1), 0));

		this.top = curve;
	}

	public BezierCurve getLeft() {
		return left;
	}

	public void setLeft() {
		BezierCurve curve = new BezierCurve(2);

		curve.setControlPoint(0, VectorMatrixFactory.newIVector3(B.get(0), B.get(1), 0));

		curve.setControlPoint(1, VectorMatrixFactory.newIVector3(B.get(0), B.get(1) - hoehe / 4, 0));

		curve.setControlPoint(2, VectorMatrixFactory.newIVector3(A.get(0) + laenge / 4, A.get(1) + hoehe / 2, 0));

		this.left = curve;
	}

	public BezierCurve getBottom() {
		return bottom;
	}

	public void setBottom(IVector3 tp) {
		BezierCurve curve = new BezierCurve(2);

		curve.setControlPoint(0, VectorMatrixFactory.newIVector3(A.get(0) + laenge / 4, A.get(1) + hoehe / 2, 0));

		curve.setControlPoint(1, VectorMatrixFactory.newIVector3(A.get(0), A.get(1) + hoehe / 4, 0));

		curve.setControlPoint(2, VectorMatrixFactory.newIVector3(tp.get(0), tp.get(1), 0));

		this.bottom = curve;
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

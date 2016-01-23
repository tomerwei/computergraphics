package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.curves.BezierCurve;
import cgresearch.graphics.datastructures.primitives.Line3D;

public class Heck2D {
	
	private final int carVektor = 28;

	private IVector3 center;

	private IVector3 A;
	private IVector3 B;
	private IVector3 C;
	private IVector3 D;

	private Line3D AB;
	private Line3D BC;
	private Line3D CD;
	private Line3D DA;

	private BezierCurve left;
	private BezierCurve top;
	private BezierCurve right;
	private BezierCurve bottom;

	private double hoehe;
	private double laenge;

	private int degree;

	private Line3D[] lines = new Line3D[4];
	private List<BezierCurve> curves = new ArrayList<BezierCurve>();

	public Heck2D(IVector3 center, double hoehe, double laenge, int degree,
			int heckh, int heckv) {
		setCenter(center);
		setA(center, hoehe, laenge);
		setB(center, hoehe, laenge);
		setC(center, hoehe, laenge);
		setD(center, hoehe, laenge);
		setAB();
		setBC();
		setCD();
		setDA();
		setHoehe(hoehe);
		setLaenge(laenge);
		setLines();
		setDegree(degree);
		// setLeft(A, B);
		setTop(heckh, heckv, B, C);
		setRight(heckh, heckv, C, D);
		// setBottom(D, A);
		setCurves(top, right);
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
		;
	}

	public Line3D getCD() {
		return CD;
	}

	public void setCD() {
		CD = new Line3D(this.C, this.D);
		;
	}

	public Line3D getDA() {
		return DA;
	}

	public void setDA() {
		DA = new Line3D(this.D, this.A);
	}

	public BezierCurve getLeft() {
		return left;
	}

	public void setLeft(IVector3... points) {
		int degree = points.length - 1;
		int i = 0;
		BezierCurve curve = new BezierCurve(degree);
		for (IVector3 p : points) {
			curve.setControlPoint(
					i,
					VectorMatrixFactory.newIVector3(p.get(0), p.get(1),
							p.get(2)));
			i++;
		}

		this.left = curve;
	}

	public BezierCurve getTop() {
		return top;
	}

	public void setTop(int heckh, int heckv, IVector3... points) {

		double x = this.laenge * heckh / 100;
		double y = this.hoehe * heckv / 100;

		BezierCurve curve = new BezierCurve(degree);

		curve.setControlPoint(
				0,
				VectorMatrixFactory.newIVector3(points[0].get(0),
						points[0].get(1), 0));

		// for (int i = 1; i < degree; i++) {
		// curve.setControlPoint(i, VectorMatrixFactory.newIVector3(x + i * xx,
		// y + i*yy, 0));
		// }

		// curve.setControlPoint(1, VectorMatrixFactory.newIVector3(
		// points[1].get(0) - 0.2, points[1].get(1) - 0.4, 0));
		//
		// curve.setControlPoint(
		// 2,
		// VectorMatrixFactory.newIVector3(points[1].get(0),
		// points[1].get(1) - 0.2, 0));

		curve.setControlPoint(1, VectorMatrixFactory.newIVector3(
				points[1].get(0) - 2 * x, points[1].get(1) - y, 0));

		curve.setControlPoint(
				2,
				VectorMatrixFactory.newIVector3(points[1].get(0) - x,
						points[1].get(1) - y, 0));

		this.top = curve;
	}

	public BezierCurve getRight() {
		return right;
	}

	public void setRight(int heckh, int heckv, IVector3... points) {

		double x = this.laenge * heckh / 100;
		double y = this.hoehe * heckv / 100;

		BezierCurve curve = new BezierCurve(degree);

		curve.setControlPoint(
				0,
				VectorMatrixFactory.newIVector3(points[0].get(0) - x,
						points[0].get(1) - y, 0));

		// for (int i = 1; i < degree; i++) {
		// curve.setControlPoint(i, VectorMatrixFactory.newIVector3(x - i * xx,
		// y - i*yy, 0));
		// }

		// curve.setControlPoint(1, VectorMatrixFactory.newIVector3(
		// points[0].get(0) + 0.2, points[0].get(1) - 0.2, 0));
		//
		// curve.setControlPoint(2, VectorMatrixFactory.newIVector3(
		// points[1].get(0) + 0.4, points[1].get(1), 0));

		curve.setControlPoint(
				1,
				VectorMatrixFactory.newIVector3(points[0].get(0),
						points[0].get(1) - y, 0));

		curve.setControlPoint(
				2,
				VectorMatrixFactory.newIVector3(points[1].get(0),
						points[1].get(1), 0));

		this.right = curve;
	}

	public BezierCurve getBottom() {
		return bottom;
	}

	public void setBottom(IVector3... points) {
		int degree = points.length - 1;
		int i = 0;
		BezierCurve curve = new BezierCurve(degree);
		for (IVector3 p : points) {
			curve.setControlPoint(
					i,
					VectorMatrixFactory.newIVector3(p.get(0), p.get(1),
							p.get(2)));
			i++;
		}
		this.bottom = curve;
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

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
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

	public List<BezierCurve> getCurves() {
		return curves;
	}

	public void setCurves(BezierCurve... curves) {
		for (BezierCurve c : curves) {
			this.curves.add(c);
		}
	}

}

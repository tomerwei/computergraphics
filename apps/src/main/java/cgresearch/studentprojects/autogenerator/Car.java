package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.curves.BezierCurve;

public class Car {

	private List<BezierCurve> curves = new ArrayList<BezierCurve>();
	private List<IVector3> points = new ArrayList<IVector3>();
	private Vector x = new Vector(28);
	private Vector y = new Vector(28);
	private Vector z = new Vector(28);

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
			curves = new ArrayList<BezierCurve>();
			BezierCurve frontLeft = new BezierCurve(2);
			BezierCurve frontTop = new BezierCurve(2);

			BezierCurve gastLeft = new BezierCurve(2);
			BezierCurve gastTop = new BezierCurve(1);
			BezierCurve gastRight = new BezierCurve(2);

			BezierCurve hecktTop = new BezierCurve(2);
			BezierCurve heckRight = new BezierCurve(2);

			BezierCurve chassisRight = new BezierCurve(2);
			BezierCurve chassisBottom = new BezierCurve(1);
			BezierCurve chassisLeft = new BezierCurve(2);

			frontLeft.setControlPoint(0, points.get(0));
			frontLeft.setControlPoint(1, points.get(1));
			frontLeft.setControlPoint(2, points.get(2));

			frontTop.setControlPoint(0, points.get(3));
			frontTop.setControlPoint(1, points.get(4));
			frontTop.setControlPoint(2, points.get(5));

			gastLeft.setControlPoint(0, points.get(6));
			gastLeft.setControlPoint(1, points.get(7));
			gastLeft.setControlPoint(2, points.get(8));

			gastTop.setControlPoint(0, points.get(9));
			gastTop.setControlPoint(1, points.get(10));

			gastRight.setControlPoint(0, points.get(11));
			gastRight.setControlPoint(1, points.get(12));
			gastRight.setControlPoint(2, points.get(13));

			hecktTop.setControlPoint(0, points.get(14));
			hecktTop.setControlPoint(1, points.get(15));
			hecktTop.setControlPoint(2, points.get(16));

			heckRight.setControlPoint(0, points.get(17));
			heckRight.setControlPoint(1, points.get(18));
			heckRight.setControlPoint(2, points.get(19));

			chassisRight.setControlPoint(0, points.get(20));
			chassisRight.setControlPoint(1, points.get(21));
			chassisRight.setControlPoint(2, points.get(22));

			chassisBottom.setControlPoint(0, points.get(23));
			chassisBottom.setControlPoint(1, points.get(24));

			chassisLeft.setControlPoint(0, points.get(25));
			chassisLeft.setControlPoint(1, points.get(26));
			chassisLeft.setControlPoint(2, points.get(27));

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
			points = new ArrayList<IVector3>();
			for (int i = 0; i < x.getDimension(); i++) {
				IVector3 v = VectorMatrixFactory.newIVector3(x.get(i), y.get(i), z.get(i));
				points.add(v);
			}

		} else {
			System.err.println("VECTORS empty!");
		}
	}

	public void fillPoints() {
		if (!curves.isEmpty()) {
			for (BezierCurve curve : curves) {
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
			for (IVector3 v : points) {
				x.set(i, v.get(0));
				y.set(i, v.get(1));
				z.set(i, v.get(2));
				i++;
			}
		} else {
			System.err.println("List POINTS is empty!");
		}
	}

	public List<BezierCurve> getCurves() {
		return curves;
	}

	public void setCurves(List<BezierCurve> curves) {
		this.curves = curves;
	}

	public List<IVector3> getPoints() {
		return points;
	}

	public void setPoints(List<IVector3> points) {
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

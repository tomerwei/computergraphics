package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Vector;
import cgresearch.core.math.Vector;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.curves.BezierCurve;

public class ButModel {

	private final int butVektor = 46;

	private List<BezierCurve> curves = new ArrayList<BezierCurve>();
	private List<Vector> points = new ArrayList<Vector>();
	private Vector x = new Vector(butVektor);
	private Vector y = new Vector(butVektor);
	private Vector z = new Vector(butVektor);

	public ButModel() {

	}

	public ButModel(Vector x, Vector y, Vector z) {
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
			BezierCurve btl = new BezierCurve(2);
			BezierCurve btr = new BezierCurve(2);

			BezierCurve rtt = new BezierCurve(2);
			BezierCurve rtr = new BezierCurve(2);
			BezierCurve rtb = new BezierCurve(2);

			BezierCurve rbr = new BezierCurve(3);
			BezierCurve rbb = new BezierCurve(3);

			BezierCurve bbr = new BezierCurve(2);
			BezierCurve bbl = new BezierCurve(2);

			BezierCurve lbb = new BezierCurve(3);
			BezierCurve lbl = new BezierCurve(3);

			BezierCurve ltb = new BezierCurve(2);
			BezierCurve ltl = new BezierCurve(2);
			BezierCurve ltt = new BezierCurve(2);

			System.out.println(points.size());

			if (points.size() == butVektor) {
				btl.setControlPoint(0, points.get(0));
				btl.setControlPoint(1, points.get(1));
				btl.setControlPoint(2, points.get(2));

				btr.setControlPoint(0, points.get(2));
				btr.setControlPoint(1, points.get(4));
				btr.setControlPoint(2, points.get(5));

				rtt.setControlPoint(0, points.get(5));
				rtt.setControlPoint(1, points.get(7));
				rtt.setControlPoint(2, points.get(8));

				rtr.setControlPoint(0, points.get(8));
				rtr.setControlPoint(1, points.get(10));
				rtr.setControlPoint(2, points.get(11));

				rtb.setControlPoint(0, points.get(11));
				rtb.setControlPoint(1, points.get(13));
				rtb.setControlPoint(2, points.get(14));

				rbr.setControlPoint(0, points.get(14));
				rbr.setControlPoint(1, points.get(16));
				rbr.setControlPoint(2, points.get(17));
				rbr.setControlPoint(3, points.get(18));

				rbb.setControlPoint(0, points.get(18));
				rbb.setControlPoint(1, points.get(20));
				rbb.setControlPoint(2, points.get(21));
				rbb.setControlPoint(3, points.get(22));

				bbr.setControlPoint(0, points.get(22));
				bbr.setControlPoint(1, points.get(24));
				bbr.setControlPoint(2, points.get(25));

				bbl.setControlPoint(0, points.get(25));
				bbl.setControlPoint(1, points.get(27));
				bbl.setControlPoint(2, points.get(28));

				lbb.setControlPoint(0, points.get(28));
				lbb.setControlPoint(1, points.get(30));
				lbb.setControlPoint(2, points.get(31));
				lbb.setControlPoint(3, points.get(32));

				lbl.setControlPoint(0, points.get(32));
				lbl.setControlPoint(1, points.get(34));
				lbl.setControlPoint(2, points.get(35));
				lbl.setControlPoint(3, points.get(36));

				ltb.setControlPoint(0, points.get(36));
				ltb.setControlPoint(1, points.get(38));
				ltb.setControlPoint(2, points.get(39));

				ltl.setControlPoint(0, points.get(39));
				ltl.setControlPoint(1, points.get(41));
				ltl.setControlPoint(2, points.get(42));

				ltt.setControlPoint(0, points.get(42));
				ltt.setControlPoint(1, points.get(44));
				ltt.setControlPoint(2, points.get(0));

			} else {
				btl.setControlPoint(0, points.get(0));
				btl.setControlPoint(1, points.get(1));
				btl.setControlPoint(2, points.get(2));

				btr.setControlPoint(0, points.get(2));
				btr.setControlPoint(1, points.get(3));
				btr.setControlPoint(2, points.get(4));

				rtt.setControlPoint(0, points.get(4));
				rtt.setControlPoint(1, points.get(5));
				rtt.setControlPoint(2, points.get(6));

				rtr.setControlPoint(0, points.get(6));
				rtr.setControlPoint(1, points.get(7));
				rtr.setControlPoint(2, points.get(8));

				rtb.setControlPoint(0, points.get(8));
				rtb.setControlPoint(1, points.get(9));
				rtb.setControlPoint(2, points.get(10));

				rbr.setControlPoint(0, points.get(10));
				rbr.setControlPoint(1, points.get(11));
				rbr.setControlPoint(2, points.get(12));
				rbr.setControlPoint(3, points.get(13));

				rbb.setControlPoint(0, points.get(13));
				rbb.setControlPoint(1, points.get(14));
				rbb.setControlPoint(2, points.get(15));
				rbb.setControlPoint(3, points.get(16));

				bbr.setControlPoint(0, points.get(16));
				bbr.setControlPoint(1, points.get(17));
				bbr.setControlPoint(2, points.get(18));

				bbl.setControlPoint(0, points.get(18));
				bbl.setControlPoint(1, points.get(19));
				bbl.setControlPoint(2, points.get(20));

				lbb.setControlPoint(0, points.get(20));
				lbb.setControlPoint(1, points.get(21));
				lbb.setControlPoint(2, points.get(22));
				lbb.setControlPoint(3, points.get(23));

				lbl.setControlPoint(0, points.get(23));
				lbl.setControlPoint(1, points.get(24));
				lbl.setControlPoint(2, points.get(25));
				lbl.setControlPoint(3, points.get(26));

				ltb.setControlPoint(0, points.get(26));
				ltb.setControlPoint(1, points.get(27));
				ltb.setControlPoint(2, points.get(28));

				ltl.setControlPoint(0, points.get(28));
				ltl.setControlPoint(1, points.get(29));
				ltl.setControlPoint(2, points.get(30));

				ltt.setControlPoint(0, points.get(30));
				ltt.setControlPoint(1, points.get(31));
				ltt.setControlPoint(2, points.get(0));
			}
			curves.add(btl);
			curves.add(btr);
			curves.add(rtt);
			curves.add(rtr);
			curves.add(rtb);
			curves.add(rbr);
			curves.add(rbb);
			curves.add(bbr);
			curves.add(bbl);
			curves.add(lbb);
			curves.add(lbl);
			curves.add(ltb);
			curves.add(ltl);
			curves.add(ltt);

		} else {
			System.err.println("POINTS empty!");
		}
	}

	private void refillPoints() {
		points = null;
		if (x != null && y != null && z != null) {
			points = new ArrayList<Vector>();
			for (int i = 0; i < x.getDimension(); i++) {
				Vector v = VectorMatrixFactory.newVector(x.get(i), y.get(i), z.get(i));
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

	public List<BezierCurve> getCurves() {
		return curves;
	}

	public void setCurves(List<BezierCurve> curves) {
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

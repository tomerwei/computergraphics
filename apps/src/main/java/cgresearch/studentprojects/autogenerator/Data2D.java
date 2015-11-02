package cgresearch.studentprojects.autogenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.IVector3;
import cgresearch.graphics.datastructures.curves.BezierCurve;

public class Data2D implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<BezierCurve> curves = new ArrayList<BezierCurve>();
	private List<IVector3> points = new ArrayList<IVector3>();
	private List<Double> x = new ArrayList<Double>();
	private List<Double> y = new ArrayList<Double>();

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
			for (IVector3 v : points) {
				x.add(v.get(0));
				y.add(v.get(1));
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

	public List<Double> getX() {
		return x;
	}

	public void setX(List<Double> x) {
		this.x = x;
	}

	public List<Double> getY() {
		return y;
	}

	public void setY(List<Double> y) {
		this.y = y;
	}

}

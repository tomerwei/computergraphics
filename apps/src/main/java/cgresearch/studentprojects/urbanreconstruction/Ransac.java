package cgresearch.studentprojects.urbanreconstruction;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.datastructures.primitives.Plane;

public class Ransac {

	// PlyConverter pc = new PlyConverter();

	private Plane plane;

	// private IPointCloud points;

	private int forseeable_support;
	private final double alpha = 0.99;
	private double t;

	public int getForseeable_support() {
		return forseeable_support;
	}

	public void setForseeable_support(int forseeable_support) {
		this.forseeable_support = forseeable_support;
	}

	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}

	public Ransac(int support, double dev) {
		// this.points = pc.getPointCloud();
		this.forseeable_support = support;
		this.t = dev;

	}

	public void runRansac(List<Point> points) {
		Vector norm_s = new Vector(3);

		int numberOfPoints = points.size();
		// System.out.println("Number of points: " + numberOfPoints);

		int bestSupport = 0;
		Plane bestPlane = new Plane(VectorMatrixFactory.newVector(0, 0, 0),
				VectorMatrixFactory.newVector(0, 0, 0));
		int i = 0;
		double e = 1 - (double) forseeable_support / (double) numberOfPoints;
		// System.out.println("e: " + e);

		double N = Math.round(Math.log(1 - alpha)
				/ Math.log(1 - Math.pow((1 - e), 3)));
		System.out.println("N: " + N);

		while (i < N) {
			System.out.println("Running... " + i);

			Point[] sample = new Point[3];
			boolean[] checked = new boolean[numberOfPoints];
			int r1, r2, r3;
			List<Double> distances = new ArrayList<Double>(numberOfPoints);

			do {
				r1 = (int) (Math.random() * numberOfPoints);
				r2 = (int) (Math.random() * numberOfPoints);
				r3 = (int) (Math.random() * numberOfPoints);
			} while (r1 == r2 || r1 == r3 || r2 == r3 || checked[r1]
					|| checked[r2] || checked[r3]);

			sample[0] = points.get(r1);
			sample[1] = points.get(r2);
			sample[2] = points.get(r3);

			checked[r1] = true;
			checked[r2] = true;
			checked[r3] = true;

			findPlane(sample[0], sample[1], sample[2]);

			// Save norm
			norm_s = this.plane.getNormal();

			normalize();
			for (int j = 0; j < numberOfPoints; j++) {
				distances.add(checkPoint(j, points));
				// System.out.println("Distance to Point: " + j + " = "
				// + distances.get(j));

			}

			// Restore norm
			this.plane.setNormal(norm_s);

			List<Double> remove = new ArrayList<Double>();
			for (Double d : distances) {
				if (d > t)
					remove.add(d);
			}
			distances.removeAll(remove);

			// Show filtered distances

			// int j = 0;
			// for (Double d : distances) {
			//
			// System.out.println("Distance to Point: " + j + " = " + d);
			// j++;
			// }

			// My version of findDistance

			// for (int j = 0; j < numberOfPoints; j++) {
			// distances.add(getDistance(points.getPoint(j)));
			// System.out.println("Distance to Point: " + j + " = "
			// + distances.get(j));
			// }

			if (distances.size() > bestSupport) {
				bestSupport = distances.size();
				bestPlane = this.plane;
			}

			i += 1;
			// i = (int) N + 1;
		}

		System.out.println("Done!");
		this.plane = bestPlane;

	}

	private void findPlane(Point x, Point y, Point z) {

		double A, B, C;

		double x1 = x.getPosition().get(0);
		double x2 = x.getPosition().get(1);
		double x3 = x.getPosition().get(2);
		double y1 = y.getPosition().get(0);
		double y2 = y.getPosition().get(1);
		double y3 = y.getPosition().get(2);
		double z1 = z.getPosition().get(0);
		double z2 = z.getPosition().get(1);
		double z3 = z.getPosition().get(2);

		// Finding vectors
		double xy1 = y1 - x1;
		double xy2 = y2 - x2;
		double xy3 = y3 - x3;
		double xz1 = z1 - x1;
		double xz2 = z2 - x2;
		double xz3 = z3 - x3;

		// Finding cross product
		A = xy2 * xz3 - xy3 * xz2;
		B = xy3 * xz1 - xy1 * xz3;
		C = xy1 * xz2 - xy2 * xz1;

		// double D = A * x1 + B * x2 + C * x3;

		// Plane:
		// Ax + By + Cz + D = 0;

		Vector norm = new Vector(A, B, C);
		// System.out.println("A = " + A);
		// System.out.println("B = " + B);
		// System.out.println("C = " + C);
		Vector point = new Vector(x1, x2, x3);
		// System.out.println("x1 = " + x1);
		// System.out.println("x2 = " + x2);
		// System.out.println("x3 = " + x3);
		// System.out.println("y1 = " + y1);
		// System.out.println("y2 = " + y2);
		// System.out.println("y3 = " + y3);
		// System.out.println("z1 = " + z1);
		// System.out.println("z2 = " + z2);
		// System.out.println("z3 = " + z3);
		Plane p = new Plane(point, norm);
		this.plane = null;
		this.plane = p;

	}

	private double checkPoint(int pos, List<Point> points) {
		double d = Double.MAX_VALUE;

		d = plane.computeDistance(points.get(pos).getPosition());

		return d;
	}

	private void normalize() {
		double x, y, z, l, nx, ny, nz;

		x = this.plane.getNormal().get(0);
		y = this.plane.getNormal().get(1);
		z = this.plane.getNormal().get(2);
		l = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));

		nx = x / l;
		ny = y / l;
		nz = z / l;

		Vector norm = VectorMatrixFactory.newVector(nx, ny, nz);
		this.plane.setNormal(norm);

	}

	public IPointCloud getResult(List<Point> points) {
		IPointCloud result = new PointCloud();
		Vector norm_s = this.plane.getNormal();
		normalize();

		for (int i = 0; i < points.size(); i++) {
			if (checkPoint(i, points) <= t)
				result.addPoint(points.get(i));
		}

		this.plane.setNormal(norm_s);

		return result;
	}

	public static void main(String[] args) {
		// Ransac ran = new Ransac(2500, 0.002);
		// PlyConverter pc = new PlyConverter(new File("pmvs_options.txt.ply"));
		//
		// IPointCloud pointCloud = pc.getPointCloud();
		// List<Point> list = new ArrayList<Point>();
		// list = copyListOfPoints(pointCloud);
		// System.out.println("run");
		// ran.runRansac(list);

		// Ransac r = new Ransac(2500, 0.002);
		//
		// Point x = new Point(new Vector(-1, 8, 0), new Vector(), new
		// Vector());
		// Point z = new Point(new Vector(2, 4, -3), new Vector(), new
		// Vector());
		// Point y = new Point(new Vector(-1, 9, 2), new Vector(), new
		// Vector());
		//
		// r.findPlane(x, y, z);
		// r.normalize();
		// System.out.println("Get Normalized "
		// + r.getPlane().getNormal().getNormalized());
		//
		// System.out
		// .println("Norm " + r.plane.getNormal().get(0) + " "
		// + r.plane.getNormal().get(1) + " "
		// + r.plane.getNormal().get(2));
		// System.out.println("Point " + r.getPlane().getPoint().get(0) + " "
		// + r.getPlane().getPoint().get(1) + " "
		// + r.getPlane().getPoint().get(2));

	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}

}

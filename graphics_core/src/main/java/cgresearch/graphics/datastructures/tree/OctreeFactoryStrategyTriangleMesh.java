package cgresearch.graphics.datastructures.tree;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;

/**
 * Strategy class to create an octree of triangles.
 * 
 * Intersection algorithm based on the method of AABB-triangle overlap test code
 * by Tomas Akenine-Möller
 * 
 * @author Philipp Jenke
 *
 */
public class OctreeFactoryStrategyTriangleMesh implements
		IOctreeFactoryStrategy<Integer> {

	/**
	 * Constant fields.
	 */
	private final static int X = 0;
	private final static int Y = 1;
	private final static int Z = 2;

	/**
	 * Reference to the mesh.
	 */
	private final ITriangleMesh mesh;

	/**
	 * Constructor.
	 */
	public OctreeFactoryStrategyTriangleMesh(ITriangleMesh mesh) {
		this.mesh = mesh;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return mesh.getBoundingBox();
	}

	@Override
	public int getNumberOfElements() {
		return mesh.getNumberOfTriangles();
	}

	@Override
	public boolean elementFitsInNode(int elementIndex, OctreeNode<Integer> node) {
		ITriangle triangle = mesh.getTriangle(elementIndex);
		IVector3 boxhalfsize = VectorMatrixFactory.newIVector3(
				node.getLength() / 2.0, node.getLength() / 2.0,
				node.getLength() / 2.0);
		IVector3 boxcenter = node.getLowerLeft().add(boxhalfsize);
		IVector3 a = mesh.getVertex(triangle.getA()).getPosition();
		IVector3 b = mesh.getVertex(triangle.getB()).getPosition();
		IVector3 c = mesh.getVertex(triangle.getC()).getPosition();

		IVector3 v0, v1, v2;
		double fex, fey, fez;
		IVector3 normal;
		v0 = a.subtract(boxcenter);
		v1 = b.subtract(boxcenter);
		v2 = c.subtract(boxcenter);
		IVector3 e0 = v1.subtract(v0);
		IVector3 e1 = v2.subtract(v1);
		IVector3 e2 = v0.subtract(v2);
		fex = Math.abs(e0.get(X));
		fey = Math.abs(e0.get(Y));
		fez = Math.abs(e0.get(Z));
		if (!axisTestX01(v0, v2, boxhalfsize, e0.get(Z), e0.get(Y), fez, fey)) {
			return false;
		}
		if (!axisTestY02(v0, v2, boxhalfsize, e0.get(Z), e0.get(X), fez, fex)) {
			return false;
		}
		if (!axisTestZ12(v1, v2, boxhalfsize, e0.get(Y), e0.get(X), fey, fex)) {
			return false;
		}
		fex = Math.abs(e1.get(X));
		fey = Math.abs(e1.get(Y));
		fez = Math.abs(e1.get(Z));
		if (!axisTestX01(v0, v2, boxhalfsize, e1.get(Z), e1.get(Y), fez, fey)) {
			return false;
		}
		if (!axisTestY02(v0, v2, boxhalfsize, e1.get(Z), e1.get(X), fez, fex)) {
			return false;
		}
		if (!axisTestZ0(v0, v1, boxhalfsize, e1.get(Y), e1.get(X), fey, fex)) {
			return false;
		}
		fex = Math.abs(e2.get(X));
		fey = Math.abs(e2.get(Y));
		fez = Math.abs(e2.get(Z));
		if (!axisTestX2(v0, v1, boxhalfsize, e2.get(Z), e2.get(Y), fez, fey)) {
			return false;
		}
		if (!axisTestY1(v0, v1, boxhalfsize, e2.get(Z), e2.get(X), fez, fex)) {
			return false;
		}
		if (!axisTestZ12(v1, v2, boxhalfsize, e2.get(Y), e2.get(X), fey, fex)) {
			return false;
		}
		double min = Math.min(v0.get(X), Math.min(v1.get(X), v2.get(X)));
		double max = Math.min(v0.get(X), Math.min(v1.get(X), v2.get(X)));
		if (min > boxhalfsize.get(X) || max < -boxhalfsize.get(X)) {
			return false;
		}
		min = Math.min(v0.get(Y), Math.min(v1.get(Y), v2.get(Y)));
		max = Math.min(v0.get(Y), Math.min(v1.get(Y), v2.get(Y)));
		if (min > boxhalfsize.get(Y) || max < -boxhalfsize.get(Y)) {
			return false;
		}
		min = Math.min(v0.get(Z), Math.min(v1.get(Z), v2.get(Z)));
		max = Math.min(v0.get(Z), Math.min(v1.get(Z), v2.get(Z)));
		if (min > boxhalfsize.get(Z) || max < -boxhalfsize.get(Z)) {
			return false;
		}
		normal = e0.cross(e1);
		if (!planeBoxOverlap(normal, v0, boxhalfsize)) {
			return false;
		}
		return true;
	}

	/**
	 * Use the normal to check if the triangle plane and the box overlap.
	 */
	private boolean planeBoxOverlap(IVector3 normal, IVector3 vert,
			IVector3 maxbox) {
		IVector3 vmin = VectorMatrixFactory.newIVector3();
		IVector3 vmax = VectorMatrixFactory.newIVector3();
		double v;
		for (int q = 0; q <= 2; q++) {
			v = vert.get(q);
			if (normal.get(q) > 0.0f) {
				vmin.set(q, -maxbox.get(q) - v);
				vmax.set(q, maxbox.get(q) - v);
			} else {
				vmin.set(q, maxbox.get(q) - v);
				vmax.set(q, -maxbox.get(q) - v);
			}
		}
		if (normal.multiply(vmin) > 0.0f) {
			return false;
		}
		if (normal.multiply(vmax) >= 0.0f) {
			return true;
		}
		return false;

	}

	/**
	 * Axis test.
	 */
	private boolean axisTestX01(IVector3 v0, IVector3 v2,
			IVector3 boxhalfsize, double a, double b, double fa, double fb) {
		double p0 = a * v0.get(Y) - b * v0.get(Z);
		double p2 = a * v2.get(Y) - b * v2.get(Z);
		double min, max;
		if (p0 < p2) {
			min = p0;
			max = p2;
		} else {
			min = p2;
			max = p0;
		}
		double rad = fa * boxhalfsize.get(Y) + fb * boxhalfsize.get(Z);
		if (min > rad || max < -rad) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Axis test.
	 */
	private boolean axisTestX2(IVector3 v0, IVector3 v1, IVector3 boxhalfsize,
			double a, double b, double fa, double fb) {
		double p0 = a * v0.get(Y) - b * v0.get(Z);
		double p1 = a * v1.get(Y) - b * v1.get(Z);
		double min, max;
		if (p0 < p1) {
			min = p0;
			max = p1;
		} else {
			min = p1;
			max = p0;
		}
		double rad = fa * boxhalfsize.get(Y) + fb * boxhalfsize.get(Z);
		if (min > rad || max < -rad) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Axis test.
	 */
	private boolean axisTestY02(IVector3 v0, IVector3 v2,
			IVector3 boxhalfsize, double a, double b, double fa, double fb) {
		double p0 = -a * v0.get(X) + b * v0.get(Z);
		double p2 = -a * v2.get(X) + b * v2.get(Z);
		double min, max;
		if (p0 < p2) {
			min = p0;
			max = p2;
		} else {
			min = p2;
			max = p0;
		}
		double rad = fa * boxhalfsize.get(X) + fb * boxhalfsize.get(Z);
		if (min > rad || max < -rad) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Axis test.
	 */
	private boolean axisTestY1(IVector3 v0, IVector3 v1, IVector3 boxhalfsize,
			double a, double b, double fa, double fb) {
		double p0 = -a * v0.get(X) + b * v0.get(Z);
		double p1 = -a * v1.get(X) + b * v1.get(Z);
		double min, max;
		if (p0 < p1) {
			min = p0;
			max = p1;
		} else {
			min = p1;
			max = p0;
		}
		double rad = fa * boxhalfsize.get(X) + fb * boxhalfsize.get(Z);
		if (min > rad || max < -rad) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Axis test.
	 */
	private boolean axisTestZ12(IVector3 v1, IVector3 v2,
			IVector3 boxhalfsize, double a, double b, double fa, double fb) {
		double p1 = a * v1.get(X) - b * v1.get(Y);
		double p2 = a * v2.get(X) - b * v2.get(Y);
		double min, max;
		if (p2 < p1) {
			min = p2;
			max = p1;
		} else {
			min = p1;
			max = p2;
		}
		double rad = fa * boxhalfsize.get(X) + fb * boxhalfsize.get(Y);
		if (min > rad || max < -rad) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Axis test.
	 */
	private boolean axisTestZ0(IVector3 v0, IVector3 v1, IVector3 boxhalfsize,
			double a, double b, double fa, double fb) {
		double p0 = a * v0.get(X) - b * v0.get(Y);
		double p1 = a * v1.get(X) - b * v1.get(Y);
		double min, max;
		if (p0 < p1) {
			min = p0;
			max = p1;
		} else {
			min = p1;
			max = p0;
		}
		double rad = fa * boxhalfsize.get(X) + fb * boxhalfsize.get(Y);
		if (min > rad || max < -rad) {
			return false;
		} else {
			return true;
		}
	}
}

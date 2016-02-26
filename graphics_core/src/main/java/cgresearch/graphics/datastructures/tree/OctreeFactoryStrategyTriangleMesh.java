package cgresearch.graphics.datastructures.tree;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
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
public class OctreeFactoryStrategyTriangleMesh implements IOctreeFactoryStrategy<Integer> {
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
    Vector a = mesh.getVertex(triangle.getA()).getPosition();
    Vector b = mesh.getVertex(triangle.getB()).getPosition();
    Vector c = mesh.getVertex(triangle.getC()).getPosition();
    return Intersection.intersect(node.getBoundingBox(), a, b, c);
    // return deprecatedNodeTriangleIntersectionTest(elementIndex, node);
  }

  // /**
  // * Use the normal to check if the triangle plane and the box overlap.
  // */
  // private boolean planeBoxOverlap(Vector normal, Vector vert, Vector
  // maxbox) {
  // Vector vmin = VectorMatrixFactory.newVector();
  // Vector vmax = VectorMatrixFactory.newVector();
  // double v;
  // for (int q = 0; q <= 2; q++) {
  // v = vert.get(q);
  // if (normal.get(q) > 0.0f) {
  // vmin.set(q, -maxbox.get(q) - v);
  // vmax.set(q, maxbox.get(q) - v);
  // } else {
  // vmin.set(q, maxbox.get(q) - v);
  // vmax.set(q, -maxbox.get(q) - v);
  // }
  // }
  // if (normal.multiply(vmin) > 0.0f) {
  // return false;
  // }
  // if (normal.multiply(vmax) >= 0.0f) {
  // return true;
  // }
  // return false;
  //
  // }
  //
  // /**
  // * Axis test.
  // */
  // private boolean axisTestX01(Vector v0, Vector v2, Vector boxhalfsize,
  // double a, double b, double fa,
  // double fb) {
  // double p0 = a * v0.get(Y) - b * v0.get(Z);
  // double p2 = a * v2.get(Y) - b * v2.get(Z);
  // double min, max;
  // if (p0 < p2) {
  // min = p0;
  // max = p2;
  // } else {
  // min = p2;
  // max = p0;
  // }
  // double rad = fa * boxhalfsize.get(Y) + fb * boxhalfsize.get(Z);
  // if (min > rad || max < -rad) {
  // return false;
  // } else {
  // return true;
  // }
  // }
  //
  // /**
  // * Axis test.
  // */
  // private boolean axisTestX2(Vector v0, Vector v1, Vector boxhalfsize,
  // double a, double b, double fa, double fb) {
  // double p0 = a * v0.get(Y) - b * v0.get(Z);
  // double p1 = a * v1.get(Y) - b * v1.get(Z);
  // double min, max;
  // if (p0 < p1) {
  // min = p0;
  // max = p1;
  // } else {
  // min = p1;
  // max = p0;
  // }
  // double rad = fa * boxhalfsize.get(Y) + fb * boxhalfsize.get(Z);
  // if (min > rad || max < -rad) {
  // return false;
  // } else {
  // return true;
  // }
  // }
  //
  // /**
  // * Axis test.
  // */
  // private boolean axisTestY02(Vector v0, Vector v2, Vector boxhalfsize,
  // double a, double b, double fa,
  // double fb) {
  // double p0 = -a * v0.get(X) + b * v0.get(Z);
  // double p2 = -a * v2.get(X) + b * v2.get(Z);
  // double min, max;
  // if (p0 < p2) {
  // min = p0;
  // max = p2;
  // } else {
  // min = p2;
  // max = p0;
  // }
  // double rad = fa * boxhalfsize.get(X) + fb * boxhalfsize.get(Z);
  // if (min > rad || max < -rad) {
  // return false;
  // } else {
  // return true;
  // }
  // }
  //
  // /**
  // * Axis test.
  // */
  // private boolean axisTestY1(Vector v0, Vector v1, Vector boxhalfsize,
  // double a, double b, double fa, double fb) {
  // double p0 = -a * v0.get(X) + b * v0.get(Z);
  // double p1 = -a * v1.get(X) + b * v1.get(Z);
  // double min, max;
  // if (p0 < p1) {
  // min = p0;
  // max = p1;
  // } else {
  // min = p1;
  // max = p0;
  // }
  // double rad = fa * boxhalfsize.get(X) + fb * boxhalfsize.get(Z);
  // if (min > rad || max < -rad) {
  // return false;
  // } else {
  // return true;
  // }
  // }
  //
  // /**
  // * Axis test.
  // */
  // private boolean axisTestZ12(Vector v1, Vector v2, Vector boxhalfsize,
  // double a, double b, double fa,
  // double fb) {
  // double p1 = a * v1.get(X) - b * v1.get(Y);
  // double p2 = a * v2.get(X) - b * v2.get(Y);
  // double min, max;
  // if (p2 < p1) {
  // min = p2;
  // max = p1;
  // } else {
  // min = p1;
  // max = p2;
  // }
  // double rad = fa * boxhalfsize.get(X) + fb * boxhalfsize.get(Y);
  // if (min > rad || max < -rad) {
  // return false;
  // } else {
  // return true;
  // }
  // }
  //
  // /**
  // * Axis test.
  // */
  // private boolean axisTestZ0(Vector v0, Vector v1, Vector boxhalfsize,
  // double a, double b, double fa, double fb) {
  // double p0 = a * v0.get(X) - b * v0.get(Y);
  // double p1 = a * v1.get(X) - b * v1.get(Y);
  // double min, max;
  // if (p0 < p1) {
  // min = p0;
  // max = p1;
  // } else {
  // min = p1;
  // max = p0;
  // }
  // double rad = fa * boxhalfsize.get(X) + fb * boxhalfsize.get(Y);
  // if (min > rad || max < -rad) {
  // return false;
  // } else {
  // return true;
  // }
  // }
  //
  // private boolean deprecatedNodeTriangleIntersectionTest(int elementIndex,
  // OctreeNode<Integer> node) {
  // double EPSILON = 1e-2;
  // ITriangle triangle = mesh.getTriangle(elementIndex);
  // Vector boxhalfsize =
  // VectorMatrixFactory.newVector(node.getLength() / 2.0, node.getLength() /
  // 2.0, node.getLength() / 2.0);
  // Vector boxcenter = node.getLowerLeft().add(boxhalfsize);
  // Vector a = mesh.getVertex(triangle.getA()).getPosition();
  // Vector b = mesh.getVertex(triangle.getB()).getPosition();
  // Vector c = mesh.getVertex(triangle.getC()).getPosition();
  // Vector numericalOffset = VectorMatrixFactory.newVector(EPSILON,
  // EPSILON, EPSILON);
  //
  // Vector v0, v1, v2;
  // double fex, fey, fez;
  // Vector normal;
  // v0 = a.subtract(boxcenter);
  // v1 = b.subtract(boxcenter);
  // v2 = c.subtract(boxcenter);
  // Vector e0 = v1.subtract(v0);
  // Vector e1 = v2.subtract(v1);
  // Vector e2 = v0.subtract(v2);
  // fex = Math.abs(e0.get(X));
  // fey = Math.abs(e0.get(Y));
  // fez = Math.abs(e0.get(Z));
  // if (!axisTestX01(v0, v2, boxhalfsize.add(numericalOffset), e0.get(Z),
  // e0.get(Y), fez, fey)) {
  // return false;
  // }
  // if (!axisTestY02(v0, v2, boxhalfsize.add(numericalOffset), e0.get(Z),
  // e0.get(X), fez, fex)) {
  // return false;
  // }
  // if (!axisTestZ12(v1, v2, boxhalfsize.add(numericalOffset), e0.get(Y),
  // e0.get(X), fey, fex)) {
  // return false;
  // }
  // fex = Math.abs(e1.get(X));
  // fey = Math.abs(e1.get(Y));
  // fez = Math.abs(e1.get(Z));
  // if (!axisTestX01(v0, v2, boxhalfsize.add(numericalOffset), e1.get(Z),
  // e1.get(Y), fez, fey)) {
  // return false;
  // }
  // if (!axisTestY02(v0, v2, boxhalfsize.add(numericalOffset), e1.get(Z),
  // e1.get(X), fez, fex)) {
  // return false;
  // }
  // if (!axisTestZ0(v0, v1, boxhalfsize.add(numericalOffset), e1.get(Y),
  // e1.get(X), fey, fex)) {
  // return false;
  // }
  // fex = Math.abs(e2.get(X));
  // fey = Math.abs(e2.get(Y));
  // fez = Math.abs(e2.get(Z));
  // if (!axisTestX2(v0, v1, boxhalfsize.add(numericalOffset), e2.get(Z),
  // e2.get(Y), fez, fey)) {
  // return false;
  // }
  // if (!axisTestY1(v0, v1, boxhalfsize.add(numericalOffset), e2.get(Z),
  // e2.get(X), fez, fex)) {
  // return false;
  // }
  // if (!axisTestZ12(v1, v2, boxhalfsize.add(numericalOffset), e2.get(Y),
  // e2.get(X), fey, fex)) {
  // return false;
  // }
  // double min = Math.min(v0.get(X), Math.min(v1.get(X), v2.get(X)));
  // double max = Math.min(v0.get(X), Math.min(v1.get(X), v2.get(X)));
  // if (min > (boxhalfsize.get(X) + EPSILON) || max < -(boxhalfsize.get(X) +
  // EPSILON)) {
  // return false;
  // }
  // min = Math.min(v0.get(Y), Math.min(v1.get(Y), v2.get(Y)));
  // max = Math.min(v0.get(Y), Math.min(v1.get(Y), v2.get(Y)));
  // if (min > (boxhalfsize.get(Y) + EPSILON) || max < -(boxhalfsize.get(Y) +
  // EPSILON)) {
  // return false;
  // }
  // min = Math.min(v0.get(Z), Math.min(v1.get(Z), v2.get(Z)));
  // max = Math.min(v0.get(Z), Math.min(v1.get(Z), v2.get(Z)));
  // if (min > (boxhalfsize.get(Z) + EPSILON) || max < -(boxhalfsize.get(Z) +
  // EPSILON)) {
  // return false;
  // }
  // normal = e0.cross(e1);
  // if (!planeBoxOverlap(normal, v0, boxhalfsize.add(numericalOffset))) {
  // return false;
  // }
  // return true;
  // }
}

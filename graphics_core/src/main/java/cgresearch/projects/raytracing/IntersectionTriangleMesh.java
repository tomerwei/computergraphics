package cgresearch.projects.raytracing;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.Ray3D;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * Intersection computation for planes.
 * 
 * @author Philipp Jenke
 */
public class IntersectionTriangleMesh implements Intersect<ITriangleMesh> {

  @Override
  public IntersectionResult intersect(Ray3D ray, ICgNodeContent object) {
    if (!(object instanceof ITriangleMesh)) {
      return null;
    }
    ITriangleMesh mesh = (ITriangleMesh) object;

    IntersectionResult result = null;
    double currentSqrDist = Double.POSITIVE_INFINITY;

    // TODO: Use hierarchical data structure.

    for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
      IntersectionResult triangleIntersection = intersect(mesh, i, ray);
      if (triangleIntersection != null) {
        double triangleIntersectionSqrDist = triangleIntersection.point.subtract(ray.getPoint()).getSqrNorm();
        if (result == null) {
          result = triangleIntersection;
          currentSqrDist = triangleIntersectionSqrDist;
        } else if (triangleIntersectionSqrDist < currentSqrDist) {
          result = triangleIntersection;
          currentSqrDist = triangleIntersectionSqrDist;
        }
      }
    }
    return result;
  }

  /**
   * Compute the intersection between the ray an the triangle.
   * 
   * @return Intersection result. Return null if there is no intersection.
   */
  private IntersectionResult intersect(ITriangleMesh mesh, int triangleIndex, Ray3D ray) {

    ITriangle triangle = mesh.getTriangle(triangleIndex);
    IVector3 vA = mesh.getVertex(triangle.getA()).getPosition();

    // Step 1: compute intersection between ray an plane defined by triangle
    double nr = triangle.getNormal().multiply(ray.getDirection());
    if (Math.abs(nr) < 1e-5) {
      return null;
    }
    double lambda = (triangle.getNormal().multiply(vA) - triangle.getNormal().multiply(ray.getPoint())) / nr;
    if (lambda < EPISLON) {
      return null;
    }

    IVector3 vB = mesh.getVertex(triangle.getB()).getPosition();
    IVector3 vC = mesh.getVertex(triangle.getC()).getPosition();
    IVector3 intersectionPoint = ray.getPoint().add(ray.getDirection().multiply(lambda));
    IVector3 ab = vB.subtract(vA);
    IVector3 ac = vC.subtract(vA);
    double areaTriangle = ab.cross(ac).getNorm() / 2.0;
    IVector3 pa = intersectionPoint.subtract(vA);
    IVector3 pb = intersectionPoint.subtract(vB);
    IVector3 pc = intersectionPoint.subtract(vC);
    double areaA = pb.cross(pc).getNorm() / 2.0;
    double areaB = pa.cross(pc).getNorm() / 2.0;
    double areaC = pa.cross(pb).getNorm() / 2.0;
    double alpha = areaA / areaTriangle;
    double beta = areaB / areaTriangle;
    double gamma = areaC / areaTriangle;

    if (Math.abs(alpha + beta + gamma - 1) < EPISLON && alpha >= 0 && beta >= 0 && gamma >= 0) {
      IntersectionResult result = new IntersectionResult();
      result.point = intersectionPoint;
      result.normal = triangle.getNormal();
      result.object = mesh;
      return result;
    } else {
      return null;
    }
  }

  @Override
  public Class<?> getHandledType() {
    return ITriangleMesh.class;
  }

  @Override
  public boolean canHandle(ICgNodeContent object) {
    return object instanceof ITriangleMesh;
  }
}

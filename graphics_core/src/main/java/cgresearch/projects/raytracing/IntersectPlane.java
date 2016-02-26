package cgresearch.projects.raytracing;

import cgresearch.core.math.Ray3D;
import cgresearch.graphics.datastructures.primitives.Plane;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * Intersection computation for planes.
 * 
 * @author Philipp Jenke
 */
public class IntersectPlane implements Intersect<Plane> {

  @Override
  public IntersectionResult intersect(Ray3D ray, ICgNodeContent object) {

    if (!(object instanceof Plane)) {
      return null;
    }

    Plane plane = (Plane) object;

    double nr = plane.getNormal().multiply(ray.getDirection());
    if (Math.abs(nr) < 1e-5) {
      return null;
    }
    double lambda =
        (plane.getNormal().multiply(plane.getPoint()) - plane.getNormal()
            .multiply(ray.getPoint())) / nr;

    if (lambda < EPISLON) {
      return null;
    }

    IntersectionResult result = new IntersectionResult();
    result.point = ray.getPoint().add(ray.getDirection().multiply(lambda));
    result.normal = plane.getNormal();
    result.object = plane;
    return result;
  }

  @Override
  public Class<?> getHandledType() {
    return Plane.class;
  }

  @Override
  public boolean canHandle(ICgNodeContent object) {
    return object instanceof Plane;
  }

}

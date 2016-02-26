package cgresearch.projects.raytracing;

import cgresearch.core.math.Ray3D;
import cgresearch.graphics.datastructures.primitives.Sphere;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * Intersection computation for planes.
 * 
 * @author Philipp Jenke
 */
public class IntersectSphere implements Intersect<Sphere> {

  @Override
  public IntersectionResult intersect(Ray3D ray, ICgNodeContent object) {

    if (!(object instanceof Sphere)) {
      return null;
    }

    Sphere sphere = (Sphere) object;

    double p =
        (ray.getPoint().multiply(ray.getDirection()) * 2 - sphere.getCenter()
            .multiply(ray.getDirection()) * 2)
            / (ray.getDirection().multiply(ray.getDirection()));
    double q =
        (ray.getPoint().multiply(ray.getPoint())
            - ray.getPoint().multiply(sphere.getCenter()) * 2
            + sphere.getCenter().multiply(sphere.getCenter()) - sphere
            .getRadius() * sphere.getRadius())
            / (ray.getDirection().multiply(ray.getDirection()));

    double underSqrt = p * p / 4.0 - q;
    if (underSqrt < 0) {
      return null;
    }
    double lambda1 = -p / 2.0 - Math.sqrt(underSqrt);
    double lambda2 = -p / 2.0 + Math.sqrt(underSqrt);

    // Select the smaller of the two lambda which is larger than 0.
    double lambdaSmaller = Math.min(lambda1, lambda2);
    double lambdaLarger = Math.max(lambda1, lambda2);
    double lambda = lambdaSmaller;
    if (lambda1 < EPISLON) {
      lambda = lambdaLarger;
    }

    if (lambda > EPISLON) {
      IntersectionResult result = new IntersectionResult();
      result.point = ray.getPoint().add(ray.getDirection().multiply(lambda));
      result.normal = result.point.subtract(sphere.getCenter()).getNormalized();
      result.object = sphere;
      return result;
    } else {
      // Self intersection - ignored
      return null;
    }
  }

  @Override
  public Class<?> getHandledType() {
    return Sphere.class;
  }

  @Override
  public boolean canHandle(ICgNodeContent object) {
    return object instanceof Sphere;
  }

}

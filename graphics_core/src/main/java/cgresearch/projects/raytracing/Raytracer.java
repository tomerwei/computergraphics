package cgresearch.projects.raytracing;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import cgresearch.core.StopWatch;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;
import cgresearch.core.math.Ray3D;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.datastructures.primitives.Plane;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.graphics.scenegraph.ICgNodeContent;
import cgresearch.graphics.scenegraph.LightSource;

/**
 * Creates a raytraced image of the current scene;
 */
public class Raytracer {

  /**************************** BEGIN: SETTINGS ******************************/

  /**
   * Set this flag to calculate the phong lighting model. The object color is
   * used directly otherwise.
   */
  private boolean usePhongLighting = true;

  /**
   * Set this flag if shadow rays should be used when evaluating the lighting.
   */
  private boolean useShadowRays = true;

  /**
   * Recursion depth of rays (reflection, refraction).
   */
  private int maxRecursionDepth = 5;

  /**
   * Background color
   */
  private Vector backgroundColor = VectorMatrixFactory.newVector(0, 0, 0);

  /**
   * Use checkerboard texture for planes.
   */
  private boolean usePlaneCheckerBoard = true;

  /**************************** BEGIN: SETTINGS ******************************/

  public static double REFRACTION_FACTOR_AIR = 1;
  public static double REFRACTION_FACTOR_GLASS = 1.4;

  /**
   * Root node of the scene.
   */
  private final CgRootNode rootNode;

  /**
   * Map between a class type and a corresponding intersect computation method.
   */
  private List<Intersect<?>> intersectors = new ArrayList<Intersect<?>>();

  /**
   * Constructor.
   */
  public Raytracer(CgRootNode rootNode) {
    this.rootNode = rootNode;
    registerIntersectionHandler(new IntersectPlane());
    registerIntersectionHandler(new IntersectSphere());
    registerIntersectionHandler(new IntersectionTriangleMesh());
  }

  /**
   * Register intersection handler for an additional object type.
   */
  private void registerIntersectionHandler(Intersect<?> intersectionHandler) {
    intersectors.add(intersectionHandler);
  }

  /**
   * Setup raytracing parameters.
   */
  public void setup(boolean usePhongLighting, boolean useShadowRays, int maxRecursionDepth,
      boolean usePlaneCheckerBoard) {
    this.usePhongLighting = usePhongLighting;
    this.useShadowRays = useShadowRays;
    this.maxRecursionDepth = maxRecursionDepth;
    this.usePlaneCheckerBoard = usePlaneCheckerBoard;
  }

  /**
   * Creates a raytraced image for the current view with the provided
   * resolution. The opening angle in x-direction is grabbed from the camera,
   * the opening angle in y-direction is computed accordingly.
   */
  public Image render(int resolutionX, int resolutionY) {

    StopWatch stopSwatch = new StopWatch();
    stopSwatch.start();

    BufferedImage image = new BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_RGB);

    Vector viewDirection = Camera.getInstance().getRef().subtract(Camera.getInstance().getEye()).getNormalized();
    Vector xDirection = viewDirection.cross(Camera.getInstance().getUp()).getNormalized();
    Vector yDirection = viewDirection.cross(xDirection).getNormalized();
    double openingAngleYScale = Math.sin(Camera.getInstance().getOpeningAngle() * Math.PI / 180.0);
    double openingAngleXScale = openingAngleYScale * (double) resolutionX / (double) resolutionY;

    for (int i = 0; i < resolutionX; i++) {
      double alpha = (double) i / (double) (resolutionX + 1) - 0.5;
      for (int j = 0; j < resolutionY; j++) {
        double beta = (double) j / (double) (resolutionY + 1) - 0.5;
        Vector rayDirection = viewDirection.add(xDirection.multiply(alpha * openingAngleXScale))
            .add(yDirection.multiply(beta * openingAngleYScale)).getNormalized();
        Ray3D ray = new Ray3D(Camera.getInstance().getEye(), rayDirection);

        Vector color = trace(ray, 0, false);

        // Adjust color boundaries
        for (int index = 0; index < 3; index++) {
          color.set(index, Math.max(0, Math.min(1, color.get(index))));
        }

        image.setRGB(i, j,
            new Color((int) (255 * color.get(0)), (int) (255 * color.get(1)), (int) (255 * color.get(2))).getRGB());
      }
    }

    stopSwatch.stop();
    Logger.getInstance().message("Time required for image with " + resolutionX + "x" + resolutionY + " pixels: "
        + stopSwatch.getSeconds() + " seconds.");

    return image;
  }

  /**
   * Compute a color from tracing the ray into the scene.
   * 
   * @return Color in RGB. All values are in [0,1];
   */
  private Vector trace(Ray3D ray, int recursion, boolean isInsideObject) {

    // Check for recursion depth
    if (recursion > maxRecursionDepth) {
      return VectorMatrixFactory.newVector(0, 0, 0);
    }

    // Find the first intersection with an object.
    IntersectionResult result = computeFirstIntersection(ray, rootNode);
    if (result != null) {
      // Compute color
      double factorReflection = Math.max(0, Math.min(1, result.object.getMaterial().getReflection()));
      double factorRefraction = Math.max(0, Math.min(1, result.object.getMaterial().getRefraction()));
      double factorLighting = Math.max(0, Math.min(1, 1 - factorReflection - factorRefraction));
      Vector color = VectorMatrixFactory.newVector(0, 0, 0);

      // Lighting
      if (factorLighting > 0) {
        color.addSelf(computeLighting(result).multiply(factorLighting));
      }

      // Reflection
      if (factorReflection > 0) {
        color.addSelf(computeReflection(result, ray, recursion, factorReflection));
      }

      // Refraction
      if (factorRefraction > 0) {
        color.addSelf(computeRefraction(result, ray, recursion, factorRefraction, isInsideObject));
      }

      // Assembled color
      return color;
    }

    // Background color
    return backgroundColor;
  }

  /**
   * Compute the refracted ray (recursive call).
   */
  private Vector computeRefraction(IntersectionResult result, Ray3D ray, int recursion, double factorRefraction,
      boolean isInsideObject) {

    double refractionIndex1 = REFRACTION_FACTOR_AIR;
    double refractionIndex2 = REFRACTION_FACTOR_GLASS;
    double r = refractionIndex1 / refractionIndex2;
    if (!isInsideObject) {
      r = 1.0 / r;
    }

    Vector inverseNormal = (isInsideObject) ? result.normal : result.normal.multiply(-1).getNormalized();
    double incomingAngle = Math.acos(inverseNormal.multiply(ray.getDirection()));
    double exitAngle = Math.asin(Math.sin(incomingAngle) * r);
    Vector rotationAxis = inverseNormal.cross(ray.getDirection()).getNormalized();
    Matrix rotationMatrix = VectorMatrixFactory.getRotationMatrix(rotationAxis, -exitAngle);
    Vector refractionDirection = rotationMatrix.multiply(inverseNormal).getNormalized();

    // System.out.println(ray.getDirection());
    // System.out.println(refractionDirection);
    // System.out.println(Math.acos(refractionDirection.multiply(inverseNormal)));

    if (refractionDirection.multiply(result.normal) > 0) {
      isInsideObject = true;
    } else {
      isInsideObject = false;
    }

    return trace(new Ray3D(result.point, refractionDirection), recursion + 1, !isInsideObject)
        .multiply(factorRefraction);

    // double w = -(ray.getDirection().multiply(result.normal)) * r;
    // double k = 1 + (w - r) * (w + r);
    // Vector colorRefraction = VectorMatrixFactory.newVector(0, 0, 0);
    // if (k > 0) {
    // Vector refractedDirection =
    // ray.getDirection().multiply(r)
    // .add(result.normal.multiply(w - Math.sqrt(k)));
    // colorRefraction =
    // trace(new Ray3D(result.point, refractedDirection), recursion + 1,
    // !isInsideObject).multiply(factorRefraction);
    // }
  }

  /**
   * Compute the reflection (recursive call).
   */
  private Vector computeReflection(IntersectionResult result, Ray3D ray, int recursion, double factorReflection) {
    Vector reflectedDirection = ray.getDirection()
        .subtract(result.normal.multiply(ray.getDirection().multiply(result.normal) * 2)).getNormalized();
    return trace(new Ray3D(result.point, reflectedDirection), recursion + 1, false).multiply(factorReflection);
  }

  /**
   * Compute the lighting value at the intersection point.
   */
  private Vector computeLighting(IntersectionResult result) {
    if (!usePhongLighting) {
      // Color only
      return result.object.getMaterial().getReflectionDiffuse();
    }

    Vector color = VectorMatrixFactory.newVector(0, 0, 0);

    // Iterate over all light sources
    for (int lightIndex = 0; lightIndex < rootNode.getNumberOfLights(); lightIndex++) {

      LightSource light = rootNode.getLight(lightIndex);

      // Phong model
      Vector lightPosition = light.getPosition();
      Vector lightDirection = lightPosition.subtract(result.point).getNormalized();

      boolean isInShadow = computeIsInShadow(result.point, lightPosition, lightDirection);

      if (!isInShadow) {
        Vector lighting = VectorMatrixFactory.newVector(0, 0, 0);

        // Diffuse
        double diffuseFactor = lightDirection.multiply(result.normal);
        if (diffuseFactor > 0) {
          Vector diffuseMaterialColor = result.object.getMaterial().getReflectionDiffuse();
          // Special case: checkerboard plane
          if (result.object instanceof Plane && usePlaneCheckerBoard) {
            diffuseMaterialColor = ((Plane) (result.object)).getReflectionDiffuseCheckerBoard(0.5, result.point);
          }
          lighting.addSelf(diffuseMaterialColor.multiply(diffuseFactor));
        }

        // Specular
        Vector viewerDirection = Camera.getInstance().getEye().subtract(result.point).getNormalized();
        Vector lightReflectionDirection =
            lightDirection.subtract(result.normal.multiply(result.normal.multiply(lightDirection) * 2)).getNormalized();
        double specularFactor = Math.pow(Math.max(0, -viewerDirection.multiply(lightReflectionDirection)),
            result.object.getMaterial().getSpecularShininess());
        if (specularFactor > 0) {
          lighting.addSelf(result.object.getMaterial().getReflectionSpecular().multiply(specularFactor));
        }
        color.addSelf(lighting);
      }
    }

    return color;
  }

  /**
   * Check if the point is in the shadow of another object.
   */
  public boolean computeIsInShadow(Vector point, Vector lightPosition, Vector lightDirection) {
    if (useShadowRays) {
      Ray3D shadowRay = new Ray3D(point, lightDirection);
      IntersectionResult shadowRayIntersection = computeFirstIntersection(shadowRay, rootNode);
      if (shadowRayIntersection != null) {
        double sqrDistance2Light = point.subtract(lightPosition).getSqrNorm();
        double sqrDistance2ShadowObject = point.subtract(shadowRayIntersection.point).getSqrNorm();
        if (sqrDistance2ShadowObject < sqrDistance2Light) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Computes the first intersection with an object in the scene (if there are
   * any). Returns null otherwise.
   */
  private IntersectionResult computeFirstIntersection(Ray3D ray, CgNode rootNode) {

    List<ICgNodeContent> objects = getAllNodes(rootNode);
    IntersectionResult closestIntersection = null;
    for (ICgNodeContent object : objects) {
      IntersectionResult result = null;

      for (Intersect<?> intersect : intersectors) {
        if (intersect.canHandle(object)) {
          result = intersect.intersect(ray, object);
          break;
        }
      }

      if (closestIntersection == null) {
        closestIntersection = result;
      } else {
        if (result != null) {
          closestIntersection = selectCloserIntersection(closestIntersection, result, ray.getPoint());
        }
      }
    }

    return closestIntersection;
  }

  /**
   * Select the intersection which is closer to given point.
   */
  private IntersectionResult selectCloserIntersection(IntersectionResult intersection1,
      IntersectionResult intersection2, Vector point) {
    double distance1 = intersection1.point.subtract(point).getSqrNorm();
    double distance2 = intersection2.point.subtract(point).getSqrNorm();
    if (distance1 < distance2) {
      return intersection1;
    } else {
      return intersection2;
    }
  }

  /**
   * Flatten the scene graph.
   */
  private List<ICgNodeContent> getAllNodes(CgNode node) {
    List<ICgNodeContent> nodes = new ArrayList<ICgNodeContent>();
    if (node.getContent() != null) {
      nodes.add(node.getContent());
    }
    for (int i = 0; i < node.getNumChildren(); i++) {
      List<ICgNodeContent> childNodes = getAllNodes(node.getChildNode(i));
      for (ICgNodeContent childContent : childNodes) {
        if (childContent != null) {
          nodes.add(childContent);
        }
      }
    }
    return nodes;
  }
}
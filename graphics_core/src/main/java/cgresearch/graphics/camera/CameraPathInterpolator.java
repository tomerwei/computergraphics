package cgresearch.graphics.camera;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.curves.HermiteCurve;

/**
 * The CameraPathController allows to define a interpolated patn thru the scene.
 * 
 * @author Philipp Jenke
 *
 */
public class CameraPathInterpolator {

  /**
   * Camera path
   */
  private CameraPath cameraPath = new CameraPath();

  /**
   * Constructor.
   */
  public CameraPathInterpolator() {
  }

  /**
   * Add additional key point.
   */
  public void addKeyPoint(Vector pos, Vector up, Vector ref) {
    cameraPath.addKeyPoint(new CameraPathKeypoint(pos, up, ref));
  }

  /**
   * Return the interpolated position for a given parameter value. The parameter
   * t must be within [0,1].
   */
  public Vector getInterpolatedPos(float t) {
    if (cameraPath.getNumberOfKeyPoints() < 2) {
      throw new IllegalArgumentException();
    }
    int i = getKeyPointLowerIndex(t);
    float tLocal = getLocalParameter(t, i);
    HermiteCurve hermite = new HermiteCurve(cameraPath.getKeyPoint(i).getPos(), getPosGradient(i),
        getPosGradient(i + 1), cameraPath.getKeyPoint(i + 1).getPos());
    return hermite.eval(tLocal);
  }

  /**
   * Estimate the gradient vector at the key point for the position.
   */
  private Vector getPosGradient(int i) {
    Vector gradient = null;
    if (i > 0 && (i + 1 < cameraPath.getNumberOfKeyPoints())) {
      gradient = cameraPath.getKeyPoint(i + 1).getPos().subtract(cameraPath.getKeyPoint(i - 1).getPos());
    } else if (i == 0) {
      gradient = cameraPath.getKeyPoint(1).getPos().subtract(cameraPath.getKeyPoint(0).getPos());
    } else {
      gradient = cameraPath.getKeyPoint(cameraPath.getNumberOfKeyPoints() - 1).getPos()
          .subtract(cameraPath.getKeyPoint(cameraPath.getNumberOfKeyPoints() - 2).getPos());
    }
    return gradient;
  }

  /**
   * Estimate the gradient vector at the key point for the position.
   */
  private Vector getRefGradient(int i) {
    Vector gradient = null;
    if (i > 0 && (i + 1 < cameraPath.getNumberOfKeyPoints())) {
      gradient = cameraPath.getKeyPoint(i + 1).getRef().subtract(cameraPath.getKeyPoint(i - 1).getRef());
    } else if (i == 0) {
      gradient = cameraPath.getKeyPoint(1).getRef().subtract(cameraPath.getKeyPoint(0).getRef());
    } else {
      gradient = cameraPath.getKeyPoint(cameraPath.getNumberOfKeyPoints() - 1).getRef()
          .subtract(cameraPath.getKeyPoint(cameraPath.getNumberOfKeyPoints() - 2).getRef());
    }
    double length = gradient.getNorm();
    if (length > 1e-5) {
      gradient.normalize();
    }
    return gradient;
  }

  /**
   * Compute the local parameter in the current interval as a value between 0
   * and 0.
   */
  private float getLocalParameter(float t, int i) {
    float delta = 1.0f / (cameraPath.getNumberOfKeyPoints() - 1);
    return (t - i * delta) / delta;
  }

  /**
   * Return the lower index of the two key points required for the given
   * parameter value of t.
   */
  private int getKeyPointLowerIndex(float t) {
    int maxKeyPointIndex = cameraPath.getNumberOfKeyPoints() - 1;
    int index = (int) (t * maxKeyPointIndex);
    if (index >= cameraPath.getNumberOfKeyPoints() - 2) {
      index = cameraPath.getNumberOfKeyPoints() - 2;
    }
    return index;
  }

  /**
   * Return the interpolated up vector for a given parameter value. The
   * parameter t must be within [0,1].
   */
  public Vector getInterpolatedUp(float t) {
    // TODO
    return VectorFactory.createVector3(0, 1, 0);
  }

  /**
   * Return the interpolated ref point for a given parameter value. The
   * parameter t must be within [0,1].
   */
  public Vector getInterpolatedRef(float t) {
    if (cameraPath.getNumberOfKeyPoints() < 2) {
      throw new IllegalArgumentException();
    }
    int i = getKeyPointLowerIndex(t);
    float tLocal = getLocalParameter(t, i);
    HermiteCurve hermite = new HermiteCurve(cameraPath.getKeyPoint(i).getRef(), getRefGradient(i),
        getRefGradient(i + 1), cameraPath.getKeyPoint(i + 1).getRef());
    return hermite.eval(tLocal);
  }

  /**
   * Clear list of key points.
   */
  public void clearKeyPoints() {
    cameraPath.clear();
  }

  /**
   * Getter. Required for I/O.
   */
  public CameraPath getCameraPath() {
    return cameraPath;
  }

  /**
   * Setter. Required for I/O.
   */
  public void setCameraPath(CameraPath cameraPath) {
    this.cameraPath = cameraPath;
  }
}

/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.graphics.camera;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Represents a camera.
 * 
 * @author Philipp Jenke
 * 
 */
public class Camera extends Observable {

  /**
   * Constants
   */
  public static final double ZOOM_FACTOR = 0.05;

  /**
   * Opening Angle (x-direction) of the camera.
   */

  private double openingAngle = 45.0;

  /**
   * Position of the eye.
   */
  private IVector3 eye;

  /**
   * Reference point of the camera (look at)
   */
  private IVector3 ref;

  /**
   * Up-vector of the camera.
   */
  private IVector3 up;

  /**
   * Singleton instance.
   */
  private static Camera instance = null;

  /**
   * Possible camera controllers.
   */
  public enum ControllerType {
    INSPECTION,
    MOVEABLE_INSPECTION,
    CAMERA_PATH,
    FIRST_PERSON
  };

  /**
   * Flag to show the camera path - I am not happy with the state at this
   * location.
   */
  private boolean showCameraPath = false;

  /**
   * The controller is implemented using the strategy pattern.
   */
  private Map<ControllerType, CameraController> controller =
      new HashMap<ControllerType, CameraController>();

  /**
   * Currently selected controller.
   */
  private ControllerType currentControllerType =
      ControllerType.INSPECTION;

  /**
   * Interpolator for a camera path.
   */
  private CameraPathInterpolator cameraPath =
      new CameraPathInterpolator();

  /**
   * Set this flag to true if the camera should center the complete scene.
   */
  private boolean isCenterViewRequired = false;

  /**
   * Constructor.
   */
  private Camera() {
    eye = VectorMatrixFactory.newIVector3(0, 0, 5);
    ref = VectorMatrixFactory.newIVector3(0, 0, 0);
    up = VectorMatrixFactory.newIVector3(0, 1, 0);

    setChanged();
    notifyObservers();
  }

  public void registerController(ControllerType type,
      CameraController controlle) {
    controller.put(type, controlle);
  }

  /**
   * Get singleton instance.
   */
  public static Camera getInstance() {
    if (instance == null) {
      instance = new Camera();
    }
    return instance;
  }

  /**
   * Getter for the current controller.
   */
  public CameraController getCurrentController() {
    return controller.get(currentControllerType);
  }

  /**
   * Get the controller of the specified type.
   */
  public CameraController getController(ControllerType type) {
    return controller.get(type);
  }

  /**
   * Getter.
   */
  public IVector3 getEye() {
    return eye;
  }

  /**
   * Getter.
   */
  public IVector3 getRef() {
    return ref;
  }

  /**
   * Getter.
   */
  public IVector3 getUp() {
    return up;
  }

  /**
   * Setter.
   */
  public void setEye(IVector3 e) {
    eye = VectorMatrixFactory.newIVector3(e);

    setChanged();
    notifyObservers();
  }

  /**
   * Setter.
   */
  public void setRef(IVector3 e) {
    ref = VectorMatrixFactory.newIVector3(e);

    setChanged();
    notifyObservers();
  }

  /**
   * Setter.
   */
  public void setUp(IVector3 e) {
    up = VectorMatrixFactory.newIVector3(e);

    setChanged();
    notifyObservers();
  }

  /**
   * Move the camera (eye and ref) the given distance vertically.
   */
  public void translateVertically(double distance) {
    // up/down
    IVector3 movement =
        up.getNormalized().multiply(distance);

    // apply translation
    setRef(ref.add(movement));
    setEye(eye.add(movement));
  }

  /**
   * Move the camera (eye and ref) the given distance horizontally.
   * 
   * @param distance
   */
  public void translateHorizontally(double distance) {
    // left/right
    IVector3 direction = eye.subtract(ref);
    IVector3 movement =
        direction.cross(up).getNormalized()
            .multiply(distance);

    // apply translation
    setRef(ref.add(movement));
    setEye(eye.add(movement));
  }

  /**
   * Rotate the camera around the up vector and the eye position.
   */
  public void rotateEyeVertically(final double angle) {
    // Normalize direction
    IVector3 oldDirection = eye.subtract(ref);
    double length = oldDirection.getNorm();
    oldDirection = oldDirection.multiply(1.0 / length);

    // Apply rotation
    IMatrix3 rotationMatrix =
        VectorMatrixFactory.getRotationMatrix(up, angle);
    IVector3 newDirection =
        rotationMatrix.multiply(oldDirection)
            .getNormalized();
    IVector3 newEye =
        ref.add(newDirection.multiply(length));

    // Assign new coordinate frame
    setEye(newEye);
  }

  /**
   * Rotate the camera around a horizontal axis.
   */
  public void rotateEyeHorizontally(final double angle) {
    // Normalize direction
    IVector3 oldDirection = eye.subtract(ref);
    double length = oldDirection.getNorm();
    oldDirection = oldDirection.getNormalized();

    // Compute rotation axis
    IVector3 axis = oldDirection.cross(up).getNormalized();

    // Apply rotation
    IMatrix3 rotationMatrix =
        VectorMatrixFactory.getRotationMatrix(axis, angle);
    IVector3 newDirection =
        rotationMatrix.multiply(oldDirection)
            .getNormalized();
    IVector3 newEye =
        ref.add(newDirection.multiply(length));

    // Assign new coordinate frame
    setEye(newEye);
    setUp(VectorMatrixFactory.newIVector3(
        axis.cross(newDirection)).getNormalized());
  }

  /**
   * Rotate the camera reference point around a vertical axis.
   */
  public void rotateRefVertically(double angle) {
    // Normalize direction
    IVector3 oldDirection = ref.subtract(eye);
    double length = oldDirection.getNorm();
    oldDirection = oldDirection.multiply(1.0 / length);

    // Apply rotation
    IMatrix3 rotationMatrix =
        VectorMatrixFactory.getRotationMatrix(up, angle);
    IVector3 newDirection =
        rotationMatrix.multiply(oldDirection)
            .getNormalized();
    IVector3 newRef =
        eye.add(newDirection.multiply(length));

    // Assign new coordinate frame
    setRef(newRef);
  }

  /**
   * Rotate the camera reference point around a horizontal axis.
   */
  public void rotateRefHorizontally(double angle) {
    // Normalize direction
    IVector3 oldDirection = ref.subtract(eye);
    double length = oldDirection.getNorm();
    oldDirection = oldDirection.getNormalized();

    // Compute rotation axis
    IVector3 axis = oldDirection.cross(up).getNormalized();

    // Apply rotation
    IMatrix3 rotationMatrix =
        VectorMatrixFactory.getRotationMatrix(axis, angle);
    IVector3 newDirection =
        rotationMatrix.multiply(oldDirection)
            .getNormalized();
    IVector3 newRef =
        eye.add(newDirection.multiply(length));

    // Assign new coordinate frame
    setRef(newRef);
    setUp(VectorMatrixFactory.newIVector3(
        axis.cross(newDirection)).getNormalized());
  }

  /**
   * Zooming
   * 
   * @param d
   */
  public void zoom(double d) {
    if (d > 0) {
      setEye(eye.add(ref.subtract(eye)
          .multiply(ZOOM_FACTOR)));
    } else if (d < 0) {
      setEye(eye.subtract(ref.subtract(eye).multiply(
          ZOOM_FACTOR)));
    }
  }

  /**
   * Set the controller.
   */
  public void setController(ControllerType type) {
    currentControllerType = type;
  }

  /**
   * Clear the list of camera path key points.
   */
  public void clearKeyPoints() {
    cameraPath.clearKeyPoints();
  }

  /**
   * Append a new key point at the end of the key points list.
   */
  public void appendKeyPoint(IVector3 pos, IVector3 up,
      IVector3 ref) {
    cameraPath.addKeyPoint(pos, up, ref);
  }

  /**
   * Get the interpolated path position.
   */
  public IVector3 getCameraPathInterpolation(float t) {
    return cameraPath.getInterpolatedPos(t);
  }

  /**
   * Getter.
   */
  public boolean showCameraPath() {
    return showCameraPath;
  }

  /**
   * Setter.
   */
  public void setShowCameraPath(boolean selected) {
    showCameraPath = selected;

  }

  /**
   * Move forward.
   */
  public void moveForwards(double length) {
    IVector3 eye = getEye();
    IVector3 ref = getRef();
    IVector3 moveDirection = ref.subtract(eye);
    moveDirection.normalize();
    moveDirection = moveDirection.multiply(length);
    setEye(eye.add(moveDirection));
    setRef(ref.add(moveDirection));
  }

  /**
   * Getter.
   */
  public ControllerType getCurrentControllerType() {
    return currentControllerType;
  }

  /**
   * Save the current camera path to file.
   */
  public void saveCameraPathToFile(String filename) {
    ObjectOutputStream outputStream = null;
    try {
      outputStream =
          new ObjectOutputStream(new FileOutputStream(
              filename));
      outputStream.writeObject(cameraPath.getCameraPath());
      Logger.getInstance().message(
          "Successfully saved camera path to file "
              + filename);
    } catch (FileNotFoundException e) {
      Logger.getInstance().exception(
          "Failed to writre camera path", e);
    } catch (IOException e) {
      Logger.getInstance().exception(
          "Failed to writre camera path", e);
    } finally {
      try {
        outputStream.close();
      } catch (IOException e) {
        Logger.getInstance().exception(
            "Failed to writre camera path", e);
      }
    }
  }

  /**
   * Load the current camera path from a file.
   */
  public void loadCameraPathFromFile(String filename) {
    ObjectInputStream inputStream = null;
    try {
      inputStream =
          new ObjectInputStream(new FileInputStream(
              filename));
      Object o = inputStream.readObject();
      if (o instanceof CameraPath) {
        cameraPath.setCameraPath((CameraPath) o);
      } else {
        Logger.getInstance().error(
            "Invalid object file for camera path import");
      }
      Logger.getInstance().message(
          "Successfully loaded camera path from file "
              + filename);
    } catch (FileNotFoundException e) {
      Logger.getInstance().exception(
          "Failed to writre camera path", e);
    } catch (IOException e) {
      Logger.getInstance().exception(
          "Failed to writre camera path", e);
    } catch (ClassNotFoundException e) {
      Logger.getInstance().exception(
          "Failed to writre camera path", e);
    } finally {
      try {
        inputStream.close();
      } catch (IOException e) {
        Logger.getInstance().exception(
            "Failed to writre camera path", e);
      }
    }
  }

  /**
   * Getter. Resets the flag if true.
   */
  public boolean getCenterViewRequired() {
    if (isCenterViewRequired) {
      isCenterViewRequired = false;
      return true;
    } else {
      return false;
    }
  }

  public void setCenterViewRequired() {
    isCenterViewRequired = true;
  }

  /**
   * Getter.
   */
  public CameraPathInterpolator getCameraPathInterpolator() {
    return cameraPath;
  }

  /**
   * Getter.
   */
  public double getOpeningAngle() {
    return openingAngle;
  }
}

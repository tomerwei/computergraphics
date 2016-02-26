package homeautomation.sensors;

/**
 * This interface method is called if a motion detector detects something.
 * 
 * @author Philipp Jenke
 *
 */
public interface IMotionDetectorCallback {

  /**
   * Callback method.
   */
  public void motionDetected();
}

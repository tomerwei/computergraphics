package homeautomation.actors;

/**
 * Parent interface for all Servos.
 * 
 * @author Philipp Jenke
 *
 */
public interface IServo extends IActor {

  /**
   * Set the current position of the servo.
   * 
   * @param degree
   *          Current angle. Values between -180 and 180 degrees are allowed.
   *          Note, that some servos only support -45 ... 45 degrees.
   */
  public void setDegree(int degree);

  /**
   * Shutdown servo.
   */
  public void shutdown();

}

package homeautomation.actors;

/**
 * Parent interface for all (DC) motors.
 * 
 * @author Philipp Jenke
 *
 */
public interface IMotor extends IActor {

  /**
   * Stop the motor.
   */
  public void stop();

  /**
   * Run the motor.
   * 
   * @param percentage
   *          Percentage of the max velocity. Negative value for inverse
   *          direction.
   */
  public void setVelocity(double percentage);

  /**
   * Shutdown device, disable brick.
   */
  public void shutdown();

}

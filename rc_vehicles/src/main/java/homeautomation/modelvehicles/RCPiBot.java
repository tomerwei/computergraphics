package homeautomation.modelvehicles;

import homeautomation.actors.HomeAutomationConstants;
import homeautomation.actors.ServoAtAdafruitBoard;
import homeautomation.apps.IHomeautomationApp;
import homeautomation.foundation.AdafruitPCA9685;

public class RCPiBot implements IHomeautomationApp {
  private int MAX_SERVO_DEGREE = 45;
  private int LEFT_SERVO_INDEX = 0;
  private int RIGHT_SERVO_INDEX = 1;

  /**
   * Servo controlling the left wheel.
   */
  private ServoAtAdafruitBoard servoLeftWheel = null;

  /**
   * Servo controlling the right wheel.
   */
  private ServoAtAdafruitBoard servoRightWheel = null;;

  private int velocity = 0;
  private int rotation = 0;

  {
    AdafruitPCA9685 board = new AdafruitPCA9685();
    board.setPWMFreq(HomeAutomationConstants.MG_995_FREQUENCY);
    servoLeftWheel = new ServoAtAdafruitBoard(LEFT_SERVO_INDEX, MAX_SERVO_DEGREE,
        HomeAutomationConstants.MG_995_00_MIN_PULSE, HomeAutomationConstants.MG_995_00_MAX_PULSE, board);
    servoRightWheel = new ServoAtAdafruitBoard(RIGHT_SERVO_INDEX, MAX_SERVO_DEGREE,
        HomeAutomationConstants.MG_995_01_MIN_PULSE, HomeAutomationConstants.MG_995_01_MAX_PULSE, board);
  }

  @Override
  public void init() {

  }

  @Override
  public void shutdown() {
  }

  @Override
  public void command(String line) {
  }

  /**
   * Sets the velocity of the bot in percent. Value must be in [-100;100].
   * 
   * @param percentage
   *          Relative velocity in percent.
   */
  public void setVelocity(int percentage) {
    // System.out.println("Velocity: " + percentage + "%");
    this.velocity = percentage;
    go();
  }

  /**
   * Sets the rotation of the bot. Negative values mean left, positive values
   * mean right.
   * 
   * @param percentage
   *          Must be in [-100;100]; < 0 means left, >0 means right, == 0 means
   *          straight.
   */
  public void setRotation(int percentage) {
    // System.out.println("Rotation: " + Math.abs(percentage) + "% " +
    // ((percentage < 0) ? "left" : "right"));
    this.rotation = percentage;
    go();
  }

  /**
   * Returns true of the server is connected to the bot.
   * 
   * @return Connection status flag.
   */
  public boolean isConnected() {
    return false;
  }

  /**
   * Compute the servo control based on velocity and rotation.
   */
  private void go() {

    int left = 0;
    int right = 0;

    if (rotation < 0) {
      // Turn left
      double rotationDampening = 1 - Math.abs(rotation) / 100.0;
      left = (int) (rotationDampening * velocity / 100.0 * MAX_SERVO_DEGREE);
      right = (int) (velocity / 100.0 * MAX_SERVO_DEGREE);
    } else if (rotation > 0) {
      // Turn right
      double rotationDampening = 1 - Math.abs(rotation) / 100.0;
      left = (int) (velocity / 100.0 * MAX_SERVO_DEGREE);
      right = (int) (rotationDampening * velocity / 100.0 * MAX_SERVO_DEGREE);
    }
    servoLeftWheel.setDegree(left);
    // right in installed ccw -> needs inverse direction.
    servoRightWheel.setDegree(-right);

    //System.out.format("left: %d, right: %d\n", left, right);
  }

  public static void main(String[] args) {
    RCPiBot bot = new RCPiBot();
    try {
      bot.setVelocity(30);
      System.out.println("Waiting for rotation");
      Thread.sleep(2000);
      bot.setRotation(50);
      System.out.println("Waiting for stop");
      Thread.sleep(2000);
      bot.setVelocity(0);
    } catch (InterruptedException e) {
    }
  }

}

package homeautomation.modelvehicles;

import homeautomation.actors.IServo;
import homeautomation.actors.TinkerforgeServo;
import homeautomation.actors.TinkerforgeServoBrick;
import homeautomation.apps.IHomeautomationApp;
import cgresearch.core.logging.*;
import homeautomation.platform.TinkerforgeIpConnection;

public class RCBoat implements IHomeautomationApp {

  private final String SERVO_BRICK_UID = "5VjBzL";
  private final int SERVO_INDEX_RUDDER = 0;
  private final int SERVO_INDEX_MOTOR = 1;
  private final int RUDDER_MAX_DEGREE = 45;
  private final int MIN_PULSE_WIDTH = 1000; // 1ms
  private final int MAX_PULSE_WIDTH = 2000; // 2ms
  private final int RUDDER_VELOCITY = 50000;
  private final int RUDDER_ACCELERATION = 65000;
  private final int RUDDER_PERIOD = 19500;
  private final int SERVO_OUTPUT_VOLTAGE = 5;
  private final int PROPELLER_MAX_VELOCITY = 50000;
  private final int PROPELLER_ACCELERATION = 65000;
  private final int PROPELLER_PERIOD = 15000; // 15 ms
  private final int PROPELLER_MAX_DEGREE = 90;

  /**
   * The servo is controlled by a Tinkerforge brick.
   */
  private final IServo ruderServo;

  /**
   * The motor is controlled by a Tinkerforge DC brick.
   */
  private final IServo propellerServo;

  /**
   * Central IP connection.
   */
  private final TinkerforgeIpConnection ipConnection;

  /**
   * Servo brick instance.
   */
  private final TinkerforgeServoBrick servoBrick;

  /**
   * Constructor.
   */
  public RCBoat() {
    ipConnection = new TinkerforgeIpConnection();

    servoBrick = new TinkerforgeServoBrick(SERVO_BRICK_UID, ipConnection, SERVO_OUTPUT_VOLTAGE);
    ruderServo = new TinkerforgeServo(servoBrick, SERVO_INDEX_RUDDER, RUDDER_MAX_DEGREE, MIN_PULSE_WIDTH,
        MAX_PULSE_WIDTH, RUDDER_VELOCITY, RUDDER_ACCELERATION, RUDDER_PERIOD);
    propellerServo = new TinkerforgeServo(servoBrick, SERVO_INDEX_MOTOR, PROPELLER_MAX_DEGREE, MIN_PULSE_WIDTH,
        MAX_PULSE_WIDTH, PROPELLER_MAX_VELOCITY, PROPELLER_ACCELERATION, PROPELLER_PERIOD);
    Logger.getInstance().message(propellerServo + "\n\n" + ruderServo + "\n");
  }

  @Override
  public void init() {
  }

  @Override
  public void shutdown() {
    propellerServo.setDegree(0);
    propellerServo.shutdown();
    ruderServo.shutdown();
    propellerServo.shutdown();
    ipConnection.disconnect();
  }

  @Override
  public void command(String line) {
    Logger.getInstance().message("Not implemented.");
  }

  /**
   * Set the rudder position.
   * 
   * @param angle
   *          Angle position of the rudder in [-45,45]
   */
  public void setRudder(int angle) {
    if (angle < -45 || angle > 45) {
      Logger.getInstance().error("Invalid rudder angle: " + angle);
      return;
    }
    ruderServo.setDegree(angle);
  }

  /**
   * Set the motor speed.
   * 
   * @param speed
   *          Relative speed of the motor in [-100,100].
   */
  public void setSpeed(int speed) {
    if (speed < -100 || speed > 100) {
      Logger.getInstance().error("Invalid motor speed value: " + speed);
      return;
    }
    propellerServo.setDegree((int) (PROPELLER_MAX_DEGREE * speed / 100.0f));
  }

  /**
   * Returns the stack voltage.
   * 
   * @return Voltage in [V]
   */
  public double getVoltage() {
    if (isConnected() && servoBrick != null) {
      return servoBrick.getInputVoltage();
    }
    return -1;
  }

  /**
   * Check connection to Tinkerforge stack.
   * 
   * @return True if the connection is established, false otherwise.
   */
  boolean isConnected() {
    if (!ipConnection.isConnected()) {
      // No connection to demon
      return false;
    }
    // Check connection to servo brick
    return servoBrick.isConnected();
  }

  public static void main(String[] args) {
    new ConsoleLogger();
    RCBoat boat = new RCBoat();
    System.out.println(boat.isConnected());
  }
}

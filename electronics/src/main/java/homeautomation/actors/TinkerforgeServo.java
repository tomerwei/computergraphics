package homeautomation.actors;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import homeautomation.foundation.Logger;
import homeautomation.JsonConstants;

/**
 * Representation of a servo attached to a Tinkerforge servo brick.
 * 
 * @author Philipp Jenke
 *
 */
public class TinkerforgeServo implements IServo {

  /**
   * Servo brick instance.
   */
  private final TinkerforgeServoBrick servoBrick;

  /**
   * Index (port) of the servo at the Tinkerforge brick.
   */
  private short servoNumber;

  /**
   * Maximum degree (+ and -) of the servos. Default is 90.
   */
  private int maxDegree = 90;

  /**
   * Minimum pulse width. Default: 1ms
   */
  private int pulseWidthMax = 1000;

  /**
   * Maximum pulse width. Default: 2ms
   */
  private int pulseWidthMin = 2000;

  /**
   * Velocity of the servo.
   */
  private int velocity;

  /**
   * Accelleration of the servo.
   */
  private int acceleration;

  /**
   * Servo control period. Default: 19.5ms.
   */
  private int period = 19500;

  /**
   * Constructor.
   * 
   * @param servoBrick
   *          Attached servo brick.
   * @param servoIndex
   *          Index of the servo at the brick.
   * @param maxDegree
   *          Max turning degree of the servo.
   * @param pulseWidthMin
   *          Minimum servo pulse width.
   * @param pulseWidthMax
   *          Maximum servo pulse width.
   * @param velocity
   *          Servo velocity.
   * @param acceleration
   *          Servo acceleration.
   * @param period
   *          Servo pwm frequency.
   */
  public TinkerforgeServo(TinkerforgeServoBrick servoBrick, int servoIndex,
      int maxDegree, int pulseWidthMin, int pulseWidthMax, int velocity,
      int acceleration, int period) {
    this.servoBrick = servoBrick;
    this.servoNumber = (short) servoIndex;
    this.maxDegree = maxDegree;
    this.pulseWidthMin = pulseWidthMin;
    this.pulseWidthMax = pulseWidthMax;
    this.velocity = velocity;
    this.acceleration = acceleration;
    this.period = period;
    try {
      servoBrick.getServoBrick().enable(servoNumber);
      servoBrick.getServoBrick().setPulseWidth(servoNumber, pulseWidthMin,
          pulseWidthMax);
      servoBrick.getServoBrick().setVelocity(servoNumber, velocity);
      servoBrick.getServoBrick().setAcceleration(servoNumber, acceleration);
      servoBrick.getServoBrick().setPeriod(servoNumber, period);
    } catch (TimeoutException e) {
      Logger.getInstance().exception("Failed to enable servo.", e);
    } catch (NotConnectedException e) {
      Logger.getInstance().exception("Failed to enable servo.", e);
    }
  }

  @Override
  public ActorType getType() {
    return ActorType.SERVO;
  }

  @Override
  public void sendCommand(String command) {
    Logger.getInstance().message("Not implemented yet.");
  }

  @Override
  public String getLocation() {
    return "-";
  }

  @Override
  public String getHumanRedableType() {
    return "Tinkerforge Servo";
  }

  @Override
  public String toJson() {
    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
    jsonBuilder.add(JsonConstants.TYPE, getType().toString());
    jsonBuilder.add(JsonConstants.TINKERFORGE_SERVO_BRICK, servoBrick.toJson());
    jsonBuilder.add(JsonConstants.LOCATION, getLocation().toString());
    jsonBuilder.add(JsonConstants.MAX_DEGREE, maxDegree);
    jsonBuilder.add(JsonConstants.PULSE_WIDTH_MIN, pulseWidthMin);
    jsonBuilder.add(JsonConstants.PULSE_WIDTH_MAX, pulseWidthMax);
    jsonBuilder.add(JsonConstants.VELOCITY, velocity);
    jsonBuilder.add(JsonConstants.ACCELERATION, acceleration);
    jsonBuilder.add(JsonConstants.PERIOD, period);
    return jsonBuilder.build().toString();
  }

  @Override
  public void setDegree(int degree) {
    if (Math.abs(degree) > maxDegree) {
      Logger.getInstance().error("Invalid degree value");
      return;
    }
    short pos = (short) ((double)degree / (double)maxDegree * 9000);
    try {
      servoBrick.getServoBrick().setPosition(servoNumber, pos);
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().exception("Failed to set servo angle.", e);
    }
  }

  @Override
  public void shutdown() {
    try {
      servoBrick.getServoBrick().disable(servoNumber);
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().exception("Failed to shutdown servo.", e);
    }
  }

  @Override
  public String toString() {
    String brickString =
        "  " + servoBrick.toString().replace("\n", "\n  ") + "\n";
    return "*** Tinkerforge Servo *** \n" + brickString + "- type:"
        + getType().toString() + "\n" + "- location: "
        + getLocation().toString() + "\n" + "- max degree: " + maxDegree + "\n"
        + "- min pulse width: " + pulseWidthMin + "\n" + "- max pulse width: "
        + pulseWidthMax + "\n" + "- velocity: " + velocity + "\n"
        + "- acceleration: " + acceleration + "\n" + "- period: " + period;
  }

}

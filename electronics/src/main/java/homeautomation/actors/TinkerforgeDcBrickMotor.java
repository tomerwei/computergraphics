package homeautomation.actors;

import homeautomation.foundation.Logger;
import homeautomation.JsonConstants;
import homeautomation.modules.TinkerforgeModule;
import homeautomation.platform.TinkerforgeIpConnection;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.tinkerforge.BrickDC;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * A DC motor attached to a Tinkerforge DC brick can run with a velocity between
 * -100% and -100%.
 * 
 * @author Philipp Jenke
 *
 */
public class TinkerforgeDcBrickMotor extends TinkerforgeModule implements
    IMotor {

  /**
   * Tinkerforge DC brick.
   */
  private final BrickDC dcBrick;

  /**
   * Maximum velocity of the motor (in rotations per minute?)
   */
  private int maxVelocity = 10000;

  /**
   * Motor output voltage in [V].
   */
  private int outputVoltage = 5;

  /**
   * Motor acceleration.
   */
  private int acceleration = 1000;

  /**
   * Motor PWM frequency in 10^-6s
   */
  private int pwmFrequency = 15000;

  /**
   * Constructor.
   * 
   * @param tinkerforgeUid
   *          Uid of the Tinkerforge module.
   * @param tinkerforgeIpConnection
   *          Shared IP connection
   * @param maxVelocity
   *          Maximum velocity of the motor.
   * @param outputVoltage
   *          Output voltage for the motor.
   * @param acceleration
   *          Motor acceleration.
   * @param pwmFrequency
   *          Motor PWM frequency.
   */
  public TinkerforgeDcBrickMotor(String tinkerforgeUid,
      TinkerforgeIpConnection tinkerforgeIpConnection, int maxVelocity,
      int outputVoltage, int acceleration, int pwmFrequency) {
    super(tinkerforgeUid, null);
    this.outputVoltage = outputVoltage;
    this.acceleration = acceleration;
    this.pwmFrequency = pwmFrequency;
    dcBrick =
        new BrickDC(tinkerforgeUid, tinkerforgeIpConnection.getIpConnection());
    try {
      dcBrick.enable();
      dcBrick.setMinimumVoltage(outputVoltage);
      dcBrick.setAcceleration(acceleration);
      dcBrick.setPWMFrequency(pwmFrequency);
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().exception("Failed to enable DC brick", e);
    }
  }

  @Override
  public void setVelocity(double percentage) {
    if (percentage < -1 || percentage > 1) {
      Logger.getInstance().error("Invalid parameter value for setVelocity.");
      return;
    }

    try {
      dcBrick.setVelocity((short) (percentage * maxVelocity));
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().exception("Failed to set velocity", e);
    }
  }

  @Override
  public void stop() {
    try {
      dcBrick.fullBrake();
    } catch (TimeoutException e) {
      Logger.getInstance().exception("Failed to stop motor.", e);
    } catch (NotConnectedException e) {
      Logger.getInstance().exception("Failed to stop motor.", e);
    }
  }

  @Override
  public ActorType getType() {
    return ActorType.DC_MOTOR;
  }

  @Override
  public void sendCommand(String command) {
  }

  @Override
  public String getLocation() {
    return "-";
  }

  @Override
  public String getHumanRedableType() {
    return "DC Motor";
  }

  @Override
  public String toJson() {
    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
    jsonBuilder.add(JsonConstants.TYPE, getType().toString());
    jsonBuilder.add(JsonConstants.TINKERFORGE_UID, getUid());
    jsonBuilder.add(JsonConstants.LOCATION, getLocation().toString());
    jsonBuilder.add(JsonConstants.MAX_VELOCITY, maxVelocity);
    jsonBuilder.add(JsonConstants.OUTPUT_VOLTAGE, outputVoltage);
    jsonBuilder.add(JsonConstants.ACCELERATION, acceleration);
    jsonBuilder.add(JsonConstants.PWM_FREQUENCY, pwmFrequency);
    return jsonBuilder.build().toString();
  }

  @Override
  public void shutdown() {
    try {
      dcBrick.disable();
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().exception("Failed to disable DC brick.", e);
    }
  }

  @Override
  public String toString() {
    return "*** Tinkerforge DC Brick Motor *** \n" + "type: "
        + getType().toString() + "\n" + "- uid: " + getUid() + "\n"
        + "- location: " + getLocation().toString() + "\n" + "- max velocity: "
        + maxVelocity + "\n" + "- output voltage: " + outputVoltage + "\n"
        + "- acceleration: " + acceleration + "\n" + "- PWM frequency: "
        + pwmFrequency;
  }
}

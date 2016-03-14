package homeautomation.actors;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.tinkerforge.BrickServo;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import cgresearch.core.logging.*;
import homeautomation.JsonConstants;
import homeautomation.modules.TinkerforgeModule;
import homeautomation.platform.TinkerforgeIpConnection;

/**
 * Representation of a Tinkerforge servo brick.
 * 
 * @author Philipp Jenke
 *
 */
public class TinkerforgeServoBrick extends TinkerforgeModule implements IActor {

  /**
   * Tinkerforge servo brick object.
   */
  private final BrickServo servoBrick;

  /**
   * Output voltage provided to the servos.
   */
  private int outputVoltage = 5;

  /**
   * Constructor.
   * 
   * @param tinkerforgeUid
   *          Tinkerforge brick UID.
   * @param ipConnection
   *          Central IP connection to the stack.
   * @param outputVoltage
   *          Output voltage provided to the servos.
   */
  public TinkerforgeServoBrick(String tinkerforgeUid,
      TinkerforgeIpConnection ipConnection, int outputVoltage) {
    super(tinkerforgeUid, null);
    this.servoBrick =
        new BrickServo(tinkerforgeUid, ipConnection.getIpConnection());
    this.outputVoltage = outputVoltage;
    try {
      servoBrick.setOutputVoltage(outputVoltage);
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().exception("Failed to set output voltage.", e);
    }
  }

  @Override
  public ActorType getType() {
    return ActorType.TINKERFORGE_SERVO_BRICK;
  }

  @Override
  public void sendCommand(String command) {
    Logger.getInstance().message("Not implemented");
  }

  @Override
  public String getLocation() {
    return "-";
  }

  @Override
  public String getHumanRedableType() {
    return "Tinkerforge Servo Brick";
  }

  @Override
  public String toJson() {
    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
    jsonBuilder.add(JsonConstants.TYPE, getType().toString());
    jsonBuilder.add(JsonConstants.LOCATION, getLocation().toString());
    jsonBuilder.add(JsonConstants.OUTPUT_VOLTAGE, outputVoltage);
    return jsonBuilder.build().toString();
  }

  /**
   * Getter.
   * 
   * @return Corresponding Tinkerforge object.
   */
  public BrickServo getServoBrick() {
    return servoBrick;
  }

  @Override
  public String toString() {
    return "*** Tinkerforge Servo Brick ***\n" + "type: "
        + getType().toString() + "\n" + "- location: "
        + getLocation().toString() + "\n" + "- output voltage: "
        + outputVoltage;
  }

  @Override
  public void shutdown() {
  }

  /**
   * Returns the brick input voltage. Returns -1 on error.
   * 
   * @return Input voltage in [V]
   */
  public double getInputVoltage() {
    try {
      return Math.max(servoBrick.getStackInputVoltage() / 1000.0,
          servoBrick.getExternalInputVoltage() / 1000.0);
    } catch (TimeoutException | NotConnectedException e) {
      // System.out.println("Timeout");
      return -1;
    }
  }

  /**
   * Check for connection to servo brick.
   * 
   * @return True, if the connection is valid.
   */
  public boolean isConnected() {
    try {
      // Try to read identiy.
      servoBrick.getIdentity();
      return true;
    } catch (TimeoutException | NotConnectedException e) {
      // Could not be read
      return false;
    }
  }
}

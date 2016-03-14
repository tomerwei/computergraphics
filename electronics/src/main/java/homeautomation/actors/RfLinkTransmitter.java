package homeautomation.actors;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import cgresearch.core.logging.*;
import homeautomation.JsonConstants;

/**
 * Representation of a switch for electrical outlet.
 * 
 * @author Philipp Jenke
 *
 */
public class RfLinkTransmitter implements IActor {

  /**
   * The dummy mode is used for debugging where the physical sensor is not
   * available.
   */
  public enum Mode {
    REAL, DUMMY
  }

  private homeautomation.swig.RfLinkTransmitter rfLinkTransmitter = null;
  private homeautomation.swig.RfLinkTransmitterDummy rfLinkTransmitterDummy =
      null;

  /**
   * Dummy vs. real
   */
  private Mode mode;

  /**
   * Raspberry Pi pin number.
   */
  private int pinNumber;

  /**
   * Constructor.
   * 
   * @param mode
   *          Dummy or real.
   * @param pinNumber
   *          GPIO pin.
   */
  public RfLinkTransmitter(Mode mode, int pinNumber) {
    this.mode = mode;
    this.pinNumber = pinNumber;
    if (mode == Mode.REAL) {
      rfLinkTransmitter = new homeautomation.swig.RfLinkTransmitter(pinNumber);
    } else {
      rfLinkTransmitterDummy =
          new homeautomation.swig.RfLinkTransmitterDummy(pinNumber);
    }
  }

  /**
   * Set the state.
   * 
   * @param systemCode
   *          System code of the connection.
   * @param unitCode
   *          Unit code of the connection.
   * @param state
   *          State to be set.
   */
  public void setState(String systemCode, int unitCode, boolean state) {

    Logger.getInstance().debug("RfLinkTransmitter.setState()");

    if (mode == Mode.REAL) {
      Logger.getInstance().debug("RfLinkTransmitter.setState() -> real");
      rfLinkTransmitter.setState(systemCode, unitCode, state);
    } else {
      Logger.getInstance().debug("RfLinkTransmitter.setState() -> dummy");
      rfLinkTransmitterDummy.setState(systemCode, unitCode, state);
    }
  }

  @Override
  public ActorType getType() {
    if (mode == Mode.REAL) {
      return ActorType.RF_LINK_TRANSMITTER;
    } else {
      return ActorType.RF_LINK_TRANSMITTER_DUMMY;
    }
  }

  @Override
  public String toString() {
    return "RF Link Transmitter (" + ((mode == Mode.DUMMY) ? "Dummy, " : "")
        + "pin=" + pinNumber + ")";
  }

  @Override
  public void sendCommand(String command) {

    Logger.getInstance().debug("RfLinkTransmitter.sendCommand(): " + command);

    String[] tokens = command.trim().split("\\s+");
    if (tokens.length != 3) {
      Logger.getInstance().error("Invalid command: " + command);
      return;
    }

    String systemCode = tokens[0];
    int unitCode = Integer.parseInt(tokens[1]);
    Commands op = Commands.valueOf(tokens[2]);

    switch (op) {
      case ON:
        setState(systemCode, unitCode, true);
        break;
      case OFF:
        setState(systemCode, unitCode, false);
        break;
      case UNDEFINED:
        // Do nothing
        Logger.getInstance()
            .debug("RfLinkTransmitter.sndCommand(): Undefined!");
        break;
      default:
        Logger.getInstance().error("Undefined command for actor " + getType());
    }
  }

  @Override
  public String getLocation() {
    return "@Raspberry Pi";
  }

  @Override
  public String getHumanRedableType() {
    if (mode == Mode.REAL) {
      return "RF Link Transmitter";
    } else {
      return "RF Link Transmitter (dummy)";
    }
  }

  @Override
  public String toJson() {
    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
    jsonBuilder.add(JsonConstants.TYPE, getType().toString());
    jsonBuilder.add(JsonConstants.PIN_NUMBER, pinNumber);
    return jsonBuilder.build().toString();
  }
}

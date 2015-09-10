package homeautomation.actors;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import homeautomation.foundation.Logger;
import homeautomation.JsonConstants;

/**
 * Representation of an electronical outlet switch controlled by an rf link
 * transmitter.
 * 
 * @author Philipp Jenke
 *
 */
public class ElecticalOutletSwitch implements IActor {

  /**
   * System code.
   */
  private String systemCode;

  /**
   * Unit code of the device.
   */
  private int unitCode;

  /**
   * RF link transmitter extension.
   */
  private RfLinkTransmitter rfLinkTransmitter;

  /**
   * Location.
   */
  private final String location;

  /**
   * Constructor.
   * 
   * @param rfLinkTransmitter
   *          A transmitter is required to use the switch.
   * @param systemCode
   *          System code of the connection.
   * @param unitCode
   *          Unit code of the connection.
   * @param location
   *          Location of the actor.
   */
  public ElecticalOutletSwitch(RfLinkTransmitter rfLinkTransmitter,
      String systemCode, int unitCode, String location) {
    this.rfLinkTransmitter = rfLinkTransmitter;
    this.systemCode = systemCode;
    this.unitCode = unitCode;
    this.location = location;
  }

  @Override
  public ActorType getType() {
    return ActorType.ELECTRICAL_OUTLET_SWITCH;
  }

  @Override
  public void sendCommand(String command) {
    String combinedCommand =
        "" + systemCode + " " + unitCode + " " + command.toString();
    Logger.getInstance().debug(
        "ElectronicalOutletSwitch.sendCommand(): " + combinedCommand);
    rfLinkTransmitter.sendCommand(combinedCommand);
  }

  @Override
  public String toString() {
    return "Electrical Outlet Switch (" + "systemCode=" + systemCode
        + ", unitCode=" + unitCode + ")";
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public String getHumanRedableType() {
    return "Electrical Outlet Switch";
  }

  @Override
  public String toJson() {
    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
    jsonBuilder.add(JsonConstants.TYPE, getType().toString());
    jsonBuilder.add(JsonConstants.SYSTEM_CODE, systemCode);
    jsonBuilder.add(JsonConstants.UNIT_CODE, unitCode);
    jsonBuilder.add(JsonConstants.LOCATION, getLocation().toString());
    return jsonBuilder.build().toString();
  }

}

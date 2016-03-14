package homeautomation.apps;

import homeautomation.actors.ElecticalOutletSwitch;
import homeautomation.actors.IActor.Commands;
import homeautomation.actors.RfLinkTransmitter;
import homeautomation.actors.RfLinkTransmitter.Mode;
import cgresearch.core.logging.Logger;

public class ElectricalSwitchTest implements IHomeautomationApp {

  private final ElecticalOutletSwitch electricalOutletSwitch;

  private final String systemCode = "11111";
  private final int unitCode = 1;
  private final String location = "Dummy";
  private final int pinNumber = 0;

  /**
   * Constructor.
   */
  public ElectricalSwitchTest() {
    electricalOutletSwitch =
        new ElecticalOutletSwitch(new RfLinkTransmitter(Mode.REAL, pinNumber),
            systemCode, unitCode, location);
  }

  @Override
  public void init() {
    Logger.getInstance().message("Command: on|off|exit");

  }

  @Override
  public void shutdown() {
  }

  @Override
  public String toString() {
    return "ElectricalSwitchTest";
  }

  @Override
  public void command(String command) {
    switch (command) {
      case "on":
        Logger.getInstance().message("On");
        electricalOutletSwitch.sendCommand(Commands.ON.toString());
        break;
      case "off":
        Logger.getInstance().message("Off");
        electricalOutletSwitch.sendCommand(Commands.OFF.toString());
        break;
      default:
    }
  }
}

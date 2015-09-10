package homeautomation.modelvehicles;

import homeautomation.foundation.Logger;

/**
 * Handle string commands an pass them (if possible) to the boat.
 * 
 * @author Philipp Jenke
 *
 */
public class CommandHandler {

  /**
   * Command constants for the interactive control.
   */
  private static final String COMMAND_RUDDER = "rudder";
  private static final String COMMAND_MOTOR = "motor";

  /**
   * Reference the controlled boat.
   */
  private final RCBoat boat;

  /**
   * Constructor.
   * 
   * @param boat
   *          Boat reference.
   */
  public CommandHandler(RCBoat boat) {
    this.boat = boat;
  }

  /**
   * Handle String command an pass to boat (if possible).
   * 
   * @param command
   *          String command
   */
  public void handle(String command) {
    if (command.contains(COMMAND_RUDDER)) {
      handleRudderCommand(command);
    } else if (command.contains(COMMAND_MOTOR)) {
      handleMotorCommand(command);
    }
  }

  /**
   * Display the valid commands at the logger.
   */
  public void printValidCommand() {
    Logger.getInstance().message(
        "-> Motor: " + COMMAND_MOTOR + " <speed (-100...100)>");
    Logger.getInstance().message(
        "-> Rudder: " + COMMAND_RUDDER + " <speed (-45...45)>");
  }

  /**
   * Handle a command to control the servo.
   * 
   * @param command
   *          Command to be handled.
   */
  private void handleRudderCommand(String command) {
    String[] tokens = command.trim().split("\\s+");
    if (tokens.length > 1) {
      try {
        int degree = Integer.parseInt(tokens[1]);
        boat.setRudder(degree);
      } catch (NumberFormatException e) {
        Logger.getInstance().exception("Invalid number format.", e);
      }
    } else {
      Logger.getInstance().error("Malformed rudder command.");
    }
  }

  /**
   * Handle a command for the motor.
   * 
   * @param command
   *          Command string. Range: [-100,100]
   */
  private void handleMotorCommand(String command) {
    String[] tokens = command.trim().split("\\s+");
    if (tokens.length > 1) {
      try {
        int power = Integer.parseInt(tokens[1]);
        boat.setSpeed(power);
      } catch (NumberFormatException e) {
        Logger.getInstance().exception("Invalid number format.", e);
      }
    } else {
      Logger.getInstance().error("Malformed motor command.");
    }
  }

}

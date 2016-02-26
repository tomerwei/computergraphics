package homeautomation.modelvehicles;

import homeautomation.foundation.ConsoleLogger;
import homeautomation.foundation.Logger;

import java.util.Scanner;

/**
 * Command line controller for a RC boat.
 * 
 * @author Philipp Jenke
 *
 */
public class RCBoatCommandLineController {

  /**
   * Command to quit the controller.
   */
  private static final String COMMAND_QUIT = "quit";

  /**
   * This instance handles the commands an calls the corresponding boat methods.
   */
  private final CommandHandler commandHandler;

  /**
   * Constructor.
   * 
   * @param boat
   *          Boat to be controlled.
   */
  public RCBoatCommandLineController(RCBoat boat) {
    commandHandler = new CommandHandler(boat);
  }

  /**
   * Interactive (console) control of the boat.
   */
  public void interactiveControl() {
    Scanner scanner = new Scanner(System.in);
    String command = "";
    while (!command.equals(COMMAND_QUIT)) {
      Logger.getInstance().message("*** Commands *** ");
      commandHandler.printValidCommand();
      Logger.getInstance().message("-> Quit: " + COMMAND_QUIT);
      Logger.getInstance().message("Please enter command: ");
      command = scanner.nextLine();
      commandHandler.handle(command);
    }
    scanner.close();
  }

  public static void main(String[] args) {
    new ConsoleLogger();
    RCBoat boat = new RCBoat();
    RCBoatCommandLineController controller =
        new RCBoatCommandLineController(boat);
    controller.interactiveControl();
    boat.shutdown();
    Logger.getInstance().message("Goodbye.");
  }
}

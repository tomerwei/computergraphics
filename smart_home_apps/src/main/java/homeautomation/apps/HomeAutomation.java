package homeautomation.apps;

import homeautomation.factories.HomeAutomationAppFactory;
import cgresearch.core.logging.Logger;

import java.util.Scanner;

/**
 * Generic entry point for multiple applications.
 * 
 * @author Philipp Jenke
 *
 */
public class HomeAutomation {

  /**
   * Program entry point.
   * 
   * @param args
   *          Command line parameters.
   */
  public static void main(String[] args) {
    // String configJsonFilename =
    // "/Users/abo781/abo781/code/homeautomation/configs/rcboat.json";
    String configJsonFilename = "/Users/abo781/abo781/code/homeautomation/configs/raspiserver_mac.json";
    // String configJsonFilename =
    // "/Users/abo781/abo781/code/homeautomation/configs/electrical_switch_test.json";
    // String configJsonFilename ="configs/raspiserver_raspberry.json";
    // String configJsonFilename = "configs/visclient_local.json";
    // String configJsonFilename = "configs/sensortest_mac.json";
    // String configJsonFilename = "configs/sensortest_raspberry.json";

    // String configJsonFilename = "configs/visclient_remote.json";

    // String configJsonFilename = "configs/sensor_controls_led.json";
    if (args.length > 0) {
      configJsonFilename = args[0];
    }

    Logger.getInstance().message("Using config file " + configJsonFilename);

    HomeAutomationAppFactory factory = new HomeAutomationAppFactory();
    IHomeautomationApp app = factory.createApplication(configJsonFilename);
    Logger.getInstance().message("Type 'exit' to quit application.");

    if (app != null) {
      app.init();

      boolean done = false;
      String line = "";
      Scanner scanner = new Scanner(System.in);
      while (!done && (line = scanner.nextLine()) != null) {
        switch (line) {
          case "exit":
            app.shutdown();
            done = true;
            break;
          default:
            app.command(line);
        }
      }
      scanner.close();

    } else {
      Logger.getInstance().error("Failed to create RasPiServer instance.");
    }
  }
}

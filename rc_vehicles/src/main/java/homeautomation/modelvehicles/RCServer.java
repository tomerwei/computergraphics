package homeautomation.modelvehicles;

import homeautomation.foundation.ConsoleLogger;
import homeautomation.foundation.Logger;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.service.LogService;

/**
 * Webserver for the RCBoat app
 * 
 * @author Philipp Jenke
 *
 */
public class RCServer {

  /**
   * Port used.
   */
  private int port;

  /**
   * Constructor.
   * 
   * @param fileBaseDirectory
   *          Root directory for the image data.
   * @param port
   *          Server port.
   */
  public RCServer(int port) {
    this.port = port;

    Component component = new Component();
    component.setLogService(new LogService(true));
    component.getServers().add(Protocol.HTTP, port);

    // Register sensor table html page
    Router router = new Router(component.getContext().createChildContext());
    //router.attach("/rcboat/commands/{command}", RCBoatCommandsResource.class);
    //router.attach("/rcboat/state/{state}", RCBoatStateResource.class);
    router.attach("/rcpibot/commands/{command}", RCPiBotCommandsResource.class);
    component.getDefaultHost().attach("", router);

    try {
      component.start();
    } catch (Exception e) {
      Logger.getInstance().exception("Failed to start RC boat web service: " + e.getMessage(), e);
    }
    Logger.getInstance().message("Successfully started RC boat web service.");
  }

  @Override
  public String toString() {
    String returnValue = "*** WebService (RC Boat) ***\n";
    returnValue += "  port: " + port + "\n";
    return returnValue;
  }

  /**
   * Program entry point for testing.
   * 
   * @param args
   *          Command line arguments.
   */
  public static void main(String[] args) {
    new ConsoleLogger();
    new RCServer(8182);
  }
}

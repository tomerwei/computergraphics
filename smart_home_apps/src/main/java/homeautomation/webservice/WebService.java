package homeautomation.webservice;

import homeautomation.foundation.ConsoleLogger;
import homeautomation.foundation.Logger;
import homeautomation.webservice.dataaccess.RaspiServerAccessSingleton;
import homeautomation.webservice.resources.ResourceActorsControl;
import homeautomation.webservice.resources.ResourceHtmlPage;
import homeautomation.webservice.resources.ResourceImageFiles;
import homeautomation.webservice.resources.ResourceSensorStatsImage;
import homeautomation.webservice.resources.ResourceSensors;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.service.LogService;

/**
 * This file server provides access to files in the local file system (e.g.
 * images).
 * 
 * @author Philipp Jenke
 *
 */
public class WebService {

  /**
   * Path of the root folder for files.
   */
  private String fileBaseDirectory;

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
  public WebService(final String fileBaseDirectory, int port) {
    this.fileBaseDirectory = fileBaseDirectory;
    this.port = port;

    RaspiServerAccessSingleton.getInstance().setRootPath(fileBaseDirectory);

    Component component = new Component();
    component.setLogService(new LogService(true));
    component.getServers().add(Protocol.HTTP, port);
    component.getClients().add(Protocol.FILE);

    // Register sensor table html page
    Router router = new Router(component.getContext().createChildContext());
    router.attach("/{page}.html", ResourceHtmlPage.class);
    router.attach("/images/{image}", ResourceImageFiles.class);
    router.attach("/stats/{params}", ResourceSensorStatsImage.class);
    router.attach("/actors/{command}", ResourceActorsControl.class);
    router.attach("/sensors/{command}", ResourceSensors.class);

    component.getDefaultHost().attach("/raspiserver", router);

    try {
      component.start();
    } catch (Exception e) {
      Logger.getInstance().exception(
          "Failed to start web service: " + e.getMessage(), e);
    }
    Logger.getInstance().message("Successfully started web service.");
  }

  @Override
  public String toString() {
    String returnValue = "*** WebService ***\n";
    returnValue += "  port: " + port + "\n";
    returnValue += "  root path: " + fileBaseDirectory + "\n";
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
    new WebService("/Users/abo781/abo781/code/homeautomation", 8182);
  }
}

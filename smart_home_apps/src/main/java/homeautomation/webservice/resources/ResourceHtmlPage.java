package homeautomation.webservice.resources;

import homeautomation.foundation.Logger;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Parent resource for all html page resources.
 * 
 * @author Philipp Jenke
 *
 */
public class ResourceHtmlPage extends ServerResource {

  @Override
  public void doInit() {
  }

  /**
   * Handle GET command.
   */
  @Get
  public Representation get() {

    String filePath = getRequest().getResourceRef().getPath().trim();
    Logger.getInstance().message(filePath);

    PageFactoryAbstract pageFactory = null;
    if (filePath.endsWith("index.html")) {
      pageFactory = new PageFactorySensors();
    } else if (filePath.endsWith("sensors.html")) {
      pageFactory = new PageFactorySensors();
    } else if (filePath.endsWith("actors.html")) {
      pageFactory = new PageFactoryActors();
    }

    String htmlCode = "";
    if (pageFactory != null) {
      htmlCode = pageFactory.getHtmlCode();

    }

    StringRepresentation sr = new StringRepresentation(htmlCode,
        MediaType.TEXT_HTML);
    return sr;
  }
}

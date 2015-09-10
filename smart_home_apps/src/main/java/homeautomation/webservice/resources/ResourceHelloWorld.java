package homeautomation.webservice.resources;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Dummy testing resource to display "Hello World".
 * 
 * @author Philipp Jenke
 *
 */
public class ResourceHelloWorld extends ServerResource {

  /**
   * Handle GET command.
   */
  @Get
  public String toString() {
    return "hello, Homeautomation";
  }

}

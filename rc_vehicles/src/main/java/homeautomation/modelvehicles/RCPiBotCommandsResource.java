package homeautomation.modelvehicles;

import java.util.Dictionary;

import cgresearch.core.logging.*;
import homeautomation.webservice.resources.RestResource;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

/**
 * Control actors.
 * 
 * @author Philipp Jenke
 *
 */
public class RCPiBotCommandsResource extends RestResource {

  /**
   * Handle POST command.
   * 
   * @param entity
   *          Unused. Required?
   * 
   * @return HTML answer.
   */
  @Override
  protected Representation post(Representation entity) throws ResourceException {
    String uri = getRequest().getResourceRef().getPath();
    Dictionary<String, String> dictionary = createParameterDictionary(uri);
    try {
      int velocity = Integer.valueOf(dictionary.get(Constants.VELOCITY));
      int rotation = Integer.valueOf(dictionary.get(Constants.ROTATION));
      RCPiBotSingleton.getInstance().getBot().setVelocity(velocity);
      RCPiBotSingleton.getInstance().getBot().setRotation(rotation);
    } catch (Exception e) {
      Logger.getInstance().exception("Failed to parse command " + uri, e);
    }
    return null;
  }

  /**
   * Handle GET command.
   * 
   * @return HTML answer.
   */
  @Get
  public Representation get() {
    return null;
  }
}

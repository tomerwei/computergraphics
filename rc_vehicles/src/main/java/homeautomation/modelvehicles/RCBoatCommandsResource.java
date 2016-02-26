package homeautomation.modelvehicles;

import java.util.Dictionary;

import homeautomation.foundation.Logger;
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
public class RCBoatCommandsResource extends RestResource {

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
      int rudderAngle = Integer.valueOf(dictionary.get(Constants.RUDDER));
      int propellerSpeed = Integer.valueOf(dictionary.get(Constants.PROPELLER));
      RCBoatSingleton.getInstance().getBoat().setRudder(rudderAngle);
      RCBoatSingleton.getInstance().getBoat().setSpeed(propellerSpeed);
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

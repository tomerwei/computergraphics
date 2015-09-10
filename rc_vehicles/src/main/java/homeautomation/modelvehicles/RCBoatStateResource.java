package homeautomation.modelvehicles;

import homeautomation.webservice.resources.RestResource;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

/**
 * Control actors.
 * 
 * @author Philipp Jenke
 *
 */
public class RCBoatStateResource extends RestResource {

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
    return null;
  }

  /**
   * Handle GET command.
   * 
   * @return HTML answer.
   */
  @Get
  public Representation get() {
    String uri = getRequest().getResourceRef().getPath();
    if (uri.equals("/rcboat/state/isAlive")) {
      boolean isConnected = RCBoatSingleton.getInstance().getBoat().isConnected();
      StringRepresentation result = new StringRepresentation(
          isConnected ? Constants.IS_CONNECTED : Constants.SERVER_CONNECTED_NO_BRICK_CONNECTION);
      return result;
    } else if (uri.equals("/rcboat/state/getVoltage")) {
      double voltage = RCBoatSingleton.getInstance().getBoat().getVoltage();
      return new StringRepresentation(String.format("%f", voltage));
    }
    return null;
  }
}

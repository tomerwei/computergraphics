package homeautomation.webservice.resources;

import java.util.Dictionary;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import homeautomation.actors.IActor;
import homeautomation.apps.RasPiServer;
import homeautomation.foundation.Logger;
import homeautomation.webservice.WebServiceConstants;
import homeautomation.webservice.dataaccess.RaspiServerAccessSingleton;

import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.data.Status;

/**
 * Control actors.
 * 
 * @author Philipp Jenke
 *
 */
public class ResourceActorsControl extends RestResource {

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

    if (!checkDictionarySwitchOnOff(dictionary)) {
      Representation result = new EmptyRepresentation();
      getResponse().setStatus(Status.SUCCESS_NO_CONTENT,
          "Invalid parameters for switch on/off.");
      Logger.getInstance().error("Failed to parse parameters.");
      return result;
    }

    int actorIndex =
        Integer.parseInt(dictionary.get(WebServiceConstants.PARAMETER_INDEX));
    String command = dictionary.get(WebServiceConstants.PARAMETER_COMMAND);
    applyCommand(actorIndex, command);
    Representation result = new EmptyRepresentation();
    getResponse().setStatus(Status.SUCCESS_NO_CONTENT, "Command executed.");
    return result;
  }

  /**
   * Handle GET command.
   * 
   * @return HTML answer.
   */
  @Get
  public Representation get() {

    // Handle getList
    String uri = getRequest().getResourceRef().getPath();
    Dictionary<String, String> dictionary = createParameterDictionary(uri);

    if (!checkDictionaryGetList(dictionary)) {
      Representation result = new EmptyRepresentation();
      Logger.getInstance().error("Invalid parameter for GET .");
      return result;
    }

    String jsonListOfActors = getJsonListOfActors();

    return new StringRepresentation(jsonListOfActors);
  }

  /**
   * Create a JSON array list of actors.
   * 
   * @return JSON-formatted representation of the actors list.
   */
  private String getJsonListOfActors() {
    RasPiServer raspiServer =
        RaspiServerAccessSingleton.getInstance().getRaspiServer();
    if (raspiServer == null) {
      Logger.getInstance().error("Failed to access raspi server.");
      return "[]";
    }
    return raspiServer.getJsonActorsList();
  }

  /**
   * Check the params for a request for a sensors list.
   * 
   * @param dictionary
   *          Dictionary of valid commands.
   * 
   * @return True, if the operation is available, false otherwise.
   */
  private boolean checkDictionaryGetList(Dictionary<String, String> dictionary) {
    if (dictionary.get(WebServiceConstants.OPERATION) == null) {
      return false;
    }
    if (dictionary.get(WebServiceConstants.OPERATION).compareTo(
        WebServiceConstants.GET_LIST) != 0) {
      return false;
    }
    return true;
  }

  /**
   * Check if the parameters are valid for the switch on/off command.
   * 
   * @param dictionary
   *          Dictionary of valid commands.
   * 
   * @return True, if the operation is available, false otherwise.
   */
  private boolean checkDictionarySwitchOnOff(
      Dictionary<String, String> dictionary) {
    if (dictionary.get(WebServiceConstants.PARAMETER_INDEX) == null) {
      return false;
    } else if (dictionary.get(WebServiceConstants.PARAMETER_COMMAND) == null) {
      return false;
    }
    try {
      Integer.parseInt(dictionary.get(WebServiceConstants.PARAMETER_INDEX));
    } catch (NumberFormatException exception) {
      return false;
    }
    return true;
  }

  /**
   * Apply a command to a given actor.
   * 
   * @param actorIndex
   *          Index of the actor
   * @param command
   *          Command string.
   * @return HTML request answer.
   */
  private Representation applyCommand(int actorIndex, String command) {
    RasPiServer raspiServer =
        RaspiServerAccessSingleton.getInstance().getRaspiServer();
    if (raspiServer == null) {
      return null;
    }

    IActor actor = raspiServer.getActor(actorIndex);
    if (actor == null) {
      return null;
    }

    actor.sendCommand(command);

    return null;
  }

  /**
   * Parse the sensor index from the resource URI.
   * 
   * @param resourceUri
   *          URI of a requested resource.
   * 
   * @return Index of the resource, -1 if not available.
   */
  public static int getActorIndex(String resourceUri) {
    Matcher matcher = createMatcher(resourceUri);
    int actorIndex = -1;
    try {
      actorIndex = Integer.parseInt(matcher.group(1));
    } catch (Exception e) {
      Logger.getInstance().exception("Failed to parse for sensor index", e);
    }
    return actorIndex;
  }

  /**
   * Parse the stats type from the resource URI.
   * 
   * @param resourceUri
   *          URI of a requested resource.
   * 
   * @return Command of the requested URI.
   */
  public static String getActorCommand(String resourceUri) {
    Matcher matcher = createMatcher(resourceUri);
    try {
      return matcher.group(2);
    } catch (Exception e) {
      Logger.getInstance().exception("Failed to parse for stats", e);
    }
    return null;
  }

  /**
   * Create a Matcher to parse an URI.
   * 
   * @param resourceUri
   *          URI to be parsed
   * @return Corresponding matcher.
   */
  private static Matcher createMatcher(String resourceUri) {
    String regex = "/raspiserver/actors/index=(.*),command=(.*)";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(resourceUri);
    matcher.find();
    return matcher;
  }
}

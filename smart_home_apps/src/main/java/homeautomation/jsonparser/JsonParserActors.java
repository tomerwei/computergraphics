package homeautomation.jsonparser;

import homeautomation.actors.RfLinkTransmitter;

import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import homeautomation.actors.RaspPiLed;
import homeautomation.platform.Pi4JRaspPiSingleton;
import homeautomation.JsonConstants;
import homeautomation.actors.ElecticalOutletSwitch;
import homeautomation.actors.RfLinkTransmitter.Mode;
import homeautomation.actors.IActor;
import homeautomation.actors.IActor.ActorType;
import homeautomation.foundation.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Parsing of JSON files for sensor information.
 * 
 * @author Philipp Jenke
 *
 */
public class JsonParserActors {

  /**
   * Parse for the list of actors.
   * 
   * @param parser
   *          Parser instance at the ready for parsing the object.
   * 
   * @return List of already parsed actors.
   */
  public static List<IActor> parseActors(JsonParser parser) {
    List<IActor> actors = new ArrayList<IActor>();
    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.START_OBJECT) {
        IActor actor = parseActor(parser, actors);
        if (actor != null) {
          actors.add(actor);
        }
      }
      if (event == Event.END_ARRAY) {
        return actors;
      }
    }
    return null;
  }

  /**
   * Parse for an actor object.
   * 
   * @param parser
   *          Parser instance at the ready for parsing the object.
   * @param alreadyParsedActors
   *          List of already parsed actors.
   * @return Created actor instance.
   */
  private static IActor parseActor(JsonParser parser,
      List<IActor> alreadyParsedActors) {
    String type = null;
    int pinNumber = -1;
    String systemCode = "";
    int unitCode = -1;
    String location = "";
    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.TYPE)) {
        event = parser.next();
        type = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.PIN_NUMBER)) {
        event = parser.next();
        pinNumber = Integer.parseInt(parser.getString());
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.SYSTEM_CODE)) {
        event = parser.next();
        systemCode = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.UNIT_CODE)) {
        event = parser.next();
        unitCode = Integer.parseInt(parser.getString());
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.LOCATION)) {
        event = parser.next();
        location = parser.getString();
      } else if (event == Event.END_OBJECT) {
        return createActor(type, pinNumber, systemCode, unitCode,
            alreadyParsedActors, location);
      }
    }
    return null;
  }

  /**
   * Create a actor object from the config information.
   * 
   * @param type
   *          Actor type.
   * @param pinNumber
   *          GPIO pin number (wiringPi format).
   * @param systemCode
   *          Connection system code.
   * @param unitCode
   *          Connection unit code.
   * @param alreadyParsedActors
   *          List of already parsed actors.
   * @param location
   *          Location of the actor.
   * @return Created actor.
   */
  private static IActor createActor(String type, int pinNumber,
      String systemCode, int unitCode, List<IActor> alreadyParsedActors,
      String location) {
    ActorType actorType = ActorType.valueOf(type);
    if (actorType.equals(IActor.ActorType.RF_LINK_TRANSMITTER)) {
      return new RfLinkTransmitter(Mode.REAL, pinNumber);
    } else if (actorType.equals(IActor.ActorType.RF_LINK_TRANSMITTER_DUMMY)) {
      return new RfLinkTransmitter(Mode.DUMMY, pinNumber);
    } else if (actorType.equals(IActor.ActorType.ELECTRICAL_OUTLET_SWITCH)) {

      // Check of we already parsed an RL link transmitter
      RfLinkTransmitter rfLinkTransmitter = null;
      for (IActor actor : alreadyParsedActors) {
        if (actor instanceof RfLinkTransmitter) {
          rfLinkTransmitter = (RfLinkTransmitter) actor;
        }
      }
      if (rfLinkTransmitter != null) {
        return new ElecticalOutletSwitch(rfLinkTransmitter, systemCode,
            unitCode, location);
      } else {
        Logger
            .getInstance()
            .error(
                "Cannor create Electrical outlet switch without RfLinkTransmitter object.");
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Parse for an LED object.
   * 
   * @param parser
   *          Parser instance at the ready for parsing the object.
   * @return Created app.
   */
  public static RaspPiLed parseLed(JsonParser parser) {
    String identifier = null;
    String mode = null;
    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.IDENTIFIER)) {
        event = parser.next();
        identifier = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.MODE)) {
        event = parser.next();
        mode = parser.getString();
      } else if (event == Event.END_OBJECT) {
        return createLed(mode, identifier);
      }
    }
    return null;
  }

  /**
   * Create an LED from a JSON config.
   * 
   * @param mode
   *          Dummy or real.
   * @param identifier
   *          GPIO pin number (wiringPi format).
   * @return LED app instance.
   */
  private static RaspPiLed createLed(String mode, String identifier) {
    return new RaspPiLed(RaspPiLed.Mode.valueOf(mode),
        Pi4JRaspPiSingleton.getGpioPin4Identifier(identifier),
        Pi4JRaspPiSingleton.getInstance().getRaspPi());
  }
}

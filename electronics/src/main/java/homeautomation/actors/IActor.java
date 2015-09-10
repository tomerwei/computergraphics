package homeautomation.actors;

/**
 * Parent interface for all actors.
 * 
 * @author Philipp Jenke
 *
 */
public interface IActor {

  /**
   * The interface knows the type of the actors, bad style.
   */
  public enum ActorType {
    LED,
    RF_LINK_TRANSMITTER,
    RF_LINK_TRANSMITTER_DUMMY,
    ELECTRICAL_OUTLET_SWITCH,
    DC_MOTOR,
    SERVO,
    TINKERFORGE_SERVO_BRICK,
  }

  /**
   * POssible actor commands.
   *
   */
  public enum Commands {
    UNDEFINED, ON, OFF
  }

  /**
   * Getter.
   * 
   * @return Type of the actor.
   */
  public ActorType getType();

  /**
   * Send command to actor.
   * 
   * @param command Command
   *          to be handled.
   */
  public void sendCommand(String command);

  /**
   * Getter.
   * 
   * @return Location of the actor.
   */
  public String getLocation();

  /**
   * Getter.
   * 
   * @return Type as a human readable string.
   */
  public String getHumanRedableType();

  /**
   * Getter.
   * 
   * @return Actor in JSON format.
   */
  public String toJson();

}

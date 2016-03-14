package homeautomation.actors;

import homeautomation.platform.ArduinoConnection;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Representation of a Servo attached to an Arduino board which is connected via
 * Ardulink.
 * 
 * Commands:<br>
 * attach [pin]<br>
 * detach [pin]<br>
 * angle [pin] [degree]<br>
 * 
 * @author Philipp Jenke
 */
public class ArduinoServo implements IServo {

  /**
   * Pin on the Arduino board (must be a PWM pin)
   */
  private int pin;

  /**
   * Connection to the Arduino board
   */
  private ArduinoConnection connection;

  public ArduinoServo(ArduinoConnection connection, int pin) {
    this.connection = connection;
    this.pin = pin;
  }

  public void attach() {
    connection.setCustomCommand("servo attach " + pin);
  }

  public void detach() {
    connection.setCustomCommand("servo dettach " + pin);
  }

  @Override
  public void setDegree(int degree) {
    connection.setCustomCommand("servo angle " + pin + " " + degree);
  }

  @Override
  public ActorType getType() {
    return ActorType.SERVO;
  }

  @Override
  public void sendCommand(String command) {
    throw new IllegalArgumentException();
  }

  @Override
  public String getLocation() {
    return "";
  }

  @Override
  public String getHumanRedableType() {
    return "Arduino Servo (Port " + pin + ")";
  }

  @Override
  public String toJson() {
    throw new NotImplementedException();
  }

  @Override
  public void shutdown() {
    detach();
  }

}

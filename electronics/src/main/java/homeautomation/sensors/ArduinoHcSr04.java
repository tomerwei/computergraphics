package homeautomation.sensors;

import homeautomation.platform.ArduinoConnection;

/**
 * Access to a HC-SR04 distance sensor.
 * 
 * @author Philipp Jenke
 */
public class ArduinoHcSr04 {

  /**
   * Connection to the Arduino
   */
  private ArduinoConnection connection;
  
  /**
   * Digital pin used to trigger the sensor
   */
  private int triggerPin;
  
  /**
   * Digital pin used to receive the echo event
   */
  private int echoPin;
  
  /**
   * Caches the newest received distance
   */
  private double latestDistance = -1;
  
  /**
   * Constants
   */
  private final String DISTANCE = "distance";

  public ArduinoHcSr04(ArduinoConnection connection, int triggerPin, int echoPin) {
    this.connection = connection;
    connection.addMessageCallback(message -> {
      parseValues(message);
    });
    this.triggerPin = triggerPin;
    this.echoPin = echoPin;
  }

  /**
   * Parse the answer from the Arduino
   * @param message
   */
  private void parseValues(String message) {
    String[] tokens = message.trim().split(" ");
    if (tokens.length < 2 || !tokens[0].contains(DISTANCE)) {
      return;
    }
    latestDistance = Double.parseDouble(tokens[1]);
  }

  /**
   * Request new distance measurement from Arduino
   */
  public void requestDistance() {
    connection.sendCustomCommand(DISTANCE + " " + triggerPin + " " + echoPin);
  }

  public double getDistance() {
    return latestDistance;
  }
}

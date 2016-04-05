package electronics.distance;

import java.util.Timer;
import java.util.TimerTask;

import cgresearch.core.logging.ConsoleLogger;
import cgresearch.core.logging.Logger;
import homeautomation.platform.ArduinoConnection;
import homeautomation.sensors.ArduinoHcSr04;

/**
 * Test accessing the HC-SR40 distance sensor via Arduino.
 * 
 * @author Philipp
 * 
 *  * Requires command line VM arguments:
 * -Djava.library.path=/Users/abo781/abo781/code/computergraphics/libs/native/
 * osx/
 * 
 * Arduino Sketch: ArdulinkProtocol.ino
 *
 */
public class Distance {

  /**
   * Sensor representation on Arduino board.
   */
  private ArduinoHcSr04 distanceSensor;

  public Distance() {
    ArduinoConnection connection = new ArduinoConnection();
    String port = connection.getMostLikelyPort();
    connection.connect(port);
    distanceSensor = new ArduinoHcSr04(connection, 8, 7);
  }

  /**
   * This is an endless loop which continuously requests distance values from the sensor.
   */
  public void run() {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        distanceSensor.requestDistance();
        Logger.getInstance().message("Distance in cm: " + distanceSensor.getDistance());
      }
    }, 100, 100);
  }

  public static void main(String[] args) {
    new ConsoleLogger();
    Distance distance = new Distance();
    distance.run();
  }
}

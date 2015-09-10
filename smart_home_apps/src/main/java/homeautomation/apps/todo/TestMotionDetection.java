package homeautomation.apps.todo;

import java.io.IOException;

import homeautomation.modules.RaspberryPi;
import homeautomation.sensors.RaspPiMotionDetectionSensor;
import homeautomation.sensors.RaspPiMotionDetectionSensor.Mode;

import com.pi4j.io.gpio.RaspiPin;

/**
 * Simple application to test the motion detection sensor.
 * 
 * @author Philipp Jenke
 *
 */
public class TestMotionDetection extends Thread {
  /**
   * Program entry point.
   * 
   * @param args
   *          Command line parameters.
   */
  public static void main(String[] args) {
    RaspberryPi pi = new RaspberryPi();
    new RaspPiMotionDetectionSensor(Mode.REAL, RaspiPin.GPIO_01, pi);

    System.out.println("Press enter to exit.");
    try {
      System.in.read();
      System.out.println("Exiting");
      System.exit(0);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

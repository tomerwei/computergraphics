package homeautomation.apps.todo;

import homeautomation.actors.RaspPiLed;
import homeautomation.actors.RaspPiLed.Mode;
import homeautomation.modules.RaspberryPi;

import com.pi4j.io.gpio.RaspiPin;

/**
 * Test the LED.
 * 
 * @author Philipp Jenke
 *
 */
public class TestLed {

  /**
   * Program entry point.
   * 
   * @param args
   *          Command line parameters.
   */
  public static void main(String[] args) {
    RaspberryPi pi = new RaspberryPi();
    RaspPiLed led = new RaspPiLed(Mode.REAL, RaspiPin.GPIO_00, pi);
    int numberOfBlinks = 3;
    System.out.println("Toggeling LED " + numberOfBlinks + " times.");
    for (int i = 0; i < numberOfBlinks; i++) {
      led.switchLight(true);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      led.switchLight(false);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println("Done..");
  }
}

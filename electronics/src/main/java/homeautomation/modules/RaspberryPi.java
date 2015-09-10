package homeautomation.modules;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

/**
 * Wrapper for access to Raspberry Pi functionality.
 * 
 * @author Philipp Jenke
 *
 */
public class RaspberryPi {

  /**
   * Constructor.
   */
  public RaspberryPi() {
  }

  /**
   * Getter.
   * 
   * @return GPIO object.
   */
  public GpioController getGpio() {
    return GpioFactory.getInstance();
  }

}

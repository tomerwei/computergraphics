package homeautomation.platform;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import cgresearch.core.logging.*;
import homeautomation.modules.RaspberryPi;

/**
 * Singleton instance for Tinkerforge access.
 * 
 * @author Pilipp Jenke
 *
 */
public class Pi4JRaspPiSingleton {

  /**
   * Singleton instance.
   */
  private static Pi4JRaspPiSingleton instance = null;

  /**
   * Master brick reference.
   */
  private RaspberryPi pi = null;

  /**
   * Private constructor.
   */
  private Pi4JRaspPiSingleton() {
    pi = new RaspberryPi();
  }

  /**
   * Access to the singleton instance.
   * 
   * @return Singleton instance.
   */
  public static Pi4JRaspPiSingleton getInstance() {
    if (instance == null) {
      instance = new Pi4JRaspPiSingleton();
    }
    return instance;
  }

  /**
   * Getter.
   * 
   * @return Raspberry Pi instance.
   */
  public RaspberryPi getRaspPi() {
    return pi;
  }

  /**
   * Convert String to Pin.
   * 
   * @param identifier
   *          String descriping a pin.
   * 
   * @return Pin described by the string identifier.
   */
  public static Pin getGpioPin4Identifier(String identifier) {
    switch (identifier) {
      case "0":
        return RaspiPin.GPIO_00;
      case "1":
        return RaspiPin.GPIO_01;
      case "2":
        return RaspiPin.GPIO_02;
      case "3":
        return RaspiPin.GPIO_03;
      case "4":
        return RaspiPin.GPIO_04;
      case "5":
        return RaspiPin.GPIO_05;
      case "6":
        return RaspiPin.GPIO_06;
      case "7":
        return RaspiPin.GPIO_07;
      case "8":
        return RaspiPin.GPIO_08;
      case "9":
        return RaspiPin.GPIO_09;
      case "10":
        return RaspiPin.GPIO_10;
      case "11":
        return RaspiPin.GPIO_11;
      case "12":
        return RaspiPin.GPIO_12;
      case "13":
        return RaspiPin.GPIO_13;
      case "14":
        return RaspiPin.GPIO_14;
      case "15":
        return RaspiPin.GPIO_15;
      case "16":
        return RaspiPin.GPIO_16;
      case "17":
        return RaspiPin.GPIO_17;
      case "18":
        return RaspiPin.GPIO_18;
      case "19":
        return RaspiPin.GPIO_19;
      case "20":
        return RaspiPin.GPIO_20;
      default:
        Logger.getInstance().error("Invalid identifier for pin: " + identifier);
        return null;
    }
  }
}

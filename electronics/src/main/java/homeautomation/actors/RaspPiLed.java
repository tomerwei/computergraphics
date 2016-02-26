package homeautomation.actors;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import homeautomation.foundation.Logger;
import homeautomation.modules.HomeAutomationModule;
import homeautomation.modules.RaspberryPi;

public class RaspPiLed implements HomeAutomationModule {

  /**
   * The dummy mode is used for debugging where the physical sensor is not
   * available.
   */
  public enum Mode {
    REAL, DUMMY
  }

  /**
   * Pin on the GPIO board.
   */
  private final GpioPinDigitalOutput pin;

  /**
   * Dummy vs. real.
   * 
   */
  private final Mode mode;

  /**
   * Id of GPIO pin.
   */
  private final Pin pinId;

  /**
   * Constructor.
   * 
   * @param mode
   *          Dummy or real.
   * @param pinId
   *          ID of the GPIO pin.
   * @param pi
   *          Raspi instance.
   */
  public RaspPiLed(Mode mode, Pin pinId, RaspberryPi pi) {
    this.pinId = pinId;
    this.mode = mode;
    String name = "MotionDetectionSensorPin";
    switch (mode) {
      case REAL:
        pin = pi.getGpio().provisionDigitalOutputPin(pinId, name, PinState.LOW);
        break;
      case DUMMY:
        pin = null;
        break;
      default:
        pin = null;
    }
  }

  @Override
  public void shutdown() {
    switch (mode) {
      case REAL:
        pin.setState(PinState.LOW);
        break;
      case DUMMY:
        break;
      default:
        break;
    }
  }

  /**
   * Switch the light on/off.
   * 
   * @param status
   *          State to be set.
   */
  public void switchLight(boolean status) {
    switch (mode) {
      case REAL:
        pin.setState((status) ? PinState.HIGH : PinState.LOW);
        break;
      case DUMMY:
        Logger.getInstance().message("LED: " + ((status) ? "on" : "off"));
        break;
      default:
    }
  }

  /**
   * Getter.
   * 
   * @return Current state of the LED.
   */
  public boolean isOn() {
    switch (mode) {
      case REAL:
        return pin.isHigh();
      default:
        return false;
    }
  }

  @Override
  public String toString() {
    switch (mode) {
      case REAL:
        return "*** RaspPiLed (" + mode + ") ***\n Pin: " + pinId;
      default:
        return "*** RaspPiLed (" + mode + ") ***\n Pin: " + pinId.getName();
    }

  }

  /**
   * Enlighten the LED for some milliseconds.
   * 
   * @param milliseconds
   *          Milliseconds of ON-state.
   */
  public void shine(final int milliseconds) {
    Thread shineThread = new Thread() {
      @Override
      public void run() {
        switchLight(true);
        try {
          Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
          Logger.getInstance().exception("Error while sleeping LED light", e);
        }
        switchLight(false);
      }
    };
    shineThread.start();
  }
}

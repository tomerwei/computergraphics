package homeautomation.sensors;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import homeautomation.foundation.Logger;
import homeautomation.modules.HomeAutomationModule;
import homeautomation.modules.RaspberryPi;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * Wrapper for the functionality to access the motion sensor. If the motion
 * detector is in DUMMY mode, some events are simulated (every 3 sesonds)
 * 
 * @author Philipp Jenke
 *
 */
public class RaspPiMotionDetectionSensor implements HomeAutomationModule,
    GpioPinListenerDigital {

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
  private final GpioPinDigitalInput pin;

  /**
   * Dummy or real sensor.
   */
  private final Mode mode;

  /**
   * Pin saved in dummy version.
   */
  private final Pin pinId;

  /**
   * Listener for the callbacks.
   */
  private List<IMotionDetectorCallback> listener =
      new ArrayList<IMotionDetectorCallback>();

  /**
   * Constructor.
   * 
   * @param mode
   *          Dummy or Real.
   * @param pinId
   *          Raspi-Pin.
   * @param pi
   *          Raspberry Pi instance.
   */
  public RaspPiMotionDetectionSensor(Mode mode, Pin pinId, RaspberryPi pi) {
    this.mode = mode;
    this.pinId = pinId;
    String name = "MotionDetectionSensorPin";

    if (mode == Mode.REAL) {
      pin =
          pi.getGpio().provisionDigitalInputPin(pinId, name,
              PinPullResistance.PULL_DOWN);
      pin.addListener(this);
    } else {
      pin = null;
      int simulatedMotionInterval = 4000;
      Timer timer = new Timer();
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          motionDetected();
        }
      }, simulatedMotionInterval, simulatedMotionInterval);
    }
  }

  /**
   * Getter.
   * 
   * @return True if a motion was detected, false otherwise.
   */
  public boolean isActive() {
    switch (mode) {
      case REAL:
        return pin.isHigh();
      case DUMMY:
        return false;
      default:
        return false;
    }
  }

  @Override
  public void handleGpioPinDigitalStateChangeEvent(
      GpioPinDigitalStateChangeEvent event) {
    // display pin state on console
    if (isActive()) {
      motionDetected();
    }
  }

  /**
   * A motion detected event is fired.
   */
  private void motionDetected() {
    for (IMotionDetectorCallback l : listener) {
      l.motionDetected();
    }
  }

  /**
   * Register additional listener.
   * 
   * @param callback
   *          Callback to be registered.
   */
  public void registerListener(IMotionDetectorCallback callback) {
    listener.add(callback);
  }

  @Override
  public void shutdown() {
    Logger.getInstance().debug("Shutting down motion detection sensor.");
  }

  @Override
  public String toString() {
    switch (mode) {
      case REAL:
        return "*** RaspPiMotionDetectionSensor (" + mode + ") ***\n Pin: "
            + pinId;
      default:
        return "*** RaspPiMotionDetectionSensor (" + mode + ") ***\n Pin: "
            + pinId.getName();
    }
  }
}

package homeautomation.apps;

import homeautomation.actors.RaspPiLed;
import homeautomation.sensors.IMotionDetectorCallback;
import homeautomation.sensors.RaspPiMotionDetectionSensor;
import homeautomation.foundation.Logger;

/**
 * Light the LED for some time, if the sensor detects movement.
 * 
 * @author Philipp Jenke
 *
 */
public class SensorControlsLed implements IHomeautomationApp,
    IMotionDetectorCallback {

  /**
   * Motion sensor instance.
   */
  private RaspPiMotionDetectionSensor motionSensor;

  /**
   * LED instance.
   */
  private RaspPiLed led;

  /**
   * Constructor.
   * 
   * @param motionSensor
   *          Motion sensor instance.
   * @param led
   *          Led instance.
   */
  public SensorControlsLed(RaspPiMotionDetectionSensor motionSensor,
      RaspPiLed led) {
    this.motionSensor = motionSensor;
    this.led = led;
    motionSensor.registerListener(this);
    led.shine(2000);
  }

  @Override
  public void init() {

  }

  @Override
  public void shutdown() {
  }

  @Override
  public String toString() {
    return "*** SensorControlsLed *** \n" + motionSensor + "\n" + led;
  }

  @Override
  public void motionDetected() {
    Logger.getInstance().message("Sensor detected motion");
    led.shine(2000);
  }

  @Override
  public void command(String line) {
  }

}

package homeautomation.sensors;

import cgresearch.core.logging.*;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 *
 * @see https 
 *      ://www.modmypi.com/blog/hc-sr04-ultrasonic-range-sensor-on-the-raspberry
 *      -pi
 */
public class UltraSonicSensor implements IDistanceSensor {

  private final static double SOUND_SPEED = 34300; // in cm, 343 m/s
  private final static double DIST_FACT = SOUND_SPEED / 2; // round trip
  private final static int MIN_DIST = 10;

  /**
   * Tripper-Pin (Signalausgang).
   */
  private final GpioPinDigitalOutput trigPin;

  /**
   * Echo-Pin (Signaleingang).
   */
  private final GpioPinDigitalInput echoPin;

  /**
   * Initialisierungs-Konstruktor.
   *
   * @param triggerGpioPin
   *          GPIO Pin of the trigger (WiringPi-Syntax).
   * @param echoGpioPin
   *          GPIO Pin of the echo (WiringPi-Syntax).
   */
  public UltraSonicSensor(Pin triggerGpioPin, Pin echoGpioPin) {
    final GpioController gpio = GpioFactory.getInstance();
    trigPin =
        gpio.provisionDigitalOutputPin(triggerGpioPin, "Trig", PinState.LOW);
    echoPin = gpio.provisionDigitalInputPin(echoGpioPin, "Echo");
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        gpio.shutdown();
      }
    });
  }

  @Override
  public double getDistance() {
    double gemittelteEntfernung = 0;
    int anzahlGueltigeMessungen = 0;
    for (int i = 0; i < 3; i++) {
      triggerSenden();
      double signalDauer = signalEmpfangen();
      double entfernung = entfernungBerechnen(signalDauer);
      if (entfernung > 0) {
        gemittelteEntfernung += entfernung;
        anzahlGueltigeMessungen++;
      }
    }
    if (anzahlGueltigeMessungen > 0) {
      return gemittelteEntfernung / (double) anzahlGueltigeMessungen;
    } else {
      return -1;
    }
  }

  @Override
  public void shutdown() {
    // Shutdown
    trigPin.low();
    GpioFactory.getInstance().shutdown();
  }

  /**
   * Sendet das Signal an den Sensor, dass eine Messung gestartet werden soll.
   */
  private void triggerSenden() {
    try {
      // Send trigger to start measurement
      trigPin.low();
      Thread.sleep(2000);
      trigPin.high();
      Thread.sleep(0, 10000);
      trigPin.low();

    } catch (InterruptedException ex) {
      System.out.println("Fehler beim Senden des Triggers");
    }
  }

  /**
   * Empfangen eines Signals.
   *
   * @return Dauer des Signals in Sekunden.
   */
  private double signalEmpfangen() {
    double start = 0d, end = 0d;
    // Wait for the signal to return
    while (echoPin.isLow()) {
      start = System.nanoTime();
    }
    // Wait for signal to finish
    while (echoPin.isHigh()) {
      end = System.nanoTime();
    }

    double dauer = (end - start) / 1e9;
    return dauer;
  }

  private double entfernungBerechnen(double signalDauer) {
    // Compute distance
    double entfernung = signalDauer * DIST_FACT;

    // Print distance to console
    if (entfernung < MIN_DIST) {
      return -1;
    } else {
      return entfernung;
    }
  }

  @Override
  public String getModel() {
    return SensorModel.HC_SR04.toString();
  }

  @Override
  public String getIdentifier() {
    Logger.getInstance().error(
        "Method getIdentifier(); not implemented in " + getClass().getName());
    return null;
  }

  @Override
  public String getLocation() {
    Logger.getInstance().error(
        "Method getLocation(); not implemented in " + getClass().getName());
    return null;
  }

  @Override
  public double getValue() {
    return getDistance();
  }

  @Override
  public SensorType getType() {
    return SensorType.DISTANCE;
  }

  @Override
  public String toJson() {
    Logger.getInstance().error(
        "Method toJson(); not implemented in " + getClass().getName());
    return null;
  }

  /**
   * Programmeinstieg.
   */
  public static void main(String[] args) {
    UltraSonicSensor ultraschallSensor =
        new UltraSonicSensor(RaspiPin.GPIO_05, RaspiPin.GPIO_06);
    double entfernung = ultraschallSensor.getDistance();
    System.out.format("Entfernung: %.1f cm\n", entfernung);
    ultraschallSensor.shutdown();
  }

}

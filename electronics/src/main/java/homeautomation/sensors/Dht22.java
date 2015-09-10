package homeautomation.sensors;

import homeautomation.JsonConstants;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 * Representation of a DHT22 temperature and humidity sensor.
 * 
 * @author Philipp Jenke
 *
 */
public class Dht22 implements ITemperatureSensor, IHumiditySensor {

  /**
   * The dummy mode is used for debugging where the physical sensor is not
   * available.
   */
  public enum Mode {
    REAL, DUMMY
  }

  /**
   * Location of the sensor.
   */
  private final String location;

  /**
   * Pin in the RaspPi GPIO board.
   */
  private final int pinNumber;

  /**
   * The DHT22 can be used as temperature and as humidity sensor.
   */
  public SensorType type;

  /**
   * See @Mode.
   */
  private Mode mode = Mode.REAL;

  /**
   * Real sensor native object.
   */
  private homeautomation.swig.Dht22 swigDht22 = null;

  /**
   * Dummy sensor native object.
   */
  private homeautomation.swig.Dht22Dummy swigDht22Dummy = null;

  /**
   * Constructor.
   * 
   * @param type
   *          Sensor type.
   * @param location
   *          Where the sensor is located.
   * @param pinNumber
   *          GPIO pin at the Raspberry Pi.
   * @param mode
   *          Debug or real.
   */
  public Dht22(SensorType type, String location, int pinNumber, Mode mode) {
    this.location = location;
    this.pinNumber = pinNumber;
    this.mode = mode;
    this.type = type;
    if (mode == Mode.REAL) {
      swigDht22 = new homeautomation.swig.Dht22(pinNumber);
    } else {
      swigDht22Dummy = new homeautomation.swig.Dht22Dummy(pinNumber);
    }
  }

  @Override
  public double getHumidity() {
    double value = Double.NEGATIVE_INFINITY;
    if (mode == Mode.REAL) {
      value = swigDht22.getHumidity();
    } else {
      value = swigDht22Dummy.getHumidity();
    }
    if (value < -1000) {
      value = Double.NEGATIVE_INFINITY;
    }
    return value;
  }

  @Override
  public double getTemperature() {
    double value = Double.NEGATIVE_INFINITY;
    if (mode == Mode.REAL) {
      value = swigDht22.getTemperature();
    } else {
      value = swigDht22Dummy.getTemperature();
    }
    if (value < -1000) {
      value = Double.NEGATIVE_INFINITY;
    }
    return value;
  }

  @Override
  public String getModel() {
    String model = "DHT22";
    if (mode == Mode.DUMMY) {
      model += "Dummy";
    }
    return model;
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public String getIdentifier() {
    return "" + pinNumber;
  }

  @Override
  public double getValue() {
    switch (type) {
      case TEMPERATURE:
        return getTemperature();
      case HUMIDITY:
        return getHumidity();
      default:
        return Double.NEGATIVE_INFINITY;
    }
  }

  @Override
  public SensorType getType() {
    return type;
  }

  @Override
  public String toString() {
    return getModel() + " (" + getIdentifier() + ", " + mode + "): "
        + getType();
  }

  @Override
  public String toJson() {
    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
    jsonBuilder.add(JsonConstants.MODEL, getModel().toString());
    jsonBuilder.add(JsonConstants.TYPE, getType().toString());
    jsonBuilder.add(JsonConstants.IDENTIFIER, getIdentifier().toString());
    jsonBuilder.add(JsonConstants.LOCATION, getLocation().toString());
    jsonBuilder.add(JsonConstants.VALUE, String.format("%.2f", getValue())
        .replace(",", "."));
    return jsonBuilder.build().toString();
  }
}

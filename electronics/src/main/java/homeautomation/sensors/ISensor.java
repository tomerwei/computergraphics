package homeautomation.sensors;

/**
 * Generic interface for all sensors.
 * 
 * @author Philipp Jenke
 *
 */
public interface ISensor {

  public enum SensorType {
    TEMPERATURE, HUMIDITY, DISTANCE
  }

  public enum SensorModel {
    DHT22,
    DHT22Dummy,
    TinkerforgeTemperature,
    TinkerforgeTemperatureDummy,
    TinkerforgeHumidity,
    TinkerforgeHumidityDummy,
    DS18B20,
    HC_SR04

  }

  /**
   * Return a string representing the sensor type (e.g. DHT22).
   * 
   * @return String description.
   */
  public String getModel();

  /**
   * Getter.
   * 
   * @return Specification string for the specific instance of the model (e.g.
   *         GPIO 7).
   */
  public String getIdentifier();

  /**
   * Getter.
   * 
   * @return Current location of the sensor.
   */
  public String getLocation();

  /**
   * Getter.
   * 
   * @return The (current) measurement value of the sensor. Return
   *         Double.NEGATIVE_INFINITY if the sensor cannot be read.
   */
  public double getValue();

  /**
   * Getter.
   * 
   * @return Type of the sensor.
   */
  public SensorType getType();

  /**
   * Getter.
   * 
   * @return Convert sensor to JSON format.
   */
  public String toJson();
}

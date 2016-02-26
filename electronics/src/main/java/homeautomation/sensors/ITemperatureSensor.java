package homeautomation.sensors;

/**
 * Parent interface for a temperature sensor.
 * 
 * @author Philipp Jenke
 *
 */
public interface ITemperatureSensor extends ISensor {
  /**
   * Return the current temperature. Return Double.NEGATIVE_INFINITY if no value
   * is available.
   * 
   * @return Temperature value.
   */
  public double getTemperature();
}

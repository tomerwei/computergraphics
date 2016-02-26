package homeautomation.sensors;

/**
 * Parent interface for a humidity sensor.
 * 
 * @author Philipp Jenke
 *
 */
public interface IHumiditySensor extends ISensor {
  /**
   * Getter.
   * 
   * @return The current humidity. Return -DOUBLE.NEGATIVE_INFINITY if no value
   *         is available.
   */
  public double getHumidity();
}

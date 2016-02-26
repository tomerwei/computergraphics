package homeautomation.sensors;

/**
 * Gemeinsame Schnittstelle für alle Entfernungssensoren basierend auf
 * Ultraschall.
 *
 * @author Philipp Jenke
 */
public interface IDistanceSensor extends ISensor {

  /**
   * Return the distance
   *
   * @return Distance in cm. -1, in case of an error.
   */
  public double getDistance();

  /**
   * Cleanup.
   */
  public void shutdown();
}

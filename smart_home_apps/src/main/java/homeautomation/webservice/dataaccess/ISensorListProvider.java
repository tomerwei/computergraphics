package homeautomation.webservice.dataaccess;

import java.util.List;

import homeautomation.sensors.SensorInformation;

/**
 * Provides a list of sensors (e.g. from the RaspiServer or directly from the
 * database).
 *
 */
public interface ISensorListProvider {

  /**
   * Return a list of sensors with database keys and current value.
   * 
   * @return List of sensors.
   */
  public List<SensorInformation> getSensorList();
}

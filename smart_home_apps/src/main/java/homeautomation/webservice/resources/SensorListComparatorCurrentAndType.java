package homeautomation.webservice.resources;

import homeautomation.sensors.SensorInformation;

import java.util.Comparator;

/**
 * Compares two sensor infos. Criterion 1: last measurement is after 24h ago.
 * Criterion 2: Sort by type
 * 
 * @author Philipp Jenke
 *
 */
public class SensorListComparatorCurrentAndType implements
    Comparator<SensorInformation> {

  @Override
  public int compare(SensorInformation sensorInfo1,
      SensorInformation sensorInfo2) {
    boolean isCurrent1 = sensorInfo1.isCurrent();
    boolean isCurrent2 = sensorInfo2.isCurrent();
    if (isCurrent1 && !isCurrent2) {
      // First is current, second is not.
      return -1;
    } else if (!isCurrent1 && isCurrent2) {
      // Second is current, first is not.
      return 1;
    } else {
      // Compare type (temperature first)
      return -sensorInfo1.getType().toString()
          .compareTo(sensorInfo2.getType().toString());
    }
  }
}

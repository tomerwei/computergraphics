package homeautomation.database;

import java.sql.Timestamp;

/**
 * Class to represent a single measurement.
 * 
 * @author Philipp Jenke
 *
 */
public class Measurement {
  public Timestamp timestamp;
  public double value;

  /**
   * Constructor.
   * 
   * @param timestamp
   *          Measurement timestamp.
   * @param value
   *          Measurement value.
   */
  public Measurement(Timestamp timestamp, double value) {
    this.timestamp = timestamp;
    this.value = value;
  }

  @Override
  public String toString() {
    return value + "/" + timestamp;
  }
}

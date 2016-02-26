package homeautomation.apps;

import java.util.ArrayList;
import java.util.List;

import homeautomation.sensors.ISensor;

/**
 * Test the sensors.
 * 
 * @author Philipp Jenke
 *
 */
public class SensorTest implements IHomeautomationApp {

  private List<ISensor> sensors = new ArrayList<ISensor>();
  private List<String> libraries = new ArrayList<String>();

  /**
   * Constructor.
   */
  public SensorTest() {
  }

  /**
   * Setter.
   * 
   * @param sensor
   *          Sensor instance to be added.
   */
  public void addSensor(ISensor sensor) {
    sensors.add(sensor);
  }

  /**
   * Setter.
   * 
   * @param library
   *          Library  (filename) to be added.
   */
  public void addLibrary(String library) {
    libraries.add(library);
  }

  @Override
  public String toString() {
    String returnStatement = "*** RaspPiServer ***\n";
    returnStatement += "  #sensors: " + sensors.size() + "\n";
    for (ISensor sensor : sensors) {
      returnStatement += "      " + sensor + "\n";
    }
    returnStatement += "  #libraries: " + libraries.size() + "\n";
    for (String lib : libraries) {
      returnStatement += "      " + lib + "\n";
    }
    return returnStatement;
  }

  @Override
  public void init() {
    for (ISensor sensor : sensors) {
      System.out.println(sensor);
      System.out.println("Value: " + sensor.getValue());
    }
  }

  @Override
  public void shutdown() {
  }

  @Override
  public void command(String line) {
  }
}

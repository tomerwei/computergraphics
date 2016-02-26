package homeautomation.apps.todo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import homeautomation.data.DoubleDataItem;
import homeautomation.data.DoubleDataset;
import homeautomation.sensors.DS18B20TemperatureSensor;
import homeautomation.sensors.DS18B20TemperatureSensorStub;

/**
 * Simple test for the temperature sensor.
 * 
 * @author Philipp Jenke
 *
 */
public class TestTemperature {
  /**
   * Timer object for the measurements.
   */
  private Timer timer;

  /**
   * Sensor and corresponding datasets.
   */
  private List<DS18B20TemperatureSensor> sensors =
      new ArrayList<DS18B20TemperatureSensor>();
  private List<DoubleDataset> datasets = new ArrayList<DoubleDataset>();

  /**
   * Constructor.
   */
  public TestTemperature() {
  }

  /**
   * Setup sensors and corresponding datasets.
   */
  public void setupSensors() {

    // Real sensors
    // sensors.add(new RaspPiTemperatureSensor("28-00000607f8e7"));
    // sensors.add(new RaspPiTemperatureSensor("28-000006098585"));
    // sensors.add(new RaspPiTemperatureSensor("28-0000060a06b2"));
    // sensors.add(new RaspPiTemperatureSensor("28-0000060a778b"));
    // sensors.add(new RaspPiTemperatureSensor("28-0000060aafca"));

    // Dummy sensor
    sensors.add(new DS18B20TemperatureSensorStub());
    sensors.add(new DS18B20TemperatureSensorStub());
    sensors.add(new DS18B20TemperatureSensorStub());

    // Create a dataset for each sensor
    for (int i = 0; i < sensors.size(); i++) {
      DoubleDataset dataset = new DoubleDataset();
      datasets.add(dataset);
    }
  }

  /**
   * Start continuous reading of sensors.
   */
  public void startMeasurements() {
    // Read all sensors
    timer = new Timer();
    int timeout = 1000;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.out.println("*** Next measurement ***");
        for (int i = 0; i < sensors.size(); i++) {
          DS18B20TemperatureSensor sensor = sensors.get(i);
          double value = sensor.getValue();
          System.out.println(String.format("%s Temp: %.2f",
              sensor.getIdentifier(), value));
          DoubleDataset dataset = datasets.get(i);
          dataset.addDataItem(new DoubleDataItem(value, Calendar.getInstance()
              .getTime()));
        }
      }
    }, timeout, timeout);
  }

  public int getNumberOfDatasets() {
    return datasets.size();
  }

  public DoubleDataset getDataset(int index) {
    return datasets.get(index);
  }

  /**
   * Program entry point.
   * 
   * @param args
   *          Command line parameters.
   */
  public static void main(String[] args) {
    TestTemperature testTemp = new TestTemperature();
    testTemp.setupSensors();
    testTemp.startMeasurements();
  }
}

package homeautomation.apps;

import homeautomation.actors.IActor;
import homeautomation.database.DatabaseAccess;
import homeautomation.foundation.Logger;
import homeautomation.sensors.ISensor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Temperature and Humidity Measurement in the cellar.
 */
public class RasPiServer extends Timer implements IHomeautomationApp {
  /**
   * Sensors.
   */
  private List<ISensor> sensors = new ArrayList<ISensor>();

  /**
   * Sensors.
   */
  private List<IActor> actors = new ArrayList<IActor>();

  /**
   * Database access.
   */
  private final DatabaseAccess databaseAccess;

  /**
   * JNI libraries to be loaded.
   */
  private List<String> libraries = new ArrayList<String>();

  /**
   * Target for the output.
   */
  public enum ExportTarget {
    CONSOLE, DATABASE
  }

  /**
   * See @ExportTarget.
   */
  private ExportTarget exportTarget = ExportTarget.DATABASE;

  /**
   * Timer interval between two consecutive measurements.
   */
  private final int timerInterval;

  /**
   * Constructor.
   * 
   * @param timerInterval
   *          The sensors are read every this seconds.
   * @param databaseAccess
   *          Interface to the database.
   * @param exportTarget
   *          console or database.
   */
  public RasPiServer(int timerInterval, DatabaseAccess databaseAccess,
      ExportTarget exportTarget) {
    this.timerInterval = timerInterval;
    this.databaseAccess = databaseAccess;
    this.exportTarget = exportTarget;
    schedule(new RemindTask(), timerInterval * 1000, timerInterval * 1000);
    System.out
        .println("Started timer tick each " + timerInterval + " seconds.");
  }

  /**
   * Timer task for the iterative measurement.
   * 
   * @author Philipp Jenke
   *
   */
  class RemindTask extends TimerTask {
    public void run() {
      measure();
    }
  }

  /**
   * Add additional libraries.
   * 
   * @param library
   *          Name of the library file.
   */
  public void addLibrary(String library) {
    libraries.add(library);
  }

  /**
   * Load all system libraries.
   */
  public void loadLibraries() {
    for (String lib : libraries) {
      System.out.println("Loading lib " + lib);
      System.load(lib);
    }
  }

  /**
   * Read all sensors, write output.
   */
  public void measure() {
    System.out.print("Measurement @" + Calendar.getInstance().getTime()
        + " ... \n");

    if (sensors.size() > 0) {

      if (exportTarget == ExportTarget.DATABASE) {
        databaseAccess.connect();
      }
      for (ISensor sensor : sensors) {
        double value = sensor.getValue();
        // Ignore invalid values
        if (value > -1000) {
          writeMeasurement(sensor, Calendar.getInstance().getTime().getTime(),
              value);
        }
      }
      if (databaseAccess.isConnected()) {
        databaseAccess.disconnect();
      }
    }
    System.out.println("done!");
  }

  /**
   * Write measurements to output (e.g. database).
   * 
   * @param sensor
   *          The value is taken from this sensor.
   * @param time
   *          Timestamp.
   * @param value
   *          Sensor value.
   */
  private void writeMeasurement(ISensor sensor, long time, double value) {

    if (value < -1000) {
      // Do not save invalid values.
      return;
    }

    String title = sensor.getModel() + " (" + sensor.getIdentifier() + ")";
    if (exportTarget == ExportTarget.DATABASE) {
      int sensorKey = databaseAccess.getSensorKey(sensor);
      if (sensorKey >= 0) {
        databaseAccess.insertSensorMeasurement(time, value, sensorKey);
        Logger.getInstance().debug(
            "Insert success with sensor key " + sensorKey);
      } else {
        Logger.getInstance().debug("Could not find Sensor ID.");
      }
    } else {
      System.out.println(title + ", " + new Date(time) + ", " + value + ", "
          + sensor.getLocation() + ", " + sensor.getType());
    }
  }

  @Override
  public void shutdown() {
  }

  /**
   * @param sensor
   *          Sensor to be added.
   */
  public void addSensor(ISensor sensor) {
    sensors.add(sensor);
  }

  @Override
  public String toString() {
    String returnStatement = "*** RaspPiServer ***\n";
    returnStatement += "  exportTarget: " + exportTarget + "\n";
    returnStatement += "  timerInterval: " + timerInterval + "\n";
    returnStatement += "  #libs: " + libraries.size() + "\n";
    for (String lib : libraries) {
      returnStatement += "      " + lib + "\n";
    }
    returnStatement += "  #sensors: " + sensors.size() + "\n";
    for (ISensor sensor : sensors) {
      returnStatement += "      " + sensor + "\n";
    }
    returnStatement += "  #actors: " + actors.size() + "\n";
    for (IActor actor : actors) {
      returnStatement += "      " + actor + "\n";
    }
    returnStatement += databaseAccess;
    return returnStatement;
  }

  @Override
  public void init() {
    measure();
  }

  /**
   * Getter.
   * 
   * @return Numer of sensors.
   */
  public int getNumberOfSensors() {
    return sensors.size();
  }

  /**
   * Getter.
   * 
   * @param index
   *          Index of the sensor.
   * 
   * @return Sensor at the given index.
   */
  public ISensor getSensor(int index) {
    if (index < 0 || index >= sensors.size()) {
      Logger.getInstance().error("Invalid sensor index: " + index);
      return null;
    }

    return sensors.get(index);
  }

  /**
   * Setter.
   * 
   * @param actor
   *          Actor to be added.
   */
  public void addActor(IActor actor) {
    actors.add(actor);
  }

  /**
   * Getter.
   * 
   * @param index
   *          Index of the actor to be accessed.
   * 
   * @return Actor at the given index.
   */
  public IActor getActor(int index) {
    return actors.get(index);
  }

  /**
   * Getter.
   * 
   * @return Number of actors.
   */
  public int getNumberOfActors() {
    return actors.size();
  }

  @Override
  public void command(String line) {
    // Ignore all.
  }

  /**
   * Getter.
   * 
   * @return List of actors as JSON array.
   */
  public String getJsonActorsList() {
    String json = "[";
    boolean isFirst = true;
    for (IActor actor : actors) {
      if (isFirst) {
        isFirst = false;
      } else {
        json += ",\n";
      }
      json += actor.toJson();
    }
    json += "]";
    return json;
  }

  /**
   * Getter.
   * 
   * @return List of sensors as JSON array.
   */
  public String getJsonSensorsList() {
    String json = "[";
    boolean isFirst = true;
    for (ISensor sensor : sensors) {
      if (isFirst) {
        isFirst = false;
      } else {
        json += ",\n";
      }
      json += sensor.toJson();
    }
    json += "]";
    return json;
  }
}
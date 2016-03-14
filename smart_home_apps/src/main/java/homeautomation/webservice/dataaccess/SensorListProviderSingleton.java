package homeautomation.webservice.dataaccess;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import homeautomation.apps.RasPiServer;
import homeautomation.database.DatabaseAccess;
import homeautomation.database.Measurement;
import cgresearch.core.logging.Logger;
import homeautomation.sensors.ISensor;
import homeautomation.sensors.SensorInformation;

/**
 * Provider implementation for a list of sensors.
 * 
 * @author Philipp Jenke
 *
 */
public class SensorListProviderSingleton implements ISensorListProvider {

  public enum DataSource {
    RASPI_SERVER, DATABASE
  }

  /**
   * Sensor list provider.
   */
  private static SensorListProviderSingleton instance;

  /**
   * Constructor.
   */
  private SensorListProviderSingleton() {
  }

  /**
   * Data source.
   */
  private DataSource dataSource = DataSource.DATABASE;

  /**
   * Access to the singleton instance.
   * 
   * @return Singleton instance.
   */
  public static SensorListProviderSingleton getInstance() {
    if (instance == null) {
      instance = new SensorListProviderSingleton();
    }
    return instance;
  }

  @Override
  public List<SensorInformation> getSensorList() {
    switch (dataSource) {
      case RASPI_SERVER:
        return getSensorListRaspiServer();
      case DATABASE:
        return getSensorListDatabase();
      default:
        return new ArrayList<SensorInformation>();
    }
  }

  /**
   * Create a sensor list with the raspi server information.
   * 
   * @return List of information objects for the sensors.
   */
  private List<SensorInformation> getSensorListRaspiServer() {
    List<SensorInformation> sensorList = new ArrayList<SensorInformation>();

    RasPiServer raspiServer =
        RaspiServerAccessSingleton.getInstance().getRaspiServer();

    if (raspiServer == null) {
      return sensorList;
    }

    DatabaseAccess databaseAccess =
        DatabaseAccessSingleton.getInstance().getDatabaseAccess();
    if (databaseAccess == null) {
      Logger.getInstance().error("Failed to access database access object.");
      return sensorList;
    }
    databaseAccess.connect();

    for (int i = 0; i < raspiServer.getNumberOfSensors(); i++) {
      ISensor sensor = raspiServer.getSensor(i);
      // Get sensor
      SensorInformation sensorInfo = new SensorInformation(sensor);

      // Get database key
      int key = databaseAccess.getSensorKey(sensor);
      sensorInfo.setDatabaseKey(key);

      // Get current value
      sensorInfo.setLastMeasurement(new Measurement(new Timestamp(
          GregorianCalendar.getInstance().getTime().getTime()), sensor
          .getValue()));

      sensorList.add(sensorInfo);
    }

    databaseAccess.disconnect();

    return sensorList;
  }

  /**
   * Create a sensor list with the database information.
   * 
   * @return List of information objects for the sensors from the database.
   */
  private List<SensorInformation> getSensorListDatabase() {
    DatabaseAccess databaseAccess =
        DatabaseAccessSingleton.getInstance().getDatabaseAccess();
    if (databaseAccess == null) {
      Logger.getInstance().error("Failed to access database access object.");
      return new ArrayList<SensorInformation>();
    }
    databaseAccess.connect();

    List<SensorInformation> sensorList = databaseAccess.getSensors();
    if (sensorList == null) {
      return new ArrayList<SensorInformation>();
    }

    for (SensorInformation sensorInfo : sensorList) {
      sensorInfo.lastMeasurement =
          databaseAccess.getLastMeasurement(sensorInfo.getDatabaseKey());
    }

    databaseAccess.disconnect();

    return sensorList;
  }
}

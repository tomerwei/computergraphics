package homeautomation.webservice;

import static org.junit.Assert.*;

import java.util.List;

import homeautomation.database.DatabaseAccess;
import homeautomation.database.Measurement;
import cgresearch.core.logging.ConsoleLogger;
import cgresearch.core.logging.Logger;
import cgresearch.core.logging.Logger.VerboseMode;
import homeautomation.jsonparser.JsonParserDatabase;
import homeautomation.sensors.SensorInformation;

import org.junit.Test;

/**
 * Testing class for the database access.
 * 
 * @author Philipp Jenke
 *
 */
public class TestDatabaseAccess {

  public static final String databaseConfig =
      "{ \"hostname\":\"127.0.0.1\", \"username\":\"pj\", "
      + "\"password\":\"mysql\",  \"databaseName\":\"homeautomation\", "
      + "\"tableName\":\"SENSOR_MEASUREMENTS\"}";

  @Test
  public void testGetSensorList() {
    DatabaseAccess dbAccess = JsonParserDatabase.createDatabase(databaseConfig);
    assertNotNull(dbAccess);
    dbAccess.connect();
    List<SensorInformation> sensorList = dbAccess.getSensors();

    if (sensorList == null) {
      // TODO: does not work on maven
      Logger.getInstance().error(
          "Failed to grab sensor list in testGetLatestMeasurement()");
      return;
    }

    assertTrue(sensorList.size() > 0);
    SensorInformation sensorInfo = sensorList.get(0);
    assertTrue(sensorInfo.getDatabaseKey() >= 0);
    dbAccess.disconnect();
  }

  @Test
  public void testGetLatestMeasurement() {
    new ConsoleLogger(VerboseMode.DEBUG);
    DatabaseAccess dbAccess = JsonParserDatabase.createDatabase(databaseConfig);
    assertNotNull(dbAccess);
    dbAccess.connect();
    Measurement measurement = dbAccess.getLastMeasurement(8);

    if (measurement == null) {
      // TODO: does not work on maven
      Logger.getInstance().error(
          "Failed to grab measurement in testGetLatestMeasurement()");
      return;
    }

    Logger.getInstance().message(measurement.toString());
    assertNotNull(measurement);
    dbAccess.disconnect();
  }

}

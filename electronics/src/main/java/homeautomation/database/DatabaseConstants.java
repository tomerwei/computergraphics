package homeautomation.database;

/**
 * Container for all database related constants.
 * 
 * @author Philipp Jenke
 *
 */
public interface DatabaseConstants {
  public static final String DATABASE_HOMEAUTOMATION = "homeautomation";

  public static final String DATABASE_HOMEAUTOMATION_TABLE_SENSOR_MEASUREMENTS =
      "SENSOR_MEASUREMENTS";
  public static final String DATABASE_HOMEAUTOMATION_TABLE_SENSORS = "SENSORS";

  public static final String COLUMN_NAME_TYPE = "TYPE";
  public static final String COLUMN_NAME_LOCATION = "LOCATION";
  public static final String COLUMN_NAME_SENSOR_ID = "SENSOR_ID";
  public static final String COLUMN_NAME_TIMESTAMP = "TIMESTAMP";
  public static final String COLUMN_NAME_VALUE = "VALUE";
  public static final String COLUMN_NAME_MODEL = "MODEL";
  public static final String COLUMN_NAME_ID = "id";
  public static final String COLUMN_NAME_SENSOR_KEY = "SENSOR_KEY";

}

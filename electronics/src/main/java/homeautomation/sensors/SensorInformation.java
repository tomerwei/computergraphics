package homeautomation.sensors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import homeautomation.database.Measurement;
import homeautomation.JsonConstants;
import homeautomation.sensors.ISensor.SensorType;

/**
 * Helper class to represent sensor information.
 * 
 * @author Philipp Jenke
 *
 */
public class SensorInformation {
  /**
   * Id of the sensor.
   */
  public String identifier;

  /**
   * Sensor location.
   */
  public String location;

  /**
   * Sensor type.
   */
  public SensorType type;

  /**
   * Model identifier.
   */
  public String model;

  /**
   * Key of the sensor in the database.
   */
  public int databaseKey = -1;

  /**
   * Last measurement of the sensor.
   */
  public Measurement lastMeasurement;

  /**
   * Constructor.
   * 
   * @param identifier
   *          Sensor-ID.
   * @param location
   *          Sensor location.
   * @param type
   *          Sensor type.
   * @param model
   *          Sensor model.
   * @param databaseKey
   *          Sensor key in the database.
   */
  public SensorInformation(String identifier, String location, SensorType type, String model, int databaseKey) {
    this.identifier = identifier;
    this.location = location;
    this.type = type;
    this.model = model;
    this.databaseKey = databaseKey;
  }

  /**
   * Copy constructor.
   * 
   * @param sensor
   *          Sensor instance to take the values from.
   */
  public SensorInformation(ISensor sensor) {
    this(sensor.getIdentifier(), sensor.getLocation(), sensor.getType(), sensor.getModel(), -1);
  }

  @Override
  public String toString() {
    return location + ", " + type + " (" + model + ",  " + identifier + ")";
  }

  /**
   * Setter
   * 
   * @param measurement
   *          Latest (most current) measurement.
   */
  public void setLastMeasurement(Measurement measurement) {
    lastMeasurement = measurement;

  }

  /**
   * Setter.
   * 
   * @param key
   *          Database key.
   */
  public void setDatabaseKey(int key) {
    databaseKey = key;
  }

  /**
   * Getter
   * 
   * @return Unique identifier.
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * Getter.
   * 
   * @return Location.
   */
  public String getLocation() {
    return location;
  }

  /**
   * Getter.
   * 
   * @return Sensor type.
   */
  public SensorType getType() {
    return type;
  }

  /**
   * Getter.
   * 
   * @return Sensor model.
   */
  public String getModel() {
    return model;
  }

  /**
   * Getter.
   * 
   * @return Sensor key in the database.
   */
  public int getDatabaseKey() {
    return databaseKey;
  }

  /**
   * Getter.
   * 
   * @return Latest (most current) measurement.
   */
  public Measurement getLastMeasurement() {
    return lastMeasurement;
  }

  /**
   * Getter.
   * 
   * @return True if the timestamp of the last measurement is younger than 24h.
   */
  public boolean isCurrent() {
    long day = 24L * 60L * 60L * 1000L;
    return getLastMeasurement().timestamp.after(new Date(Calendar.getInstance().getTime().getTime() - day));
  }

  /**
   * Getter.
   * 
   * @return JSON representation of the sensor.
   */
  public String toJson() {
    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
    jsonBuilder.add(JsonConstants.IDENTIFIER, getIdentifier().toString());
    jsonBuilder.add(JsonConstants.LOCATION, getLocation().toString());
    jsonBuilder.add(JsonConstants.TYPE, getType().toString());
    jsonBuilder.add(JsonConstants.MODEL, getModel().toString());
    jsonBuilder.add(JsonConstants.IS_CURRENT, isCurrent());
    double value = lastMeasurement.value;
    SimpleDateFormat formatterDate = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
    String date = formatterDate.format(new Date(lastMeasurement.timestamp.getTime()));
    String time = formatterTime.format(new Date(lastMeasurement.timestamp.getTime()));
    jsonBuilder.add(JsonConstants.VALUE, String.format("%.2f", value).replace(",", "."));
    jsonBuilder.add(JsonConstants.DATE, date);
    jsonBuilder.add(JsonConstants.TIME, time);
    jsonBuilder.add(JsonConstants.DATABASE_KEY, databaseKey);
    return jsonBuilder.build().toString();
  }
}

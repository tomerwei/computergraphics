package homeautomation.sensors;

import homeautomation.JsonConstants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class DS18B20TemperatureSensor implements ITemperatureSensor {

  /**
   * Unique ID of the sensor.
   */
  private final String id;

  /**
   * Location of the sensor.
   */
  private final String location;

  /**
   * Contructor.
   * 
   * @param id
   *          Sensor id (I2C id).
   * @param location
   *          Location of the sensor.
   */
  public DS18B20TemperatureSensor(String id, String location) {
    this.id = id;
    this.location = location;
  }

  /**
   * Getter.
   * 
   * @return Current value of the sensor.
   */
  public double getValue() {
    return readValueFromFile(getDataFilename());
  }

  /**
   * Getter.
   * 
   * @return Filename of the data file.
   */
  private String getDataFilename() {
    return "/sys/devices/w1_bus_master1/" + id + "/w1_slave";
  }

  /**
   * Read the data entry from the file.
   * 
   * @param filename
   *          Name of the file to read the param from.
   * @return Value.
   */
  private double readValueFromFile(String filename) {
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line = br.readLine();
      while (line != null) {
        if (line.contains("t=")) {
          double value = parseLineForValue(line);
          if (value < 80) {
            return value;
          } else {
            return Double.NEGATIVE_INFINITY;
          }

        }
        line = br.readLine();
      }
    } catch (IOException e) {
      System.out.println("Failed to open file " + filename);
    }
    return Double.NEGATIVE_INFINITY;
  }

  /**
   * Parse for value in line.
   * 
   * @param line
   *          Line to parse.
   * @return Parsed value.
   */
  private double parseLineForValue(String line) {
    String valueString = line.substring(line.indexOf("t=") + 2);
    return Integer.parseInt(valueString) / 1000.0;
  }

  @Override
  public String getModel() {
    return SensorModel.DS18B20.toString();
  }

  @Override
  public String getIdentifier() {
    return id;
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public SensorType getType() {
    return SensorType.TEMPERATURE;
  }

  @Override
  public double getTemperature() {
    return getValue();
  }

  @Override
  public String toString() {
    return getModel() + " (" + getIdentifier() + "): " + getType();
  }

  @Override
  public String toJson() {
    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
    jsonBuilder.add(JsonConstants.MODEL, getModel().toString());
    jsonBuilder.add(JsonConstants.TYPE, getType().toString());
    jsonBuilder.add(JsonConstants.IDENTIFIER, getIdentifier().toString());
    jsonBuilder.add(JsonConstants.LOCATION, getLocation().toString());
    jsonBuilder.add(JsonConstants.VALUE, String.format("%.2f", getValue())
        .replace(",", "."));
    return jsonBuilder.build().toString();
  }
}

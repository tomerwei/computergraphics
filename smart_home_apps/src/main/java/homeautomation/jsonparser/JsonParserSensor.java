package homeautomation.jsonparser;

import homeautomation.JsonConstants;
import homeautomation.platform.Pi4JRaspPiSingleton;
import homeautomation.platform.TinkerforgeSingleton;
import homeautomation.sensors.Dht22;
import homeautomation.sensors.ISensor;
import homeautomation.sensors.DS18B20TemperatureSensor;
import homeautomation.sensors.RaspPiMotionDetectionSensor;
import homeautomation.sensors.RaspPiMotionDetectionSensor.Mode;
import homeautomation.sensors.TinkerforgeHumiditySensor;
import homeautomation.sensors.TinkerforgeTemperatureSensor;
import homeautomation.sensors.ISensor.SensorType;

import java.util.ArrayList;
import java.util.List;

import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

/**
 * Parsing of JSON files for sensor information.
 * 
 * @author Philipp Jenke
 *
 */
public class JsonParserSensor {

  /**
   * Parse for the list of sensors.
   * 
   * @param parser
   *          Parser at the current position.
   * 
   * @return Created list of sensors.
   */
  public static List<ISensor> parseSensors(JsonParser parser) {
    List<ISensor> sensors = new ArrayList<ISensor>();
    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.START_OBJECT) {
        ISensor sensor = parseSensor(parser);
        if (sensor != null) {
          sensors.add(sensor);
        }
      }
      if (event == Event.END_ARRAY) {
        return sensors;
      }
    }
    return null;
  }

  /**
   * Parse for a sensor object.
   * 
   * @param parser
   *          Parser at the current position.
   * 
   * @return Created sensor instance.
   */
  private static ISensor parseSensor(JsonParser parser) {
    String type = null;
    String location = null;
    String model = null;
    String identifier = null;
    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.TYPE)) {
        event = parser.next();
        type = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.LOCATION)) {
        event = parser.next();
        location = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.MODEL)) {
        event = parser.next();
        model = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.IDENTIFIER)) {
        event = parser.next();
        identifier = parser.getString();
      } else if (event == Event.END_OBJECT) {
        return createSensor(model, identifier, type, location);
      }
    }
    return null;
  }

  /**
   * Create a sensor object from the config information.
   * 
   * @param model
   *          Sensor model.
   * @param identifier
   *          Sensor GPIO port.
   * @param type
   *          Sensor type.
   * @param location
   *          Current location.
   * @return Created sensor instance.
   */
  private static ISensor createSensor(String model, String identifier,
      String type, String location) {
    SensorType sensorType = SensorType.valueOf(type);
    if (model.equals(ISensor.SensorModel.DHT22.toString())) {
      return new Dht22(sensorType, location, Integer.parseInt(identifier),
          Dht22.Mode.REAL);
    } else if (model.equals(ISensor.SensorModel.DHT22Dummy.toString())) {
      return new Dht22(sensorType, location, Integer.parseInt(identifier),
          Dht22.Mode.DUMMY);
    } else if (model.equals(ISensor.SensorModel.TinkerforgeTemperature
        .toString())) {
      return new TinkerforgeTemperatureSensor(location, identifier,
          TinkerforgeSingleton.getInstance().getMasterBrick());
    } else if (model.equals(ISensor.SensorModel.TinkerforgeHumidity.toString())) {
      return new TinkerforgeHumiditySensor(location, identifier,
          TinkerforgeSingleton.getInstance().getMasterBrick());
    } else if (model.equals(ISensor.SensorModel.DS18B20.toString())) {
      return new DS18B20TemperatureSensor(identifier, location);
    } else {
      return null;
    }
  }

  /**
   * Parse for a @RaspPiMotionDetectionSensor.
   * 
   * @param parser Parser at the current position.
   * @return Created sensor instance.
   */
  public static RaspPiMotionDetectionSensor parseMotionDetectionSensor(
      JsonParser parser) {
    String identifier = null;
    String mode = null;
    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.IDENTIFIER)) {
        event = parser.next();
        identifier = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.MODE)) {
        event = parser.next();
        mode = parser.getString();
      } else if (event == Event.END_OBJECT) {
        return createMotionDetectionSensor(mode, identifier);
      }
    }
    return null;
  }

  /**
   * Create a motion detector sensor instance.
   * 
   * @param mode
   *          Dummy or real
   * @param identifier
   *          GPIO pin index.
   * @return Created sensor instance.
   */
  private static RaspPiMotionDetectionSensor createMotionDetectionSensor(
      String mode, String identifier) {
    return new RaspPiMotionDetectionSensor(Mode.valueOf(mode),
        Pi4JRaspPiSingleton.getGpioPin4Identifier(identifier),
        Pi4JRaspPiSingleton.getInstance().getRaspPi());
  }
}

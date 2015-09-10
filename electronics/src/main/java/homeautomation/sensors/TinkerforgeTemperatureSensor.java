package homeautomation.sensors;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.tinkerforge.BrickletTemperature;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import homeautomation.JsonConstants;
import homeautomation.modules.TinkerforgeMasterBrick;
import homeautomation.modules.TinkerforgeModule;

/**
 * Wrapper for a Tinkerforge temperature sensor.
 * 
 * @author Philipp Jenke
 *
 */
public class TinkerforgeTemperatureSensor extends TinkerforgeModule implements
    ITemperatureSensor {
  /**
   * Tinkerforge object.
   */
  private BrickletTemperature temp = null;

  /**
   * Location of the sensor.
   */
  private final String location;

  /**
   * Constructor.
   * 
   * @param location
   *          Current location of the sensor.
   * @param uid
   *          Tinkerforge UID.
   * @param masterBrick
   *          The sensor must be attached to this master brick.
   */
  public TinkerforgeTemperatureSensor(String location, String uid,
      TinkerforgeMasterBrick masterBrick) {
    super(uid, masterBrick);
    this.location = location;
    temp =
        new BrickletTemperature(uid, masterBrick.getIpConnection()
            .getIpConnection());
  }

  /**
   * Getter
   * 
   * @return Temperature value. Returns Double.NEGATIVE_INFINITY on error.
   */
  public double getTemperature() {
    try {
      return temp.getTemperature() / 100.0;
    } catch (TimeoutException e) {
      System.out.println("Failed to read from bricklet");
      return Double.NEGATIVE_INFINITY;
    } catch (NotConnectedException e) {
      System.out.println("Failed to read from bricklet");
      return Double.NEGATIVE_INFINITY;
    }
  }

  @Override
  public String toString() {
    return getModel() + " (" + getIdentifier() + "): " + getType();
  }

  @Override
  public void shutdown() {
    System.out.println("Shutting down temperature sensor.");
  }

  @Override
  public String getModel() {
    return "Tinkerforge Temperature bricklet";
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public String getIdentifier() {
    return uid;
  }

  @Override
  public double getValue() {
    return getTemperature();
  }

  @Override
  public SensorType getType() {
    return SensorType.TEMPERATURE;
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

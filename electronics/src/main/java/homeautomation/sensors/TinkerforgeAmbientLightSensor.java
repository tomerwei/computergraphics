package homeautomation.sensors;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import homeautomation.modules.TinkerforgeMasterBrick;
import homeautomation.modules.TinkerforgeModule;

/**
 * Wrapper for a Tinkerforge temperature sensor.
 * 
 * @author Philipp Jenke
 *
 */
public class TinkerforgeAmbientLightSensor extends TinkerforgeModule {
  /**
   * Tinkerforge object.
   */
  private BrickletAmbientLight amb = null;

  /**
   * Constructor.
   * 
   * @param uid
   *          Tinkerforge UID.
   * @param masterBrick
   *          The sensor must be attached to this brick.
   */
  public TinkerforgeAmbientLightSensor(String uid,
      TinkerforgeMasterBrick masterBrick) {
    super(uid, masterBrick);
    amb =
        new BrickletAmbientLight(uid, masterBrick.getIpConnection()
            .getIpConnection());
  }

  /**
   * Getter for the illuminance value. Returns Double.NEGATIVE_INFINITY on
   * error.
   * 
   * @return Current illuminance value.
   */
  public double getIlluminance() {
    try {
      return amb.getIlluminance();
    } catch (TimeoutException e) {
      e.printStackTrace();
    } catch (NotConnectedException e) {
      e.printStackTrace();
    }
    return Double.NEGATIVE_INFINITY;
  }

  @Override
  public String toString() {
    return String.format("Humidity: %.2f", getIlluminance()).replace(',', '.');
  }

  @Override
  public void shutdown() {
    System.out.println("Shutting down ambient light sensor.");
  }
}

package homeautomation.sensors;

import java.util.function.Consumer;

import com.tinkerforge.BrickletLinearPoti;
import com.tinkerforge.BrickletLinearPoti.PositionListener;

import cgresearch.core.logging.Logger;

import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * Controll a linear poti.
 * 
 * @author Philipp Jenke
 *
 */
public class TinkerforgeLinearPoti {

  /**
   * Tinkerforge object.
   */
  private BrickletLinearPoti poti;

  /**
   * Current position of the Poti.
   */
  private int position = -1;

  public TinkerforgeLinearPoti(String uid, IPConnection connection, Consumer<Integer> positionChangedConsumer) {
    poti = new BrickletLinearPoti(uid, connection);
    poti.addPositionListener(new PositionListener() {
      @Override
      public void position(int position) {
        positionChangedConsumer.accept(position);
        setPosition(position);
      }
    });
    try {
      poti.setPositionCallbackPeriod(10);
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().message("Failed to set position callback period.");
    }
  }

  private void setPosition(int position) {
    this.position = position;
  }

  public int getPosition() {
    return position;
  }

}

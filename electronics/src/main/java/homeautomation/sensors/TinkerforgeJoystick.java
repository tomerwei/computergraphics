package homeautomation.sensors;

import java.util.function.Consumer;

import com.tinkerforge.BrickletJoystick;
import com.tinkerforge.BrickletJoystick.PositionListener;

import cgresearch.core.logging.Logger;

import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * Tinkerforge joystick handler.
 * 
 * @author Philipp Jenke
 *
 */
public class TinkerforgeJoystick {
  /**
   * Tinkerforge object.
   */
  private BrickletJoystick joystick;

  /**
   * Representation of joystick position.
   */
  public class JoystickPosition {
    public int x;
    public int y;

    public JoystickPosition(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public void copy(JoystickPosition other) {
      this.x = other.x;
      this.y = other.y;
    }

    @Override
    public String toString() {
      return x + "/" + y;
    }
  };

  /**
   * Current position of the joystick.
   */
  private JoystickPosition position = new JoystickPosition(0, 0);

  public TinkerforgeJoystick(String uid, IPConnection connection, Consumer<JoystickPosition> positionChangedConsumer) {
    joystick = new BrickletJoystick(uid, connection);
    joystick.addPositionListener(new PositionListener() {
      @Override
      public void position(short x, short y) {
        positionChangedConsumer.accept(new JoystickPosition(x, y));
        setPosition(x, y);
      }
    });
    try {
      joystick.setPositionCallbackPeriod(200);
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().message("Failed to set position callback period.");
    }
  }

  private void setPosition(int x, int y) {
    this.position.x = x;
    this.position.y = y;
  }

  public JoystickPosition getPosition() {
    return position;
  }
}

package homeautomation.actors;

import homeautomation.modules.TinkerforgeMasterBrick;
import homeautomation.modules.TinkerforgeModule;

import com.tinkerforge.BrickletLCD20x4.ButtonPressedListener;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * Wrapper class for the LCD bricklet. Implements a button listener. Button1
 * (left) is configured to toggle the backlight.
 * 
 * @author Philipp Jenke
 *
 */
public class TinkerforgeLcd extends TinkerforgeModule implements
    ButtonPressedListener {

  /**
   * Tinkerforge object.
   */
  private BrickletLCD20x4 lcd = null;

  /**
   * Constructor.
   * 
   * @param uid
   *          Tinkerforge UID.
   * @param masterBrick
   *          Master brick instance this LCD is connected to.
   */
  public TinkerforgeLcd(String uid, TinkerforgeMasterBrick masterBrick) {
    super(uid, masterBrick);
    lcd =
        new BrickletLCD20x4(uid, masterBrick.getIpConnection()
            .getIpConnection());
    lcd.addButtonPressedListener(this);

    try {
      lcd.clearDisplay();
    } catch (TimeoutException | NotConnectedException e) {
      System.out.println("Failed to clear display.");
    }
  }

  @Override
  public void buttonPressed(short button) {
    if (button == 0) {
      try {
        if (lcd.isBacklightOn()) {
          lcd.backlightOff();
        } else {
          lcd.backlightOn();
        }
      } catch (TimeoutException | NotConnectedException e) {
        System.out.println("Failed to swith on backlight");
      }
    }
  }

  /**
   * Write a line to the LCD.
   * 
   * @param index
   *          Line index.
   * @param line
   *          Text to be written.
   */
  public void writeLine(int index, String line) {
    try {
      lcd.writeLine((short) index, (short) 0, line);
    } catch (TimeoutException | NotConnectedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void shutdown() {
    System.out.println("Shutting down LCD actor.");
    try {
      lcd.backlightOff();
    } catch (TimeoutException e) {
      System.out.println("Failed to turn of display backlight.");
    } catch (NotConnectedException e) {
      System.out.println("Failed to turn of display backlight.");
    }
  }
}

package homeautomation.platform;

import java.util.List;

import org.zu.ardulink.Link;
import org.zu.ardulink.protocol.IProtocol;

import homeautomation.foundation.Logger;

public class ArduinoConnection {

  private boolean isConnected = false;
  private Link link = null;

  public ArduinoConnection() {
    link = Link.getDefaultInstance();
  }

  public List<String> getPortList() {
    return link.getPortList();
  }

  /**
   * Connect to a Arduino board
   */
  public void connect(String port) {
    if (port == null) {
      Logger.getInstance().error("Invalid port");
      isConnected = false;
      return;
    }

    try {
      if (port != null) {
        System.out.println("Connecting on port: " + port);
        isConnected = link.connect(port);
        Logger.getInstance().message("Connected:" + isConnected);
        Thread.sleep(2000);
      }
    } catch (Exception e) {
      Logger.getInstance().exception("Failed to connect to arduino board", e);
    }
  }

  public void disconnect() {
    if (isConnected) {
      link.disconnect();
      isConnected = false;
    }
  }

  public boolean test() {

    int power = IProtocol.HIGH;
    while (true) {
      System.out.println("Send power:" + power);
      link.sendPowerPinSwitch(2, power); // 5
      if (power == IProtocol.HIGH) {
        power = IProtocol.LOW;
      } else {
        power = IProtocol.HIGH;
      }
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
      }
    }
  }
}

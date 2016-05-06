package homeautomation.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.zu.ardulink.Link;
import org.zu.ardulink.RawDataListener;
import org.zu.ardulink.protocol.IProtocol;
import org.zu.ardulink.protocol.ProtocolHandler;

import cgresearch.core.logging.*;

/**
 * Requires command line VM arguments:
 * -Djava.library.path=/Users/abo781/abo781/code/computergraphics/libs/native/
 * osx/
 * 
 * Arduino Sketch: ArdulinkProtocol.ino
 * 
 * @author Philipp Jenke
 *
 */
public class ArduinoConnection {

  private boolean isConnected = false;
  private Link link = null;

  /**
   * Consumers for message callbacks
   */
  List<Consumer<String>> messageCallbacks = new ArrayList<Consumer<String>>();

  public ArduinoConnection() {
    link = Link.getDefaultInstance();
    link.addRawDataListener(new RawDataListener() {
      @Override
      public void parseInput(String message, int arg1, int[] arg2) {
        String msg = "";
        for (int i = 0; i < arg1; i++) {
          msg += (char) arg2[i];
        }
        for (Consumer<String> messageCallback : messageCallbacks) {
          messageCallback.accept(msg);
        }
        // Logger.getInstance().message("Message received: " + msg);
      }
    });
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

  /**
   * Set custom command to Arduino, return result message.
   */
  public void sendCustomCommand(String command) {
    if (link.isConnected()) {
      ProtocolHandler.getCurrentProtocolImplementation().sendCustomMessage(link,
          command);
      // Logger.getInstance().message("Sent command: " + command);
    }
  }

  public void addMessageCallback(Consumer<String> messageCallback) {
    messageCallbacks.add(messageCallback);
  }

  /**
   * Returns the port which most likely is the correct port from the port list.
   */
  public String getMostLikelyPort() {
    List<String> portList = getPortList();
    for (String port : portList) {
      if (port.contains("usb")) {
        return port;
      }
    }
    if (!portList.isEmpty()) {
      return portList.get(0);
    }
    return null;
  }
}

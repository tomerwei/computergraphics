package homeautomation.platform;

import homeautomation.foundation.Logger;

import java.io.IOException;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;

/**
 * Representation of the IP connection.
 */
public class TinkerforgeIpConnection {

  /**
   * Constants: Host
   */
  private static final String DEFAULT_HOST = "localhost";

  /**
   * Constants: Port
   */
  private static final int DEFAULT_PORT = 4223;

  /**
   * Connection object.
   */
  private IPConnection ipcon = null;

  /**
   * Constructor.
   */
  public TinkerforgeIpConnection() {
    this(DEFAULT_HOST, DEFAULT_PORT);
  }

  /**
   * Constructor.
   * 
   * @param host
   *          Host address
   * @param port
   *          Port of the connection.
   */
  public TinkerforgeIpConnection(String host, int port) {
    ipcon = new IPConnection();
    try {
      ipcon.connect(host, port);
    } catch (AlreadyConnectedException | IOException e) {
      System.out.println("Failed to connect to stack.");
      e.printStackTrace();
    }
  }

  /**
   * Disconnect the stack.
   */
  public void disconnect() {
    try {
      ipcon.disconnect();
    } catch (NotConnectedException e) {
      Logger.getInstance().exception(
          "Failed to disconnect from Tinkerforge stack", e);
    }
  }

  /**
   * Getter.
   * 
   * @return Tinkerforge connection object.
   */
  public IPConnection getIpConnection() {
    return ipcon;
  }

  /**
   * Check connection status
   * 
   * @return True, of the connection is established.
   */
  public boolean isConnected() {
    short state = ipcon.getConnectionState();
    return IPConnection.CONNECTION_STATE_CONNECTED == state;
  }
}

package homeautomation.modules;

import homeautomation.platform.TinkerforgeIpConnection;

import java.util.ArrayList;
import java.util.List;

public class TinkerforgeMasterBrick {

  /**
   * List of modules used in the current application.
   */
  private List<TinkerforgeModule> modules = new ArrayList<TinkerforgeModule>();

  private final TinkerforgeIpConnection tinkerforgeIpConnection;

  /**
   * Constructor.
   */
  public TinkerforgeMasterBrick() {
    this.tinkerforgeIpConnection = new TinkerforgeIpConnection();
  }

  /**
   * Constructor.
   * 
   * @param ipConnection
   *          Shared IP connection.
   */
  public TinkerforgeMasterBrick(TinkerforgeIpConnection ipConnection) {
    this.tinkerforgeIpConnection = ipConnection;
  }

  /**
   * Shutdown tinkerforge connection.
   */
  public void shutdown() {
    tinkerforgeIpConnection.disconnect();
    for (TinkerforgeModule module : modules) {
      module.shutdown();
    }
  }

  /**
   * Register additional module at the brick.
   * 
   * @param tinkerforgeModule
   *          Module to be registered.
   */
  public void registerModule(TinkerforgeModule tinkerforgeModule) {
    modules.add(tinkerforgeModule);
  }

  /**
   * Getter.
   * 
   * @return IP connection instance.
   */
  public TinkerforgeIpConnection getIpConnection() {
    return tinkerforgeIpConnection;
  }
}

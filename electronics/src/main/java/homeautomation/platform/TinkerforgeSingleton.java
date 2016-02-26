package homeautomation.platform;

import homeautomation.modules.TinkerforgeMasterBrick;

/**
 * Singleton instance for Tinkerforge access.
 * 
 * @author Philipp Jenke
 *
 */
public class TinkerforgeSingleton {

  /**
   * Singleton instance.
   */
  private static TinkerforgeSingleton instance = null;

  /**
   * Master brick reference.
   */
  private TinkerforgeMasterBrick masterBrick = null;

  /**
   * Private constructor.
   */
  private TinkerforgeSingleton() {
    masterBrick = new TinkerforgeMasterBrick();
  }

  /**
   * Access to the singleton instance.
   * 
   * @return Singleton instance.
   */
  public static TinkerforgeSingleton getInstance() {
    if (instance == null) {
      instance = new TinkerforgeSingleton();
    }
    return instance;
  }

  /**
   * Getter.
   * 
   * @return Central master brick.
   */
  public TinkerforgeMasterBrick getMasterBrick() {
    return masterBrick;
  }
}

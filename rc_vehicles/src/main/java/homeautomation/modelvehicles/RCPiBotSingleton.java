package homeautomation.modelvehicles;

/**
 * Singleton to access the boat.
 * 
 * @author Philipp Jenke
 *
 */
public class RCPiBotSingleton {

  /**
   * Singleton instance
   */
  private static RCPiBotSingleton instance;

  /**
   * Boat instance.
   */
  private final RCPiBot bot;

  private RCPiBotSingleton() {
    bot = new RCPiBot();
  }

  public RCPiBot getBot() {
    return bot;
  }

  /**
   * Access the singleton instance.
   * 
   * @return
   */
  public static RCPiBotSingleton getInstance() {
    if (instance == null) {
      instance = new RCPiBotSingleton();
    }
    return instance;
  }

}

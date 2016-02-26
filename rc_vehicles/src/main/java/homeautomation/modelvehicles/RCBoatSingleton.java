package homeautomation.modelvehicles;

/**
 * Singleton to access the boat.
 * 
 * @author Philipp Jenke
 *
 */
public class RCBoatSingleton {

  /**
   * Singleton instance
   */
  private static RCBoatSingleton instance;

  /**
   * Boat instance.
   */
  private final RCBoat boat;

  private RCBoatSingleton() {
    boat = new RCBoat();
  }

  public RCBoat getBoat() {
    return boat;
  }

  /**
   * Access the singleton instance.
   * 
   * @return
   */
  public static RCBoatSingleton getInstance() {
    if (instance == null) {
      instance = new RCBoatSingleton();
    }
    return instance;
  }

}

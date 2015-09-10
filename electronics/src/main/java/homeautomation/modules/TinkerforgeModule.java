package homeautomation.modules;

/**
 * Abstract parent class for all tinlkerforge modules.
 * 
 * @author Philipp Jenke
 *
 */
public abstract class TinkerforgeModule implements HomeAutomationModule {

  /**
   * Module unique id.
   */
  protected final String uid;

  /**
   * Constructor.
   * 
   * @param uid
   *          Tinkerforge UID.
   * @param masterBrick
   *          Connected master brick.
   */
  public TinkerforgeModule(String uid, TinkerforgeMasterBrick masterBrick) {
    this.uid = uid;
    if (masterBrick != null) {
      masterBrick.registerModule(this);
    }
  }

  /**
   * Getter.
   * 
   * @return Tinkerforge UID.
   */
  public String getUid() {
    return uid;
  }
}

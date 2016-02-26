package homeautomation.webservice.dataaccess;

import homeautomation.apps.RasPiServer;

/**
 * This singleton provides access to the the raspi server instance (if
 * available).
 * 
 * @author Philipp Jenke
 *
 */
public class RaspiServerAccessSingleton {

  /**
   * Singleton instance.
   */
  private static RaspiServerAccessSingleton instance;

  /**
   * Root path for the file server.
   */
  private String rootPath;

  /**
   * Reference to the server.
   */
  private RasPiServer raspiServer = null;

  /**
   * Private constructor.
   */
  private RaspiServerAccessSingleton() {
  }

  /**
   * Access to the singleton instance.
   * 
   * @return Server singleton instance.
   */
  public static RaspiServerAccessSingleton getInstance() {
    if (instance == null) {
      instance = new RaspiServerAccessSingleton();
    }
    return instance;
  }

  /**
   * Setter
   * 
   * @param raspiServer
   *          Raspi server instance.
   */
  public void setRaspiServer(RasPiServer raspiServer) {
    this.raspiServer = raspiServer;
  }

  /**
   * Getter.
   * 
   * @return Raspi server instance.
   */
  public RasPiServer getRaspiServer() {
    return raspiServer;
  }

  /**
   * Setter
   * 
   * @param rootPath
   *          Root path of the file server.
   */
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  /**
   * Getter.
   * 
   * @return Root path of the file server.
   */
  public String getRootPath() {
    return rootPath;
  }
}

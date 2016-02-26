package homeautomation.webservice.dataaccess;

import homeautomation.database.DatabaseAccess;

/**
 * This singleton provides access to the the database access instance (if
 * available).
 * 
 * @author Philipp Jenke
 */
public class DatabaseAccessSingleton {

  /**
   * Singleton instance.
   */
  private static DatabaseAccessSingleton instance;

  /**
   * Reference to the server.
   */
  private DatabaseAccess databaseAccess = null;

  /**
   * Private constructor.
   */
  private DatabaseAccessSingleton() {
  }

  /**
   * Access to the singleton instance.
   * 
   * @return Singleton instance.
   */
  public static DatabaseAccessSingleton getInstance() {
    if (instance == null) {
      instance = new DatabaseAccessSingleton();
    }
    return instance;
  }

  /**
   * Setter
   * 
   * @param databaseAccess
   *          Database interface.
   */
  public void setDatabaseAccess(DatabaseAccess databaseAccess) {
    this.databaseAccess = databaseAccess;
  }

  /**
   * Getter
   * 
   * @return Database interface.
   */
  public DatabaseAccess getDatabaseAccess() {
    return databaseAccess;
  }
}

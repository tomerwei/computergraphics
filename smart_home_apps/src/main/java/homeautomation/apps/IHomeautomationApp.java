package homeautomation.apps;

public interface IHomeautomationApp {

  /**
   * Called a startup.
   */
  void init();

  /**
   * Called at end of lifecycle.
   */
  void shutdown();

  /**
   * Handle a command line command.
   * 
   * @param line
   *          Command to be handled.
   */
  void command(String line);

}

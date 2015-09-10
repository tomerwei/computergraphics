package homeautomation.modelvehicles;

import homeautomation.apps.IHomeautomationApp;
import homeautomation.foundation.ConsoleLogger;

/**
 * Central app for the RC boat.
 * 
 * @author Philipp Jenke
 *
 */
public class RCApp implements IHomeautomationApp {

  @Override
  public void init() {
    new ConsoleLogger();
    new RCServer(8182);
  }

  @Override
  public void shutdown() {
  }

  @Override
  public void command(String line) {
  }

}

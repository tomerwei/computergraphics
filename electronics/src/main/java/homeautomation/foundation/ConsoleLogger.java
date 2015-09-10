package homeautomation.foundation;

import homeautomation.foundation.Logger.VerboseMode;

import java.util.Observable;
import java.util.Observer;

/**
 * A logger which prints the log messages to the console.
 */
public class ConsoleLogger implements Observer {

  /**
   * Constructor.
   */
  public ConsoleLogger() {
    Logger.getInstance().addObserver(this);
  }

  /**
   * Constructor.
   * 
   * @param verboseMode
   *          mode of the Logger.
   */
  public ConsoleLogger(VerboseMode verboseMode) {
    Logger.getInstance().addObserver(this);
    Logger.getInstance().setVerboseMode(verboseMode);
  }

  @Override
  public void update(Observable observable, Object arg) {
    if (observable instanceof Logger) {

      System.out.println(arg);

      // String[] messages = Logger.getInstance().getMessages();
      // if (messages.length > 0) {
      // System.out.println(messages[messages.length - 1]);
      // }
    }

  }

}

package homeautomation.foundation;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Central logger for all logging messages. Implemented as Singleton.
 * 
 * @author Philipp Jenke
 * 
 */
public final class Logger extends Observable {

  /**
   * Verbose mode.
   */
  public enum VerboseMode {
    DEBUG, NORMAL
  }

  /**
   * Logger is implemented as a Singleton. This is the instance.
   */
  private static Logger instance = null;

  /**
   * Current verbose mode.
   */
  private VerboseMode verboseMode = VerboseMode.DEBUG;

  /**
   * List of messages.
   */
  List<String> messages = new ArrayList<String>();

  /**
   * Private contructor. Use getInstance() to access the instance.
   */
  private Logger() {
  }

  /**
   * Access to the instance of the Logger Singleton.
   * 
   * @return Singleton instance.
   */
  public static Logger getInstance() {
    if (instance == null) {
      instance = new Logger();
    }
    return instance;
  }

  /**
   * Logger general message.
   * 
   * @param message
   *          Logger message.
   */
  public void message(String message) {
    messages.add(message);
    setChanged();
    notifyObservers(message);
  }

  /**
   * Logger debug message (only logged in DEBUG verbose mode).
   * 
   * @param message
   *          Logger (debug) message.
   */
  public void debug(String message) {
    if (verboseMode == VerboseMode.DEBUG) {
      messages.add("[DEBUG] " + message);
      setChanged();
      notifyObservers(message);
    }
  }

  /**
   * Logger error message.
   * 
   * @param message
   *          Logger (error) message.
   */
  public void error(String message) {
    String errorMessage = "[ERROR] " + message;
    messages.add(errorMessage);
    setChanged();
    notifyObservers(errorMessage);
  }

  /**
   * Logger exception message.
   * 
   * @param message
   *          Logger message.
   * @param exception
   *          Exception object.
   */
  public void exception(String message, Exception exception) {
    String errorMessage =
        "[EXCEPTION] " + message + ": " + exception.getMessage();
    messages.add(errorMessage);
    setChanged();
    notifyObservers(errorMessage);
  }

  /**
   * Getter.
   * 
   * @return Array containing all messages.
   */
  public String[] getMessages() {
    String[] listOfMessages = new String[messages.size()];
    for (int i = 0; i < messages.size(); i++) {
      listOfMessages[i] = messages.get(i);
    }
    return listOfMessages;
  }

  /**
   * Getter.
   * 
   * @return Current verbose mode.
   */
  public VerboseMode getVerboseMode() {
    return verboseMode;
  }

  /**
   * Setter.
   * 
   * @param verboseMode New verbose mode.
   */
  public void setVerboseMode(VerboseMode verboseMode) {
    this.verboseMode = verboseMode;
  }
}

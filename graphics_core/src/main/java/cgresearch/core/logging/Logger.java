/**
 * Prof. Philipp Jenke
 */
package cgresearch.core.logging;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

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
	};

	/**
	 * Logger is implemented as a Singleton. This is the instance.
	 */
	private static Logger instance = null;

	/**
	 * Remembers the last messages.
	 */
	private static LinkedList<String> messageCache = new LinkedList<String>();

	/**
	 * Current verbose mode.
	 */
	private VerboseMode verboseMode = VerboseMode.NORMAL;

	/**
	 * Private constructor. Use getInstance() to access the instance.
	 */
	private Logger() {
	}

	/**
	 * Access to the instance of the Logger Singleton.
	 */
	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}

	/**
	 * Logger general message.
	 */
	public void message(String message) {
		printMessage(message);
	}

	/**
	 * Message is actually passed to the implemented loggers due to current
	 * settings.
	 */
	private synchronized void printMessage(String message) {
		messageCache.add(message);
		if (messageCache.size() > 100) {
			messageCache.removeFirst();
		}
		setChanged();
		notifyObservers(message);
	}

	/**
	 * Logger debug message (only logged in DEBUG verbose mode).
	 */
	public void debug(String message) {
		if (verboseMode == VerboseMode.DEBUG) {
			printMessage("[DEBUG] " + message);
		}
	}

	/**
	 * Logger error message.
	 */
	public void error(String message) {
		printMessage("[ERROR] " + message);
	}

	/**
	 * Logger exception message.
	 */
	public void exception(String message, Exception e) {
		printMessage("[EXCEPTION] " + message + ": " + e.getMessage());
	}

	/**
	 * Getter.
	 */
	public VerboseMode getVerboseMode() {
		return verboseMode;
	}

	/**
	 * Setter.
	 * 
	 * @param verboseMode
	 */
	public void setVerboseMode(VerboseMode verboseMode) {
		this.verboseMode = verboseMode;
	}

	@Override
	public synchronized void addObserver(Observer o) {
		super.addObserver(o);
		for (String message : messageCache) {
			o.update(this, message);
		}
	}
}

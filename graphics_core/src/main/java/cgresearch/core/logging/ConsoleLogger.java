package cgresearch.core.logging;

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
		this(Logger.VerboseMode.NORMAL);
	}

	/**
	 * Constructor.
	 */
	public ConsoleLogger(Logger.VerboseMode mode) {
		Logger.getInstance().setVerboseMode(mode);
		Logger.getInstance().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Logger) {

			System.out.println(arg);

			// String[] messages = Logger.getInstance().getMessages();
			// if (messages.length > 0) {
			// System.out.println(messages[messages.length - 1]);
			// }
		}

	}

}

package cgresearch.core;

import cgresearch.core.logging.Logger;

/**
 * This exception indicates that the code is not yet implemented.
 * 
 * @author Philipp Jenke
 *
 */
public class NotImplementedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotImplementedException() {
		this("");
	}

	public NotImplementedException(String message) {
		super(message);
		Logger.getInstance().error("Not implemented yet!");
	}

}

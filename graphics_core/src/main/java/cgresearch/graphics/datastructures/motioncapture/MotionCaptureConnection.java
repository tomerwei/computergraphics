package cgresearch.graphics.datastructures.motioncapture;

/**
 * Connection between to entities in motion capture data.
 * 
 * @author Philipp Jenke
 *
 */
public class MotionCaptureConnection {

	/**
	 * ID of the connection start.
	 */
	private String idStart = "";

	/**
	 * ID of the connection end.
	 */
	private String idEnd = "";

	/**
	 * Constructor.
	 */
	public MotionCaptureConnection(String idStart, String idEnd) {
		this.idStart = idStart;
		this.idEnd = idEnd;
	}

	/**
	 * Getter.
	 */
	public String getIdStart() {
		return idStart;
	}

	/**
	 * Getter.
	 */
	public String getIdEnd() {
		return idEnd;
	}

}

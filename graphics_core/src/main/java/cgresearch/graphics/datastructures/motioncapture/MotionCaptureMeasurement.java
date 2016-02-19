package cgresearch.graphics.datastructures.motioncapture;

import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;

/**
 * Representation of a measurement of a data point.
 * 
 * @author Philipp Jenke
 *
 */
public class MotionCaptureMeasurement {

	/**
	 * Globally unique id of the measurement.
	 */
	private final String id;

	/**
	 * Position in 3-space (required).
	 */
	private final Vector position;

	/**
	 * Orientation in 3-space (optional)
	 */
	private final Matrix orientation;

	/**
	 * Constructor.
	 */
	public MotionCaptureMeasurement(String id, Vector position,
			Matrix orientation) {
		this.id = id;
		this.position = position;
		this.orientation = orientation;
	}

	/**
	 * Constructor.
	 */
	public MotionCaptureMeasurement(String id, Vector position) {
		this(id, position, null);
	}

	/**
	 * Getter
	 */
	public String getId() {
		return id;
	}

	/**
	 * Getter.
	 */
	public Vector getPosition() {
		return position;
	}

	/**
	 * Getter.
	 */
	public Matrix getOrientation() {
		return orientation;
	}

}

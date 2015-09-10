package cgresearch.graphics.datastructures.motioncapture;

import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;

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
	private final IVector3 position;

	/**
	 * Orientation in 3-space (optional)
	 */
	private final IMatrix3 orientation;

	/**
	 * Constructor.
	 */
	public MotionCaptureMeasurement(String id, IVector3 position,
			IMatrix3 orientation) {
		this.id = id;
		this.position = position;
		this.orientation = orientation;
	}

	/**
	 * Constructor.
	 */
	public MotionCaptureMeasurement(String id, IVector3 position) {
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
	public IVector3 getPosition() {
		return position;
	}

	/**
	 * Getter.
	 */
	public IMatrix3 getOrientation() {
		return orientation;
	}

}

package cgresearch.graphics.datastructures.motioncapture;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.BoundingBox;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * Container for a single frame of motion capure data.
 * 
 * @author Philipp Jenke
 *
 */
public class MotionCaptureFrame extends ICgNodeContent {

	/**
	 * The topology shared by all frames of the same dataset. The underlying
	 * assumption is that the topology of the tracked object is constant.
	 */
	private MotionCaptureTopology topology = null;

	/**
	 * A list of measurements in the current frame. It is possible the a frame
	 * does not contain a measurement for all data points in the topology.
	 */
	private List<MotionCaptureMeasurement> measurements = new ArrayList<MotionCaptureMeasurement>();

	/**
	 * Constructor.
	 */
	public MotionCaptureFrame(MotionCaptureTopology topology) {
		this.topology = topology;
	}

	/**
	 * Constructor.
	 */
	public MotionCaptureFrame() {
		this(null);
	}

	/**
	 * Getter.
	 */
	public MotionCaptureTopology getTopology() {
		return topology;
	}

	/**
	 * Getter.
	 */
	public int getNumberOfMeasurements() {
		return measurements.size();
	}

	/**
	 * Getter.
	 */
	public MotionCaptureMeasurement getMeasurement(int index) {
		return measurements.get(index);
	}

	/**
	 * Setter
	 */
	public void addMeasurement(MotionCaptureMeasurement measurement) {
		measurements.add(measurement);
	}

	@Override
	public BoundingBox getBoundingBox() {
		BoundingBox boundingBox = new BoundingBox();
		for (MotionCaptureMeasurement measurement : measurements) {
			boundingBox.add(measurement.getPosition());
		}
		return boundingBox;
	}

	/**
	 * Setter.
	 */
	public void setTopoloyg(MotionCaptureTopology topology) {
		this.topology = topology;
	}

	/**
	 * Return a measurement by id.
	 */
	public MotionCaptureMeasurement getMeasurementById(String id) {
		for (MotionCaptureMeasurement measurement : measurements) {
			if (measurement.getId().equals(id)) {
				return measurement;
			}
		}
		return null;
	}

}

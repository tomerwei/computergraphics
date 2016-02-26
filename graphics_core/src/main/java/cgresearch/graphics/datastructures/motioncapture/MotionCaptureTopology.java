package cgresearch.graphics.datastructures.motioncapture;

import java.util.ArrayList;
import java.util.List;

/**
 * Saves the topology graph of motion capture data.
 * 
 * @author Philipp Jenke
 *
 */
public class MotionCaptureTopology {

	/**
	 * List of connections in between data points.
	 */
	private List<MotionCaptureConnection> connections = new ArrayList<MotionCaptureConnection>();

	/**
	 * Constructor.
	 */
	public MotionCaptureTopology() {
	}

	/**
	 * Getter.
	 */
	public int getNumberOfConnections() {
		return connections.size();
	}

	/**
	 * Getter.
	 */
	public MotionCaptureConnection getConnection(int index) {
		return connections.get(index);
	}

	/**
	 * Setter.
	 */
	public void add(MotionCaptureConnection connection) {
		connections.add(connection);
	}
}

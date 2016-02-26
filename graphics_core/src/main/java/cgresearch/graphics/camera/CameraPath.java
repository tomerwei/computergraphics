package cgresearch.graphics.camera;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a camera path consisting of key points.
 * 
 * @author Philipp Jenke
 *
 */
public class CameraPath implements Serializable {

	/**
	 * Version 1: Initial create.
	 */
	private static final long serialVersionUID = 1;

	/**
	 * List of key points.
	 */
	private List<CameraPathKeypoint> keyPoints = new ArrayList<CameraPathKeypoint>();

	/**
	 * Constructor.
	 */
	public CameraPath() {

	}

	/**
	 * Append an additional key point.
	 */
	public void addKeyPoint(CameraPathKeypoint keyPoint) {
		keyPoints.add(keyPoint);
	}

	/**
	 * Getter.
	 */
	public int getNumberOfKeyPoints() {
		return keyPoints.size();
	}

	/**
	 * Getter.
	 **/
	public CameraPathKeypoint getKeyPoint(int index) {
		return keyPoints.get(index);
	}

	public void clear() {
		keyPoints.clear();
	}

}

package cgresearch.graphics.camera;

import java.io.Serializable;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Key point of a camera path
 * 
 * @author Philipp Jenke
 *
 */
public class CameraPathKeypoint implements Serializable {
	/**
	 * Version 1: Initial setup.
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * All information about the key point is saved in the camera object.
	 */
	private IVector3 pos = VectorMatrixFactory.newIVector3();
	private IVector3 up = VectorMatrixFactory.newIVector3();
	private IVector3 ref = VectorMatrixFactory.newIVector3();

	/**
	 * Constructor.
	 */
	public CameraPathKeypoint(IVector3 pos, IVector3 up, IVector3 ref) {
		this.pos.copy(pos);
		this.up.copy(up);
		this.ref.copy(ref);
	}

	/**
	 * Getter.
	 */
	public IVector3 getPos() {
		return pos;
	}

	/**
	 * Getter.
	 */
	public IVector3 getUp() {
		return up;
	}

	/**
	 * Getter.
	 */
	public IVector3 getRef() {
		return ref;
	}
}

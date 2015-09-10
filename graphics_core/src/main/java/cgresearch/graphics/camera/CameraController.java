package cgresearch.graphics.camera;

import cgresearch.core.math.BoundingBox;

public abstract class CameraController {

	/**
	 * Mouse was moved (left button down) in X direction.
	 */
	public void mouseDeltaXLeftButton(final float deltaX) {
	}

	/**
	 * Mouse was moved (left button down) in Y direction.
	 */
	public void mouseDeltaYLeftButton(final float deltaY) {
	}

	/**
	 * Mouse was moved (right button down) in X direction.
	 */
	public void mouseDeltaXRightButton(final float deltaX) {
	}

	/**
	 * Mouse was moved (right button down) in Y direction.
	 */
	public void mouseDeltaYRightButton(final float deltaY) {
	}

	/**
	 * Mousewheel was moved.
	 */
	public void mouseWheelMoved(final int clicks) {
	}

	/**
	 * Adjust the camera settings so that the camera view contains the complete
	 * scene.
	 */
	public void fitToBoundingBox(BoundingBox bbox) {
	}

	/**
	 * Timer tick event.
	 */
	public void animationTimerTick() {
	}

	/**
	 * Key pressed event.
	 */
	public void keyDown(int keyCode) {
	}

	/**
	 * Key kept pressed event.
	 */
	public void keyPressed(int keyCode) {
	}

	/**
	 * This method is continuously called if a key is kept pressed.
	 */
	public void keyIsPressed(int keyCode) {
	}

}

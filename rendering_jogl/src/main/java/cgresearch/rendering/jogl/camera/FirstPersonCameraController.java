package cgresearch.rendering.jogl.camera;

import java.awt.event.KeyEvent;

import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.camera.CameraController;

/**
 * The first person camera controller is controlled like first-person
 * simulations. The mouse is used to control the view direction and the arrow
 * key are used to move into (or backwards of) the current view direction.
 * 
 * @author Philipp Jenke
 *
 */
public class FirstPersonCameraController extends CameraController {

	/**
	 * Factor to control the rotation speed of the camera.
	 */
	private static final double ROTATATION_SPEED = 0.005f;

	/**
	 * Factor to control the translation speed of the camera.
	 */
	private static final double TRANSLATION_SPEED = 0.05f;

	@Override
	public void mouseDeltaXLeftButton(float deltaX) {
		Camera.getInstance().rotateRefVertically(deltaX * ROTATATION_SPEED);
	}

	@Override
	public void mouseDeltaYLeftButton(float deltaY) {
		Camera.getInstance().rotateRefHorizontally(deltaY * ROTATATION_SPEED);
	}

	@Override
	public void keyDown(int keyCode) {
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			Camera.getInstance().moveForwards(TRANSLATION_SPEED);
		} else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			Camera.getInstance().moveForwards(-TRANSLATION_SPEED);
		}
	}

	@Override
	public void keyIsPressed(int keyCode) {
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			Camera.getInstance().moveForwards(TRANSLATION_SPEED);
		} else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			Camera.getInstance().moveForwards(-TRANSLATION_SPEED);
		}
	}
}

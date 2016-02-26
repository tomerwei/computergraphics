package cgresearch.rendering.jogl.camera;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.camera.CameraController;
import cgresearch.graphics.camera.CameraPathInterpolator;
import cgresearch.graphics.misc.AnimationTimer;

public class CameraPathController extends CameraController {
	/**
	 * Interpolation of the camera path.
	 */
	private CameraPathInterpolator cameraPathInterPolator;

	/**
	 * Constructor.
	 */
	public CameraPathController(CameraPathInterpolator cameraPath) {
		this.cameraPathInterPolator = cameraPath;
		// Create dummy path
		createDummyPath();
	}

	@Override
	public void animationTimerTick() {
		AnimationTimer timer = AnimationTimer.getInstance();
		float scale = timer.getMaxValue() - timer.getMinValue();
		float t = (timer.getValue() - timer.getMinValue()) / scale;
		Vector pos = cameraPathInterPolator.getInterpolatedPos(t);
		Vector ref = cameraPathInterPolator.getInterpolatedRef(t);
		Camera.getInstance().setEye(pos);
		Camera.getInstance().setRef(ref);
	}

	/**
	 * Create the keyframes for the camera path - todo: replace with more
	 * interactive functionality.
	 */
	private void createDummyPath() {
		cameraPathInterPolator.clearKeyPoints();
		cameraPathInterPolator.addKeyPoint(
				VectorFactory.createVector3(-1, 0.5, -1),
				VectorFactory.createVector3(0, 1, 0),
				VectorFactory.createVector3(0, 0, 0));
		cameraPathInterPolator.addKeyPoint(
				VectorFactory.createVector3(-1, 1, 1),
				VectorFactory.createVector3(0, 1, 0),
				VectorFactory.createVector3(0, 0, 0));
		cameraPathInterPolator.addKeyPoint(
				VectorFactory.createVector3(1, 0.5, 1),
				VectorFactory.createVector3(0, 1, 0),
				VectorFactory.createVector3(0, 0, 0));
		cameraPathInterPolator.addKeyPoint(
				VectorFactory.createVector3(1, 1, -1),
				VectorFactory.createVector3(0, 1, 0),
				VectorFactory.createVector3(0, 0, 0));
		cameraPathInterPolator.addKeyPoint(
				VectorFactory.createVector3(-1, 1, -1),
				VectorFactory.createVector3(0, 1, 0),
				VectorFactory.createVector3(0, 0, 0));
	}
}

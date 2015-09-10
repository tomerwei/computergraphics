package cgresearch.rendering.jogl.core;

import com.jogamp.opengl.GL2;

import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.motioncapture.MotionCaptureConnection;
import cgresearch.graphics.datastructures.motioncapture.MotionCaptureFrame;
import cgresearch.graphics.datastructures.motioncapture.MotionCaptureMeasurement;
import cgresearch.graphics.datastructures.primitives.Arrow;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Rendering a motion capture frame.
 * 
 * @author Philipp Jenke
 *
 */
public class RenderContentMotionCaptureFrame extends JoglRenderContent {

	/**
	 * Reference to the octree to be displayed.
	 */
	private final MotionCaptureFrame frame;

	/**
	 * Display list id
	 */
	private int displayListId = -1;

	/**
	 * Constructor.
	 */
	public RenderContentMotionCaptureFrame(MotionCaptureFrame frame) {
		this.frame = frame;

		// TODO: Compute automatically
		double scale = 200;
		for (int measurementIndex = 0; measurementIndex < frame
				.getNumberOfMeasurements(); measurementIndex++) {
			MotionCaptureMeasurement measurement = frame
					.getMeasurement(measurementIndex);
			IVector3 position = measurement.getPosition();
			IMatrix3 orientation = measurement.getOrientation().getTransposed();
			IVector3 x = position.add(orientation.getRow(0).multiply(scale));
			IVector3 y = position.add(orientation.getRow(1).multiply(scale));
			IVector3 z = position.add(orientation.getRow(2).multiply(scale));

			// x
			Arrow arrowX = new Arrow(position, x);
			CgNode arrowXNode = new CgNode(arrowX, "x-orientation");
			arrowX.getMaterial().setReflectionDiffuse(
					VectorMatrixFactory.newIVector3(0.75, 0.25, 0.25));
			arrowX.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
			JoglRenderNode arrowXRenderNode = JoglRenderObjectFactoryPrimitive
					.createArrow(null, arrowXNode, arrowX);
			addSubContent(arrowXRenderNode);

			// y
			Arrow arrowY = new Arrow(position, y);
			CgNode arrowYNode = new CgNode(arrowY, "y-orientation");
			arrowY.getMaterial().setReflectionDiffuse(
					VectorMatrixFactory.newIVector3(0.25, 0.75, 0.25));
			arrowY.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
			JoglRenderNode arrowYRenderNode = JoglRenderObjectFactoryPrimitive
					.createArrow(null, arrowYNode, arrowY);
			addSubContent(arrowYRenderNode);

			// z
			Arrow arrowZ = new Arrow(position, z);
			CgNode arrowZNode = new CgNode(arrowZ, "z-orientation");
			arrowZ.getMaterial().setReflectionDiffuse(
					VectorMatrixFactory.newIVector3(0.25, 0.25, 0.75));
			arrowZ.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
			JoglRenderNode arrowZRenderNode = JoglRenderObjectFactoryPrimitive
					.createArrow(null, arrowZNode, arrowZ);
			addSubContent(arrowZRenderNode);
		}

	}

	/**
	 * Render the octree.
	 */
	public void draw3D(GL2 gl) {
		boolean showConnections = false;
		if (showConnections) {
			renderConnections(gl);
		}

		drawSubContent(gl);
	}

	/**
	 * Render the connections as lines.
	 */
	private void renderConnections(GL2 gl) {
		if (displayListId < 0) {
			displayListId = gl.glGenLists(1);
			gl.glNewList(displayListId, GL2.GL_COMPILE);
			gl.glBegin(GL2.GL_LINES);
			gl.glColor3fv(Material.PALETTE0_COLOR2.floatData(), 0);
			// gl.glLineStipple(0xAAAA, (short) 1);
			for (int i = 0; i < frame.getTopology().getNumberOfConnections(); i++) {
				MotionCaptureConnection connection = frame.getTopology()
						.getConnection(i);
				MotionCaptureMeasurement startMeasurement = frame
						.getMeasurementById(connection.getIdStart());
				MotionCaptureMeasurement endMeasurement = frame
						.getMeasurementById(connection.getIdEnd());
				gl.glVertex3fv(startMeasurement.getPosition().floatData(), 0);
				gl.glVertex3fv(endMeasurement.getPosition().floatData(), 0);

			}
			gl.glEnd();
			gl.glEndList();
		}
		if (displayListId >= 0) {
			gl.glCallList(displayListId);
		}
	}

	@Override
	public void updateRenderStructures() {
	}

	@Override
	public void afterDraw(GL2 gl) {
	}
}

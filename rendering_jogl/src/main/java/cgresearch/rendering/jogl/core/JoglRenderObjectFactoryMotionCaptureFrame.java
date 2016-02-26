package cgresearch.rendering.jogl.core;

import cgresearch.graphics.datastructures.motioncapture.MotionCaptureFrame;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.IRenderObjectsFactory;

/**
 * Create render objects for a motion capzure frame.
 * 
 * @author Philipp Jenke
 *
 */
public class JoglRenderObjectFactoryMotionCaptureFrame implements
		IRenderObjectsFactory<JoglRenderNode> {

	@Override
	public JoglRenderNode createRenderObject(JoglRenderNode parentNode,
			CgNode cgNode) {
		if (cgNode.getContent() instanceof MotionCaptureFrame) {
			MotionCaptureFrame frame = (MotionCaptureFrame) cgNode.getContent();
			JoglRenderNode node = new JoglRenderNode(cgNode,
					new RenderContentMotionCaptureFrame(frame));

			// Observer pattern between scene graph node and JOGL render nodes
			cgNode.addObserver(node);

			return node;
		} else {
			return null;
		}
	}

	@Override
	public Class<?> getType() {
		return MotionCaptureFrame.class;
	}
}

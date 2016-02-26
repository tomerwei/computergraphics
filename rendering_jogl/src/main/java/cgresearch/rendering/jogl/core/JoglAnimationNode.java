package cgresearch.rendering.jogl.core;


import com.jogamp.opengl.GL2;

import cgresearch.graphics.scenegraph.CgNode;

/**
 * Special 'render' node for animations. This node just switches the visible
 * flags of its child nodes based on the current value of the timer.
 * 
 * @author Philipp Jenke
 *
 */
public class JoglAnimationNode extends JoglRenderNode {

	/**
	 * Constructor.
	 */
	public JoglAnimationNode(CgNode cgNode, IRenderContent renderContent) {
		super(cgNode, renderContent);
	}

	@Override
	public void draw3D(GL2 gl) {
		if (cgNode == null) {
			return;
		}
	}
}

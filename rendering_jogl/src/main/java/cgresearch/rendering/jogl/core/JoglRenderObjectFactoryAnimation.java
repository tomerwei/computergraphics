/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.graphics.scenegraph.Animation;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.IRenderObjectsFactory;

/**
 * Render object factory for animations.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglRenderObjectFactoryAnimation implements
		IRenderObjectsFactory<JoglRenderNode> {
	/*
	 * (nicht-Javadoc)
	 * 
	 * @see
	 * edu.haw.cg.rendering.IRenderObjectsFactory#createRenderObject(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public JoglRenderNode createRenderObject(JoglRenderNode parentNode,
			CgNode cgNode) {
		JoglRenderNode node = new JoglAnimationNode(cgNode, null);
		return node;
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.rendering.IRenderObjectsFactory#getType()
	 */
	@Override
	public Class<?> getType() {
		return Animation.class;
	}
}

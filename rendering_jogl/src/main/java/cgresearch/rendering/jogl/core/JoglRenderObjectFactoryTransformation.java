/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.Transformation;
import cgresearch.rendering.core.IRenderObjectsFactory;

/**
 * Render object factory for animations.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglRenderObjectFactoryTransformation implements
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
		if (cgNode.getContent() instanceof Transformation) {
			Transformation transformation = (Transformation) cgNode
					.getContent();
			JoglRenderNode node = new JoglRenderNode(cgNode,
					new RenderContentTransformation(transformation));
			cgNode.addObserver(node);
			return node;
		} else {
			return null;
		}
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.rendering.IRenderObjectsFactory#getType()
	 */
	@Override
	public Class<?> getType() {
		return Transformation.class;
	}
}

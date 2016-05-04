/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.graphics.datastructures.curves.Curve;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.IRenderObjectsFactory;

/**
 * Render object factory for curves.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglRenderObjectFactoryCurve implements
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
		JoglRenderNode node = new JoglRenderNode(cgNode,
				new RenderContentCurve(cgNode));
		return node;
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.rendering.IRenderObjectsFactory#getType()
	 */
	@Override
	public Class<?> getType() {
		return Curve.class;
	}
}

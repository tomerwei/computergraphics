/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.IRenderObjectsFactory;

/**
 * Render object factory for triangle meshes.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglRenderObjectFactoryTriangleMesh implements
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
		if (cgNode == null) {
			Logger.getInstance().error("Error creating render object");
		}
		ITriangleMesh triangleMesh = (ITriangleMesh) cgNode.getContent();
		JoglRenderNode node = new JoglRenderNode(cgNode,
				new RenderContentTriangleMesh(triangleMesh));
		cgNode.addObserver(node);
		return node;
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.rendering.IRenderObjectsFactory#getType()
	 */
	@Override
	public Class<?> getType() {
		return ITriangleMesh.class;
	}

}

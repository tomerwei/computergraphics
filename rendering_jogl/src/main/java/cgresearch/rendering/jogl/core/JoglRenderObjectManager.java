/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import java.util.Iterator;
import java.util.Observable;

import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.AbstractRenderObjectManager;
import cgresearch.rendering.core.IRenderObjectsFactory;

/**
 * Render object manager for the JOGL engine.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglRenderObjectManager extends
		AbstractRenderObjectManager<JoglRenderNode> {

	/**
	 * Constructor.
	 * 
	 * @param cgRootNode
	 *            Root node of the CG scene graph.
	 */
	public JoglRenderObjectManager(CgNode cgRootNode) {
		super(cgRootNode, new JoglRenderNode(cgRootNode, null));

		// Register factories
		registerRenderObjectsFactory(new JoglRenderObjectFactoryTriangleMesh());
		registerRenderObjectsFactory(new JoglRenderObjectFactoryPointCloud());
		registerRenderObjectsFactory(new JoglRenderObjectFactoryCurve());
		registerRenderObjectsFactory(new JoglRenderObjectFactoryPrimitive());
		registerRenderObjectsFactory(new JoglRenderObjectFactoryOctree());
		registerRenderObjectsFactory(new JoglRenderObjectFactoryAnimation());
		registerRenderObjectsFactory(new JoglRenderObjectFactoryMotionCaptureFrame());
		registerRenderObjectsFactory(new JoglRenderObjectFactoryTransformation());
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.rendering.AbstractRenderObjectManager#
	 * getDefaultRenderObjectsFactory()
	 */
	@Override
	protected IRenderObjectsFactory<JoglRenderNode> getDefaultRenderObjectsFactory() {
		return new JoglRenderObjectFactoryDefault();
	}

	@Override
	public Iterator<JoglRenderNode> getRenderNodeIterator() {
		return mappingCgNodeRenderObjectNode.values().iterator();
	}

	/**
	 * Return the render node for a given scene graph node.
	 */
	public JoglRenderNode getRenderNode(CgNode node) {
		return mappingCgNodeRenderObjectNode.get(node);
	}

	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);
	}
}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jmonkey;

import java.util.Iterator;

import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.AbstractRenderObjectManager;
import cgresearch.rendering.core.IRenderObjectsFactory;

import com.jme3.scene.Node;

/**
 * Render object manager for the jMonkey engine.
 * 
 * @author Philipp Jenke
 * 
 */
public class JMonkeyRenderObjectManager extends
		AbstractRenderObjectManager<Node> {

	/**
	 * Constructor.
	 * 
	 * @param cgRootNode
	 *            Root node of the CG scene graph.
	 * @param rootNode
	 *            Root node of the jMonkey scene graph.
	 */
	public JMonkeyRenderObjectManager(CgNode cgRootNode, Node rootNode) {
		super(cgRootNode, rootNode);
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.rendering.AbstractRenderObjectManager#
	 * getDefaultRenderObjectsFactory()
	 */
	@Override
	protected IRenderObjectsFactory<Node> getDefaultRenderObjectsFactory() {
		return new JMonkeyRenderObjectFactoryDefault();
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.rendering.AbstractRenderObjectManager#getRenderNode(int)
	 */
	// @Override
	// public Node getRenderNode(int index) {
	// Object object = mappingCgNodeRenderObjectNode.values().toArray()[index];
	// if (object instanceof Node) {
	// return (Node) object;
	// } else {
	// Logger.getInstance().error(
	// "Invalid render node in map - expected (JMonkey) Node.");
	// return null;
	// }
	// }

	@Override
	public Iterator<Node> getRenderNodeIterator() {
		return mappingCgNodeRenderObjectNode.values().iterator();
	}
}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgNodeStateChange;

/**
 * The purpose of the AbstractRenderObjectManager is to automaticalls create
 * render objects for all nodes in the scene graph.
 * 
 * @author Philipp Jenke
 * 
 */
public abstract class AbstractRenderObjectManager<T> implements Observer {

	/**
	 * Mapping between the CG node and the jMonkey scene graph.
	 */
	protected Map<CgNode, T> mappingCgNodeRenderObjectNode = new HashMap<CgNode, T>();

	/**
	 * Mapping between the scene graph node content class and the corresponding
	 * render objects factory.
	 */
	protected MappingRenderObjectsFactory<T> mappingRenderObjectsFactory = new MappingRenderObjectsFactory<T>();

	/**
	 * Constructor.
	 */
	public AbstractRenderObjectManager(CgNode cgRootNode, T renderObject) {
		cgRootNode.addObserver(this);
		mappingCgNodeRenderObjectNode.put(cgRootNode, renderObject);
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (!(o instanceof CgNode)) {
			return;
		}

		CgNode cgNode = (CgNode) o;
		T renderObjectNode = mappingCgNodeRenderObjectNode.get(cgNode);
		if (renderObjectNode == null) {
			return;
		}

		if (arg instanceof CgNodeStateChange) {
			CgNodeStateChange changeState = (CgNodeStateChange) arg;
			switch (changeState.getState()) {
			case ADD_CHILD:
				createRenderObjectsForNode(cgNode, renderObjectNode);
				break;
			case REMOVE_CHILD:
				Logger.getInstance().debug("Render child node removed!");
				removeNode(changeState.getNode2());
				break;
			case CHANGED:
				break;
			case VISIBILITY_CHANGED:
				break;
			}
		}
	}

	/**
	 * Remove render node for CGNode node.
	 */
	private void removeNode(CgNode node) {
		mappingCgNodeRenderObjectNode.remove(node);
	}

	/**
	 * Create the required render object for a CG node and its children.
	 * 
	 * @param cgNode
	 * @param renderObjectNode
	 */
	private void createRenderObjectsForNode(CgNode cgNode, T renderObjectNode) {
		// clearChildren(renderObjectNode);
		for (int i = 0; i < cgNode.getNumChildren(); i++) {
			CgNode childCgNode = cgNode.getChildNode(i);
			childCgNode.addObserver(this);
			T renderObjectChildNode = mappingCgNodeRenderObjectNode
					.get(childCgNode);
			if (renderObjectChildNode == null) {
				Object content = childCgNode.getContent();
				if (content != null) {
					IRenderObjectsFactory<T> factory = mappingRenderObjectsFactory
							.get(content);
					if (factory != null) {
						Logger.getInstance().debug(
								"Successfully fetched render factory for content "
										+ content.getClass());
					} else {
						factory = getDefaultRenderObjectsFactory();
						Logger.getInstance().message(
								"No render factory for content "
										+ content.getClass()
										+ " found, using default factory.");
					}
					renderObjectChildNode = factory.createRenderObject(
							renderObjectNode, childCgNode);
					mappingCgNodeRenderObjectNode.put(childCgNode,
							renderObjectChildNode);
				} else {
					// no content
					renderObjectChildNode = getDefaultRenderObjectsFactory()
							.createRenderObject(renderObjectNode, childCgNode);
					mappingCgNodeRenderObjectNode.put(childCgNode,
							renderObjectChildNode);
				}
			}
			createRenderObjectsForNode(childCgNode, renderObjectChildNode);
		}
	}

	/**
	 * Each render system must at least provide the default render objects
	 * factory (no rendering).
	 */
	protected abstract IRenderObjectsFactory<T> getDefaultRenderObjectsFactory();

	/**
	 * Register a new factory for the provided type.
	 * 
	 * @param type
	 *            Type to be rendered.
	 * @param factory
	 *            Factory object.
	 */
	public void registerRenderObjectsFactory(IRenderObjectsFactory<T> factory) {
		mappingRenderObjectsFactory.put(factory);

		// Logger.getInstance().message(
		// "Registered a render objects factory for the type "
		// + factory.getType().getName());
	}

	/**
	 * Getter.
	 */
	public int getNumberOfRenderNodes() {
		return mappingCgNodeRenderObjectNode.size();
	}

	/**
	 * Get an iterator for all render nodes.
	 */
	public abstract Iterator<T> getRenderNodeIterator();

}

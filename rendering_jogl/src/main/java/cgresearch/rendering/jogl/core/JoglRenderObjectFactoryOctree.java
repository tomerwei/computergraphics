package cgresearch.rendering.jogl.core;

import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.IRenderObjectsFactory;

/**
 * Create render objects for an octree node.
 * 
 * @author Philipp Jenke
 *
 */
public class JoglRenderObjectFactoryOctree implements
		IRenderObjectsFactory<JoglRenderNode> {

	@Override
	public JoglRenderNode createRenderObject(JoglRenderNode parentNode,
			CgNode cgNode) {
		if (cgNode.getContent() instanceof OctreeNode) {
			OctreeNode<?> rootNode = (OctreeNode<?>) cgNode.getContent();
			JoglRenderNode node = new JoglRenderNode(cgNode,
					new RenderContentOctree(rootNode));

			// Observer pattern between scene graph node and JOGL render nodes
			cgNode.addObserver(node);

			return node;
		} else {
			return null;
		}
	}

	@Override
	public Class<?> getType() {
		return OctreeNode.class;
	}
}

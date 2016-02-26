/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Philipp Jenke
 * 
 */
public class DepthFirstVisitor {

	/**
	 * Stack of nodes to be visited
	 */
	List<JoglRenderNode> stack = new ArrayList<JoglRenderNode>();

	/**
	 * Constructor.
	 */
	public DepthFirstVisitor(JoglRenderObjectManager renderObjectMananger) {
		reset(renderObjectMananger);
	}

	/**
	 * Fill the stack with all nodes of the tree, staring at the rootNode.
	 */
	private void reset(JoglRenderObjectManager renderObjectManager) {
		stack.clear();

		Iterator<JoglRenderNode> it = renderObjectManager
				.getRenderNodeIterator();
		while (it.hasNext()) {
			stack.add(it.next());
		}
	}

	/**
	 * Return the next node.
	 */
	public JoglRenderNode next() {
		JoglRenderNode currentNode = stack.get(0);
		stack.remove(0);
		return currentNode;
	}

	/**
	 * return true of all nodes have been traversed
	 * 
	 * @return
	 */
	public boolean atEnd() {
		return stack.size() == 0;
	}
}

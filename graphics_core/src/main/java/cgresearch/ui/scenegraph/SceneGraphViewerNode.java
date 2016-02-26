/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.ui.scenegraph;

import java.util.Observable;
import java.util.Observer;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgNodeStateChange;

public class SceneGraphViewerNode extends DefaultMutableTreeNode implements
		Observer {

	/**
	 * Reference to the corresponding scene graph node.
	 */
	private CgNode sgNode = null;

	/**
	 * Reference to the Swing tree.
	 */
	private final DefaultTreeModel model;

	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public SceneGraphViewerNode(DefaultTreeModel model, final CgNode sgNode) {
		super(sgNode.getName());
		this.model = model;
		this.sgNode = sgNode;
		sgNode.addObserver(this);
	}

	/**
	 * Getter.
	 */
	public boolean isVisible() {
		if (sgNode == null) {
			return false;
		}
		return sgNode.isVisible();
	}

	/**
	 * Getter.
	 */
	public String getName() {
		if (sgNode == null) {
			return "Node";
		}
		return sgNode.getName();
	}

	/**
	 * Toggle visibility.
	 */
	public void toggleVisibiliy() {
		if (sgNode == null) {
			return;
		}
		sgNode.toggleVisibiliy();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof CgNodeStateChange) {
			CgNodeStateChange stateChange = (CgNodeStateChange) arg;
			switch (stateChange.getState()) {
			case ADD_CHILD:
				// Consistency check
				if (stateChange.getNode1() != getSceneGraphNode()) {
					Logger.getInstance().error(
							"Inconsistent tree view for the scene graph!");
					break;
				}
				addChild(stateChange.getNode2());
				break;
			case REMOVE_CHILD:
				// Consistency check
				if (stateChange.getNode1() != getSceneGraphNode()) {
					Logger.getInstance().error(
							"Inconsistent tree view for the scene graph!");
					break;
				}
				removeChild(stateChange.getNode2());
				break;
			case CHANGED:
				model.nodeChanged(this);
				break;
			case VISIBILITY_CHANGED:
				model.nodeChanged(this);
				break;
			}
		}
	}

	/**
	 * Remove the tree node which corresponds to the scene graph node.
	 */
	private void removeChild(CgNode cgNode) {
		boolean removeSuccess = false;
		for (int i = 0; i < getChildCount(); i++) {
			SceneGraphViewerNode treeNode = (SceneGraphViewerNode) getChildAt(i);
			if (treeNode != null && treeNode.getSceneGraphNode() == cgNode) {
				model.removeNodeFromParent((MutableTreeNode) getChildAt(i));
				i--;
				removeSuccess = true;
			}
		}
		// Consistency check
		if (!removeSuccess) {
			Logger.getInstance().error(
					"Failed to update tree for scene graph (remove node)!");
		}
	}

	/**
	 * Create a tree node for the added CgNode.
	 */
	private void addChild(CgNode cgNode) {
		SceneGraphViewerNode childTreeNode = new SceneGraphViewerNode(model,
				cgNode);
		model.insertNodeInto(childTreeNode, this, getChildCount());
		childTreeNode.buildTree();

	}

	/**
	 * Getter.
	 */
	public CgNode getSceneGraphNode() {
		return sgNode;
	}

	/**
	 * (Re-)Build the tree below the current node.
	 */
	public void buildTree() {
		// Remove all children
		for (int i = 0; i < getChildCount(); i++) {
			model.removeNodeFromParent((MutableTreeNode) getChildAt(i));
			i--;
		}

		// Build up new children
		for (int childIndex = 0; childIndex < sgNode.getNumChildren(); childIndex++) {
			CgNode sgChildNode = sgNode.getChildNode(childIndex);
			SceneGraphViewerNode childTreeNode = new SceneGraphViewerNode(
					model, sgChildNode);
			model.insertNodeInto(childTreeNode, this, getChildCount());
			// add(childTreeNode);
			childTreeNode.buildTree();
		}
	}
}

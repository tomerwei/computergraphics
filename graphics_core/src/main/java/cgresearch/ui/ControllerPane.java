/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.ui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.ui.scenegraph.NodePopupMenu;
import cgresearch.ui.scenegraph.SceneGraphViewerNode;
import cgresearch.ui.scenegraph.SceneGraphViewerRenderer;

public class ControllerPane extends JTabbedPane implements MouseListener {
	/**
     * 
     */
	private static final long serialVersionUID = 4121458448228309864L;

	/**
	 * Reference to the scene graph tree.
	 */
	private JTree tree = null;

	/**
	 * Reference to the scroll pane used to display the edit dialogs.
	 */
	private final JScrollPane editFieldParent;

	/**
	 * Constructor.
	 */
	public ControllerPane(CgNode sgRootNode, JScrollPane editFieldParent) {
		this.editFieldParent = editFieldParent;
		addTab("Scenegraph", createTree(sgRootNode));
	}

	/**
	 * Create the tree
	 */
	private Component createTree(CgNode sgRootNode) {
		DefaultTreeModel treeModel = new DefaultTreeModel(null);
		SceneGraphViewerNode rootTreeNode = new SceneGraphViewerNode(treeModel,
				sgRootNode);
		treeModel.setRoot(rootTreeNode);
		tree = new JTree(treeModel);
		tree.setEditable(true);
		tree.setCellRenderer(new SceneGraphViewerRenderer());
		tree.addMouseListener(this);
		rootTreeNode.buildTree();
		return tree;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		TreePath path = tree.getPathForLocation(e.getX(), e.getY());
		if (path == null) {
			return;
		}

		SceneGraphViewerNode node = (SceneGraphViewerNode) (path
				.getLastPathComponent());

		// Reset the editor field.
		editFieldParent.setViewportView(null);

		if (node != null) {
			if (e.getButton() == MouseEvent.BUTTON2) {
				node.toggleVisibiliy();
				repaint();
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				// Right click
				NodePopupMenu nodePopupMenu = new NodePopupMenu(
						editFieldParent, node);
				nodePopupMenu.show(tree, e.getX(), e.getY());
			}
		}

	}
}

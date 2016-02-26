/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.ui.scenegraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

/**
 * Right-click popup menu for the scene graph tree viewer.
 * 
 * @author Philipp Jenke
 * 
 */
public class NodePopupMenu extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String INFO_EDIT = "INFO_EDIT";
	private static final String TOGGLE_VISIBLE = "TOGGLE_VISIBLE";
	private static final String DELETE = "DELETE";
	private static final String MATERIAL = "MATERIAL";

	private final SceneGraphViewerNode node;

	private final JScrollPane parent;

	/**
	 * Constructor.
	 */
	public NodePopupMenu(JScrollPane parent, SceneGraphViewerNode node) {
		this.node = node;
		this.parent = parent;

		JMenuItem itemInfoEdit = new JMenuItem("Info/Edit");
		add(itemInfoEdit);
		itemInfoEdit.addActionListener(this);
		itemInfoEdit.setActionCommand(INFO_EDIT);

		JMenuItem itemToggleVisible = new JMenuItem("Toggle Visible");
		add(itemToggleVisible);
		itemToggleVisible.addActionListener(this);
		itemToggleVisible.setActionCommand(TOGGLE_VISIBLE);

		JMenuItem itemMaterial = new JMenuItem("Material");
		add(itemMaterial);
		itemMaterial.addActionListener(this);
		itemMaterial.setActionCommand(MATERIAL);

		JMenuItem itemDelete = new JMenuItem("Delete node");
		add(itemDelete);
		itemDelete.addActionListener(this);
		itemDelete.setActionCommand(DELETE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(INFO_EDIT)
				&& node.getSceneGraphNode() != null) {
			SceneGraphNodeInfoEditFactory.createInfoEdit(parent, node);
		} else if (e.getActionCommand().equals(TOGGLE_VISIBLE)
				&& node.getSceneGraphNode() != null) {
			node.getSceneGraphNode().toggleVisibiliy();
		} else if (e.getActionCommand().equals(DELETE)
				&& node.getSceneGraphNode() != null) {
			node.getSceneGraphNode().deleteNode();
		} else if (e.getActionCommand().equals(MATERIAL)
				&& node.getSceneGraphNode() != null) {
			if (node.getSceneGraphNode().getContent() != null) {
				SceneGraphNodeInfoEditFactory.createInfoEditMaterial(parent,
						node.getSceneGraphNode());
			}
		}

	}
}

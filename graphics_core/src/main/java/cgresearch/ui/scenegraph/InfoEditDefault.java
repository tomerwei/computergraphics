/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.ui.scenegraph;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class InfoEditDefault extends InfoEditDialog implements MouseListener {

	private final SceneGraphViewerNode node;
	private JCheckBox cbVisible = new JCheckBox();
	private GridBagConstraints c = new GridBagConstraints();
	private int currentLayoutX = 0;
	private int currentLayoutY = 0;

	private static final long serialVersionUID = 1L;

	private static final String EDIT_MATERIAL = "EDIT_MATERIAL";
	private static final String ACTION_VISIBLE = "ACTION_VISIBLE";

	/**
	 * Edit dialog for a material.
	 */
	private InfoEditMaterial infoEditMaterial = null;

	private JButton buttonEditMaterial;

	/**
	 * Constructor.
	 */
	public InfoEditDefault(JScrollPane parent, SceneGraphViewerNode node) {
		super(parent);
		this.node = node;
	}

	public void setup() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Info/Edit"));
		createGuiComponents();
		validate();
		add(Box.createVerticalGlue());
	}

	protected SceneGraphViewerNode getNode() {
		return node;
	}

	void addToLayout(Component comp, boolean spanTwoFields) {
		c.gridx = currentLayoutX;
		c.gridy = currentLayoutY;
		c.gridwidth = (spanTwoFields && currentLayoutX == 0) ? 2 : 1;
		add(comp, c);

		if (spanTwoFields && currentLayoutX == 0) {
			// Component spans two fields
			c.gridwidth = 2;
			currentLayoutY++;
		} else {
			// Component spans single field
			if (currentLayoutX == 1) {
				currentLayoutY++;
				currentLayoutX = 0;
			} else {
				currentLayoutX = 1;
			}
		}
	}

	protected void createGuiComponents() {
		addToLayout(new JLabel("Name: "), false);
		JLabel labelName = new JLabel(node.getName());
		addToLayout(labelName, false);

		addToLayout(new JLabel("Id:"), false);
		JLabel labelId = new JLabel("" + node.getSceneGraphNode().getId());
		addToLayout(labelId, false);

		addToLayout(new JLabel("Visible:"), false);
		cbVisible.setSelected(node.getSceneGraphNode().isVisible());
		addToLayout(cbVisible, false);
		cbVisible.addMouseListener(this);
		cbVisible.setActionCommand(ACTION_VISIBLE);

		buttonEditMaterial = new JButton("Edit Material");
		addToLayout(buttonEditMaterial, true);
		buttonEditMaterial.addMouseListener(this);
		buttonEditMaterial.setActionCommand(EDIT_MATERIAL);
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
		if (e.getSource() == cbVisible) {
			node.getSceneGraphNode().setVisible(cbVisible.isSelected());
		} else if (e.getSource() == buttonEditMaterial) {
			if (infoEditMaterial == null) {
				infoEditMaterial = new InfoEditMaterial(parent,
						node.getSceneGraphNode());
			}
			infoEditMaterial.setVisible(true);
		}
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// if (e.getActionCommand().equals(EDIT_MATERIAL)) {
	// if (infoEditMaterial == null) {
	// ICgNodeContent content = node.getSceneGraphNode().getContent();
	// infoEditMaterial = new InfoEditMaterial(content.getMaterial());
	// }
	// infoEditMaterial.setVisible(true);
	// } else if (e.getActionCommand().equals(ACTION_VISIBLE)) {
	// node.getSceneGraphNode().setVisible(cbVisible.isSelected());
	// }
	// }
}

package cgresearch.ui.lights;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.graphics.scenegraph.LightSource;

/**
 * Editor for the lighting situation.
 * 
 * @author Philipp Jenke
 *
 */
public class LightEditor extends JFrame implements Observer, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1020860760255387704L;
	private static final String ACTION_COMMAND_ADD = "ACTION_COMMAND_ADD";
	/**
	 * Reference to the scene gtaph root node.
	 */
	private final CgRootNode rootNode;

	/**
	 * Central table
	 */
	private AbstractTableModel tableModel = null;

	/**
	 * Constructor.
	 */
	public LightEditor(CgRootNode rootNode) {
		this.rootNode = rootNode;
		createUi();
		setSize(800, 300);
		setVisible(false);
	}

	private void createUi() {
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		tableModel = new LightTableModel(rootNode);
		JTable table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		getContentPane().add(scrollPane);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					edit(row, column);
				}
			}
		});

		JPanel panel = new JPanel();
		getContentPane().add(panel);

		// Add button
		JButton button = new JButton("add");
		button.addActionListener(this);
		button.setActionCommand(ACTION_COMMAND_ADD);
		panel.add(button);
	}

	private void updateTable() {
		if (tableModel != null) {
			tableModel.fireTableDataChanged();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof CgRootNode) {
			updateTable();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_ADD)) {
			rootNode.addLight(new LightSource(LightSource.Type.POINT));
			updateTable();
		}
	}

	/**
	 * Edit a cell in the table
	 */
	private void edit(int row, int column) {
		Vector vNew = null;
		switch (column) {
		case 0:
			break;
		case 1:
			// Type
			break;
		case 2:
			vNew = getEditVector(rootNode.getLight(row).getPosition());
			if (vNew != null) {
				rootNode.getLight(row).getPosition().copy(vNew);
				rootNode.lightingChanged();
			}
			break;
		case 3:
			vNew = getEditVector(rootNode.getLight(row).getDiffuseColor());
			if (vNew != null) {
				rootNode.getLight(row).getDiffuseColor().copy(vNew);
				rootNode.lightingChanged();
			}
			break;
		case 4:
			vNew = getEditVector(rootNode.getLight(row).getDirection());
			if (vNew != null) {
				rootNode.getLight(row).getDirection().copy(vNew);
				rootNode.lightingChanged();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Show an editor for a 3-vector.
	 */
	private Vector getEditVector(Vector v) {
		JTextField tx = new JTextField(v.get(0) + "");
		JTextField ty = new JTextField(v.get(1) + "");
		JTextField tz = new JTextField(v.get(2) + "");
		JComponent[] inputs = new JComponent[] { tx, ty, tz };
		JOptionPane.showMessageDialog(null, inputs, "Edit vector",
				JOptionPane.PLAIN_MESSAGE);
		try {
			return VectorFactory.createVector3(
					Double.parseDouble(tx.getText()),
					Double.parseDouble(ty.getText()),
					Double.parseDouble(tz.getText()));
		} catch (Exception e) {
			return null;
		}

	}
}

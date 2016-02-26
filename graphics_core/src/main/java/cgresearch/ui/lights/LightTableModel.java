package cgresearch.ui.lights;

import javax.swing.table.AbstractTableModel;

import cgresearch.graphics.scenegraph.CgRootNode;

public class LightTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1680058695611578100L;

	/**
	 * Reference to the scene graph root node
	 */
	private final CgRootNode rootNode;

	/**
	 * Constructor
	 */
	public LightTableModel(CgRootNode rootNode) {
		this.rootNode = rootNode;
	}

	@Override
	public String getColumnName(int index) {
		switch (index) {
		case 0:
			return "Index";
		case 1:
			return "Type";
		case 2:
			return "Position";
		case 3:
			return "Color";
		case 4:
			return "Direction";
		default:
			return "none";
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public int getRowCount() {
		return rootNode.getNumberOfLights();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return rowIndex;
		case 1:
			return rootNode.getLight(rowIndex).getType();
		case 2:
			return rootNode.getLight(rowIndex).getPosition().toString(2);
		case 3:
			return rootNode.getLight(rowIndex).getDiffuseColor().toString(2);
		case 4:
			return rootNode.getLight(rowIndex).getDirection().toString(2);
		default:
			return "none";
		}
	}
}

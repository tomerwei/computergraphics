package cgresearch.ui.resources;

import javax.swing.table.AbstractTableModel;

import cgresearch.graphics.material.GenericManager;

public class ResourceManagerTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1680058695611578100L;

	/**
	 * Reference to the resource manager
	 */
	private final GenericManager<?> resourceManager;

	/**
	 * Constructor
	 */
	public ResourceManagerTableModel(GenericManager<?> resourceManager) {
		this.resourceManager = resourceManager;
	}

	@Override
	public String getColumnName(int index) {
		switch (index) {
		case 0:
			return "Id";
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
		return resourceManager.getNumberOfResources();
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return resourceManager.getKey(rowIndex);
	}
}

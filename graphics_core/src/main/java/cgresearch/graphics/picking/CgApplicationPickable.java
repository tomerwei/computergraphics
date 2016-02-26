package cgresearch.graphics.picking;

/**
 * Implement this interface to allow for object (point) picking.
 * 
 * @author Philipp Jenke
 *
 */
public abstract class CgApplicationPickable {

	/**
	 * Constructor.
	 */
	public CgApplicationPickable() {
		Picking.registerPickable(this);
	}

	/**
	 * Add a picking item to the scene.
	 */
	public void addPickingItem(PickingItem pickingItem) {
		Picking.getInstance().addItem(pickingItem);
	}

	/**
	 * This callback method is called if an item is selected.
	 */
	public abstract void itemPicked(String id);

	/**
	 * This callback method is called if an item is moved.
	 */
	public abstract void itemMoved(String id);

	/**
	 * Getter for an item by ID.
	 */
	public PickingItem getItem(String id) {
		return Picking.getInstance().getPickingItem(id);
	}
}

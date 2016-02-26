package cgresearch.graphics.picking;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;

public class PickingItem {

	/**
	 * Unique id of the item
	 */
	private String id;

	/**
	 * Static counter used to create unique ids.
	 */
	private static int idCounter = 0;

	/**
	 * Current position of the item;
	 */
	private Vector position = VectorFactory.createVector3(0, 0, 0);

	/**
	 * Constructor.
	 */
	public PickingItem(Vector position) {
		this.position.copy(position);
		this.id = "ID" + idCounter;
		idCounter++;
	}

	/**
	 * Getter.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Getter.
	 */
	public Vector getPosition() {
		return position;
	}

	/**
	 * Setter.
	 * 
	 * @param position
	 */
	public void setPosition(Vector position) {
		this.position.copy(position);
	}
}

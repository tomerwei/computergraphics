package cgresearch.graphics.picking;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

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
	private IVector3 position = VectorMatrixFactory.newIVector3(0, 0, 0);

	/**
	 * Constructor.
	 */
	public PickingItem(IVector3 position) {
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
	public IVector3 getPosition() {
		return position;
	}

	/**
	 * Setter.
	 * 
	 * @param position
	 */
	public void setPosition(IVector3 position) {
		this.position.copy(position);
	}
}
